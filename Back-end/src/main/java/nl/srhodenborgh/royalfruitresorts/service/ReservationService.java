package nl.srhodenborgh.royalfruitresorts.service;

import nl.srhodenborgh.royalfruitresorts.dto.ReservationDTO;
import nl.srhodenborgh.royalfruitresorts.enums.ReservationStatus;
import nl.srhodenborgh.royalfruitresorts.mapper.ReservationMapper;
import nl.srhodenborgh.royalfruitresorts.model.Account;
import nl.srhodenborgh.royalfruitresorts.model.Reservation;
import nl.srhodenborgh.royalfruitresorts.model.Room;
import nl.srhodenborgh.royalfruitresorts.model.User;
import nl.srhodenborgh.royalfruitresorts.repository.*;
import nl.srhodenborgh.royalfruitresorts.service.util.DataFormatter;
import nl.srhodenborgh.royalfruitresorts.service.util.InputValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private DataFormatter dataFormatter;
    @Autowired
    private InputValidator inputValidator;
    @Autowired
    private ReservationMapper reservationMapper;
    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);



    // Create
    public String createReservation (ReservationDTO reservationDTO) {

        if (inputValidator.areRequiredFieldsInvalid(reservationDTO)) {
            logger.error("Failed to create reservation. Input fields are invalid");
            return null;
        }

        Optional<Room> roomOptional = roomRepository.findById(reservationDTO.getRoomId());
        Optional<User> userOptional = userRepository.findById(reservationDTO.getUserId());

        if (roomOptional.isEmpty()) {
            logger.error("Failed to create reservation. Room (id: {}) doesn't exist", reservationDTO.getRoomId());
            return null;
        }

        if (userOptional.isEmpty()) {
            logger.error("Failed to create reservation. User (id: {}) doesn't exist", reservationDTO.getUserId());
            return null;
        }

        Reservation reservation = reservationMapper.mapToReservation(reservationDTO);

        dataFormatter.formatFields(reservation);

        reservation.setRoom(roomOptional.get());
        reservation.setUser(userOptional.get());

        String uuid = UUID.randomUUID().toString();
        reservation.setUuid(uuid);

        reservationRepository.save(reservation);
        logger.info("Successfully created reservation on Id: {}", reservation.getId());
        return uuid;
    }



    // Read
    public Iterable<ReservationDTO> getAllReservations(String sort) {

        Iterable<Reservation> reservations = reservationRepository.findAll();
        List<ReservationDTO> DTOList = new ArrayList<>();

        if (!reservations.iterator().hasNext()) {
            logger.error("No reservations found in database");
        }

        for (Reservation reservation : reservations) {
            DTOList.add(reservationMapper.mapToReservationDTO(reservation));
        }
        return sortList(DTOList, sort);
    }


    public ReservationDTO getReservation(long id) {
        Optional<Reservation> reservationOptional = reservationRepository.findById(id);

        if (reservationOptional.isEmpty()) {
            logger.error("Failed to get reservation. Cannot find reservation (id: {})", id);
            return null;
        }

        return reservationMapper.mapToReservationDTO(reservationOptional.get());
    }



    // Update
    public boolean updateReservation(long id, ReservationDTO updatedReservation) {
        Optional<Reservation> reservationOptional = reservationRepository.findById(id);

        if (reservationOptional.isEmpty()) {
            logger.error("Failed to update reservation. Cannot find reservation (id: {})", id);
            return false;
        }

        if (inputValidator.areRequiredFieldsInvalid(updatedReservation)) {
            logger.error("Failed to update reservation (id: {}). Input fields are invalid", id);
            return false;
        }

        Optional<Room> roomOptional = roomRepository.findById(updatedReservation.getRoomId());

        if (roomOptional.isEmpty()) {
            logger.error("Failed to update reservation. Cannot find room (id: {})", updatedReservation.getRoomId());
            return false;
        }

        Reservation reservation = reservationOptional.get();
        dataFormatter.formatFields(reservation);
        reservation.setRoom(roomOptional.get());

        reservation.setCheckInDate(updatedReservation.getCheckInDate());
        reservation.setCheckOutDate(updatedReservation.getCheckOutDate());
        reservation.setAdults(updatedReservation.getAdults());
        reservation.setChildren(updatedReservation.getChildren());
        reservation.setSurcharge(updatedReservation.getChildren() != 0);
        reservation.setSpecialRequest(updatedReservation.getSpecialRequest());
        reservation.setStatus(ReservationStatus.RESERVED);
        //TODO: moet status op reserved staan? Moet de booking dan verwijderd worden?

        reservationRepository.save(reservation);
        logger.info("Successfully updated reservation (id: {})", id);
        return true;
    }


    // Delete
    public boolean cancelReservation(long id) {
        // Met deze methode wordt de booking verwijderd en de reservering op status CANCELLED gezet
        Optional<Reservation> reservationOptional = reservationRepository.findById(id);

        if (reservationOptional.isEmpty()) {
            logger.error("Failed to cancel reservation. Cannot find reservation (id: {})", id);
            return false;
        }

        Reservation reservation = reservationOptional.get();

        // Als er een account en een booking is gekoppeld aan de reservering, worden er loyalty punten van het account in mindering gebracht
        if (reservation.getUser().getAccount() != null && reservation.getBooking() != null) {
            subtractLoyaltyPoints(reservation);
        }

        // Als er een booking gekoppeld is aan de reservering, wordt de booking verwijderd
        if (reservation.getBooking() != null) {
            long bookingId = reservation.getBooking().getId();
            reservation.setBooking(null);
            bookingRepository.deleteById(bookingId);
            logger.info("Successfully deleted booking (id: {}) associated with reservation (id: {})", bookingId, id);
        }

        // Reserveringen worden niet verwijderd, maar op reservationStatus CANCELLED gezet
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);
        logger.info("Reservation set to status CANCELLED for Id: {}", id);
        return true;
    }



    // Andere methodes
    private List<ReservationDTO> sortList(List<ReservationDTO> list, String sort) {
        // Als er geen sort-waarde is meegegeven, wordt er gesorteerd op roomId (default)
        // TODO: Surcharge en reservationStatus in filter methode maken en de overige parameters ook in filtersysteem zetten
        if (sort == null) { sort = ""; }

        switch (sort) {
            case "hotelId":
                list.sort(Comparator.comparingLong(ReservationDTO::getHotelId));
                break;
            case "hotelName":
                list.sort(Comparator.comparing(ReservationDTO::getHotelName));
                break;
            case "reservationId":
                list.sort(Comparator.comparingLong(ReservationDTO::getReservationId));
                break;
            case "checkInDate":
                list.sort(Comparator.comparing(ReservationDTO::getCheckInDate));
                break;
            case "checkOutDate":
                list.sort(Comparator.comparing(ReservationDTO::getCheckOutDate));
                break;
            case "adults":
                list.sort(Comparator.comparingInt(ReservationDTO::getAdults));
                break;
            case "children":
                list.sort(Comparator.comparingInt(ReservationDTO::getChildren));
                break;
            case "userId":
                list.sort(Comparator.comparingLong(ReservationDTO::getUserId));
                break;
            case "firstName":
                list.sort(Comparator.comparing(ReservationDTO::getFirstName));
                break;
            case "lastName":
                list.sort(Comparator.comparing(ReservationDTO::getLastName));
                break;
            default:
                list.sort(Comparator.comparingLong(ReservationDTO::getRoomId));
        } return list;
    }


    public String getReservationStatus(String uuid) {

        Optional<Reservation> reservationOptional = reservationRepository.findByUuid(uuid);

        // TODO: In front end cancelled status fixen
        // Stuurt een datum (String) uit indien de reservering al betaald is
        if (reservationOptional.isEmpty()) {
            return "NOT_FOUND";
        } else if (reservationOptional.get().getReservationStatus() == ReservationStatus.CANCELLED) {
            return "CANCELLED";
        } else if (reservationOptional.get().getBooking() == null) {
            return "NOT_PAID";
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            return reservationOptional.get().getBooking().getDate().format(formatter);
        }
    }


    private void subtractLoyaltyPoints(Reservation reservation) {
        // TODO: Settings loyalty points amount
        Optional<Account> accountOptional = accountRepository.findById(reservation.getUser().getAccount().getId());
        Account account = accountOptional.get();

        account.setLoyaltyPoints(account.getLoyaltyPoints() - 100);
        logger.info("Loyalty points removed from account: {}", account.getId());
        accountRepository.save(account);
    }
}

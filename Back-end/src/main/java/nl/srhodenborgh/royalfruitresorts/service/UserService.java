package nl.srhodenborgh.royalfruitresorts.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import nl.srhodenborgh.royalfruitresorts.enums.ReservationStatus;
import nl.srhodenborgh.royalfruitresorts.repository.BookingRepository;
import nl.srhodenborgh.royalfruitresorts.repository.ReservationRepository;
import nl.srhodenborgh.royalfruitresorts.repository.UserRepository;
import nl.srhodenborgh.royalfruitresorts.dto.ReservationDTO;
import nl.srhodenborgh.royalfruitresorts.model.Reservation;
import nl.srhodenborgh.royalfruitresorts.model.User;
import nl.srhodenborgh.royalfruitresorts.service.util.DataFormatter;
import nl.srhodenborgh.royalfruitresorts.service.util.InputValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private InputValidator inputValidator;
    @Autowired
    private DataFormatter dataFormatter;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);


    // Create
    public boolean createUser(User user) {

        if (inputValidator.areRequiredFieldsInvalid(user)) {
            logger.error("Failed to create user. Input fields are invalid");
            return false;
        }

        dataFormatter.formatFields(user);

        userRepository.save(user);
        logger.info("Successfully created user on Id: {}", user.getId());
        return true;

    }


    // Read
    public Iterable<User> getAllUsers() {
        Iterable<User> users = userRepository.findAll();

        if (!users.iterator().hasNext()) {
            logger.error("No users found in database");
        }

        return users;
    }

    public Optional<User> getUser(long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            logger.error("Failed to get user. Cannot find user (id: {})", id);
        }

        return user;
    }

    public Iterable<ReservationDTO> findReservationsOfUser(long id, String pastOrPresent) {
        // TODO: deze cleanen/fixen. Afhankelijk van reservationDTO. Check ook sort in JPA Query
        pastOrPresent = pastOrPresent.toLowerCase().trim();

        List<ReservationDTO> dtoList = new ArrayList<>();

        if (userRepository.existsById(id)) {
            Iterable<Reservation> reservations;
            if (pastOrPresent.equals("past")) {
                reservations = reservationRepository.findPastReservationsOfUser(id);
            } else {
                reservations = reservationRepository.findPresentReservationsOfUser(id);
            }

            for (Reservation reservation : reservations) {
                dtoList.add(new ReservationDTO(reservation));
            }
            dtoList.sort(Comparator.comparing(dto -> dto.getReservation().getCheckInDate()));
//            System.out.println("Returning list of reservations from user with Id: " + id);
        } else {
            System.err.println("Failed to get reservations. Cannot find user on Id: " + id);
        }
        return dtoList;
    }


    // Update
    public boolean updateUser(long id, User updatedUser) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            logger.error("Failed to update user. Cannot find user (id: {})", id);
            return false;
        }

        if (inputValidator.areRequiredFieldsInvalid(updatedUser)) {
            logger.error("Failed to update user (id: {}). Input fields are invalid", id);
            return false;
        }

        dataFormatter.formatFields(updatedUser);

        user.get().setFirstName(updatedUser.getFirstName());
        user.get().setLastName(updatedUser.getLastName());
        user.get().setDateOfBirth(updatedUser.getDateOfBirth());
        user.get().setStreet(updatedUser.getStreet());
        user.get().setHouseNumber(updatedUser.getHouseNumber());
        user.get().setZipCode(updatedUser.getZipCode());
        user.get().setCity(updatedUser.getCity());
        user.get().setCountry(updatedUser.getCountry());
        user.get().setEmail(updatedUser.getEmail());
        user.get().setPhoneNumber(updatedUser.getPhoneNumber());

        userRepository.save(user.get());
        logger.info("Successfully updated user (id: {})", id);
        return true;
    }


    // Delete
    public boolean deleteUser(long id) {
        // TODO: Indien er eem account is moet deze ook loskoppelen om user te kunnen verwijderen
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            logger.error("Failed to delete user. Cannot find user (id: {})", id);
            return false;
        }

        if (user.get().getReservations() != null) {
            cancelBookingsAndReservations(user.get());
        }

        userRepository.deleteById(id);
        logger.info("Successfully deleted user (id: {})", id);
        return true;
    }


    private void cancelBookingsAndReservations(User user) {

        LocalDate now = LocalDate.now();

        for (Reservation reservation : user.getReservations()) {

            // Alle toekomstige boekingen (incl dezelfde dag als nu) worden verwijderd
            // Alle toekomstige reserveringen (incl dezelfde dag als nu) worden op status CANCELLED gezet
            if (reservation.getBooking() != null && !reservation.getCheckInDate().isBefore(now)) {
                bookingRepository.deleteById(reservation.getBooking().getId());
                reservation.setStatus(ReservationStatus.CANCELLED);
            }

            reservation.setUser(null);
            reservationRepository.save(reservation);
        }
    }
}

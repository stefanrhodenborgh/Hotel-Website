package nl.srhodenborgh.royalfruitresorts.service;

import java.util.*;

import nl.srhodenborgh.royalfruitresorts.repository.HotelRepository;
import nl.srhodenborgh.royalfruitresorts.repository.ReservationRepository;
import nl.srhodenborgh.royalfruitresorts.repository.ReviewRepository;
import nl.srhodenborgh.royalfruitresorts.repository.RoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nl.srhodenborgh.royalfruitresorts.model.Hotel;
import nl.srhodenborgh.royalfruitresorts.model.Reservation;
import nl.srhodenborgh.royalfruitresorts.dto.ReservationDTO;
import nl.srhodenborgh.royalfruitresorts.model.Review;
import nl.srhodenborgh.royalfruitresorts.model.Room;

@Service
public class HotelService {
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    private static final Logger logger = LoggerFactory.getLogger(HotelService.class);



    // Create
    public boolean createHotel (Hotel hotel){

        if (isAnyFieldBlank(hotel)) {
            logger.error("Failed to create hotel. Fields of hotel cannot be null");
            return false;
        }

        hotelRepository.save(hotel);
        logger.info("Successfully created hotel on Id: {}", hotel.getId());
        return true;
    }



    // Read
    public Iterable<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }


    public Optional<Hotel> getHotel(long id) {
        Optional<Hotel> hotel = hotelRepository.findById(id);

        if (hotel.isEmpty())
            logger.error("Failed to get hotel. Cannot find hotel on Id: {}", id);

        return hotel;
    }


    public Iterable<Room> getRooms(long hotelId) {
        Optional<Hotel> hotel = hotelRepository.findById(hotelId);

        if (hotel.isEmpty()) {
            logger.error("Failed to get rooms. Cannot find hotel on Id: {}", hotelId);
            return null;
        }

        return new ArrayList<>(hotel.get().getRooms());
    }


    // TODO: reservationDTO 1-dimensionaal maken en deze methode fixen
    public Iterable<ReservationDTO> getReservationsOfHotel(long id){
        try {
            Iterable<Reservation> reservations = reservationRepository.findReservationsOfHotel(id);

            List<ReservationDTO> dtoList = new ArrayList<>();

            for (Reservation r : reservations) {
                dtoList.add(new ReservationDTO(id, r.getRoom().getHotel().getName(), r.getId(),
                        r, r.getUser().getId(), r.getUser().getFirstName(), r.getUser().getLastName()));
            }
            System.out.println("Returning list of reservations of hotel with Id: " + id);
            return dtoList;
        } catch (NoSuchElementException e) {
            System.err.println("Failed to get hotel. Cannot find hotel on Id: " + id);
        }
        return null;
    }


    public Iterable<Review> getReviewsOfHotel(long id) {
        return reviewRepository.getReviewsFromHotel(id);
    }



    // Edit
    public boolean editHotel(long id, Hotel updatedHotel)  {
        Optional<Hotel> hotel = hotelRepository.findById(id);

        if (hotel.isEmpty()) {
            logger.error("Failed to edit hotel. Cannot find hotel on Id: {}", id);
            return false;
        }

        if (isAnyFieldBlank(updatedHotel)) {
            logger.error("Failed to edit hotel. Fields cannot be blank");
            return false;
        }

        hotel.get().setName(updatedHotel.getName());
        hotel.get().setStreet(updatedHotel.getStreet());
        hotel.get().setHouseNumber(updatedHotel.getHouseNumber());
        hotel.get().setZipCode(updatedHotel.getZipCode());
        hotel.get().setCity(updatedHotel.getCity());
        hotel.get().setCountry(updatedHotel.getCountry());
        hotel.get().setDescription(updatedHotel.getDescription());

        hotelRepository.save(hotel.get());
        logger.info("Successfully edited hotel on id: {}", id);
        return true;
    }



    //Delete
    public boolean deleteHotel (long id) {
        Optional<Hotel> hotel = hotelRepository.findById(id);

        if (hotel.isEmpty()) {
            logger.error("Failed to delete hotel. Cannot find hotel on Id: {}", id);
            return false;
        }

        hotelRepository.deleteById(id);
        logger.info("Successfully deleted hotel on Id: {}", id);
        return true;
    }



    // Andere methodes
    private boolean isAnyFieldBlank(Hotel hotel) {
        // hotel description mag null zijn
        return hotel.getName() == null ||
                hotel.getStreet() == null ||
                hotel.getHouseNumber() == null ||
                hotel.getZipCode() == null ||
                hotel.getCity() == null ||
                hotel.getCountry() == null;
    }
}



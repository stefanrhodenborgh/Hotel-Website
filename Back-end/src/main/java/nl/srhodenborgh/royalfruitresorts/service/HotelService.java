package nl.srhodenborgh.royalfruitresorts.service;

import java.util.*;

import nl.srhodenborgh.royalfruitresorts.repository.HotelRepository;
import nl.srhodenborgh.royalfruitresorts.repository.ReservationRepository;
import nl.srhodenborgh.royalfruitresorts.repository.ReviewRepository;
import nl.srhodenborgh.royalfruitresorts.repository.RoomRepository;
import nl.srhodenborgh.royalfruitresorts.service.util.InputValidator;
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
    @Autowired
    private InputValidator inputValidator;
    private static final Logger logger = LoggerFactory.getLogger(HotelService.class);



    // Create
    public boolean createHotel (Hotel hotel){

        if (inputValidator.areRequiredFieldsInvalid(hotel)) {
            logger.error("Failed to create hotel. Input fields are invalid");
            return false;
        }

        hotelRepository.save(hotel);
        logger.info("Successfully created hotel on Id: {}", hotel.getId());
        return true;
    }



    // Read
    public Iterable<Hotel> getAllHotels() {
        Iterable<Hotel> hotels = hotelRepository.findAll();

        if (!hotels.iterator().hasNext()) {
            logger.error("No hotels found in database");
        }

        return hotels;
    }


    public Optional<Hotel> getHotel(long id) {
        Optional<Hotel> hotel = hotelRepository.findById(id);

        if (hotel.isEmpty()) {
            logger.error("Failed to get hotel. Cannot find hotel (id: {})", id);
        }

        return hotel;
    }


    public Iterable<Room> getRooms(long hotelId) {
        Optional<Hotel> hotel = hotelRepository.findById(hotelId);

        if (hotel.isEmpty()) {
            logger.error("Failed to get rooms. Cannot find hotel (id: {})", hotelId);
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
        Iterable<Review> reviews = reviewRepository.getReviewsFromHotel(id);

        if (!reviews.iterator().hasNext()) {
            logger.warn("No reviews found of hotel (id: {})", id);
        }

        return reviews;
    }



    // Edit
    public boolean editHotel(long id, Hotel updatedHotel)  {
        Optional<Hotel> hotel = hotelRepository.findById(id);

        if (hotel.isEmpty()) {
            logger.error("Failed to edit hotel. Cannot find hotel (id: {})", id);
            return false;
        }

        if (inputValidator.areRequiredFieldsInvalid(updatedHotel)) {
            logger.error("Failed to edit hotel (id: {}). Input fields are invalid", id);
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
        logger.info("Successfully edited hotel (id: {})", id);
        return true;
    }



    //Delete
    public boolean deleteHotel (long id) {
        Optional<Hotel> hotel = hotelRepository.findById(id);

        if (hotel.isEmpty()) {
            logger.error("Failed to delete hotel. Cannot find hotel (id: {})", id);
            return false;
        }

        hotelRepository.deleteById(id);
        logger.info("Successfully deleted hotel (id: {})", id);
        return true;
    }



    // Andere methodes




}



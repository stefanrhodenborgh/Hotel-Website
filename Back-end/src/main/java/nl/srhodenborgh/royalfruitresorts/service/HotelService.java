package nl.srhodenborgh.royalfruitresorts.service;

import java.util.*;

import nl.srhodenborgh.royalfruitresorts.mapper.ReservationMapper;
import nl.srhodenborgh.royalfruitresorts.repository.HotelRepository;
import nl.srhodenborgh.royalfruitresorts.repository.ReservationRepository;
import nl.srhodenborgh.royalfruitresorts.repository.ReviewRepository;
import nl.srhodenborgh.royalfruitresorts.repository.RoomRepository;
import nl.srhodenborgh.royalfruitresorts.service.util.DataFormatter;
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

import javax.swing.text.html.Option;

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
    @Autowired
    private DataFormatter dataFormatter;
    @Autowired
    private ReservationMapper reservationMapper;
    private static final Logger logger = LoggerFactory.getLogger(HotelService.class);



    // Create
    public boolean createHotel(Hotel hotel){

        if (inputValidator.areRequiredFieldsInvalid(hotel)) {
            logger.error("Failed to create hotel. Input fields are invalid");
            return false;
        }

        dataFormatter.formatFields(hotel);

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
        Optional<Hotel> hotelOptional = hotelRepository.findById(id);

        if (hotelOptional.isEmpty()) {
            logger.error("Failed to get hotel. Cannot find hotel (id: {})", id);
        }

        return hotelOptional;
    }


    public Iterable<Room> getRoomsOfHotel(long hotelId) {
        Optional<Hotel> hotelOptional = hotelRepository.findById(hotelId);

        if (hotelOptional.isEmpty()) {
            logger.error("Failed to get rooms. Cannot find hotel (id: {})", hotelId);
            return null;
        }

        return new ArrayList<>(hotelOptional.get().getRooms());
    }


    public Iterable<ReservationDTO> getReservationsOfHotel(long id){

        if (!hotelRepository.existsById(id)) {
            logger.error("Failed to get reservations. Cannot find hotel (id: {})", id);
            return null;
        }

        Iterable<Reservation> reservations = reservationRepository.findReservationsOfHotel(id);

        if (!reservations.iterator().hasNext()) {
            logger.warn("No reservations found of hotel (id: {})", id);
            return null;
        }

        List<ReservationDTO> dtoList = new ArrayList<>();

        for (Reservation reservation : reservations) {
            dtoList.add(reservationMapper.mapToReservationDTO(reservation));
        }

        return dtoList;
    }


    public Iterable<Review> getReviewsOfHotel(long id) {
        Optional<Hotel> hotelOptional = hotelRepository.findById(id);

        if (hotelOptional.isEmpty()) {
            logger.error("Failed to get reviews. Cannot find hotel (id: {})", id);
            return null;
        }

        List<Review> reviews = hotelOptional.get().getReviews();

        if (reviews.isEmpty()) {
            logger.warn("No reviews found of hotel (id: {})", id);
        }

        return reviews;
    }



    // Update
    public boolean updateHotel(long id, Hotel updatedHotel)  {
        Optional<Hotel> hotelOptional = hotelRepository.findById(id);

        if (hotelOptional.isEmpty()) {
            logger.error("Failed to update hotel. Cannot find hotel (id: {})", id);
            return false;
        }

        if (inputValidator.areRequiredFieldsInvalid(updatedHotel)) {
            logger.error("Failed to update hotel (id: {}). Input fields are invalid", id);
            return false;
        }

        dataFormatter.formatFields(updatedHotel);
        Hotel hotel = hotelOptional.get();

        hotel.setName(updatedHotel.getName());
        hotel.setStreet(updatedHotel.getStreet());
        hotel.setHouseNumber(updatedHotel.getHouseNumber());
        hotel.setZipCode(updatedHotel.getZipCode());
        hotel.setCity(updatedHotel.getCity());
        hotel.setCountry(updatedHotel.getCountry());
        hotel.setEmail(updatedHotel.getEmail());
        hotel.setPhoneNumber(updatedHotel.getPhoneNumber());
        hotel.setDescription(updatedHotel.getDescription());

        hotelRepository.save(hotel);
        logger.info("Successfully updated hotel (id: {})", id);
        return true;
    }



    //Delete
    public boolean deleteHotel (long id) {
        Optional<Hotel> hotelOptional = hotelRepository.findById(id);

        if (hotelOptional.isEmpty()) {
            logger.error("Failed to delete hotel. Cannot find hotel (id: {})", id);
            return false;
        }

        hotelRepository.deleteById(id);
        logger.info("Successfully deleted hotel (id: {})", id);
        return true;
    }



    // Andere methodes

}



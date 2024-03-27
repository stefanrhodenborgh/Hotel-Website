package nl.srhodenborgh.royalfruitresorts.controller;

import java.util.Optional;

import nl.srhodenborgh.royalfruitresorts.dto.ReservationDTO;
import nl.srhodenborgh.royalfruitresorts.model.Hotel;
import nl.srhodenborgh.royalfruitresorts.model.Review;
import nl.srhodenborgh.royalfruitresorts.model.Room;
import nl.srhodenborgh.royalfruitresorts.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(maxAge = 3600)
public class HotelController {
    @Autowired
    private HotelService hotelService;



    // Create
    @PostMapping("/create-hotel")
    public boolean createHotel (@RequestBody Hotel hotel) {
        return hotelService.createHotel(hotel);
    }


    // Read
    @GetMapping("/all-hotels")
    public Iterable<Hotel> getAllHotels() {
        return hotelService.getAllHotels();
    }

    @GetMapping("/hotel/{id}")
    public Optional<Hotel> getHotel(@PathVariable ("id") long id) {
        return hotelService.getHotel(id);
    }

    @GetMapping("/hotel/{id}/rooms")
    public Iterable<Room> getRoomsOfHotel(@PathVariable ("id")long id) {
        return hotelService.getRoomsOfHotel(id);
    }

    @GetMapping("/hotel/{id}/reservations")
    public Iterable<ReservationDTO> getReservationsOfHotel(@PathVariable ("id") long id) {
        return hotelService.getReservationsOfHotel(id);
    }

    @GetMapping("/hotel/{id}/reviews")
    public Iterable<Review> getReviewsFromHotel(@PathVariable ("id") long id) {
        return hotelService.getReviewsOfHotel(id);
    }


    // Update
    @PutMapping ("/update-hotel/{id}")
    public boolean updateHotel(@PathVariable ("id") long id, @RequestBody Hotel updatedHotel) {
        return hotelService.updateHotel(id, updatedHotel);
    }


    // Delete
    @DeleteMapping ("/delete-hotel/{id}")
    public boolean deleteHotel(@PathVariable ("id") long id) {
        return hotelService.deleteHotel(id);
    }


}

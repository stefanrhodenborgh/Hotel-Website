package nl.srhodenborgh.royalfruitresorts.controller;

import java.util.Optional;

import nl.srhodenborgh.royalfruitresorts.model.Booking;
import nl.srhodenborgh.royalfruitresorts.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(maxAge = 3600)
public class BookingController {
    @Autowired
    private BookingService bookingService;



    // Create
    @PostMapping("/create-booking")
    public boolean createBooking(@RequestParam String uuid) {
        return bookingService.createBooking(uuid);
    }



    // Read
    @GetMapping("/all-bookings")
    public Iterable<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/booking/{id}")
    public Optional<Booking> getBooking(@PathVariable ("id") long id) {
        return bookingService.getBooking(id);
    }



    // Update
    @PutMapping("/update-booking/{id}")
    public boolean updateBooking(@PathVariable ("id") long id, @RequestBody Booking updatedBooking) {
        return bookingService.updateBooking(id, updatedBooking);
    }



    // Delete
    // Om een booking te verwijderen: gebruik cancel-reservation endpoint
}

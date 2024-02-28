package com.stefanrhodenborgh.royalfruitresorts.controller;

import java.util.Optional;

import com.stefanrhodenborgh.royalfruitresorts.model.Booking;
import com.stefanrhodenborgh.royalfruitresorts.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(maxAge = 3600)
public class BookingController {
    @Autowired
    private BookingService bookingService;



    // Create
//    @PostMapping("/create-booking/{reservationId}")
//    public boolean createBooking(@PathVariable ("reservationId") long reservationId, @RequestBody Booking booking) {
//        return bookingService.createBooking(reservationId, booking);
//    }

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


    // Edit
    @PutMapping("/edit-booking/{id}")
    public boolean editBooking(@PathVariable ("id") long id, @RequestBody Booking updatedBooking) {
        return bookingService.editBooking(id, updatedBooking);
    }


    // Delete
    // Om een booking te verwijderen: gebruik cancel-reservation endpoint
//    @DeleteMapping("/delete-booking/{bookingId}/{reservationId}")
//    public boolean deleteBooking(@PathVariable ("bookingId") long bookingId, @PathVariable ("reservationId") long reservationId) {
//        return bookingService.deleteBooking(bookingId, reservationId);
//    }



}

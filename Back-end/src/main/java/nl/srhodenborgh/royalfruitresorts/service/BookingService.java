package nl.srhodenborgh.royalfruitresorts.service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import nl.srhodenborgh.royalfruitresorts.repository.AccountRepository;
import nl.srhodenborgh.royalfruitresorts.repository.BookingRepository;
import nl.srhodenborgh.royalfruitresorts.repository.ReservationRepository;
import nl.srhodenborgh.royalfruitresorts.enums.PaymentMethod;
import nl.srhodenborgh.royalfruitresorts.enums.ReservationStatus;
import nl.srhodenborgh.royalfruitresorts.model.Booking;
import nl.srhodenborgh.royalfruitresorts.model.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private AccountRepository accountRepository;



    // Create
    public boolean createBooking(String uuid) {
        Optional<Reservation> reservation = reservationRepository.findByUuidAndBookingIdIsNull(uuid);

        if (reservation.isEmpty()) {
            System.err.println("Failed to create booking. Reservation doesn't exist on uuid: " + uuid);
            System.err.println("or booking already exists on this uuid");
            return false;
        }

        if (reservation.get().getReservationStatus() == ReservationStatus.CANCELLED) {
            System.err.println("Reservation with uuid " + uuid + " is already cancelled. Create new reservation");
            return false;
        }

        Booking booking = new Booking();

        // Loyalty points geven indien er een account is
        if (reservation.get().getUser().getAccount() != null) {
            reservation.get().getUser().getAccount().setLoyaltyPoints((reservation.get().getUser().getAccount().getLoyaltyPoints() + 100));
            accountRepository.save(reservation.get().getUser().getAccount());
        }

        reservation.get().setStatus(ReservationStatus.BOOKED);

        booking.setDate(LocalDateTime.now());
        booking.setPaymentMethod(PaymentMethod.IDEAL);
        booking.setReservation(reservation.get());

        bookingRepository.save(booking);
        reservationRepository.save(reservation.get());

        System.out.println("Successfully created booking on Id: " + booking.getId());
        return true;
    }


    // Read
    public Iterable<Booking> getAllBookings() {
        System.out.println("Returning list of all bookings");
        return bookingRepository.findAll();
    }

    public Optional<Booking> getBooking(long id) {
        if (bookingRepository.findById(id).isPresent()) {
            System.out.println("Returning booking with Id: " + id);
        } else {
            System.err.println("Failed to get booking. Cannot find booking on Id: " + id);
        }
        return bookingRepository.findById(id);
    }


    // Edit
    public boolean editBooking(long id, Booking updatedBooking) {
        try {
            Booking booking = bookingRepository.findById(id).orElseThrow();
            booking.setPaymentMethod(updatedBooking.getPaymentMethod());
            bookingRepository.save(booking);
            System.out.println("Successfully updated booking with Id: " + id);
            return true;
        } catch (NoSuchElementException e) {
            System.err.println("Failed to edit booking. Cannot find booking on Id: " + id);
            System.err.println(e.getMessage());
        } catch (Exception e) {
            System.err.println("Failed to edit booking with Id: " + id);
            System.err.println(e.getMessage());
        }
        return false;
    }


    // Delete
    // Om een booking te verwijderen: gebruik cancel-reservation endpoint
}

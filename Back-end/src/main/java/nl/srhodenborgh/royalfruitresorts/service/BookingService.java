package nl.srhodenborgh.royalfruitresorts.service;

import nl.srhodenborgh.royalfruitresorts.enums.PaymentMethod;
import nl.srhodenborgh.royalfruitresorts.enums.ReservationStatus;
import nl.srhodenborgh.royalfruitresorts.model.Account;
import nl.srhodenborgh.royalfruitresorts.model.Booking;
import nl.srhodenborgh.royalfruitresorts.model.Reservation;
import nl.srhodenborgh.royalfruitresorts.repository.AccountRepository;
import nl.srhodenborgh.royalfruitresorts.repository.BookingRepository;
import nl.srhodenborgh.royalfruitresorts.repository.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private AccountRepository accountRepository;
    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);



    // Create
    public boolean createBooking(String uuid) {
        Optional<Reservation> reservationOptional = reservationRepository.findByUuid(uuid);

        if (reservationOptional.isEmpty()) {
            logger.error("Failed to create booking. Reservation (uuid: {}) doesn't exist", uuid);
            return false;
        }

        if (reservationOptional.get().getBooking() != null) {
            logger.error("Failed to create booking. Booking already exists on reservation (uuid: {})", uuid);
            return false;
        }

        if (reservationOptional.get().getReservationStatus() == ReservationStatus.CANCELLED) {
            logger.error("Failed to create booking. Reservation (uuid: {}) is already cancelled. Create a new reservation", uuid);
            return false;
        }

        Reservation reservation = reservationOptional.get();

        // Loyalty points geven indien er een account is
        if (reservation.getUser().getAccount() != null) {
            addLoyaltyPoints(reservation.getUser().getAccount());
        }

        Booking booking = new Booking();
        booking.setDate(LocalDateTime.now());
        booking.setPaymentMethod(PaymentMethod.IDEAL);
        booking.setReservation(reservation);

        reservation.setStatus(ReservationStatus.BOOKED);

        bookingRepository.save(booking);
        reservationRepository.save(reservation);

        logger.info("Successfully created booking on Id: {}", booking.getId());
        return true;
    }


    // Read
    public Iterable<Booking> getAllBookings() {
        Iterable<Booking> bookings = bookingRepository.findAll();

        if (!bookings.iterator().hasNext()) {
            logger.error("No bookings found in database");
        }

        return bookings;
    }


    public Optional<Booking> getBooking(long id) {
        Optional<Booking> bookingOptional = bookingRepository.findById(id);

        if (bookingOptional.isEmpty()) {
            logger.error("Failed to get booking. Cannot find booking (id: {})", id);
        }

        return bookingOptional;
    }



    // Update
    public boolean updateBooking(long id, Booking updatedBooking) {
        Optional<Booking> bookingOptional = bookingRepository.findById(id);

        if (bookingOptional.isEmpty()) {
            logger.error("Failed to update booking. Cannot find booking (id: {})", id);
            return false;
        }

        bookingOptional.get().setPaymentMethod(updatedBooking.getPaymentMethod());
        bookingRepository.save(bookingOptional.get());
        logger.info("Successfully updated booking (id: {})", id);
        return true;
    }



    // Delete
    // Om een booking te verwijderen: gebruik cancel-reservation endpoint



    // Andere methodes
    private void addLoyaltyPoints(Account account) {
        // TODO: Loyalty points amount in settings page
        account.setLoyaltyPoints(account.getLoyaltyPoints() + 100);
        accountRepository.save(account);
    }
}

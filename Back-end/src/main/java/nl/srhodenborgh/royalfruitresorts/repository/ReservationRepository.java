package nl.srhodenborgh.royalfruitresorts.repository;

import nl.srhodenborgh.royalfruitresorts.model.Reservation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface ReservationRepository extends CrudRepository<Reservation, Long> {

    // TODO: check of join nodig is
    @Query(value = "SELECT res.* " +
            "FROM reservation res " +
            "JOIN room r on r.id = res.room_id " +
            "WHERE r.hotel_id = ?1", nativeQuery = true)
    Iterable<Reservation> findReservationsOfHotel(long id);

    @Query(value = "SELECT * FROM reservation WHERE user_id = ?1 AND check_in_date >= CURRENT_DATE", nativeQuery = true)
    Iterable<Reservation> findPresentReservationsOfUser(long id);

    @Query(value = "SELECT * FROM reservation WHERE user_id = ?1 AND check_in_date < CURRENT_DATE", nativeQuery = true)
    Iterable<Reservation> findPastReservationsOfUser(long id);

    Optional<Reservation> findByUuid(String uuid);

    Optional<Reservation> findByUuidAndBookingIdIsNull(String uuid);
}

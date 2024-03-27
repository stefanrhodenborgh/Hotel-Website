package nl.srhodenborgh.royalfruitresorts.repository;

import nl.srhodenborgh.royalfruitresorts.model.Booking;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface BookingRepository extends CrudRepository<Booking, Long> {
}

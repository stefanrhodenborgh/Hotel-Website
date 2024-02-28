package com.stefanrhodenborgh.royalfruitresorts.repository;

import com.stefanrhodenborgh.royalfruitresorts.model.Booking;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface BookingRepository extends CrudRepository<Booking, Long> {
}

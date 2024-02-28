package com.stefanrhodenborgh.royalfruitresorts.repository;

import com.stefanrhodenborgh.royalfruitresorts.model.Hotel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface HotelRepository extends CrudRepository<Hotel, Long> {
    }


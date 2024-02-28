package com.stefanrhodenborgh.royalfruitresorts.repository;

import com.stefanrhodenborgh.royalfruitresorts.model.Room;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface RoomRepository extends CrudRepository<Room, Long> {

    Iterable<Room> findByHotelIdAndRoomType(long hotelId, Room.RoomType roomType);
}

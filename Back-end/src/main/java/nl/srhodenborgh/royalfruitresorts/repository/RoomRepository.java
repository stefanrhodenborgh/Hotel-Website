package nl.srhodenborgh.royalfruitresorts.repository;

import nl.srhodenborgh.royalfruitresorts.enums.RoomType;
import nl.srhodenborgh.royalfruitresorts.model.Room;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface RoomRepository extends CrudRepository<Room, Long> {

    Iterable<Room> findByHotelIdAndRoomType(long hotelId, RoomType roomType);
}

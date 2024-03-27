package nl.srhodenborgh.royalfruitresorts.repository;

import nl.srhodenborgh.royalfruitresorts.enums.RoomType;
import nl.srhodenborgh.royalfruitresorts.model.Room;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface RoomRepository extends CrudRepository<Room, Long> {

    Iterable<Room> findByHotelIdAndRoomType(long hotelId, RoomType roomType);

    @Query (value = "SELECT * FROM room WHERE hotel_id = ?1 AND num_beds >= ?2", nativeQuery = true)
    Iterable<Room> findSuitableRooms(long hotelId, int numBeds);
}

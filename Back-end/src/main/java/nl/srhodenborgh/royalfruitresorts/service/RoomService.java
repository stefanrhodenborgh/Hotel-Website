package nl.srhodenborgh.royalfruitresorts.service;

import nl.srhodenborgh.royalfruitresorts.dto.RoomDTO;
import nl.srhodenborgh.royalfruitresorts.dto.RoomSearchDTO;
import nl.srhodenborgh.royalfruitresorts.enums.ReservationStatus;
import nl.srhodenborgh.royalfruitresorts.enums.RoomType;
import nl.srhodenborgh.royalfruitresorts.model.Hotel;
import nl.srhodenborgh.royalfruitresorts.model.Reservation;
import nl.srhodenborgh.royalfruitresorts.model.Room;
import nl.srhodenborgh.royalfruitresorts.repository.HotelRepository;
import nl.srhodenborgh.royalfruitresorts.repository.RoomRepository;
import nl.srhodenborgh.royalfruitresorts.service.util.DataFormatter;
import nl.srhodenborgh.royalfruitresorts.service.util.InputValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private InputValidator inputValidator;
    @Autowired
    private DataFormatter dataFormatter;

    private static final Logger logger = LoggerFactory.getLogger(RoomService.class);



    // Create
    public boolean createRoom (Room room, long hotelId) {
        Optional<Hotel> hotel = hotelRepository.findById(hotelId);

        if (hotel.isEmpty()) {
            logger.error("Failed to create room. Hotel (id: {}) doesn't exist", hotelId);
            return false;
        }

        if (inputValidator.areRequiredFieldsInvalid(room)) {
            logger.error("Failed to create room. Input fields are invalid");
            return false;
        }

        dataFormatter.formatFields(room);

        room.setHotel(hotel.get());
        roomRepository.save(room);
        logger.info("Successfully created room on Id: {}", room.getId());
        return true;
    }


    // Read
    public Iterable<Room> getAllRooms() {
        Iterable<Room> rooms = roomRepository.findAll();

        if (!rooms.iterator().hasNext()) {
            logger.error("No rooms found in database");
        }

        return rooms;
    }

    public Optional<Room> getRoom(long id) {
        Optional<Room> room = roomRepository.findById(id);

        if (room.isEmpty()) {
            logger.error("Failed to get room. Cannot find room (id: {})", id);
        }

        return room;
    }


    // Update
    public boolean updateRoom(Room updatedRoom, long hotelId) {
        Optional<Hotel> hotel = hotelRepository.findById(hotelId);
        Optional<Room> room = roomRepository.findById(updatedRoom.getId());

        if (hotel.isEmpty()) {
            logger.error("Failed to update room. Cannot find hotel (id: {})", hotelId);
            return false;
        }

        if (room.isEmpty()) {
            logger.error("Failed to update room. Cannot find room (id: {})", updatedRoom.getId());
            return false;
        }

        if (inputValidator.areRequiredFieldsInvalid(updatedRoom)) {
            logger.error("Failed to update room (id: {}). Input fields are invalid", updatedRoom.getId());
            return false;
        }

        dataFormatter.formatFields(updatedRoom);

        room.get().setRoomType(updatedRoom.getRoomType());
        room.get().setNumBeds(updatedRoom.getNumBeds());
        room.get().setDescription(updatedRoom.getDescription());
        room.get().setPrice(updatedRoom.getPrice());
        room.get().setHotel(hotel.get());

        roomRepository.save(room.get());
        logger.info("Successfully updated room (id: {})", updatedRoom.getId());
        return true;
    }





    public boolean setRoomDescriptionByRoomType(long hotelId, String roomType, String description) {

        RoomType roomTypeEnum = createRoomTypeEnum(roomType);
        if (roomTypeEnum == null) return false;

        if (description == null) description = "";

        if (hotelRepository.findById(hotelId).isEmpty()) {
            logger.error("Failed to set description of roomType {}. Cannot find hotel (id: {})", roomTypeEnum, hotelId);
            return false;
        }

        Iterable<Room> rooms = roomRepository.findByHotelIdAndRoomType(hotelId, roomTypeEnum);
        for (Room room : rooms) {
            room.setDescription(description.trim());
        }

        roomRepository.saveAll(rooms);
        logger.info("Successfully set descriptions of roomType {} of hotel (id: {})", roomTypeEnum, hotelId);
        return true;

    }

    private RoomType createRoomTypeEnum(String roomTypeInput) {

        // functie maakt een enum van de doorgegeven roomTypeInput indien het overeenkomt met de mogelijke roomTypes uit de enum class
        roomTypeInput = roomTypeInput.toUpperCase();

        for (RoomType roomType : RoomType.values()) {
            if (roomType.toString().equals(roomTypeInput)) {
                return RoomType.valueOf(roomTypeInput);

            }
        }

        logger.error("Failed to set description of rooms. RoomType should be one of the following: {}", (Object) RoomType.values());
        return null;
    }



    // Delete
    public boolean deleteRoom (long id){
        Optional<Room> room = roomRepository.findById(id);

        if (room.isEmpty()) {
            logger.error("Failed to delete room. Cannot find room (id: {})", id);
            return false;
        }

        roomRepository.deleteById(id);
        logger.info("Successfully deleted room (id: {})", id);
        return true;
    }



    // Andere methoden
    public Iterable<RoomDTO> searchRooms (RoomSearchDTO query) {

        if (inputValidator.areRequiredFieldsInvalid(query)) {
            logger.error("Failed to search for rooms. Input fields are invalid");
            return null;
        }

        Optional<Hotel> hotel = hotelRepository.findById(query.getHotelId());

        if (hotel.isEmpty()) {
            logger.error("Cannot find hotel (id: {})", query.getHotelId());
            return null;
        }

        // Zoekt naar geschikte kamers op basis van aantal beschikbare bedden en zet het in een List
        List<Room> suitableRooms = findSuitableRooms(hotel.get(), (query.getAdults() + query.getChildren()));

        if (suitableRooms == null || suitableRooms.isEmpty()) {
            logger.warn("No available rooms found");
            return null;
        }

        // Stopt hier de beschikbare (niet-gereserveerde) kamers in
        List<RoomDTO> availableRooms = findAvailableRooms(suitableRooms, query);

        if (!availableRooms.isEmpty()) {
            logger.info("Available rooms found. Returning list of available rooms");
        } else {
            logger.warn("No available rooms found");
        }

        return availableRooms;
    }

    private List<Room> findSuitableRooms(Hotel hotel, int numOfGuests) {

        if (hotel.getRooms() == null)
            return null;

        // TODO: SQL Query hiervan maken in de repository
        // Zoekt naar geschikte kamers op basis van aantal beschikbare bedden en zet het in suitableRooms List
        List<Room> suitableRooms = new ArrayList<>();

        for (Room room : hotel.getRooms()) {
            if (numOfGuests <= room.getNumBeds()) {
                suitableRooms.add(room);
            }
        }
        return suitableRooms;
    }

    private List<RoomDTO> findAvailableRooms(List<Room> suitableRooms, RoomSearchDTO query) {
        List<RoomDTO> availableRooms = new ArrayList<>();

        // Gaat elk geschikte kamer af om te kijken of er niet al een reservering of booking op staat
        for (Room suitableRoom : suitableRooms) {

            // Checkt of de kamer beschikbaar is en niet een duplicaat is van een al-bestaande kamer in de availableRooms List
            if ((checkAvailability(suitableRoom, query) && (!isDuplicateRoom(suitableRoom, availableRooms)))) {
                RoomDTO room = new RoomDTO(suitableRoom);

                // Totaalprijs wordt berekend waarna de room in de availableRooms List wordt gezet
                room.setPrice(calculateTotalPrice(room.getPrice(), query));
                availableRooms.add(room);
            }
        }

        return availableRooms;
    }

    private boolean checkAvailability(Room suitableRoom, RoomSearchDTO query) {
        // Gaat alle bestaande reserveringen per geschikte kamer af
        for (Reservation reservation : suitableRoom.getReservation()) {
            boolean cancelled = reservation.getReservationStatus() == ReservationStatus.CANCELLED;

            if (!cancelled && isOverlap(reservation, query)) {
                return false;
            }
        }
        return true;
    }

    private boolean isOverlap(Reservation reservation, RoomSearchDTO query) {
        LocalDate resCI = reservation.getCiDate();
        LocalDate resCO = reservation.getCoDate();

        LocalDate queryCI = query.getCheckInDate();
        LocalDate queryCO = query.getCheckOutDate();

        // Kijkt of er overlap is wat betreft ciDate en coDate tussen reservering en zoekopdracht
        return (resCI.isBefore(queryCO) && resCO.isAfter(queryCI)) ||
                (resCI.isBefore(queryCI) && resCO.isAfter(queryCI)) ||
                (resCI.isAfter(queryCI) && resCO.isBefore(queryCO)) ||
                (resCI.isEqual(queryCI) && resCO.isEqual(queryCO));
    }

    private boolean isDuplicateRoom(Room room1, List<RoomDTO> roomList) {
        if (roomList.isEmpty()) {
            return false;
        }

        for (RoomDTO room2 : roomList) {

            // checkt of er in de roomList een duplicaat is op basis van roomType, numBeds, description en price
            // description is nullable, vandaar Objects
            if (Objects.equals(room1.getRoomType(), room2.getRoomType()) &&
                    room1.getNumBeds() == room2.getNumBeds() &&
                    Objects.equals(room1.getDescription(), room2.getDescription()) &&
                    room1.getPrice() == room2.getPrice()) {
                return true;
            }
        }

        return false;
    }

    private double calculateTotalPrice(double price, RoomSearchDTO query) {
        long numOfDays = ChronoUnit.DAYS.between(query.getCheckInDate(), query.getCheckOutDate());
        double totalPrice = (numOfDays * price);

        // TODO: Settings (id, key, value, description) page met surcharge en loyalty points hoogte
        // Als er kinderen zijn, komt er een toeslag van 25 euro bovenop
        if (query.getChildren() > 0) {
            totalPrice += 25;
        }
        return totalPrice;
    }


}
package nl.srhodenborgh.royalfruitresorts.controller;

import nl.srhodenborgh.royalfruitresorts.dto.RoomDTO;
import nl.srhodenborgh.royalfruitresorts.dto.RoomSearchDTO;
import nl.srhodenborgh.royalfruitresorts.model.Room;
import nl.srhodenborgh.royalfruitresorts.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(maxAge = 3600)
public class RoomController {
    @Autowired
    private RoomService roomService;



    // Create
    @PostMapping("/create-room")
    public boolean createRoom (@RequestBody Room room, @RequestParam long hotelId) {

//        //BEVEILIGEN MET HTTPREQUEST
//    	Account account = (Account)request.getAttribute("YC_ACCOUNT");
//    	if (account == null) {
//    		return null;
//    	}
//
//    	if (account.getRole() != Role.OWNER) {
//    		return null;
//    	}

    	return roomService.createRoom(room, hotelId);
    }


    // Read
    @GetMapping("/all-rooms")
    public Iterable<Room> getAllRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/room/{id}")
    public Optional<Room> getRoom(@PathVariable ("id") long id){
        return roomService.getRoom(id);
    }

    @GetMapping("/search-rooms")
    public Iterable<RoomDTO> searchRooms(@RequestBody RoomSearchDTO roomSearchDTO) {
        // TODO: parameters fixen in Front end naar RoomSearchDTO omzetten
        return roomService.searchRooms(roomSearchDTO);
    }


    // Update
    @PutMapping("/update-room")
    public boolean updateRoom (@RequestBody Room updatedRoom, @RequestParam long hotelId){
        return roomService.updateRoom(updatedRoom, hotelId);
    }

    @PutMapping("/set-roomdescription/{hotelId}")
    public boolean setRoomDescriptionsByRoomType(@PathVariable ("hotelId") long hotelId, @RequestParam String roomType, @RequestBody(required = false) String description) {
        return roomService.setRoomDescriptionByRoomType(hotelId, roomType, description);
    }



    // Delete
    @DeleteMapping ("/delete-room/{id}")
    public boolean deleteRoom(@PathVariable ("id") long id){
        return roomService.deleteRoom(id);
    }
}

package nl.srhodenborgh.royalfruitresorts.dto;

import nl.srhodenborgh.royalfruitresorts.enums.RoomType;
import nl.srhodenborgh.royalfruitresorts.model.Room;

public class RoomDTO {
    private long id;
    private String hotelName;
    private RoomType roomType;
    private String description;
    private int numBeds;
    private double price;


    public RoomDTO(Room room) {
        this.id = room.getId();
        this.hotelName = room.getHotel().getName();
        this.roomType = room.getRoomType();
        this.description = room.getDescription();
        this.numBeds = room.getNumBeds();
        this.price = room.getPrice();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumBeds() {
        return numBeds;
    }

    public void setNumBeds(int numBeds) {
        this.numBeds = numBeds;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

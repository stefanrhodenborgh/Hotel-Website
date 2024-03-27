package nl.srhodenborgh.royalfruitresorts.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import nl.srhodenborgh.royalfruitresorts.enums.RoomType;
import jakarta.persistence.*;

@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomType roomType;
    @Column(nullable = false, length = 4)
    private int numBeds;
    @Column(length = 1000)
    private String description;
    @Column(nullable = false, length = 10)
    private double price;
    @ManyToOne
    private Hotel hotel;
    @OneToMany(mappedBy = "room")
    private List<Reservation> reservation = new ArrayList<>();



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNumBeds() {
        return numBeds;
    }

    public void setNumBeds(int numBeds) {
        this.numBeds = numBeds;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    @JsonIgnore
    public Hotel getHotel() {
        return hotel;
    }

    @JsonIgnore
    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public List<Reservation> getReservation() {
        return reservation;
    }

    public void setReservation(List<Reservation> reservation) {
        this.reservation = reservation;
    }

}



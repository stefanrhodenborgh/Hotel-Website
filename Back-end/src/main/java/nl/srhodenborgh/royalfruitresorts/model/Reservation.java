package nl.srhodenborgh.royalfruitresorts.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import nl.srhodenborgh.royalfruitresorts.enums.ReservationStatus;
import jakarta.persistence.*;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private LocalDate checkInDate;
    @Column(nullable = false)
    private LocalDate checkOutDate;
    @Column(nullable = false, length = 2)
    private int adults;
    @Column(nullable = false, length = 2)
    private int children;
    @Column(nullable = false)
    private boolean surcharge;
    @Column(length = 500)
    private String specialRequest;
    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus = ReservationStatus.RESERVED;
    private String uuid;
    @ManyToOne
    private Room room;
    @ManyToOne
    private User user;
    @OneToOne(mappedBy = "reservation")
    private Booking booking;




    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public int getAdults() {
        return adults;
    }

    public void setAdults(int adults) {
        this.adults = adults;
    }

    public int getChildren() {
        return children;
    }

    public void setChildren(int children) {
        this.children = children;
    }

    public boolean isSurcharge() {
        return surcharge;
    }

    public void setSurcharge(boolean surcharge) {
        this.surcharge = surcharge;
    }

    public String getSpecialRequest() {
        return specialRequest;
    }

    public void setSpecialRequest(String specialRequest) {
        this.specialRequest = specialRequest;
    }

    public ReservationStatus getReservationStatus() {
        return reservationStatus;
    }

    public void setStatus(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @JsonIgnore
    public Room getRoom() {
        return room;
    }

    @JsonIgnore
    public void setRoom(Room room) {
        this.room = room;
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }

    @JsonIgnore
    public void setUser(User user) {
        this.user = user;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }
}

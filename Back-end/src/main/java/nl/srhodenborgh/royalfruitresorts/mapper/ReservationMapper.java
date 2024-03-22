package nl.srhodenborgh.royalfruitresorts.mapper;

import nl.srhodenborgh.royalfruitresorts.dto.ReservationDTO;
import nl.srhodenborgh.royalfruitresorts.model.Reservation;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {
    public Reservation mapToReservation(ReservationDTO reservationDTO) {
        Reservation reservation = new Reservation();

        reservation.setId(reservationDTO.getReservationId());
        reservation.setCheckInDate(reservationDTO.getCheckInDate());
        reservation.setCheckOutDate(reservationDTO.getCheckOutDate());
        reservation.setAdults(reservationDTO.getAdults());
        reservation.setChildren(reservationDTO.getChildren());
        reservation.setSurcharge(reservationDTO.getChildren() != 0);
        reservation.setSpecialRequest(reservationDTO.getSpecialRequest());

        return reservation;
    }

    public ReservationDTO mapToReservationDTO(Reservation reservation) {
        ReservationDTO reservationDTO = new ReservationDTO();

        reservationDTO.setHotelId(reservation.getRoom().getHotel().getId());
        reservationDTO.setHotelName(reservation.getRoom().getHotel().getName());
        reservationDTO.setRoomId(reservation.getRoom().getId());

        reservationDTO.setReservationId(reservation.getId());
        reservationDTO.setCheckInDate(reservation.getCheckInDate());
        reservationDTO.setCheckOutDate(reservation.getCheckOutDate());
        reservationDTO.setAdults(reservation.getAdults());
        reservationDTO.setChildren(reservation.getChildren());
        reservationDTO.setSpecialRequest(reservation.getSpecialRequest());

        reservationDTO.setUserId(reservation.getUser().getId());
        reservationDTO.setFirstName(reservation.getUser().getFirstName());
        reservationDTO.setLastName(reservation.getUser().getLastName());

        return reservationDTO;
    }
}

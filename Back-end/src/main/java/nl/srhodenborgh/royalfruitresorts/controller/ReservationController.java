package nl.srhodenborgh.royalfruitresorts.controller;

import nl.srhodenborgh.royalfruitresorts.dto.ReservationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nl.srhodenborgh.royalfruitresorts.service.ReservationService;

@RestController
@CrossOrigin(maxAge = 3600)
public class ReservationController {
    @Autowired
    private ReservationService reservationService;



    // Create
    @PostMapping("/create-reservation")
    public String createReservation(@RequestBody ReservationDTO reservationDTO) {
        return reservationService.createReservation(reservationDTO);
    }


    // Read
    @GetMapping("/all-reservations")
    public Iterable<ReservationDTO> getAllReservations(@RequestParam(required = false) String sort){
        return reservationService.getAllReservations(sort);
    }

    @GetMapping("/reservations/{id}")
    public ReservationDTO getReservation(@PathVariable ("id") long id) {
        return reservationService.getReservation(id);
    }

    @GetMapping("/reservation-status")
    public String getReservationStatus(@RequestParam String uuid) {
        return reservationService.getReservationStatus(uuid);
    }



    // Update
    @PutMapping("/update-reservation/{id}")
    public boolean updateReservation(@PathVariable ("id") long id, @RequestBody ReservationDTO updatedReservation) {
        return reservationService.updateReservation(id, updatedReservation);
    }


    // Delete
    @DeleteMapping("/cancel-reservation/{id}")
    public boolean cancelReservation(@PathVariable ("id") long id) {
        return reservationService.cancelReservation(id);
    }
}

package nl.srhodenborgh.royalfruitresorts.controller;

import java.util.Optional;

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

import nl.srhodenborgh.royalfruitresorts.dto.ReservationDTO;
import nl.srhodenborgh.royalfruitresorts.model.User;
import nl.srhodenborgh.royalfruitresorts.service.UserService;

@RestController
@CrossOrigin(maxAge = 3600)
public class UserController {
    @Autowired
    private UserService userService;



    // Create
    @PostMapping("/create-user")
    public boolean createUser (@RequestBody User user) {
        return userService.createUser(user);
    }


    // Read
    @GetMapping("/all-users")
    public Iterable<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/user/{id}")
    public Optional<User> getUser(@PathVariable ("id") long id) {
        return userService.getUser(id);
    }

    @GetMapping("/{id}/reservations")
    public Iterable<ReservationDTO> findReservationsOfUser(@PathVariable ("id") long id, @RequestParam(required = false) String pastOrPresent) {
        // Voer als parameter "past" in of "present"(=default)
        pastOrPresent = pastOrPresent.toLowerCase().trim();
        return userService.findReservationsOfUser(id, pastOrPresent);
    }


    // Update
    @PutMapping ("/update-user/{id}")
    public boolean updateUser(@PathVariable ("id") long id, @RequestBody User updatedUser) {
        return userService.updateUser(id, updatedUser);
    }


    // Delete
    @DeleteMapping ("/delete-user/{id}")
    public boolean deleteUser(@PathVariable ("id") long id) {
        return userService.deleteUser(id);
    }
}

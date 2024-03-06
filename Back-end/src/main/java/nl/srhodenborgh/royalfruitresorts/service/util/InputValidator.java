package nl.srhodenborgh.royalfruitresorts.service.util;

import nl.srhodenborgh.royalfruitresorts.model.*;
import org.springframework.stereotype.Component;

@Component
public class InputValidator {
    public boolean areRequiredFieldsInvalid(Hotel hotel) {
        // Nullable: description

        return hotel.getName() == null ||
                hotel.getStreet() == null ||
                hotel.getHouseNumber() == null ||
                hotel.getZipCode() == null ||
                hotel.getCity() == null ||
                hotel.getCountry() == null;
    }


    private boolean areRequiredFieldsInvalid(Room room) {
        // Nullable: description

        return room.getRoomType() == null ||
                room.getNumBeds() <= 0 ||
                room.getPrice() <= 0.0;
    }


    private boolean areRequiredFieldsInvalid(Reservation reservation) {
        // Nullable: Special Request, uuid
        // Surcharge is op default false

        return reservation.getCiDate() == null ||
                reservation.getCoDate() == null ||
                reservation.getAdults() <= 0 ||
                reservation.getChildren() < 0 ||
                reservation.getReservationStatus() == null;
    }


    private boolean areRequiredFieldsInvalid(Booking booking) {
        // Date wordt automatisch toegewezen bij het creëren van een booking

        return booking.getPaymentMethod() == null;
    }


    private boolean areRequiredFieldsInvalid(User user) {
        // Nullable: phoneNumber

        return user.getFirstName() == null ||
                user.getLastName() == null ||
                user.getDateOfBirth() == null ||
                user.getStreet() == null ||
                user.getHouseNumber() == null ||
                user.getZipCode() == null ||
                user.getCity() == null ||
                user.getCountry() == null ||
                user.getEmail() == null;
    }


    private boolean areRequiredFieldsInvalid(Account account) {
        // Nullable: token

        // stuurt true als hotelId niet overeenkomt met USER_HOTEL_ID, OWNER_HOTEL_ID, of hotelId boven 0
        if (account.getHotelId() != Account.USER_HOTEL_ID ||
        account.getHotelId() != Account.OWNER_HOTEL_ID ||
        account.getHotelId() < Account.USER_HOTEL_ID) {
            return true;
        }

        return account.getPassword() == null ||
                account.getLoyaltyPoints() <= 0 ||
                account.getRole() == null;
    }


    private boolean areRequiredFieldsInvalid(Review review) {
        // Nullable: comment
        // Date wordt automatisch toegewezen bij het creëren van een review

        return review.getName() != null ||
                review.getRating() < 1.0 ||
                review.getRating() > 5.0;
    }
}

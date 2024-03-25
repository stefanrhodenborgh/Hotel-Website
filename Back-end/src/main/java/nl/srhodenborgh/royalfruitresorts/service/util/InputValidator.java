package nl.srhodenborgh.royalfruitresorts.service.util;

import nl.srhodenborgh.royalfruitresorts.dto.ReservationDTO;
import nl.srhodenborgh.royalfruitresorts.dto.RoomSearchDTO;
import nl.srhodenborgh.royalfruitresorts.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class InputValidator {
    private static final String NAME_REGEX = "^[a-zA-Z\\u00C0-\\u017F'\\s]+$";
    private static final String STREET_REGEX = "^[a-zA-Z0-9\\u00C0-\\u017F'\\s.,-]+$";
    private static final String HOUSE_NUMBER_REGEX = "^[a-zA-Z0-9\\s.,-]+$";
    private static final String ZIP_CODE_REGEX = "^\\d{4,}(?:[-\\s]?[a-zA-Z0-9]+)?$";
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String PHONE_NUMBER_REGEX = "^[+()\\d]+$";

    /**
     * (?=.*[A-Za-z]): Positive lookahead assertion for at least one alphabetical character.
     * (?=.*\d): Positive lookahead assertion for at least one digit.
     * [A-Za-z\d@$!%*?&]{8,}: Matches at least 8 characters from the allowed character set, including letters (both uppercase and lowercase), digits, and some special characters (@$!%*?&).
     */
    private static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{8,}$";





    // Overloaded methodes die checken of de fields correct ingevoerd zijn volgens de regex
    public boolean areRequiredFieldsInvalid(Hotel hotel) {
        // Nullable: description

        return isFieldInvalid(hotel.getName(), NAME_REGEX) ||
                isFieldInvalid(hotel.getStreet(), STREET_REGEX) ||
                isFieldInvalid(hotel.getHouseNumber(), HOUSE_NUMBER_REGEX) ||
                isFieldInvalid(hotel.getZipCode(), ZIP_CODE_REGEX) ||
                isFieldInvalid(hotel.getCity(), NAME_REGEX) ||
                isFieldInvalid(hotel.getCountry(), NAME_REGEX);
    }


    public boolean areRequiredFieldsInvalid(Room room) {
        // Nullable: description

        return room.getRoomType() == null ||
                room.getNumBeds() <= 0 ||
                room.getPrice() <= 0.0 ||
                room.getDescription().length() > 1000;
    }


    public boolean areRequiredFieldsInvalid(ReservationDTO reservationDTO) {
        // Nullable: Special Request, uuid
        // Surcharge is op default false

        return reservationDTO.getCheckInDate() == null ||
                reservationDTO.getCheckOutDate() == null ||
                reservationDTO.getCheckInDate().isAfter(reservationDTO.getCheckOutDate()) ||
                reservationDTO.getCheckInDate().isEqual(reservationDTO.getCheckOutDate()) ||
                reservationDTO.getAdults() <= 0 ||
                reservationDTO.getChildren() < 0;
    }


    public boolean areRequiredFieldsInvalid(User user) {
        // Nullable: phoneNumber

        return isFieldInvalid(user.getFirstName(), NAME_REGEX) ||
                isFieldInvalid(user.getLastName(), NAME_REGEX) ||
                isFieldInvalid(user.getStreet(), STREET_REGEX) ||
                isFieldInvalid(user.getHouseNumber(), HOUSE_NUMBER_REGEX) ||
                isFieldInvalid(user.getZipCode(), ZIP_CODE_REGEX) ||
                isFieldInvalid(user.getCity(), NAME_REGEX) ||
                isFieldInvalid(user.getCountry(), NAME_REGEX) ||
                isFieldInvalid(user.getEmail(), EMAIL_REGEX) ||

                isPhoneNumberInvalid(user.getPhoneNumber()) ||
                user.getDateOfBirth() == null;
    }


    public boolean areRequiredFieldsInvalid(Account account) {
        // Nullable: token
        // TODO: loyaltypoints en role weghalen. HotelID ook?
        return isFieldInvalid(account.getPassword(), PASSWORD_REGEX)  ||
                account.getLoyaltyPoints() < 0 ||
                account.getRole() == null ||

                (account.getHotelId() != Account.USER_HOTEL_ID ||
                account.getHotelId() != Account.OWNER_HOTEL_ID ||
                account.getHotelId() < Account.USER_HOTEL_ID);
    }


    public boolean areRequiredFieldsInvalid(Review review) {
        // Nullable: comment
        // Date wordt automatisch toegewezen bij het creëren van een review

        return review.getRating() < 1.0 ||
                review.getRating() > 5.0 ||
                review.getComment().length() > 1000;
    }


    public boolean areRequiredFieldsInvalid(RoomSearchDTO query) {
        return query.getCheckInDate() == null ||
                query.getCheckOutDate() == null ||
                query.getCheckInDate().isAfter(query.getCheckOutDate()) ||
                query.getCheckInDate().isEqual(query.getCheckOutDate()) ||
                query.getAdults() < 1 ||
                query.getChildren() < 0;
    }


    private boolean isFieldInvalid(String fieldValue, String regexPattern) {
        return fieldValue == null || !fieldValue.matches(regexPattern);
    }



    private boolean isPhoneNumberInvalid(String phoneNumber) {
        return !phoneNumber.matches(PHONE_NUMBER_REGEX);
    }

    public boolean isNameInvalid(String name) {
        return isFieldInvalid(name, NAME_REGEX);
    }


    public boolean isPasswordInvalid(String newPassword) {

        return isFieldInvalid(newPassword, PASSWORD_REGEX) ||
                newPassword.length() > 100 ||
                newPassword.isBlank();
    }
}

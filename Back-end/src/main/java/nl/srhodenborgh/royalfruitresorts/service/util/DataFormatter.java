package nl.srhodenborgh.royalfruitresorts.service.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@Component
public class DataFormatter {

    // TODO: DISABLED: Deze class testen en implementeren. Class is nu tijdelijk uitgeschakeld!

    // Lijst met String fields die niet door de formatStringField gaan
    // zipCode en email hebben een eigen methode voor formatting
    private final List<String> skipFormatList = Arrays.asList("description", "specialRequest", "uuid", "password", "token", "comment", "imagePath");
    private static final Logger logger = LoggerFactory.getLogger(DataFormatter.class);


    public void formatFields(Object object) {
        logger.warn("formatFields() method is disabled!");

        // Uncomment deze sectie:

        // Gaat alle fields van object af en als het een String Field is stuurt hij de field door naar formatStringField
//        for (Field field : object.getClass().getDeclaredFields()) {
//            if (field.getType().equals(String.class)) {
//                formatStringField(object, field);
//            }
//        }
    }

    private void formatStringField(Object object, Field field) {
        try {

            field.setAccessible(true);
            String value = (String) field.get(object);

            if (field.getName().equals("zipCode")) {
                field.set(object, formatZipCode(value));
                return;
            }

            if (field.getName().equals("email")) {
                field.set(object, formatEmail(value));
                return;
            }

            if (skipFormatList.contains(field.getName())) {
                field.set(object, value.trim());
            } else {
                field.set(object, capitalizeEachWord(value));
            }

        } catch (IllegalAccessException e) {
            logger.error("Error while formatting '{}' field of object '{}'", field.getName(), object);
        }
    }



    private String capitalizeEachWord(String input) {
        // Methode die elk eerste letter van het woord omzet in een hoofdletter
        if (input == null || input.isEmpty()) {
            return input;
        }

        String[] words = input.trim().split("\\s+");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (!Character.isLetter(word.charAt(0))) {
                // Als het woord niet met een letter begint laat hij het zoals het is
                result.append(word).append(" ");
            } else {
                char firstChar = Character.toUpperCase(word.charAt(0));
                String restOfWord = word.substring(1).toLowerCase();

                result.append(firstChar).append(restOfWord).append(" ");
            }
        }

        return result.toString();
    }

    private String formatZipCode(String zipCode) {
        return zipCode.toUpperCase().trim();
    }

    private String formatEmail(String email) {
        return email.toLowerCase().trim();
    }
}

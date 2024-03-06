package nl.srhodenborgh.royalfruitresorts.service.util;

import nl.srhodenborgh.royalfruitresorts.model.Hotel;

public class DataFormatter {
    private String capitalizeEachWord(String input) {
        // Methode die elk eerste letter van het woord omzet in een hoofdletter
        if (input == null || input.isEmpty()) {
            return input;
        }

        String[] words = input.split("\\s+");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            char firstChar = Character.toUpperCase(word.charAt(0));
            String restOfWord = word.substring(1).toLowerCase();

            result.append(firstChar).append(restOfWord).append(" ");
        }

        return result.toString().trim();
    }


    public Hotel formatHotelData(Hotel hotel) {
        // Methode die de data van hotel in een standaard formaat zet in de database

        hotel.setName(capitalizeEachWord(hotel.getName()));
        hotel.setStreet(capitalizeEachWord(hotel.getStreet()));

        hotel.setHouseNumber(hotel.getHouseNumber().trim());
        hotel.setZipCode(hotel.getZipCode().trim());

        hotel.setCity(capitalizeEachWord(hotel.getCity()));
        hotel.setCountry(capitalizeEachWord(hotel.getCountry()));
        hotel.setDescription(hotel.getDescription().trim());

        return hotel;
    }
}

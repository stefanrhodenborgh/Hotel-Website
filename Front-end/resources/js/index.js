//Startup script:
document.addEventListener('DOMContentLoaded', async function () {
    // Hoteldropdown vullen met hotels
    await getAllHotels().then(hotels => {
        populateDropdown(hotels, "hotelDropdown", 1)
    });
});


document.addEventListener('DOMContentLoaded', function () {
    // Datum ophalen in format: YYYY-MM-DD
    // Minimumdatum van checkin op vandaag zetten
    let today = new Date();
    document.getElementById("checkIn").min = today.toISOString().split('T')[0];

    // Minimumdatum van checkout op morgen zetten
    let tomorrow = new Date(today);
    tomorrow.setDate(today.getDate() + 1);
    document.getElementById("checkOut").min = tomorrow.toISOString().split('T')[0];

    // On load checkin- en out datum zetten
    document.getElementById("checkIn").value = today.toISOString().split('T')[0];
    document.getElementById("checkOut").value = tomorrow.toISOString().split('T')[0];

    // On load adults value op 2, children op 0 zetten
    document.getElementById("adults").value = 2;
    document.getElementById("children").value = 0;

});


async function searchRooms() {
    // Elementen ophalen
    const hotelDropdown = document.getElementById("hotelDropdown");
    const checkInInput = document.getElementById("checkIn");
    const checkOutInput = document.getElementById("checkOut");
    const adultsInput = document.getElementById("adults");
    const childrenInput = document.getElementById("children");
    const roomTypeInput = document.getElementById("roomTypeDropDown");

    // HotelName krijgen uit de textwaarde van geselecteerde optie in hotelDropdown
    const selectedOption = hotelDropdown.options[hotelDropdown.selectedIndex];
    const hotelName = selectedOption.textContent;

    // RoomType krijgen uit de textwaarde van geselecteerde optie in roomTypeDropdown
    const selectedOptionRoom = roomTypeDropdown.options[roomTypeDropdown.selectedIndex];
    const roomTypeChoice = selectedOption.textContent;

    // Parameters om de beschikbare kamers mee te zoeken
    const hotelId = hotelDropdown.value;
    const checkInDate = checkInInput.value;
    const checkOutDate = checkOutInput.value;
    const adults = adultsInput.value;
    const children = childrenInput.value;
    const roomType = roomTypeInput.value;

    // Object maken van de query om later door te geven
    const query = {
        hotelId,
        hotelName,
        checkInDate,
        checkOutDate,
        adults,
        children,
        roomType
    };


    // Stopt deze functie als er minder dan 1 volwassene is ingevoerd
    if (adults < 1) {
        alert("Rooms may not be reserved without an adult present")
        return;
    }
}


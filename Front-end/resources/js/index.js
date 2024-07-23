//Startup script:
document.addEventListener('DOMContentLoaded', function () {
    // Hoteldropdown vullen met hotels
    getAllHotels().then(hotels => {
        populateDropdown(hotels, "hotelDropdown", 1)
    });
    disableKeyboardInput();
    loadCarousel();
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

    // HotelName krijgen uit de textwaarde van geselecteerde optie in hotelDropdown
    const selectedOption = hotelDropdown.options[hotelDropdown.selectedIndex];
    const hotelName = selectedOption.textContent;

    // Parameters om de beschikbare kamers mee te zoeken
    const hotelId = hotelDropdown.value;
    const checkInDate = checkInInput.value;
    const checkOutDate = checkOutInput.value;
    const adults = adultsInput.value;
    const children = childrenInput.value;

    // Object maken van de query om later door te geven
    const query = {
        hotelId,
        hotelName,
        checkInDate,
        checkOutDate,
        adults,
        children
    };


    // Stopt deze functie als er minder dan 1 volwassene is ingevoerd
    if (adults < 1) {
        alert("Rooms may not be reserved without an adult present")
        return;
    }

}
    // function loadCarousel() {
    //     // Alle hotelfoto's in de carousel laden

    //     const 

    //     let carousel = 
    //     `<div class="carousel-item active">
    //         <img src="./resources/images/hotels/hotel 1/1.webp" class="d-block w-100" alt="..." style="filter:brightness(70%);">
    //         <div class="carousel-caption d-none d-md-block">
    //         <h5>Eindhoven Inn</h5>
    //         <p>Where style meets excellence </p>
    //         <button class="btn btn-outline-light btn-md">Explore</button>
    //         </div>
    //     </div>`
    // }

    function loadCarousel() {
        // Initialize the Bootstrap Carousel with interval set to false
        let myCarousel = new bootstrap.Carousel(document.getElementById('carousel'), {
            interval: false // Disable automatic sliding
        });
    
        // Add event listeners for manual navigation
        document.getElementById('prevBtn').addEventListener('click', function () {
            myCarousel.prev();
        });
    
        document.getElementById('nextBtn').addEventListener('click', function () {
            myCarousel.next();
        });
    }

    async function changeHotel() {
        // Hotel uit dropdown halen
        const dropdown = document.getElementById("hotelDropdown");
        const selectedOption = dropdown.options[dropdown.selectedIndex];
        const selectedText = selectedOption.text;
    
        // Locatie van hotel vinden
        const res = await fetch(url+"/hotel/" + selectedOption.value)
        const hotel = await res.json();
    
        // Geselecteerde hotel in titel zetten
        const hotelValue = document.getElementById('hotelValue');
        hotelValue.textContent = selectedText + " situated at " + hotel.city;
    }

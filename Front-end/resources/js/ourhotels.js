// Startup script:
document.addEventListener('DOMContentLoaded', async function () {
    // Hoteldropdown vullen met hotels
    await getAllHotels().then(hotels => {
        populateDropdown(hotels, "hotelDropdown", 1)
        document.getElementById("explore-btn").click()
    });
    // Carousel inladen
    loadCarousel();
    // Minimum en maximum checkin en checkout datum inladen
    loadMinMaxDates();
});


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


function loadMinMaxDates() {
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
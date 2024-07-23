// Lokaal
const url = "http://127.0.0.1:8080";


// function getAllHotels() {
//     const hotels = fetch(url + "/all-hotels");
//     return hotels.json();
// }

function getAllHotels() {
    return fetch(url + "/all-hotels")
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .catch(error => {
            console.error('Error fetching hotels:', error);
            return null; 
        });
}


function getRoomTypes() {
    return fetch(url + "/room-types")
    .then(response => {
        if (!response.ok) {
            throw new Error("Network response was not ok");
        }
        return response.json();
    }) 
    .catch(error => {
        console.error('Error fetching roomTypes:', error);
            return null; 
        });
}


function populateDropdown(items, elementId, selectValue) {
    // Methode om een dropdown(elementId) te vullen met items en de selector op een item te zetten
    const dropdown = document.getElementById(elementId);

    items.forEach(item => {
        const option = document.createElement("option");
        option.value = item.id;
        option.textContent = item.name;
        dropdown.appendChild(option);
    });
    dropdown.value = selectValue;
}


// function populateEnumDropdown(items, elementId, selectValue) {
//     const dropdown = document.getElementById(elementId);

//     items.forEach(item => {
//         const option = document.createElement("option");
        
//         let text = item.replace('_', ' ');
//         text = text.charAt(0).toUpperCase() + text.substring(1).toLowerCase();
//         option.textContent = text;
//         option.value = item;

//         dropdown.appendChild(option);
//     });
//     // -1 is correctie zodat de eerste optie 1 is en niet 0. Dit wegens consistentie met andere functies
//     dropdown.value = items[selectValue - 1];
// }


function setMinCheckOutDate() {
    // Minimumdatum van checkout op volgende dag zetten indien checkin na checkout is
    let checkInDate = new Date(document.getElementById("checkIn").value);
    let checkOutDate = new Date(document.getElementById("checkOut").value);
    
    // Minimumdatum checkOut beweegt mee met de checkIn
    let nextDay = new Date(checkInDate);
    nextDay.setDate(checkInDate.getDate() + 1);   
    document.getElementById("checkOut").min = nextDay.toISOString().split('T')[0];
    
    // Zet de waarde op de volgende dag t.o.v. checkIn indien de checkIn datum na de checkOut is geprikt
    if (checkInDate >= checkOutDate) {    
        document.getElementById("checkOut").value = nextDay.toISOString().split('T')[0];
    }
}


// Function to disable keyboard input for specific date input fields
function disableKeyboardInput() {
    const disableKeyboardInputs = document.querySelectorAll('.disable-keyboard-input[type="date"]');
    disableKeyboardInputs.forEach(input => {
        input.addEventListener('keydown', event => {
            event.preventDefault();
        });
    });
}
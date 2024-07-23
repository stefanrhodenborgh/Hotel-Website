//Startup script:
document.addEventListener('DOMContentLoaded', async function () {
    // Hoteldropdown vullen met hotels
    await getAllHotels().then(hotels => {
        populateDropdown(hotels, "hotelDropdown", 1)
        populateDropdown(hotels, "hotelDropdown2", 1)
    });
    getAllReservations();

    displayRooms();

    // Datum ophalen in format: YYYY-MM-DD
    // Minimumdatum van checkin op vandaag zetten
    let today = new Date();
    document.getElementById("checkIn").min = today.toISOString().split('T')[0]; 
});


async function displayRooms() {
    //methode om alle kamers uit de hotel dropdown te laten zien.
    //methode werkt op onchange, dus wordt aangeroepen als de hotel dropdown waarde verandert.
    const hotelId = document.getElementById("hotelDropdown2").value;

    await fetch(url+"/hotel/" + hotelId + "/rooms")
    .then(rooms => rooms.json())
    .then(rooms => {
        let roomshtml = `
            <option value="unassigned">Unassigned</option>
            `
        for (let i=0; i<rooms.length; i++) {
            roomshtml +=`
            <option value="${rooms[i].id}">${rooms[i].id}</option>
            `
        }
        
        document.getElementById("roomDropdown").innerHTML = roomshtml;
        document.getElementById("roomDropdown").value = "unassigned";
    })
}


async function getAllReservations(){
    let sort = document.getElementById("sort").value;

    await fetch(url+"/all-reservations?sort=" + sort)
    .then(res => res.json())
    .then (reservations => {
        let reservationshtml = `
        <tr id="reservations-header">
            <td>Hotel id</td>
            <td>Hotel</td>
            <td>Room id</td>
            <td>Reservation id</td>
            <td>Check-in date</td>
            <td>Check-out date</td>
            <td>Adults</td>
            <td>Children</td>
            <td>Surcharge</td>
            <td>Status</td>
            <td>User id</td>
            <td>First name</td>
            <td>Last name</td>
        </tr>
        `

        for (let i=0; i<reservations.length; i++) {
            reservationshtml +=`
            <tr>
                <td>${reservations[i].hotelId}</td>
                <td>${reservations[i].hotelName}</td>
                <td>${reservations[i].roomId}</td>
                <td>${reservations[i].reservation.id}</td>
                <td>${reservations[i].reservation.checkInDate}</td>
                <td>${reservations[i].reservation.checkOutDate}</td>
                <td>${reservations[i].reservation.adults}</td>
                <td>${reservations[i].reservation.children}</td>
                <td>${reservations[i].reservation.surcharge}</td>
                <td>${reservations[i].reservation.status}</td>                        
                <td>${reservations[i].userId}</td>
                <td>${reservations[i].firstName}</td>
                <td>${reservations[i].lastName}</td>
                <td><button onclick="editReservation(${reservations[i].reservation.id})">Edit reservation</button></td>
                <td><button onclick="deleteReservation(${reservations[i].reservation.id})">Delete reservation</button></td>
            </tr>
            `                            
        }
        document.getElementById("reservations").innerHTML = reservationshtml;
    })
}


async function createReservation() {
    let resDTO =  {
        "hotelId": document.getElementById("hotelDropdown2").value,
        "roomId": document.getElementById("roomDropdown").value,
        "reservation": {
            "checkInDate": document.getElementById("checkIn").value,
            "checkOutDate": document.getElementById("checkOut").value,
            "adults": document.getElementById("adults").value,
            "children": document.getElementById("children").value,    
        },
        "userId": 3,
    };
    console.log(resDTO.reservation.checkInDate);

    if (resDTO.reservation.checkInDate == null) {
        alert("Please fill in check-in date")
    } else if (resDTO.reservation.checkOutDate == null) {
        alert("Please fill in check-out date")
    } else if (resDTO.reservation.adults == null) {
        alert("Please fill in the number of adults")
    } else if (resDTO.reservation.children == null) {
        alert("Please fill in the number of children")
    } else {
        const res = await fetch(url+"/create-reservation", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(resDTO),
        });

        const createdReservation = await res.json();
        getAllReservations();
        // if (createdReservation !== null) {
            
        //     console.log(createdReservation);
        //     assignRoom(createdReservation);
        //     assignUser(createdReservation);
        //     alert("Reservation successfully created");
            
}
}


async function deleteReservation(reservationId) {
    await fetch(url+"/deletereservation/" + reservationId);
    alert("Reservation successfully deleted");
    getAllReservations();
}

async function editReservation(reservationId, hotelId) {
    await fetch(url+"/update-reservation/" + reservationId)
    .then(res => res.json())
    .then(reservation => {
        let form = `
        <h2>Edit Room</h2>
        <label>Hotel: </label>
        <select id="editHotelDropdown"></select><br>

        <label>Room type: </label>
        <select id="editRoomTypeDropdown" value="${reservation.roomType}">
            <option value="SINGLE_ROOM">Single room</option>
            <option value="DOUBLE_ROOM">Double room</option>
            <option value="FAMILY_SUITE">Family suite</option>
        </select><br>

        <label>Number of beds</label>
        <input type="text: " id="editnumBeds" value="${reservation.numBeds}"><br>
        <label>Price: </label>
        <input type="text" id="editPrice" value="${reservation.price}"><br>
        <button onclick="submitRoomForm(${reservation.id})">Save changes</button>      
        `
    });
}

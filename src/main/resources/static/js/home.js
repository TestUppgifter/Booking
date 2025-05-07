document.addEventListener('DOMContentLoaded', () => {
    const username = localStorage.getItem('username');
    if (!username) window.location.href = '/login';

    // Display username
    document.getElementById('username-display').textContent = `Welcome, ${username}!`;

    // Load machines and bookings
    loadMachines();
    loadBookings();
});

async function loadMachines() {
    try {
        const response = await fetch('/api/machines');
        const machines = await response.json();

        const container = document.getElementById('machine-container');
        container.innerHTML = machines.map(machine => `
            <div class="machine-card ${machine.status === 'available' ? 'available' : 'booked'}">
                <h3>Machine #${machine.id}</h3>
                <p>Type: ${machine.type}</p>
                <p>Status: ${machine.status.toUpperCase()}</p>
                ${machine.status === 'available' ?
                    `<button onclick="bookMachine(${machine.id})">Book Now</button>` :
                    `<p>Available at: ${new Date(machine.availableAt).toLocaleTimeString()}</p>`}
            </div>
        `).join('');
    } catch (error) {
        console.error('Error loading machines:', error);
    }
}

async function bookMachine(machineId) {
    try {
        const response = await fetch(`/api/bookings`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            },
            body: JSON.stringify({
                machineId,
                duration: 60 // 60 minutes default
            })
        });

        if (response.ok) {
            alert('Booking successful!');
            loadMachines();
            loadBookings();
        }
    } catch (error) {
        console.error('Booking failed:', error);
    }
}

async function loadBookings() {
    try {
        const response = await fetch('/api/bookings', {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        });
        const bookings = await response.json();

        const container = document.getElementById('bookings-container');
        container.innerHTML = bookings.map(booking => `
            <div class="booking-item">
                <p>Machine #${booking.machineId}</p>
                <p>Time: ${new Date(booking.startTime).toLocaleString()} -
                   ${new Date(booking.endTime).toLocaleTimeString()}</p>
                <button onclick="cancelBooking('${booking.id}')">Cancel</button>
            </div>
        `).join('');
    } catch (error) {
        console.error('Error loading bookings:', error);
    }
}

async function cancelBooking(bookingId) {
    try {
        const response = await fetch(`/api/bookings/${bookingId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        });

        if (response.ok) {
            loadBookings();
            loadMachines();
        }
    } catch (error) {
        console.error('Cancellation failed:', error);
    }
}
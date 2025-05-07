document.getElementById('registerForm').addEventListener('submit', async function(e) {
    e.preventDefault();

    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const role = document.getElementById('role').value;
    const errorMessage = document.getElementById('error-message');

    try {
        const response = await fetch('/api/auth/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email, password, role })
        });

        if (response.ok) {
            window.location.href = '/login';
        } else {
            const errorData = await response.json();
                if (errorData.code === "Conflict") {
                    errorMessage.textContent = "User already exists";
                } else if (errorData.code === "Validation error") {
                    errorMessage.textContent = errorData.message;
                } else {
                    errorMessage.textContent = errorData.message || 'Registration failed';
                }
        }
    } catch (error) {
        errorMessage.textContent = 'An error occurred during registration';
    }
});
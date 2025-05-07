document.getElementById('loginForm').addEventListener('submit', async function(e) {
    e.preventDefault();

    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const errorMessage = document.getElementById('error-message');

    try {
        const response = await fetch('/api/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email, password })
        });

        if (response.ok) {
            window.location.href = '/';
        } else {
            const errorData = await response.json();
            errorMessage.textContent = 'Invalid email or password';
        }
    } catch (error) {
        errorMessage.textContent = 'An error occurred while logging in';
    }
});
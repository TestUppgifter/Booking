document.addEventListener('DOMContentLoaded', function() {
    const errorMessage = document.getElementById('error-message');

    // Check for error parameter in URL
    const urlParams = new URLSearchParams(window.location.search);
    const error = urlParams.get('error');

    if (error) {
        errorMessage.textContent = 'Authentication failed. Please try again.';
    }
});
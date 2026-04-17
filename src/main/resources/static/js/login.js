/**
 * Login — handles user authentication and credential management.
 *
 * @author Kauan Santos Ferreira
 * @since 2026
 */

// ==================== Toggle Password Visibility ====================

function togglePassword() {
    var input = document.getElementById('password');
    input.type = input.type === 'password' ? 'text' : 'password';
}

// ==================== Fill Demo Credentials ====================

function fillCredentials(email, password) {
    document.getElementById('email').value = email;
    document.getElementById('password').value = password;
}

// ==================== Error Display ====================

function showError(message) {
    var el = document.getElementById('errorMessage');
    el.textContent = message;
    el.style.display = 'block';
}

// ==================== Login Form Submit ====================

document.getElementById('loginForm').addEventListener('submit', function(e) {
    e.preventDefault();

    var email = document.getElementById('email').value;
    var password = document.getElementById('password').value;
    var btn = this.querySelector('button[type="submit"]');

    btn.disabled = true;
    btn.textContent = 'Signing in...';

    fetch('/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email: email, password: password })
    })
        .then(function(response) {
            if (response.ok) {
                return response.json().then(function(data) {
                    localStorage.setItem('smartorder-token', data.token);
                    localStorage.setItem('smartorder-user', JSON.stringify({
                        id: data.id,
                        name: data.name,
                        email: data.email,
                        role: data.role
                    }));

                    if (data.role === 'ADMIN') {
                        window.location.href = '/admin/dashboard';
                    } else {
                        window.location.href = '/store';
                    }
                });
            } else {
                return response.json().then(function(err) {
                    showError(err.message || 'Invalid email or password');
                });
            }
        })
        .catch(function() {
            showError('Connection error. Please try again.');
        })
        .finally(function() {
            btn.disabled = false;
            btn.textContent = 'Sign in';
        });
});
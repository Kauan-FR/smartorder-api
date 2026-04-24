/**
 * Register — handles customer account creation and auto-login.
 *
 * @author Kauan Santos Ferreira
 * @since 2026
 */

// ==================== Toggle Password Visibility ====================

document.getElementById('togglePassword').addEventListener('click', function () {
    var input = document.getElementById('password');
    input.type = input.type === 'password' ? 'text' : 'password';
});

document.getElementById('toggleConfirm').addEventListener('click', function () {
    var input = document.getElementById('confirmPassword');
    input.type = input.type === 'password' ? 'text' : 'password';
});

// ==================== Field Error Display ====================

function showFieldError(fieldId, message) {
    var el = document.getElementById(fieldId);
    el.textContent = message;
    el.classList.remove('hidden');
}

function clearFieldError(fieldId) {
    var el = document.getElementById(fieldId);
    el.textContent = '';
    el.classList.add('hidden');
}

function clearAllErrors() {
    clearFieldError('nameError');
    clearFieldError('emailError');
    clearFieldError('passwordError');
    clearFieldError('confirmError');

    var globalError = document.getElementById('registerError');
    globalError.textContent = '';
    globalError.classList.add('hidden');
}

// ==================== Global Error Display ====================

function showGlobalError(message) {
    var el = document.getElementById('registerError');
    el.textContent = message;
    el.classList.remove('hidden');
}

// ==================== Client-side Validation ====================

function validate(name, email, password, confirm) {
    var valid = true;

    if (!name || name.trim().length < 2) {
        showFieldError('nameError', I18n.get('registerJs.nameRequired') || 'Full name is required.');
        valid = false;
    }

    var emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!email || !emailPattern.test(email)) {
        showFieldError('emailError', I18n.get('registerJs.emailInvalid') || 'Enter a valid email.');
        valid = false;
    }

    if (!password || password.length < 6) {
        showFieldError('passwordError', I18n.get('registerJs.passwordShort') || 'Password must be at least 6 characters.');
        valid = false;
    }

    if (password !== confirm) {
        showFieldError('confirmError', I18n.get('registerJs.passwordMismatch') || 'Passwords do not match.');
        valid = false;
    }

    return valid;
}

// ==================== Register Submit ====================

document.getElementById('registerBtn').addEventListener('click', function () {
    clearAllErrors();

    var name     = document.getElementById('name').value.trim();
    var email    = document.getElementById('email').value.trim();
    var password = document.getElementById('password').value;
    var confirm  = document.getElementById('confirmPassword').value;

    if (!validate(name, email, password, confirm)) {
        return;
    }

    var btn     = document.getElementById('registerBtn');
    var btnText = document.getElementById('registerBtnText');
    var spinner = document.getElementById('registerBtnSpinner');

    btn.disabled = true;
    btnText.classList.add('hidden');
    spinner.classList.remove('hidden');

    fetch('/api/auth/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name: name, email: email, password: password })
    })
        .then(function (response) {
            if (response.ok) {
                return response.json().then(function (data) {
                    localStorage.setItem('smartorder-token', data.token);
                    localStorage.setItem('smartorder-user', JSON.stringify({
                        id: data.id,
                        name: data.name,
                        email: data.email,
                        role: data.role
                    }));

                    window.location.href = '/store';
                });
            } else {
                return response.json().then(function (err) {
                    if (err.status === 409) {
                        showFieldError('emailError', I18n.get('registerJs.emailTaken') || 'Email already in use.');
                    } else if (err.status === 400) {
                        showGlobalError(err.message || I18n.get('registerJs.invalidData') || 'Invalid data. Check the fields.');
                    } else {
                        showGlobalError(I18n.get('registerJs.server') || 'Internal error. Please try again.');
                    }
                });
            }
        })
        .catch(function () {
            showGlobalError(I18n.get('registerJs.connection') || 'Connection error. Please try again.');
        })
        .finally(function () {
            btn.disabled = false;
            btnText.classList.remove('hidden');
            spinner.classList.add('hidden');
        });
});
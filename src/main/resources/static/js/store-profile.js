/**
 * Store Profile — customer account information and password management.
 *
 * Mirrors admin-profile.js logic but adapted for the customer storefront context
 * (uses customer-navbar instead of admin sidebar/topbar).
 *
 * @author Kauan Santos Ferreira
 * @since 2026
 */

// ==================== Auth Guard ====================

AuthManager.guard();

// ==================== User Info ====================

var user = AuthManager.getUser();
if (user) {
    var initials = AuthManager.getInitials();

    // Profile header
    document.getElementById('profileHeaderName').textContent = user.name;
    document.getElementById('profileHeaderEmail').textContent = user.email;
    document.getElementById('profileAvatarInitials').textContent = initials;
    document.getElementById('formAvatarInitials').textContent = initials;
}

// ==================== API Helpers ====================

/**
 * Builds the standard authenticated request headers.
 *
 * @return an object with Authorization and Content-Type headers
 */
function getHeaders() {
    return {
        'Authorization': 'Bearer ' + AuthManager.getToken(),
        'Content-Type': 'application/json'
    };
}

// ==================== Tabs ====================

/**
 * Switches the active settings tab.
 *
 * @param tabName the tab identifier ('account' or 'security')
 * @param element the clicked tab button element
 */
function switchTab(tabName, element) {
    document.querySelectorAll('.settings-tab').forEach(function (tab) {
        tab.classList.remove('settings-tab--active');
    });
    document.querySelectorAll('.settings-panel').forEach(function (panel) {
        panel.classList.remove('settings-panel--active');
    });

    element.classList.add('settings-tab--active');
    document.getElementById('tab-' + tabName).classList.add('settings-panel--active');
}

// ==================== Load Account Info ====================

/**
 * Populates the account form with the current user's data.
 */
function loadAccountInfo() {
    if (!user) return;

    document.getElementById('accountName').value = user.name || '';
    document.getElementById('accountEmail').value = user.email || '';
    document.getElementById('accountPhone').value = user.phone || '';
    document.getElementById('accountImageUrl').value = user.profileImageUrl || '';

    loadProfileAvatar(user.profileImageUrl);
    previewAccountPhoto();
}

/**
 * Loads the profile photo into the page header avatar.
 * Falls back to initials if the URL is empty or fails to load.
 *
 * @param url the profile image URL (may be null or empty)
 */
function loadProfileAvatar(url) {
    var img = document.getElementById('profileAvatarImg');
    var initials = document.getElementById('profileAvatarInitials');

    if (url) {
        img.src = url;
        img.onload = function () {
            img.style.display = 'block';
            initials.style.display = 'none';
        };
        img.onerror = function () {
            img.style.display = 'none';
            initials.style.display = 'flex';
        };
    } else {
        img.style.display = 'none';
        initials.style.display = 'flex';
    }
}

/**
 * Live preview of the profile photo as the user types the URL.
 * Updates both the form preview and the page header avatar.
 */
function previewAccountPhoto() {
    var url = document.getElementById('accountImageUrl').value.trim();

    var formImg = document.getElementById('formAvatarImg');
    var formInitials = document.getElementById('formAvatarInitials');
    var headerImg = document.getElementById('profileAvatarImg');
    var headerInitials = document.getElementById('profileAvatarInitials');

    if (url) {
        formImg.src = url;
        formImg.onload = function () {
            formImg.style.display = 'block';
            formInitials.style.display = 'none';
            headerImg.src = url;
            headerImg.style.display = 'block';
            headerInitials.style.display = 'none';
        };
        formImg.onerror = function () {
            formImg.style.display = 'none';
            formInitials.style.display = 'flex';
            headerImg.style.display = 'none';
            headerInitials.style.display = 'flex';
        };
    } else {
        formImg.style.display = 'none';
        formInitials.style.display = 'flex';
        headerImg.style.display = 'none';
        headerInitials.style.display = 'flex';
    }
}

// ==================== Save Account Info ====================

/**
 * Persists the account information via PUT /api/users/{id}.
 * Updates the local user cache and refreshes header avatar on success.
 */
function saveAccountInfo() {
    var name = document.getElementById('accountName').value.trim();
    var email = document.getElementById('accountEmail').value.trim();
    var phone = document.getElementById('accountPhone').value.trim();
    var imageUrl = document.getElementById('accountImageUrl').value.trim();

    if (!name) {
        showToast(I18n.get('profile.errName') || 'Name is required', 'error');
        return;
    }
    if (!email) {
        showToast(I18n.get('profile.errEmail') || 'Email is required', 'error');
        return;
    }

    var body = {
        name: name,
        email: email,
        profileImageUrl: imageUrl || null,
        role: user.role,
        phone: phone || null
    };

    fetch('/api/users/' + parseInt(user.id), {
        method: 'PUT',
        headers: getHeaders(),
        body: JSON.stringify(body)
    })
        .then(function (r) {
            if (r.ok) {
                return r.json().then(function (updated) {
                    user.name = updated.name;
                    user.email = updated.email;
                    user.phone = updated.phone;
                    user.profileImageUrl = updated.profileImageUrl;
                    AuthManager.setUser(user);

                    document.getElementById('profileHeaderName').textContent = updated.name;
                    document.getElementById('profileHeaderEmail').textContent = updated.email;
                    loadProfileAvatar(updated.profileImageUrl);

                    showToast(I18n.get('profile.accountSaved') || 'Account updated', 'success');
                });
            } else {
                return r.json().then(function (err) {
                    showToast(err.message || I18n.get('profile.errUpdate') || 'Update failed', 'error');
                });
            }
        })
        .catch(function () {
            showToast(I18n.get('common.connectionError') || 'Connection error', 'error');
        });
}

// ==================== Security (Password) ====================

/**
 * Updates the user's password via PATCH /api/users/{id}/password.
 * Validates current password, new password length, and confirmation match
 * before sending the request.
 */
function updatePassword() {
    var current = document.getElementById('currentPassword').value;
    var newPass = document.getElementById('newPassword').value;
    var confirm = document.getElementById('confirmPassword').value;

    hidePasswordError();

    if (!current) {
        showPasswordError(I18n.get('profile.errCurrentPass') || 'Current password is required');
        return;
    }
    if (!newPass || newPass.length < 8) {
        showPasswordError(I18n.get('profile.errNewPassLen') || 'New password must be at least 8 characters');
        return;
    }
    if (newPass !== confirm) {
        showPasswordError(I18n.get('profile.errPassMatch') || 'Passwords do not match');
        return;
    }

    var body = JSON.stringify({
        currentPassword: current,
        newPassword: newPass
    });

    fetch('/api/users/' + parseInt(user.id) + '/password', {
        method: 'PATCH',
        headers: getHeaders(),
        body: body
    })
        .then(function (r) {
            if (r.ok) {
                document.getElementById('currentPassword').value = '';
                document.getElementById('newPassword').value = '';
                document.getElementById('confirmPassword').value = '';
                showToast(I18n.get('profile.passUpdated') || 'Password updated', 'success');
            } else {
                return r.json().then(function (err) {
                    showPasswordError(err.message || I18n.get('profile.errUpdate') || 'Update failed');
                });
            }
        })
        .catch(function () {
            showPasswordError(I18n.get('common.connectionError') || 'Connection error');
        });
}

/**
 * Displays an inline error message in the security panel.
 *
 * @param msg the error message to display
 */
function showPasswordError(msg) {
    var el = document.getElementById('passwordError');
    el.textContent = msg;
    el.classList.add('is-visible');
}

/**
 * Hides the inline password error message.
 */
function hidePasswordError() {
    document.getElementById('passwordError').classList.remove('is-visible');
}

// ==================== Initialize ====================

loadAccountInfo();
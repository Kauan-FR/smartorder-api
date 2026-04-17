/**
 * Admin Profile — account information and password management.
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
    document.getElementById('sidebarName').textContent = user.name;
    document.getElementById('sidebarAvatar').textContent = initials;
    document.getElementById('popupName').textContent = user.name;
    document.getElementById('popupAvatar').textContent = initials;

    // Profile header
    document.getElementById('profileName').textContent = user.name;
    document.getElementById('profileEmail').textContent = user.email;
    document.getElementById('profileRole').textContent = user.role;
    document.getElementById('profileAvatarInitials').textContent = initials;
    document.getElementById('formAvatarInitials').textContent = initials;
}

// ==================== Sidebar ====================

function toggleSidebar() {
    var sidebar = document.getElementById('sidebar');
    sidebar.classList.toggle('is-collapsed');
    localStorage.setItem('smartorder-sidebar', sidebar.classList.contains('is-collapsed') ? 'collapsed' : 'expanded');
}

if (localStorage.getItem('smartorder-sidebar') === 'collapsed') {
    document.getElementById('sidebar').classList.add('is-collapsed');
}

// ==================== Profile Popup ====================

function toggleProfilePopup() {
    document.getElementById('profilePopup').classList.toggle('is-open');
}

document.addEventListener('click', function(e) {
    var popup = document.getElementById('profilePopup');
    var trigger = document.getElementById('profileTrigger');
    if (popup.classList.contains('is-open') && !trigger.contains(e.target) && !popup.contains(e.target)) {
        popup.classList.remove('is-open');
    }
});

function signOut() {
    AuthManager.logout();
}

// ==================== API Helpers ====================

function getHeaders() {
    return {
        'Authorization': 'Bearer ' + AuthManager.getToken(),
        'Content-Type': 'application/json'
    };
}

// ==================== Tabs ====================

function switchTab(tabName, element) {
    document.querySelectorAll('.settings-tab').forEach(function(tab) {
        tab.classList.remove('settings-tab--active');
    });
    document.querySelectorAll('.settings-panel').forEach(function(panel) {
        panel.classList.remove('settings-panel--active');
    });

    element.classList.add('settings-tab--active');
    document.getElementById('tab-' + tabName).classList.add('settings-panel--active');
}

// ==================== Load Account Info ====================

function loadAccountInfo() {
    if (!user) return;

    document.getElementById('accountName').value = user.name || '';
    document.getElementById('accountEmail').value = user.email || '';
    document.getElementById('accountPhone').value = user.phone || '';
    document.getElementById('accountImageUrl').value = user.profileImageUrl || '';

    // Load header avatar
    loadProfileAvatar(user.profileImageUrl);

    // Load form avatar
    previewAccountPhoto();
}

function loadProfileAvatar(url) {
    var img = document.getElementById('profileAvatarImg');
    var initials = document.getElementById('profileAvatarInitials');

    if (url) {
        img.src = url;
        img.onload = function() {
            img.style.display = 'block';
            initials.style.display = 'none';
        };
        img.onerror = function() {
            img.style.display = 'none';
            initials.style.display = 'flex';
        };
    } else {
        img.style.display = 'none';
        initials.style.display = 'flex';
    }
}

function previewAccountPhoto() {
    var url = document.getElementById('accountImageUrl').value.trim();
    var img = document.getElementById('formAvatarImg');
    var initials = document.getElementById('formAvatarInitials');

    if (url) {
        img.src = url;
        img.onload = function() {
            img.style.display = 'block';
            initials.style.display = 'none';
        };
        img.onerror = function() {
            img.style.display = 'none';
            initials.style.display = 'flex';
        };
    } else {
        img.style.display = 'none';
        initials.style.display = 'flex';
    }
}

// ==================== Save Account Info ====================

function saveAccountInfo() {
    var name = document.getElementById('accountName').value.trim();
    var email = document.getElementById('accountEmail').value.trim();
    var phone = document.getElementById('accountPhone').value.trim();
    var imageUrl = document.getElementById('accountImageUrl').value.trim();

    if (!name) {
        showToast(I18n.get('profile.errName'), 'error');
        return;
    }
    if (!email) {
        showToast(I18n.get('profile.errEmail'), 'error');
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
        .then(function(r) {
            if (r.ok) {
                return r.json().then(function(updated) {
                    // Update local user cache
                    user.name = updated.name;
                    user.email = updated.email;
                    user.phone = updated.phone;
                    user.profileImageUrl = updated.profileImageUrl;
                    AuthManager.setUser(user);

                    // Update header
                    document.getElementById('profileName').textContent = updated.name;
                    document.getElementById('profileEmail').textContent = updated.email;
                    loadProfileAvatar(updated.profileImageUrl);

                    // Update sidebar
                    var newInitials = AuthManager.getInitials();
                    document.getElementById('sidebarName').textContent = updated.name;
                    document.getElementById('sidebarAvatar').textContent = newInitials;
                    document.getElementById('popupName').textContent = updated.name;
                    document.getElementById('popupAvatar').textContent = newInitials;

                    showToast(I18n.get('profile.accountSaved'), 'success');
                });
            } else {
                return r.json().then(function(err) {
                    showToast(err.message || I18n.get('profile.errUpdate'), 'error');
                });
            }
        })
        .catch(function() {
            showToast(I18n.get('common.connectionError'), 'error');
        });
}

// ==================== Security (Password) ====================

function updatePassword() {
    var current = document.getElementById('currentPassword').value;
    var newPass = document.getElementById('newPassword').value;
    var confirm = document.getElementById('confirmPassword').value;

    hidePasswordError();

    if (!current) {
        showPasswordError(I18n.get('profile.errCurrentPass'));
        return;
    }
    if (!newPass || newPass.length < 8) {
        showPasswordError(I18n.get('profile.errNewPassLen'));
        return;
    }
    if (newPass !== confirm) {
        showPasswordError(I18n.get('profile.errPassMatch'));
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
        .then(function(r) {
            if (r.ok) {
                document.getElementById('currentPassword').value = '';
                document.getElementById('newPassword').value = '';
                document.getElementById('confirmPassword').value = '';
                showToast(I18n.get('profile.passUpdated'), 'success');
            } else {
                return r.json().then(function(err) {
                    showPasswordError(err.message || I18n.get('profile.errUpdate'));
                });
            }
        })
        .catch(function() {
            showPasswordError(I18n.get('common.connectionError'));
        });
}

function showPasswordError(msg) {
    var el = document.getElementById('passwordError');
    el.textContent = msg;
    el.classList.add('is-visible');
}

function hidePasswordError() {
    document.getElementById('passwordError').classList.remove('is-visible');
}

// ==================== Toast ====================

function showToast(message, type) {
    var toast = document.getElementById('toast');
    toast.textContent = message;
    toast.className = 'toast toast-' + type + ' is-visible';
    setTimeout(function() {
        toast.classList.remove('is-visible');
    }, 3000);
}

// ==================== Keyboard Shortcuts ====================

document.addEventListener('keydown', function(e) {
    if (e.key === 'Escape') {
        // Nothing to close on this page for now
    }
});

// ==================== Language Change Callback ====================

I18n.onLanguageChange(function() {
    if (user) {
        document.getElementById('profileRole').textContent = user.role;
    }
});

// ==================== Initialize ====================

loadAccountInfo();
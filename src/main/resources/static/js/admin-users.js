/**
 * Admin Users — CRUD operations for users with password management.
 *
 * @author Kauan Santos Ferreira
 * @since 2026
 */

// ==================== Auth Guard ====================

AuthManager.guard();

// ==================== User Info ====================

var user = AuthManager.getUser();
var currentUserEmail = user ? user.email : null;

if (user) {
    var initials = AuthManager.getInitials();
    document.getElementById('sidebarName').textContent = user.name;
    document.getElementById('sidebarAvatar').textContent = initials;
    document.getElementById('popupName').textContent = user.name;
    document.getElementById('popupAvatar').textContent = initials;
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

// ==================== State ====================

var allUsers = [];
var editingId = null;
var deletingId = null;
var passwordUserId = null;

// ==================== API Helpers ====================

function getHeaders() {
    return {
        'Authorization': 'Bearer ' + AuthManager.getToken(),
        'Content-Type': 'application/json'
    };
}

// ==================== Load Users ====================

function loadUsers() {
    fetch('/api/users', { headers: getHeaders() })
        .then(function(r) { return r.json(); })
        .then(function(data) {
            allUsers = Array.isArray(data) ? data : (data.content || []);
            renderTable(allUsers);
        })
        .catch(function() {
            document.getElementById('usersTableBody').innerHTML =
                '<tr><td colspan="7" style="text-align:center;padding:24px;color:var(--text-tertiary);">Failed to load users</td></tr>';
        });
}

// ==================== Render Table ====================

function renderTable(users) {
    var tbody = document.getElementById('usersTableBody');

    if (users.length === 0) {
        tbody.innerHTML = '<tr><td colspan="7">'
            + '<div class="empty-state">'
            + '<svg class="empty-state__icon" xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><path d="M17 21v-2a4 4 0 00-4-4H5a4 4 0 00-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 00-3-3.87"/><path d="M16 3.13a4 4 0 010 7.75"/></svg>'
            + '<p class="empty-state__title">No users yet</p>'
            + '<p class="empty-state__text">Create your first user to get started</p>'
            + '<button class="btn btn-primary btn-sm" onclick="openCreateModal()">Create user</button>'
            + '</div></td></tr>';
        return;
    }

    var html = '';
    users.forEach(function(u) {
        var roleBadge = u.role === 'ADMIN'
            ? '<span class="badge badge-admin">Admin</span>'
            : '<span class="badge badge-customer">Customer</span>';

        var createdAt = u.createdAt ? formatDate(u.createdAt) : '—';

        var isSelf = currentUserEmail && u.email.toLowerCase() === currentUserEmail.toLowerCase();

        var actions = '<div class="table__actions">'
            + '<div class="table__action-btn table__action-btn--edit" onclick="openEditModal(' + u.id + ')" title="Edit">'
            + '<svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M11 4H4a2 2 0 00-2 2v14a2 2 0 002 2h14a2 2 0 002-2v-7"/><path d="M18.5 2.5a2.121 2.121 0 013 3L12 15l-4 1 1-4 9.5-9.5z"/></svg>'
            + '</div>';

        if (!isSelf) {
            actions += '<div class="table__action-btn table__action-btn--delete" onclick="openDeleteModal(' + u.id + ', \'' + escapeHtml(u.name).replace(/'/g, "\\'") + '\')" title="Delete">'
                + '<svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="3 6 5 6 21 6"/><path d="M19 6v14a2 2 0 01-2 2H7a2 2 0 01-2-2V6m3 0V4a2 2 0 012-2h4a2 2 0 012 2v2"/></svg>'
                + '</div>';
        }

        actions += '</div>';

        html += '<tr>'
            + '<td class="table__cell--primary">#' + u.id + '</td>'
            + '<td class="table__cell--primary">' + escapeHtml(u.name) + '</td>'
            + '<td>' + escapeHtml(u.email) + '</td>'
            + '<td>' + roleBadge + '</td>'
            + '<td>' + escapeHtml(u.phone || '—') + '</td>'
            + '<td>' + createdAt + '</td>'
            + '<td>' + actions + '</td></tr>';
    });
    tbody.innerHTML = html;
}

// ==================== Format Date ====================

function formatDate(dateStr) {
    var d = new Date(dateStr);
    var day = String(d.getDate()).padStart(2, '0');
    var month = String(d.getMonth() + 1).padStart(2, '0');
    var year = d.getFullYear();
    return day + '/' + month + '/' + year;
}

// ==================== Search ====================

function searchUsers(term) {
    if (!term.trim()) {
        renderTable(allUsers);
        return;
    }
    var lower = term.toLowerCase();
    var filtered = allUsers.filter(function(u) {
        return u.name.toLowerCase().indexOf(lower) !== -1 ||
               u.email.toLowerCase().indexOf(lower) !== -1 ||
               (u.phone && u.phone.toLowerCase().indexOf(lower) !== -1) ||
               u.role.toLowerCase().indexOf(lower) !== -1;
    });
    renderTable(filtered);
}

// ==================== Create Modal ====================

function openCreateModal() {
    editingId = null;
    document.getElementById('modalTitle').textContent = 'New user';
    document.getElementById('modalSubmitBtn').textContent = 'Create';
    document.getElementById('userId').value = '';
    document.getElementById('userName').value = '';
    document.getElementById('userEmail').value = '';
    document.getElementById('userPassword').value = '';
    document.getElementById('userRole').value = '';
    document.getElementById('userPhone').value = '';

    // Show password field, hide change password button
    document.getElementById('passwordGroup').style.display = '';
    document.getElementById('changePasswordGroup').style.display = 'none';

    hideModalError();
    openModal();
}

// ==================== Edit Modal ====================

function openEditModal(id) {
    var u = allUsers.find(function(usr) { return usr.id === id; });
    if (!u) return;

    editingId = id;
    document.getElementById('modalTitle').textContent = 'Edit user';
    document.getElementById('modalSubmitBtn').textContent = 'Save changes';
    document.getElementById('userId').value = id;
    document.getElementById('userName').value = u.name;
    document.getElementById('userEmail').value = u.email;
    document.getElementById('userRole').value = u.role;
    document.getElementById('userPhone').value = u.phone || '';

    // Hide password field, show change password button
    document.getElementById('passwordGroup').style.display = 'none';
    document.getElementById('changePasswordGroup').style.display = '';

    hideModalError();
    openModal();
}

// ==================== Save (Create or Update) ====================

function saveUser() {
    var name = document.getElementById('userName').value.trim();
    var email = document.getElementById('userEmail').value.trim();
    var role = document.getElementById('userRole').value;
    var phone = document.getElementById('userPhone').value.trim();

    // Validation
    if (!name) { showModalError('User name is required.'); return; }
    if (!email) { showModalError('Email is required.'); return; }
    if (!role) { showModalError('Please select a role.'); return; }

    var btn = document.getElementById('modalSubmitBtn');
    btn.disabled = true;

    if (editingId) {
        // Update (without password)
        var updateBody = JSON.stringify({
            name: name,
            email: email,
            role: role,
            phone: phone || null
        });

        fetch('/api/users/' + editingId, {
            method: 'PUT',
            headers: getHeaders(),
            body: updateBody
        })
        .then(function(r) {
            if (r.ok) {
                closeModal();
                loadUsers();
                showToast('User updated successfully', 'success');
            } else {
                return r.json().then(function(err) { showModalError(err.message || 'Failed to update user'); });
            }
        })
        .catch(function() { showModalError('Connection error'); })
        .finally(function() { btn.disabled = false; });
    } else {
        // Create (with password)
        var password = document.getElementById('userPassword').value;
        if (!password) { showModalError('Password is required.'); btn.disabled = false; return; }

        var createBody = JSON.stringify({
            name: name,
            email: email,
            password: password,
            role: role,
            phone: phone || null
        });

        fetch('/api/users', {
            method: 'POST',
            headers: getHeaders(),
            body: createBody
        })
        .then(function(r) {
            if (r.ok || r.status === 201) {
                closeModal();
                loadUsers();
                showToast('User created successfully', 'success');
            } else {
                return r.json().then(function(err) { showModalError(err.message || 'Failed to create user'); });
            }
        })
        .catch(function() { showModalError('Connection error'); })
        .finally(function() { btn.disabled = false; });
    }
}

// ==================== Delete ====================

function openDeleteModal(id, name) {
    deletingId = id;
    document.getElementById('deleteText').textContent = 'Are you sure you want to delete "' + name + '"? This action cannot be undone.';
    hideDeleteError();
    document.getElementById('deleteModal').classList.add('is-open');
    document.getElementById('modalBackdrop').classList.add('is-open');
}

function closeDeleteModal() {
    deletingId = null;
    document.getElementById('deleteModal').classList.remove('is-open');
    document.getElementById('modalBackdrop').classList.remove('is-open');
}

function confirmDelete() {
    if (!deletingId) return;

    var btn = document.getElementById('deleteConfirmBtn');
    btn.disabled = true;

    fetch('/api/users/' + deletingId, {
        method: 'DELETE',
        headers: getHeaders()
    })
    .then(function(r) {
        if (r.ok || r.status === 204) {
            closeDeleteModal();
            loadUsers();
            showToast('User deleted successfully', 'success');
        } else {
            return r.json().then(function(err) { showDeleteError(err.message || 'Failed to delete user'); });
        }
    })
    .catch(function() { showDeleteError('Connection error'); })
    .finally(function() { btn.disabled = false; });
}

// ==================== Password Modal ====================

function openPasswordModal() {
    var u = allUsers.find(function(usr) { return usr.id === editingId; });
    if (!u) return;

    passwordUserId = editingId;
    document.getElementById('passwordUserInfo').textContent = 'Changing password for: ' + u.name + ' (' + u.email + ')';
    document.getElementById('newPassword').value = '';
    hidePasswordError();

    // Close edit modal, open password modal
    document.getElementById('userModal').classList.remove('is-open');
    document.getElementById('passwordModal').classList.add('is-open');
    document.getElementById('modalBackdrop').classList.add('is-open');
    document.getElementById('newPassword').focus();
}

function closePasswordModal() {
    passwordUserId = null;
    document.getElementById('passwordModal').classList.remove('is-open');
    document.getElementById('modalBackdrop').classList.remove('is-open');
}

function savePassword() {
    var newPassword = document.getElementById('newPassword').value;
    if (!newPassword) { showPasswordError('New password is required.'); return; }

    var btn = document.getElementById('passwordSubmitBtn');
    btn.disabled = true;

    fetch('/api/users/' + passwordUserId + '/password?newPassword=' + encodeURIComponent(newPassword), {
        method: 'PATCH',
        headers: getHeaders()
    })
    .then(function(r) {
        if (r.ok) {
            closePasswordModal();
            showToast('Password updated successfully', 'success');
        } else {
            return r.json().then(function(err) { showPasswordError(err.message || 'Failed to update password'); });
        }
    })
    .catch(function() { showPasswordError('Connection error'); })
    .finally(function() { btn.disabled = false; });
}

function showPasswordError(msg) {
    var el = document.getElementById('passwordError');
    el.textContent = msg;
    el.classList.add('is-visible');
}

function hidePasswordError() {
    document.getElementById('passwordError').classList.remove('is-visible');
}

// ==================== Password Visibility Toggle ====================

function togglePasswordVisibility() {
    var input = document.getElementById('userPassword');
    var icon = document.getElementById('eyeIcon');
    if (input.type === 'password') {
        input.type = 'text';
        icon.innerHTML = '<path d="M17.94 17.94A10.07 10.07 0 0112 20c-7 0-11-8-11-8a18.45 18.45 0 015.06-5.94M9.9 4.24A9.12 9.12 0 0112 4c7 0 11 8 11 8a18.5 18.5 0 01-2.16 3.19m-6.72-1.07a3 3 0 11-4.24-4.24"/><line x1="1" y1="1" x2="23" y2="23"/>';
    } else {
        input.type = 'password';
        icon.innerHTML = '<path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/>';
    }
}

function toggleNewPasswordVisibility() {
    var input = document.getElementById('newPassword');
    var icon = document.getElementById('newEyeIcon');
    if (input.type === 'password') {
        input.type = 'text';
        icon.innerHTML = '<path d="M17.94 17.94A10.07 10.07 0 0112 20c-7 0-11-8-11-8a18.45 18.45 0 015.06-5.94M9.9 4.24A9.12 9.12 0 0112 4c7 0 11 8 11 8a18.5 18.5 0 01-2.16 3.19m-6.72-1.07a3 3 0 11-4.24-4.24"/><line x1="1" y1="1" x2="23" y2="23"/>';
    } else {
        input.type = 'password';
        icon.innerHTML = '<path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/>';
    }
}

// ==================== Modal Helpers ====================

function openModal() {
    document.getElementById('userModal').classList.add('is-open');
    document.getElementById('modalBackdrop').classList.add('is-open');
    document.getElementById('userName').focus();
}

function closeModal() {
    document.getElementById('userModal').classList.remove('is-open');
    document.getElementById('modalBackdrop').classList.remove('is-open');
    editingId = null;
}

function showModalError(msg) {
    var el = document.getElementById('modalError');
    el.textContent = msg;
    el.classList.add('is-visible');
}

function hideModalError() {
    document.getElementById('modalError').classList.remove('is-visible');
}

function showDeleteError(msg) {
    var el = document.getElementById('deleteError');
    el.textContent = msg;
    el.classList.add('is-visible');
}

function hideDeleteError() {
    document.getElementById('deleteError').classList.remove('is-visible');
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

// ==================== Escape HTML ====================

function escapeHtml(text) {
    var div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// ==================== Keyboard Shortcuts ====================

document.addEventListener('keydown', function(e) {
    if (e.key === 'Escape') {
        closeModal();
        closeDeleteModal();
        closePasswordModal();
    }
});

// ==================== Initialize ====================

loadUsers();
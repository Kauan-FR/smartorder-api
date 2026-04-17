/**
 * Admin Addresses — CRUD operations for user addresses.
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

var allAddresses = [];
var allUsers = [];
var editingId = null;
var deletingId = null;

// ==================== API Helpers ====================

function getHeaders() {
    return {
        'Authorization': 'Bearer ' + AuthManager.getToken(),
        'Content-Type': 'application/json'
    };
}

// ==================== Load Addresses ====================

function loadAddresses() {
    fetch('/api/addresses', { headers: getHeaders() })
        .then(function(r) { return r.json(); })
        .then(function(data) {
            allAddresses = Array.isArray(data) ? data : (data.content || []);
            renderTable(allAddresses);
        })
        .catch(function() {
            document.getElementById('addressesTableBody').innerHTML =
                '<tr><td colspan="9" style="text-align:center;padding:24px;color:var(--text-tertiary);">'+ I18n.get('addressesJs.failedLoad') +'</td></tr>';
        });
}

// ==================== Load Users (for dropdown) ====================

function loadUsers() {
    fetch('/api/users', { headers: getHeaders() })
        .then(function(r) { return r.json(); })
        .then(function(data) {
            allUsers = Array.isArray(data) ? data : (data.content || []);
        })
        .catch(function() {
            allUsers = [];
        });
}

function populateUserDropdown(selectedId) {
    var select = document.getElementById('addressUser');
    select.innerHTML = '<option value="">'+ I18n.get('addresses.selectUser') +'</option>';
    allUsers.forEach(function(u) {
        var option = document.createElement('option');
        option.value = u.id;
        option.textContent = u.name + ' (' + u.email + ')';
        if (selectedId && u.id === selectedId) {
            option.selected = true;
        }
        select.appendChild(option);
    });
}

// ==================== Render Table ====================

function renderTable(addresses) {
    var tbody = document.getElementById('addressesTableBody');

    if (addresses.length === 0) {
        tbody.innerHTML = '<tr><td colspan="9">'
            + '<div class="empty-state">'
            + '<svg class="empty-state__icon" xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0118 0z"/><circle cx="12" cy="10" r="3"/></svg>'
            + '<p class="empty-state__title">'+ I18n.get('addressesJs.noAddresses') +'</p>'
            + '<p class="empty-state__text">'+ I18n.get('addressesJs.createFirst') +'</p>'
            + '<button class="btn btn-primary btn-sm" onclick="openCreateModal()">'+ I18n.get('addressesJs.createAddressBtn') +'</button>'
            + '</div></td></tr>';
        return;
    }

    var html = '';
    addresses.forEach(function(addr) {
        var userName = (addr.user && addr.user.name) ? escapeHtml(addr.user.name) : '—';

        html += '<tr>'
            + '<td class="table__cell--primary">#' + addr.id + '</td>'
            + '<td>' + escapeHtml(addr.street) + '</td>'
            + '<td>' + escapeHtml(addr.number) + '</td>'
            + '<td>' + escapeHtml(addr.city) + '</td>'
            + '<td>' + escapeHtml(addr.state) + '</td>'
            + '<td>' + escapeHtml(addr.zipCode) + '</td>'
            + '<td>' + escapeHtml(addr.country) + '</td>'
            + '<td><span class="badge badge-category">' + userName + '</span></td>'
            + '<td><div class="table__actions">'
            + '<div class="table__action-btn table__action-btn--edit" onclick="openEditModal(' + addr.id + ')" title="Edit">'
            + '<svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M11 4H4a2 2 0 00-2 2v14a2 2 0 002 2h14a2 2 0 002-2v-7"/><path d="M18.5 2.5a2.121 2.121 0 013 3L12 15l-4 1 1-4 9.5-9.5z"/></svg>'
            + '</div>'
            + '<div class="table__action-btn table__action-btn--delete" onclick="openDeleteModal(' + addr.id + ', \'' + escapeHtml(addr.street).replace(/'/g, "\\'") + ' ' + escapeHtml(addr.number).replace(/'/g, "\\'") + '\')" title="Delete">'
            + '<svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="3 6 5 6 21 6"/><path d="M19 6v14a2 2 0 01-2 2H7a2 2 0 01-2-2V6m3 0V4a2 2 0 012-2h4a2 2 0 012 2v2"/></svg>'
            + '</div>'
            + '</div></td></tr>';
    });
    tbody.innerHTML = html;
}

// ==================== Search ====================

function searchAddresses(term) {
    if (!term.trim()) {
        renderTable(allAddresses);
        return;
    }
    var lower = term.toLowerCase();
    var filtered = allAddresses.filter(function(addr) {
        return addr.street.toLowerCase().indexOf(lower) !== -1 ||
               addr.city.toLowerCase().indexOf(lower) !== -1 ||
               addr.state.toLowerCase().indexOf(lower) !== -1 ||
               addr.zipCode.toLowerCase().indexOf(lower) !== -1 ||
               addr.country.toLowerCase().indexOf(lower) !== -1 ||
               (addr.user && addr.user.name && addr.user.name.toLowerCase().indexOf(lower) !== -1);
    });
    renderTable(filtered);
}

// ==================== Create Modal ====================

function openCreateModal() {
    editingId = null;
    document.getElementById('modalTitle').textContent = I18n.get('addressesJs.createTitle');
    document.getElementById('modalSubmitBtn').textContent = I18n.get('common.create');
    document.getElementById('addressId').value = '';
    document.getElementById('addressStreet').value = '';
    document.getElementById('addressNumber').value = '';
    document.getElementById('addressComplement').value = '';
    document.getElementById('addressCity').value = '';
    document.getElementById('addressState').value = '';
    document.getElementById('addressZipCode').value = '';
    document.getElementById('addressCountry').value = '';
    populateUserDropdown(null);
    hideModalError();
    openModal();
}

// ==================== Edit Modal ====================

function openEditModal(id) {
    var addr = allAddresses.find(function(a) { return a.id === id; });
    if (!addr) return;

    editingId = id;
    document.getElementById('modalTitle').textContent = I18n.get('addressesJs.editTitle');
    document.getElementById('modalSubmitBtn').textContent = I18n.get('common.saveChanges');
    document.getElementById('addressId').value = id;
    document.getElementById('addressStreet').value = addr.street;
    document.getElementById('addressNumber').value = addr.number;
    document.getElementById('addressComplement').value = addr.complement || '';
    document.getElementById('addressCity').value = addr.city;
    document.getElementById('addressState').value = addr.state;
    document.getElementById('addressZipCode').value = addr.zipCode;
    document.getElementById('addressCountry').value = addr.country;
    populateUserDropdown(addr.user ? addr.user.id : null);
    hideModalError();
    openModal();
}

// ==================== Save (Create or Update) ====================

function saveAddress() {
    var street = document.getElementById('addressStreet').value.trim();
    var number = document.getElementById('addressNumber').value.trim();
    var complement = document.getElementById('addressComplement').value.trim();
    var city = document.getElementById('addressCity').value.trim();
    var state = document.getElementById('addressState').value.trim();
    var zipCode = document.getElementById('addressZipCode').value.trim();
    var country = document.getElementById('addressCountry').value.trim();
    var userId = document.getElementById('addressUser').value;

    // Validation
    if (!street) { showModalError(I18n.get('addressesJs.streetRequired')); return; }
    if (!number) { showModalError(I18n.get('addressesJs.numberRequired')); return; }
    if (!city) { showModalError(I18n.get('addressesJs.cityRequired')); return; }
    if (!state) { showModalError(I18n.get('addressesJs.stateRequired')); return; }
    if (!zipCode) { showModalError(I18n.get('addressesJs.zipRequired')); return; }
    if (!country) { showModalError(I18n.get('addressesJs.countryRequired')); return; }
    if (!userId) { showModalError(I18n.get('addressesJs.userRequired')); return; }

    var body = JSON.stringify({
        street: street,
        number: number,
        complement: complement || null,
        city: city,
        state: state,
        zipCode: zipCode,
        country: country,
        userId: parseInt(userId)
    });

    var btn = document.getElementById('modalSubmitBtn');
    btn.disabled = true;

    if (editingId) {
        fetch('/api/addresses/' + editingId, {
            method: 'PUT',
            headers: getHeaders(),
            body: body
        })
        .then(function(r) {
            if (r.ok) {
                closeModal();
                loadAddresses();
                showToast(I18n.get('addressesJs.updateSuccess'), I18n.get('common.success') );
            } else {
                return r.json().then(function(err) { showModalError(err.message || I18n.get('addressesJs.failedUpdate')); });
            }
        })
        .catch(function() { showModalError(I18n.get('common.errorConnect')); })
        .finally(function() { btn.disabled = false; });
    } else {
        fetch('/api/addresses', {
            method: 'POST',
            headers: getHeaders(),
            body: body
        })
        .then(function(r) {
            if (r.ok || r.status === 201) {
                closeModal();
                loadAddresses();
                showToast(I18n.get('addressesJs.createSuccess'), I18n.get('common.success') );
            } else {
                return r.json().then(function(err) { showModalError(err.message || I18n.get('addressesJs.failedCreate')); });
            }
        })
        .catch(function() { showModalError(I18n.get('common.errorConnect')); })
        .finally(function() { btn.disabled = false; });
    }
}

// ==================== Delete ====================

function openDeleteModal(id, label) {
    deletingId = id;
    document.getElementById('deleteText').textContent =  I18n.get('common.sure') + ' "' + label + '"? ' + I18n.get('common.undone');
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

    fetch('/api/addresses/' + deletingId, {
        method: 'DELETE',
        headers: getHeaders()
    })
    .then(function(r) {
        if (r.ok || r.status === 204) {
            closeDeleteModal();
            loadAddresses();
            showToast(I18n.get('addressesJs.deleteSuccess'), I18n.get('common.success') );
        } else {
            return r.json().then(function(err) { showDeleteError(err.message || I18n.get('addressesJs.failedDelete')); });
        }
    })
    .catch(function() { showDeleteError(I18n.get('common.errorConnect')); })
    .finally(function() { btn.disabled = false; });
}

// ==================== Modal Helpers ====================

function openModal() {
    document.getElementById('addressModal').classList.add('is-open');
    document.getElementById('modalBackdrop').classList.add('is-open');
    document.getElementById('addressStreet').focus();
}

function closeModal() {
    document.getElementById('addressModal').classList.remove('is-open');
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
    }
});

// ==================== Language Change Callback ====================

I18n.onLanguageChange(function () {
   renderTable(allAddresses);
});
// ==================== Initialize ====================

loadUsers();
loadAddresses();
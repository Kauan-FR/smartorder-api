/**
 * Admin Orders — CRUD operations for orders with status management.
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

var allOrders = [];
var allUsers = [];
var editingId = null;
var deletingId = null;
var statusOrderId = null;

// ==================== API Helpers ====================

function getHeaders() {
    return {
        'Authorization': 'Bearer ' + AuthManager.getToken(),
        'Content-Type': 'application/json'
    };
}

// ==================== Load Orders ====================

function loadOrders() {
    fetch('/api/orders', { headers: getHeaders() })
        .then(function(r) { return r.json(); })
        .then(function(data) {
            allOrders = Array.isArray(data) ? data : (data.content || []);
            renderTable(allOrders);
        })
        .catch(function() {
            document.getElementById('ordersTableBody').innerHTML =
                '<tr><td colspan="8" style="text-align:center;padding:24px;color:var(--text-tertiary);">Failed to load orders</td></tr>';
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
    var select = document.getElementById('orderUser');
    select.innerHTML = '<option value="">Select a user...</option>';
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

// ==================== Load Addresses by User ====================

function loadUserAddresses(selectedAddressId) {
    var userId = document.getElementById('orderUser').value;
    var select = document.getElementById('orderAddress');

    if (!userId) {
        select.innerHTML = '<option value="">Select a user first...</option>';
        select.disabled = true;
        return;
    }

    select.innerHTML = '<option value="">Loading...</option>';
    select.disabled = true;

    fetch('/api/addresses/user/' + userId, { headers: getHeaders() })
        .then(function(r) { return r.json(); })
        .then(function(data) {
            var addresses = Array.isArray(data) ? data : (data.content || []);
            select.innerHTML = '<option value="">Select an address...</option>';

            if (addresses.length === 0) {
                select.innerHTML = '<option value="">No addresses for this user</option>';
                select.disabled = true;
                return;
            }

            addresses.forEach(function(addr) {
                var option = document.createElement('option');
                option.value = addr.id;
                option.textContent = addr.street + ', ' + addr.number + ' — ' + addr.city + '/' + addr.state;
                if (selectedAddressId && addr.id === selectedAddressId) {
                    option.selected = true;
                }
                select.appendChild(option);
            });
            select.disabled = false;
        })
        .catch(function() {
            select.innerHTML = '<option value="">Failed to load addresses</option>';
            select.disabled = true;
        });
}

// ==================== Status Badge ====================

function getStatusBadge(status) {
    var classes = {
        'PENDING': 'badge badge-status-pending',
        'CONFIRMED': 'badge badge-status-confirmed',
        'SHIPPED': 'badge badge-status-shipped',
        'DELIVERED': 'badge badge-status-delivered',
        'CANCELLED': 'badge badge-status-cancelled'
    };
    var cls = classes[status] || 'badge badge-category';
    return '<span class="' + cls + '">' + status + '</span>';
}

// ==================== Render Table ====================

function renderTable(orders) {
    var tbody = document.getElementById('ordersTableBody');

    if (orders.length === 0) {
        tbody.innerHTML = '<tr><td colspan="8">'
            + '<div class="empty-state">'
            + '<svg class="empty-state__icon" xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><path d="M14 2H6a2 2 0 00-2 2v16a2 2 0 002 2h12a2 2 0 002-2V8z"/><polyline points="14 2 14 8 20 8"/></svg>'
            + '<p class="empty-state__title">No orders yet</p>'
            + '<p class="empty-state__text">Create your first order to get started</p>'
            + '<button class="btn btn-primary btn-sm" onclick="openCreateModal()">Create order</button>'
            + '</div></td></tr>';
        return;
    }

    var html = '';
    orders.forEach(function(ord) {
        var userName = (ord.user && ord.user.name) ? escapeHtml(ord.user.name) : '—';
        var addressLabel = (ord.address) ? escapeHtml(ord.address.street + ', ' + ord.address.number) : '—';
        var date = ord.orderDate ? formatDate(ord.orderDate) : '—';
        var total = ord.totalAmount != null ? 'R$ ' + Number(ord.totalAmount).toFixed(2) : '—';
        var itemCount = (ord.items && ord.items.length) ? ord.items.length : 0;

        html += '<tr>'
            + '<td class="table__cell--primary">#' + ord.id + '</td>'
            + '<td>' + date + '</td>'
            + '<td><span class="table__status-click" onclick="openStatusModal(' + ord.id + ')" title="Click to change status">' + getStatusBadge(ord.status) + '</span></td>'
            + '<td>' + total + '</td>'
            + '<td><span class="badge badge-category">' + userName + '</span></td>'
            + '<td>' + addressLabel + '</td>'
            + '<td>' + itemCount + '</td>'
            + '<td><div class="table__actions">'
            + '<div class="table__action-btn table__action-btn--edit" onclick="openEditModal(' + ord.id + ')" title="Edit">'
            + '<svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M11 4H4a2 2 0 00-2 2v14a2 2 0 002 2h14a2 2 0 002-2v-7"/><path d="M18.5 2.5a2.121 2.121 0 013 3L12 15l-4 1 1-4 9.5-9.5z"/></svg>'
            + '</div>'
            + '<div class="table__action-btn table__action-btn--delete" onclick="openDeleteModal(' + ord.id + ', \'#' + ord.id + '\')" title="Delete">'
            + '<svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="3 6 5 6 21 6"/><path d="M19 6v14a2 2 0 01-2 2H7a2 2 0 01-2-2V6m3 0V4a2 2 0 012-2h4a2 2 0 012 2v2"/></svg>'
            + '</div>'
            + '</div></td></tr>';
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

function searchOrders(term) {
    if (!term.trim()) {
        renderTable(allOrders);
        return;
    }
    var lower = term.toLowerCase();
    var filtered = allOrders.filter(function(ord) {
        return String(ord.id).indexOf(lower) !== -1 ||
               ord.status.toLowerCase().indexOf(lower) !== -1 ||
               (ord.user && ord.user.name && ord.user.name.toLowerCase().indexOf(lower) !== -1) ||
               (ord.address && ord.address.street && ord.address.street.toLowerCase().indexOf(lower) !== -1);
    });
    renderTable(filtered);
}

// ==================== Create Modal ====================

function openCreateModal() {
    editingId = null;
    document.getElementById('modalTitle').textContent = 'New order';
    document.getElementById('modalSubmitBtn').textContent = 'Create';
    document.getElementById('orderId').value = '';
    document.getElementById('orderStatus').value = 'PENDING';
    document.getElementById('orderTotal').value = '';
    document.getElementById('orderAddress').innerHTML = '<option value="">Select a user first...</option>';
    document.getElementById('orderAddress').disabled = true;
    populateUserDropdown(null);
    hideModalError();
    openModal();
}

// ==================== Edit Modal ====================

function openEditModal(id) {
    var ord = allOrders.find(function(o) { return o.id === id; });
    if (!ord) return;

    editingId = id;
    document.getElementById('modalTitle').textContent = 'Edit order';
    document.getElementById('modalSubmitBtn').textContent = 'Save changes';
    document.getElementById('orderId').value = id;
    document.getElementById('orderStatus').value = ord.status;
    document.getElementById('orderTotal').value = ord.totalAmount != null ? ord.totalAmount : '';

    populateUserDropdown(ord.user ? ord.user.id : null);

    // Load addresses for the selected user, then select the current address
    if (ord.user) {
        var addressId = ord.address ? ord.address.id : null;
        document.getElementById('orderUser').value = ord.user.id;
        loadUserAddresses(addressId);
    }

    hideModalError();
    openModal();
}

// ==================== Save (Create or Update) ====================

function saveOrder() {
    var userId = document.getElementById('orderUser').value;
    var addressId = document.getElementById('orderAddress').value;
    var status = document.getElementById('orderStatus').value;
    var totalAmount = document.getElementById('orderTotal').value;

    if (!userId) { showModalError('Please select a user.'); return; }
    if (!addressId) { showModalError('Please select an address.'); return; }
    if (!status) { showModalError('Please select a status.'); return; }
    if (totalAmount === '' || Number(totalAmount) < 0) { showModalError('Total amount must be zero or positive.'); return; }

    var body = JSON.stringify({
        status: status,
        totalAmount: Number(totalAmount),
        userId: parseInt(userId),
        addressId: parseInt(addressId)
    });

    var btn = document.getElementById('modalSubmitBtn');
    btn.disabled = true;

    if (editingId) {
        fetch('/api/orders/' + editingId, {
            method: 'PUT',
            headers: getHeaders(),
            body: body
        })
        .then(function(r) {
            if (r.ok) {
                closeModal();
                loadOrders();
                showToast('Order updated successfully', 'success');
            } else {
                return r.json().then(function(err) { showModalError(err.message || 'Failed to update order'); });
            }
        })
        .catch(function() { showModalError('Connection error'); })
        .finally(function() { btn.disabled = false; });
    } else {
        fetch('/api/orders', {
            method: 'POST',
            headers: getHeaders(),
            body: body
        })
        .then(function(r) {
            if (r.ok || r.status === 201) {
                closeModal();
                loadOrders();
                showToast('Order created successfully', 'success');
            } else {
                return r.json().then(function(err) { showModalError(err.message || 'Failed to create order'); });
            }
        })
        .catch(function() { showModalError('Connection error'); })
        .finally(function() { btn.disabled = false; });
    }
}

// ==================== Status Modal ====================

function openStatusModal(id) {
    var ord = allOrders.find(function(o) { return o.id === id; });
    if (!ord) return;

    statusOrderId = id;
    var userName = (ord.user && ord.user.name) ? ord.user.name : 'Unknown';
    document.getElementById('statusOrderInfo').textContent = 'Order #' + id + ' — ' + userName;
    document.getElementById('newStatus').value = ord.status;
    hideStatusError();

    document.getElementById('statusModal').classList.add('is-open');
    document.getElementById('modalBackdrop').classList.add('is-open');
}

function closeStatusModal() {
    statusOrderId = null;
    document.getElementById('statusModal').classList.remove('is-open');
    document.getElementById('modalBackdrop').classList.remove('is-open');
}

function saveStatus() {
    var newStatus = document.getElementById('newStatus').value;
    if (!statusOrderId) return;

    var btn = document.getElementById('statusSubmitBtn');
    btn.disabled = true;

    fetch('/api/orders/' + statusOrderId + '/status/' + newStatus, {
        method: 'PATCH',
        headers: getHeaders()
    })
    .then(function(r) {
        if (r.ok) {
            closeStatusModal();
            loadOrders();
            showToast('Status updated to ' + newStatus, 'success');
        } else {
            return r.json().then(function(err) { showStatusError(err.message || 'Failed to update status'); });
        }
    })
    .catch(function() { showStatusError('Connection error'); })
    .finally(function() { btn.disabled = false; });
}

function showStatusError(msg) {
    var el = document.getElementById('statusError');
    el.textContent = msg;
    el.classList.add('is-visible');
}

function hideStatusError() {
    document.getElementById('statusError').classList.remove('is-visible');
}

// ==================== Delete ====================

function openDeleteModal(id, label) {
    deletingId = id;
    document.getElementById('deleteText').textContent = 'Are you sure you want to delete order ' + label + '? This will also delete all its items. This action cannot be undone.';
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

    fetch('/api/orders/' + deletingId, {
        method: 'DELETE',
        headers: getHeaders()
    })
    .then(function(r) {
        if (r.ok || r.status === 204) {
            closeDeleteModal();
            loadOrders();
            showToast('Order deleted successfully', 'success');
        } else {
            return r.json().then(function(err) { showDeleteError(err.message || 'Failed to delete order'); });
        }
    })
    .catch(function() { showDeleteError('Connection error'); })
    .finally(function() { btn.disabled = false; });
}

// ==================== Modal Helpers ====================

function openModal() {
    document.getElementById('orderModal').classList.add('is-open');
    document.getElementById('modalBackdrop').classList.add('is-open');
}

function closeModal() {
    document.getElementById('orderModal').classList.remove('is-open');
    document.getElementById('modalBackdrop').classList.remove('is-open');
    editingId = null;
}

function showModalError(msg) {
    var el = document.getElementById('modalError');
    el.textContent = msg;
    el.classList.add('is-visible');
}

function hideModalError() { document.getElementById('modalError').classList.remove('is-visible'); }

function showDeleteError(msg) {
    var el = document.getElementById('deleteError');
    el.textContent = msg;
    el.classList.add('is-visible');
}

function hideDeleteError() { document.getElementById('deleteError').classList.remove('is-visible'); }

// ==================== Toast ====================

function showToast(message, type) {
    var toast = document.getElementById('toast');
    toast.textContent = message;
    toast.className = 'toast toast-' + type + ' is-visible';
    setTimeout(function() { toast.classList.remove('is-visible'); }, 3000);
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
        closeStatusModal();
    }
});

// ==================== Initialize ====================

loadUsers();
loadOrders();
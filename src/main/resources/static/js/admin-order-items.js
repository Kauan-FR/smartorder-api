/**
 * Admin Order Items — CRUD operations for order items.
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

var allItems = [];
var allOrders = [];
var allProducts = [];
var editingId = null;
var deletingId = null;

// ==================== API Helpers ====================

function getHeaders() {
    return {
        'Authorization': 'Bearer ' + AuthManager.getToken(),
        'Content-Type': 'application/json'
    };
}

// ==================== Load Items ====================

function loadItems() {
    fetch('/api/order-items', { headers: getHeaders() })
        .then(function(r) { return r.json(); })
        .then(function(data) {
            allItems = Array.isArray(data) ? data : (data.content || []);
            renderTable(allItems);
        })
        .catch(function() {
            document.getElementById('itemsTableBody').innerHTML =
                '<tr><td colspan="7" style="text-align:center;padding:24px;color:var(--text-tertiary);">'+ I18n.get('orderItemsJs.failedLoad') +'</td></tr>';
        });
}

// ==================== Load Orders & Products (for dropdowns) ====================

function loadOrders() {
    fetch('/api/orders', { headers: getHeaders() })
        .then(function(r) { return r.json(); })
        .then(function(data) {
            allOrders = Array.isArray(data) ? data : (data.content || []);
        })
        .catch(function() { allOrders = []; });
}

function loadProducts() {
    fetch('/api/products', { headers: getHeaders() })
        .then(function(r) { return r.json(); })
        .then(function(data) {
            allProducts = Array.isArray(data) ? data : (data.content || []);
        })
        .catch(function() { allProducts = []; });
}

function populateOrderDropdown(selectedId) {
    var select = document.getElementById('itemOrder');
    select.innerHTML = '<option value="">'+ I18n.get('orderItemsJs.selectOrder') +'</option>';
    allOrders.forEach(function(ord) {
        var userName = (ord.user && ord.user.name) ? ord.user.name : I18n.get('common.unknown');
        var option = document.createElement('option');
        option.value = ord.id;
        option.textContent = I18n.get('orderItemsJs.orderLabel') + ord.id + ' — ' + userName + ' (' + ord.status + ')';
        if (selectedId && ord.id === selectedId) {
            option.selected = true;
        }
        select.appendChild(option);
    });
}

function populateProductDropdown(selectedId) {
    var select = document.getElementById('itemProduct');
    select.innerHTML = '<option value="">'+ I18n.get('orderItemsJs.selectProduct') +'</option>';
    allProducts.forEach(function(prod) {
        var option = document.createElement('option');
        option.value = prod.id;
        option.textContent = prod.name + ' — R$ ' + Number(prod.price).toFixed(2);
        if (selectedId && prod.id === selectedId) {
            option.selected = true;
        }
        select.appendChild(option);
    });
}

// ==================== Auto-fill Price ====================

function fillProductPrice() {
    var productId = parseInt(document.getElementById('itemProduct').value);
    if (!productId) return;

    var prod = allProducts.find(function(p) { return p.id === productId; });
    if (prod && prod.price != null) {
        document.getElementById('itemPrice').value = Number(prod.price).toFixed(2);
        calculateSubtotal();
    }
}

function calculateSubtotal() {
    var qty = parseFloat(document.getElementById('itemQuantity').value) || 0;
    var price = parseFloat(document.getElementById('itemPrice').value) || 0;
    document.getElementById('itemSubtotal').value = (qty * price).toFixed(2);
}

// ==================== Render Table ====================

function renderTable(items) {
    var tbody = document.getElementById('itemsTableBody');

    if (items.length === 0) {
        tbody.innerHTML = '<tr><td colspan="7">'
            + '<div class="empty-state">'
            + '<svg class="empty-state__icon" xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><line x1="8" y1="6" x2="21" y2="6"/><line x1="8" y1="12" x2="21" y2="12"/><line x1="8" y1="18" x2="21" y2="18"/><line x1="3" y1="6" x2="3.01" y2="6"/><line x1="3" y1="12" x2="3.01" y2="12"/><line x1="3" y1="18" x2="3.01" y2="18"/></svg>'
            + '<p class="empty-state__title">'+ I18n.get('orderItemsJs.noItems') +'</p>'
            + '<p class="empty-state__text">'+ I18n.get('orderItemsJs.firstItemText') +'</p>'
            + '<button class="btn btn-primary btn-sm" onclick="openCreateModal()">'+ I18n.get('orderItemsJs.addItem') +'</button>'
            + '</div></td></tr>';
        return;
    }

    var html = '';
    items.forEach(function(item) {
        var productName = (item.product && item.product.name) ? escapeHtml(item.product.name) : '—';
        var price = item.price != null ? 'R$ ' + Number(item.price).toFixed(2) : '—';
        var subtotal = item.subtotal != null ? 'R$ ' + Number(item.subtotal).toFixed(2) : '—';

        // Find which order this item belongs to
        var orderLabel = item.orderId ? '#' + item.orderId : '—';

        html += '<tr>'
            + '<td class="table__cell--primary">#' + item.id + '</td>'
            + '<td><span class="badge badge-category">' + orderLabel + '</span></td>'
            + '<td class="table__cell--primary">' + productName + '</td>'
            + '<td>' + (item.quantity || '—') + '</td>'
            + '<td>' + price + '</td>'
            + '<td><strong>' + subtotal + '</strong></td>'
            + '<td><div class="table__actions">'
            + '<div class="table__action-btn table__action-btn--edit" onclick="openEditModal(' + item.id + ')" title="Edit">'
            + '<svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M11 4H4a2 2 0 00-2 2v14a2 2 0 002 2h14a2 2 0 002-2v-7"/><path d="M18.5 2.5a2.121 2.121 0 013 3L12 15l-4 1 1-4 9.5-9.5z"/></svg>'
            + '</div>'
            + '<div class="table__action-btn table__action-btn--delete" onclick="openDeleteModal(' + item.id + ', \'Item #' + item.id + '\')" title="Delete">'
            + '<svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="3 6 5 6 21 6"/><path d="M19 6v14a2 2 0 01-2 2H7a2 2 0 01-2-2V6m3 0V4a2 2 0 012-2h4a2 2 0 012 2v2"/></svg>'
            + '</div>'
            + '</div></td></tr>';
    });
    tbody.innerHTML = html;
}

// ==================== Search ====================

function searchItems(term) {
    if (!term.trim()) {
        renderTable(allItems);
        return;
    }
    var lower = term.toLowerCase();
    var filtered = allItems.filter(function(item) {
        return String(item.id).indexOf(lower) !== -1 ||
               (item.product && item.product.name && item.product.name.toLowerCase().indexOf(lower) !== -1);
    });
    renderTable(filtered);
}

// ==================== Create Modal ====================

function openCreateModal() {
    editingId = null;
    document.getElementById('modalTitle').textContent = I18n.get('orderItemsJs.createTitle');
    document.getElementById('modalSubmitBtn').textContent = I18n.get('common.create');
    document.getElementById('itemId').value = '';
    document.getElementById('itemQuantity').value = '1';
    document.getElementById('itemPrice').value = '';
    document.getElementById('itemSubtotal').value = '';
    populateOrderDropdown(null);
    populateProductDropdown(null);
    hideModalError();
    openModal();
}

// ==================== Edit Modal ====================

function openEditModal(id) {
    var item = allItems.find(function(i) { return i.id === id; });
    if (!item) return;

    editingId = id;
    document.getElementById('modalTitle').textContent = I18n.get('orderItemsJs.editTitle');
    document.getElementById('modalSubmitBtn').textContent = I18n.get('common.saveChanges');
    document.getElementById('itemId').value = id;
    document.getElementById('itemQuantity').value = item.quantity || 1;
    document.getElementById('itemPrice').value = item.price != null ? Number(item.price).toFixed(2) : '';
    document.getElementById('itemSubtotal').value = item.subtotal != null ? Number(item.subtotal).toFixed(2) : '';

    populateOrderDropdown(item.orderId || null);
    populateProductDropdown(item.product ? item.product.id : null);

    hideModalError();
    openModal();
}

// ==================== Save (Create or Update) ====================

function saveItem() {
    var orderId = document.getElementById('itemOrder').value;
    var productId = document.getElementById('itemProduct').value;
    var quantity = document.getElementById('itemQuantity').value;
    var price = document.getElementById('itemPrice').value;
    var subtotal = document.getElementById('itemSubtotal').value;

    if (!orderId) { showModalError('Please select an order.'); return; }
    if (!productId) { showModalError('Please select a product.'); return; }
    if (!quantity || parseInt(quantity) < 1) { showModalError(I18n.get('orderItemsJs.requiredQuantity')); return; }
    if (price === '' || Number(price) < 0) { showModalError(I18n.get('orderItemsJs.invalidPrice')); return; }

    var body = JSON.stringify({
        quantity: parseInt(quantity),
        price: Number(price),
        subtotal: Number(subtotal),
        orderId: parseInt(orderId),
        productId: parseInt(productId)
    });

    var btn = document.getElementById('modalSubmitBtn');
    btn.disabled = true;

    if (editingId) {
        fetch('/api/order-items/' + editingId, {
            method: 'PUT',
            headers: getHeaders(),
            body: body
        })
        .then(function(r) {
            if (r.ok) {
                closeModal();
                loadItems();
                showToast(I18n.get('orderItemsJs.updateSuccess'), I18n.get('common.success'));
            } else {
                return r.json().then(function(err) { showModalError(err.message || I18n.get('orderItemsJs.failedUpdate')); });
            }
        })
        .catch(function() { showModalError(I18n.get('common.errorConnect')); })
        .finally(function() { btn.disabled = false; });
    } else {
        fetch('/api/order-items', {
            method: 'POST',
            headers: getHeaders(),
            body: body
        })
        .then(function(r) {
            if (r.ok || r.status === 201) {
                closeModal();
                loadItems();
                showToast(I18n.get('orderItemsJs.createSuccess'), I18n.get('common.success'));
            } else {
                return r.json().then(function(err) { showModalError(err.message || I18n.get('orderItemsJs.failedCreate')); });
            }
        })
        .catch(function() { showModalError(I18n.get('common.errorConnect')); })
        .finally(function() { btn.disabled = false; });
    }
}

// ==================== Delete ====================

function openDeleteModal(id, label) {
    deletingId = id;
    document.getElementById('deleteText').textContent = I18n.get('common.sure') + ' "' + label + '"? ' + I18n.get('common.undone');
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

    fetch('/api/order-items/' + deletingId, {
        method: 'DELETE',
        headers: getHeaders()
    })
    .then(function(r) {
        if (r.ok || r.status === 204) {
            closeDeleteModal();
            loadItems();
            showToast(I18n.get('orderItemsJs.deleteSuccess'), I18n.get('common.success'));
        } else {
            return r.json().then(function(err) { showDeleteError(err.message || I18n.get('orderItemsJs.failedDelete')); });
        }
    })
    .catch(function() { showDeleteError(I18n.get('common.errorConnect')); })
    .finally(function() { btn.disabled = false; });
}

// ==================== Modal Helpers ====================

function openModal() {
    document.getElementById('itemModal').classList.add('is-open');
    document.getElementById('modalBackdrop').classList.add('is-open');
}

function closeModal() {
    document.getElementById('itemModal').classList.remove('is-open');
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
    }
});

// ==================== Initialize ====================

loadOrders();
loadProducts();
loadItems();
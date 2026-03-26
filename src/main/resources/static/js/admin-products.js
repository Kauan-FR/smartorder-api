/**
 * Admin Products — CRUD operations for products.
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

var allProducts = [];
var allCategories = [];
var editingId = null;
var deletingId = null;

// ==================== API Helpers ====================

function getHeaders() {
    return {
        'Authorization': 'Bearer ' + AuthManager.getToken(),
        'Content-Type': 'application/json'
    };
}

// ==================== Load Products ====================

function loadProducts() {
    fetch('/api/products', { headers: getHeaders() })
        .then(function(r) { return r.json(); })
        .then(function(data) {
            allProducts = Array.isArray(data) ? data : (data.content || []);
            renderTable(allProducts);
        })
        .catch(function() {
            document.getElementById('productsTableBody').innerHTML =
                '<tr><td colspan="9" style="text-align:center;padding:24px;color:var(--text-tertiary);">Failed to load products</td></tr>';
        });
}

// ==================== Load Categories (for dropdown) ====================

function loadCategories() {
    fetch('/api/categories', { headers: getHeaders() })
        .then(function(r) { return r.json(); })
        .then(function(data) {
            allCategories = Array.isArray(data) ? data : (data.content || []);
        })
        .catch(function() {
            allCategories = [];
        });
}

function populateCategoryDropdown(selectedId) {
    var select = document.getElementById('productCategory');
    select.innerHTML = '<option value="">Select a category...</option>';
    allCategories.forEach(function(cat) {
        var option = document.createElement('option');
        option.value = cat.id;
        option.textContent = cat.name;
        if (selectedId && cat.id === selectedId) {
            option.selected = true;
        }
        select.appendChild(option);
    });
}

// ==================== Render Table ====================

function renderTable(products) {
    var tbody = document.getElementById('productsTableBody');

    if (products.length === 0) {
        tbody.innerHTML = '<tr><td colspan="9">'
            + '<div class="empty-state">'
            + '<svg class="empty-state__icon" xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><path d="M21 16V8a2 2 0 00-1-1.73l-7-4a2 2 0 00-2 0l-7 4A2 2 0 003 8v8a2 2 0 001 1.73l7 4a2 2 0 002 0l7-4A2 2 0 0021 16z"/></svg>'
            + '<p class="empty-state__title">No products yet</p>'
            + '<p class="empty-state__text">Create your first product to start selling</p>'
            + '<button class="btn btn-primary btn-sm" onclick="openCreateModal()">Create product</button>'
            + '</div></td></tr>';
        return;
    }

    var html = '';
    products.forEach(function(prod) {
        var activeIcon = prod.active
            ? '<span class="status-icon status-icon--active" title="Active">&#10003;</span>'
            : '<span class="status-icon status-icon--inactive" title="Inactive">&#10007;</span>';

        var thumbnail = prod.imageUrl
            ? '<img class="table__thumbnail" src="' + escapeHtml(prod.imageUrl) + '" alt="' + escapeHtml(prod.name) + '" onclick="openImagePreview(\'' + escapeHtml(prod.imageUrl).replace(/'/g, "\\'") + '\')" onerror="this.style.display=\'none\'">'
            : '<div class="table__thumbnail-placeholder"><svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="3" width="18" height="18" rx="2" ry="2"/><circle cx="8.5" cy="8.5" r="1.5"/><polyline points="21 15 16 10 5 21"/></svg></div>';

        var categoryName = (prod.category && prod.category.name) ? escapeHtml(prod.category.name) : '—';

        var price = prod.price != null ? 'R$ ' + Number(prod.price).toFixed(2) : '—';
        var stock = prod.stockQuantity != null ? prod.stockQuantity : '—';

        html += '<tr>'
            + '<td class="table__cell--primary">#' + prod.id + '</td>'
            + '<td>' + thumbnail + '</td>'
            + '<td class="table__cell--primary">' + escapeHtml(prod.name) + '</td>'
            + '<td class="table__cell--description">' + escapeHtml(prod.description || '—') + '</td>'
            + '<td>' + price + '</td>'
            + '<td>' + stock + '</td>'
            + '<td>' + activeIcon + '</td>'
            + '<td><span class="badge badge-category">' + categoryName + '</span></td>'
            + '<td><div class="table__actions">'
            + '<div class="table__action-btn table__action-btn--edit" onclick="openEditModal(' + prod.id + ')" title="Edit">'
            + '<svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M11 4H4a2 2 0 00-2 2v14a2 2 0 002 2h14a2 2 0 002-2v-7"/><path d="M18.5 2.5a2.121 2.121 0 013 3L12 15l-4 1 1-4 9.5-9.5z"/></svg>'
            + '</div>'
            + '<div class="table__action-btn table__action-btn--delete" onclick="openDeleteModal(' + prod.id + ', \'' + escapeHtml(prod.name).replace(/'/g, "\\'") + '\')" title="Delete">'
            + '<svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="3 6 5 6 21 6"/><path d="M19 6v14a2 2 0 01-2 2H7a2 2 0 01-2-2V6m3 0V4a2 2 0 012-2h4a2 2 0 012 2v2"/></svg>'
            + '</div>'
            + '</div></td></tr>';
    });
    tbody.innerHTML = html;
}

// ==================== Search ====================

function searchProducts(term) {
    if (!term.trim()) {
        renderTable(allProducts);
        return;
    }
    var lower = term.toLowerCase();
    var filtered = allProducts.filter(function(prod) {
        return prod.name.toLowerCase().indexOf(lower) !== -1 ||
               (prod.description && prod.description.toLowerCase().indexOf(lower) !== -1) ||
               (prod.category && prod.category.name && prod.category.name.toLowerCase().indexOf(lower) !== -1);
    });
    renderTable(filtered);
}

// ==================== Create Modal ====================

function openCreateModal() {
    editingId = null;
    document.getElementById('modalTitle').textContent = 'New product';
    document.getElementById('modalSubmitBtn').textContent = 'Create';
    document.getElementById('productId').value = '';
    document.getElementById('productName').value = '';
    document.getElementById('productDescription').value = '';
    document.getElementById('productPrice').value = '';
    document.getElementById('productStock').value = '';
    document.getElementById('productImageUrl').value = '';
    document.getElementById('productActive').checked = true;
    updateToggleLabel();
    populateCategoryDropdown(null);
    hideModalError();
    openModal();
}

// ==================== Edit Modal ====================

function openEditModal(id) {
    var prod = allProducts.find(function(p) { return p.id === id; });
    if (!prod) return;

    editingId = id;
    document.getElementById('modalTitle').textContent = 'Edit product';
    document.getElementById('modalSubmitBtn').textContent = 'Save changes';
    document.getElementById('productId').value = id;
    document.getElementById('productName').value = prod.name;
    document.getElementById('productDescription').value = prod.description || '';
    document.getElementById('productPrice').value = prod.price != null ? prod.price : '';
    document.getElementById('productStock').value = prod.stockQuantity != null ? prod.stockQuantity : '';
    document.getElementById('productImageUrl').value = prod.imageUrl || '';
    document.getElementById('productActive').checked = prod.active !== false;
    updateToggleLabel();
    populateCategoryDropdown(prod.category ? prod.category.id : null);
    hideModalError();
    openModal();
}

// ==================== Save (Create or Update) ====================

function saveProduct() {
    var name = document.getElementById('productName').value.trim();
    var description = document.getElementById('productDescription').value.trim();
    var price = document.getElementById('productPrice').value;
    var stock = document.getElementById('productStock').value;
    var imageUrl = document.getElementById('productImageUrl').value.trim();
    var active = document.getElementById('productActive').checked;
    var categoryId = document.getElementById('productCategory').value;

    // Validation
    if (!name) {
        showModalError('Product name is required.');
        return;
    }
    if (price === '' || Number(price) < 0) {
        showModalError('Price must be zero or a positive value.');
        return;
    }
    if (stock === '' || Number(stock) < 0) {
        showModalError('Stock quantity must be zero or a positive value.');
        return;
    }
    if (!categoryId) {
        showModalError('Please select a category.');
        return;
    }

    var body = JSON.stringify({
        name: name,
        description: description || null,
        price: Number(price),
        stockQuantity: parseInt(stock),
        imageUrl: imageUrl || null,
        active: active,
        categoryId: parseInt(categoryId)
    });

    var btn = document.getElementById('modalSubmitBtn');
    btn.disabled = true;

    if (editingId) {
        // Update
        fetch('/api/products/' + editingId, {
            method: 'PUT',
            headers: getHeaders(),
            body: body
        })
        .then(function(r) {
            if (r.ok) {
                closeModal();
                loadProducts();
                showToast('Product updated successfully', 'success');
            } else {
                return r.json().then(function(err) { showModalError(err.message || 'Failed to update product'); });
            }
        })
        .catch(function() { showModalError('Connection error'); })
        .finally(function() { btn.disabled = false; });
    } else {
        // Create
        fetch('/api/products', {
            method: 'POST',
            headers: getHeaders(),
            body: body
        })
        .then(function(r) {
            if (r.ok || r.status === 201) {
                closeModal();
                loadProducts();
                showToast('Product created successfully', 'success');
            } else {
                return r.json().then(function(err) { showModalError(err.message || 'Failed to create product'); });
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

    fetch('/api/products/' + deletingId, {
        method: 'DELETE',
        headers: getHeaders()
    })
    .then(function(r) {
        if (r.ok || r.status === 204) {
            closeDeleteModal();
            loadProducts();
            showToast('Product deleted successfully', 'success');
        } else {
            return r.json().then(function(err) { showDeleteError(err.message || 'Failed to delete product'); });
        }
    })
    .catch(function() { showDeleteError('Connection error'); })
    .finally(function() { btn.disabled = false; });
}

// ==================== Image Preview ====================

function openImagePreview(url) {
    document.getElementById('previewImage').src = url;
    document.getElementById('imagePreviewModal').classList.add('is-open');
    document.getElementById('imageBackdrop').classList.add('is-open');
}

function closeImagePreview() {
    document.getElementById('imagePreviewModal').classList.remove('is-open');
    document.getElementById('imageBackdrop').classList.remove('is-open');
    document.getElementById('previewImage').src = '';
}

// ==================== Toggle Switch ====================

function updateToggleLabel() {
    var checked = document.getElementById('productActive').checked;
    document.getElementById('toggleLabel').textContent = checked ? 'Active' : 'Inactive';
}

document.getElementById('productActive').addEventListener('change', updateToggleLabel);

// ==================== Modal Helpers ====================

function openModal() {
    document.getElementById('productModal').classList.add('is-open');
    document.getElementById('modalBackdrop').classList.add('is-open');
    document.getElementById('productName').focus();
}

function closeModal() {
    document.getElementById('productModal').classList.remove('is-open');
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
        closeImagePreview();
    }
});

// ==================== Initialize ====================

loadCategories();
loadProducts();
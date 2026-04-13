/**
 * Admin Categories — CRUD operations for product categories.
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

// ==================== Load Categories ====================

function loadCategories() {
    fetch('/api/categories', { headers: getHeaders() })
        .then(function(r) { return r.json(); })
        .then(function(data) {
            allCategories = Array.isArray(data) ? data : (data.content || []);
            renderTable(allCategories);
        })
        .catch(function() {
            document.getElementById('categoriesTableBody').innerHTML =
                '<tr><td colspan="4" style="text-align:center;padding:24px;color:var(--text-tertiary);">'+ I18n.get('categoriesJs.failedCategories') +'</td></tr>';
        });
}

// ==================== Render Table ====================

function renderTable(categories) {
    var tbody = document.getElementById('categoriesTableBody');

    if (categories.length === 0) {
        tbody.innerHTML = '<tr><td colspan="4">'
            + '<div class="empty-state">'
            + '<svg class="empty-state__icon" xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><path d="M20.59 13.41l-7.17 7.17a2 2 0 01-2.83 0L2 12V2h10l8.59 8.59a2 2 0 010 2.82z"/><line x1="7" y1="7" x2="7.01" y2="7"/></svg>'
            + '<p class="empty-state__title">'+ I18n.get('categoriesJs.noCategories') +'</p>'
            + '<p class="empty-state__text">'+ I18n.get('categoriesJs.createCategoriesText') +'</p>'
            + '<button class="btn btn-primary btn-sm" onclick="openCreateModal()">'+ I18n.get('categoriesJs.createCategories') +'</button>'
            + '</div></td></tr>';
        return;
    }

    var html = '';
    categories.forEach(function(cat) {
        html += '<tr>'
            + '<td class="table__cell--primary">#' + cat.id + '</td>'
            + '<td class="table__cell--primary">' + escapeHtml(cat.name) + '</td>'
            + '<td class="table__cell--description">' + escapeHtml(cat.description || '—') + '</td>'
            + '<td><div class="table__actions">'
            + '<div class="table__action-btn table__action-btn--edit" onclick="openEditModal(' + cat.id + ')" title="Edit">'
            + '<svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M11 4H4a2 2 0 00-2 2v14a2 2 0 002 2h14a2 2 0 002-2v-7"/><path d="M18.5 2.5a2.121 2.121 0 013 3L12 15l-4 1 1-4 9.5-9.5z"/></svg>'
            + '</div>'
            + '<div class="table__action-btn table__action-btn--delete" onclick="openDeleteModal(' + cat.id + ', \'' + escapeHtml(cat.name).replace(/'/g, "\\'") + '\')" title="Delete">'
            + '<svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="3 6 5 6 21 6"/><path d="M19 6v14a2 2 0 01-2 2H7a2 2 0 01-2-2V6m3 0V4a2 2 0 012-2h4a2 2 0 012 2v2"/></svg>'
            + '</div>'
            + '</div></td></tr>';
    });
    tbody.innerHTML = html;
}

// ==================== Search ====================

function searchCategories(term) {
    if (!term.trim()) {
        renderTable(allCategories);
        return;
    }
    var filtered = allCategories.filter(function(cat) {
        return cat.name.toLowerCase().indexOf(term.toLowerCase()) !== -1 ||
               (cat.description && cat.description.toLowerCase().indexOf(term.toLowerCase()) !== -1);
    });
    renderTable(filtered);
}

// ==================== Create Modal ====================

function openCreateModal() {
    editingId = null;
    document.getElementById('modalTitle').textContent = I18n.get('categories.newCategory');
    document.getElementById('modalSubmitBtn').textContent = I18n.get('common.create');
    document.getElementById('categoryId').value = '';
    document.getElementById('categoryName').value = '';
    document.getElementById('categoryDescription').value = '';
    hideModalError();
    openModal();
}

// ==================== Edit Modal ====================

function openEditModal(id) {
    var cat = allCategories.find(function(c) { return c.id === id; });
    if (!cat) return;

    editingId = id;
    document.getElementById('modalTitle').textContent = I18n.get('categoriesJs.editTitle');
    document.getElementById('modalSubmitBtn').textContent = I18n.get('common.saveChanges');
    document.getElementById('categoryId').value = id;
    document.getElementById('categoryName').value = cat.name;
    document.getElementById('categoryDescription').value = cat.description || '';
    hideModalError();
    openModal();
}

// ==================== Save (Create or Update) ====================

function saveCategory() {
    var name = document.getElementById('categoryName').value.trim();
    var description = document.getElementById('categoryDescription').value.trim();

    if (!name) {
        showModalError(I18n.get('categoriesJs.categoryRequired'));
        return;
    }

    var body = JSON.stringify({ name: name, description: description });
    var btn = document.getElementById('modalSubmitBtn');
    btn.disabled = true;

    if (editingId) {
        // Update
        fetch('/api/categories/' + editingId, {
            method: 'PUT',
            headers: getHeaders(),
            body: body
        })
        .then(function(r) {
            if (r.ok) {
                closeModal();
                loadCategories();
                showToast(I18n.get('categoriesJs.categoryUpdate'), I18n.get('common.success'));
            } else {
                return r.json().then(function(err) { showModalError(err.message || I18n.get('categoriesJs.failedCategoryUpdate')); });
            }
        })
        .catch(function() { showModalError(I18n.get('common.errorConnect')); })
        .finally(function() { btn.disabled = false; });
    } else {
        // Create
        fetch('/api/categories', {
            method: 'POST',
            headers: getHeaders(),
            body: body
        })
        .then(function(r) {
            if (r.ok || r.status === 201) {
                closeModal();
                loadCategories();
                showToast(I18n.get('categoriesJs.createSuccessCategories'), I18n.get('common.success'));
            } else {
                return r.json().then(function(err) { showModalError(err.message || I18n.get('categoriesJs.failedCategoryCreate')); });
            }
        })
        .catch(function() { showModalError(I18n.get('common.errorConnect')); })
        .finally(function() { btn.disabled = false; });
    }
}

// ==================== Delete ====================

function openDeleteModal(id, name) {
    deletingId = id;
    document.getElementById('deleteText').textContent = I18n.get('common.sure') + ' "' + name + '"? ' + I18n.get('common.undone');
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

    fetch('/api/categories/' + deletingId, {
        method: 'DELETE',
        headers: getHeaders()
    })
    .then(function(r) {
        if (r.ok || r.status === 204) {
            closeDeleteModal();
            loadCategories();
            showToast(I18n.get('categoriesJs.deleteSuccessCategories'), I18n.get('common.success'));
        } else {
            return r.json().then(function(err) { showDeleteError(err.message || I18n.get('categoriesJs.failedCategoryDelete')); });
        }
    })
    .catch(function() { showDeleteError(I18n.get('common.errorConnect')); })
    .finally(function() { btn.disabled = false; });
}

// ==================== Modal Helpers ====================

function openModal() {
    document.getElementById('categoryModal').classList.add('is-open');
    document.getElementById('modalBackdrop').classList.add('is-open');
    document.getElementById('categoryName').focus();
}

function closeModal() {
    document.getElementById('categoryModal').classList.remove('is-open');
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

// ==================== Initialize ====================

loadCategories();
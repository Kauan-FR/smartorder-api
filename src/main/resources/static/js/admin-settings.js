/**
 * Admin Settings — manages store configuration, notifications, account, security, and danger zone.
 * Uses localStorage for Store and Notification preferences (no backend endpoint yet).
 * Uses the real API for Account and Security updates.
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
    document.getElementById('accountAvatarInitials').textContent = initials;
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

var dangerAction = null;

// ==================== API Helpers ====================

function getHeaders() {
    return {
        'Authorization': 'Bearer ' + AuthManager.getToken(),
        'Content-Type': 'application/json'
    };
}

// ==================== Tabs ====================

function switchTab(tabName, element) {
    // Remove active from all tabs and panels
    document.querySelectorAll('.settings-tab').forEach(function(tab) {
        tab.classList.remove('settings-tab--active');
    });
    document.querySelectorAll('.settings-panel').forEach(function(panel) {
        panel.classList.remove('settings-panel--active');
    });

    // Activate clicked tab and matching panel
    element.classList.add('settings-tab--active');
    document.getElementById('tab-' + tabName).classList.add('settings-panel--active');
}

// ==================== Store Settings ====================

function loadStoreSettings() {
    var saved = localStorage.getItem('smartorder-store-settings');
    if (saved) {
        try {
            var data = JSON.parse(saved);
            document.getElementById('storeName').value = data.storeName || '';
            document.getElementById('storeDescription').value = data.storeDescription || '';
            document.getElementById('storeLogoUrl').value = data.logoUrl || '';
            document.getElementById('storeCurrency').value = data.currency || 'BRL';
            document.getElementById('storeTimezone').value = data.timezone || 'America/Sao_Paulo';
            previewLogo();
        } catch (e) {
            console.warn('Failed to parse store settings:', e);
        }
    } else {
        document.getElementById('storeName').value = 'SmartOrder Store';
        document.getElementById('storeCurrency').value = 'BRL';
        document.getElementById('storeTimezone').value = 'America/Sao_Paulo';
    }
}

function saveStoreSettings() {
    var data = {
        storeName: document.getElementById('storeName').value.trim(),
        storeDescription: document.getElementById('storeDescription').value.trim(),
        logoUrl: document.getElementById('storeLogoUrl').value.trim(),
        currency: document.getElementById('storeCurrency').value,
        timezone: document.getElementById('storeTimezone').value
    };

    if (!data.storeName) {
        showToast(I18n.get('settings.errStoreName'), 'error');
        return;
    }

    localStorage.setItem('smartorder-store-settings', JSON.stringify(data));
    showToast(I18n.get('settings.storeSaved'), 'success');
}

function previewLogo() {
    var url = document.getElementById('storeLogoUrl').value.trim();
    var preview = document.getElementById('logoPreview');
    var img = document.getElementById('logoPreviewImg');

    if (url) {
        img.src = url;
        img.onload = function() { preview.style.display = 'flex'; };
        img.onerror = function() { preview.style.display = 'none'; };
    } else {
        preview.style.display = 'none';
    }
}

// ==================== Notification Preferences ====================

function loadNotificationPreferences() {
    var saved = localStorage.getItem('smartorder-notif-prefs');
    if (saved) {
        try {
            var prefs = JSON.parse(saved);
            document.getElementById('notifDisableAll').checked = prefs.disableAll === true;
            document.getElementById('notifNewOrders').checked = prefs.newOrders !== false;
            document.getElementById('notifNewUsers').checked = prefs.newUsers !== false;
            document.getElementById('notifLowStock').checked = prefs.lowStock !== false;
            document.getElementById('notifOrderStatus').checked = prefs.orderStatus !== false;
            document.getElementById('notifNewReviews').checked = prefs.newReviews !== false;
            document.getElementById('notifNewMessages').checked = prefs.newMessages !== false;
            applyMasterToggleState();
        } catch (e) {
            console.warn('Failed to parse notification preferences:', e);
        }
    }
}

function saveNotificationPreferences() {
    var prefs = {
        disableAll: document.getElementById('notifDisableAll').checked,
        newOrders: document.getElementById('notifNewOrders').checked,
        newUsers: document.getElementById('notifNewUsers').checked,
        lowStock: document.getElementById('notifLowStock').checked,
        orderStatus: document.getElementById('notifOrderStatus').checked,
        newReviews: document.getElementById('notifNewReviews').checked,
        newMessages: document.getElementById('notifNewMessages').checked
    };

    localStorage.setItem('smartorder-notif-prefs', JSON.stringify(prefs));
    showToast(I18n.get('settings.notifSaved'), 'success');
}

function toggleAllNotifications() {
    var disableAll = document.getElementById('notifDisableAll').checked;
    var toggles = document.querySelectorAll('.settings-notif-toggle');

    toggles.forEach(function(toggle) {
        if (disableAll) {
            toggle.checked = false;
            toggle.disabled = true;
        } else {
            toggle.checked = true;
            toggle.disabled = false;
        }
    });
}

function applyMasterToggleState() {
    var disableAll = document.getElementById('notifDisableAll').checked;
    var toggles = document.querySelectorAll('.settings-notif-toggle');
    toggles.forEach(function(toggle) {
        toggle.disabled = disableAll;
    });
}

// ==================== Danger Zone ====================

function openDangerModal(action) {
    dangerAction = action;

    var titles = {
        clearOrders: I18n.get('settings.confirmClearOrders'),
        resetData: I18n.get('settings.confirmResetData'),
        deleteAccount: I18n.get('settings.confirmDeleteAccount')
    };
    var texts = {
        clearOrders: I18n.get('settings.confirmClearOrdersText'),
        resetData: I18n.get('settings.confirmResetDataText'),
        deleteAccount: I18n.get('settings.confirmDeleteAccountText')
    };

    document.getElementById('dangerModalTitle').textContent = titles[action] || 'Are you sure?';
    document.getElementById('dangerModalText').textContent = texts[action] || 'This action cannot be undone.';
    document.getElementById('dangerConfirmInput').value = '';
    document.getElementById('dangerError').classList.remove('is-visible');

    document.getElementById('dangerModal').classList.add('is-open');
    document.getElementById('modalBackdrop').classList.add('is-open');
}

function closeDangerModal() {
    dangerAction = null;
    document.getElementById('dangerModal').classList.remove('is-open');
    document.getElementById('modalBackdrop').classList.remove('is-open');
}

function confirmDangerAction() {
    var confirmText = document.getElementById('dangerConfirmInput').value.trim().toUpperCase();
    if (confirmText !== 'CONFIRM') {
        var err = document.getElementById('dangerError');
        err.textContent = I18n.get('settings.errTypeConfirm');
        err.classList.add('is-visible');
        return;
    }

    if (dangerAction === 'clearOrders') {
        deleteAllOrders();
    } else if (dangerAction === 'resetData') {
        resetTestData();
    } else if (dangerAction === 'deleteAccount') {
        deleteOwnAccount();
    }
}

function deleteAllOrders() {
    fetch('/api/orders', { headers: getHeaders() })
        .then(function(r) { return r.json(); })
        .then(function(data) {
            var orders = Array.isArray(data) ? data : (data.content || []);
            var deletes = orders.map(function(o) {
                return fetch('/api/orders/' + o.id, {
                    method: 'DELETE',
                    headers: getHeaders()
                });
            });
            return Promise.all(deletes);
        })
        .then(function() {
            closeDangerModal();
            showToast(I18n.get('settings.ordersCleared'), 'success');
        })
        .catch(function() {
            showToast(I18n.get('common.connectionError'), 'error');
        });
}

function resetTestData() {
    // Delete orders, then products, then categories (respecting FK order)
    Promise.all([
        fetch('/api/orders', { headers: getHeaders() }).then(function(r) { return r.json(); }),
        fetch('/api/products', { headers: getHeaders() }).then(function(r) { return r.json(); }),
        fetch('/api/categories', { headers: getHeaders() }).then(function(r) { return r.json(); })
    ])
        .then(function(data) {
            var orders = Array.isArray(data[0]) ? data[0] : (data[0].content || []);
            var products = Array.isArray(data[1]) ? data[1] : (data[1].content || []);
            var categories = Array.isArray(data[2]) ? data[2] : (data[2].content || []);

            // Delete orders first
            var orderDeletes = orders.map(function(o) {
                return fetch('/api/orders/' + o.id, { method: 'DELETE', headers: getHeaders() });
            });

            return Promise.all(orderDeletes).then(function() {
                // Then products
                var productDeletes = products.map(function(p) {
                    return fetch('/api/products/' + p.id, { method: 'DELETE', headers: getHeaders() });
                });
                return Promise.all(productDeletes);
            }).then(function() {
                // Then categories
                var categoryDeletes = categories.map(function(c) {
                    return fetch('/api/categories/' + c.id, { method: 'DELETE', headers: getHeaders() });
                });
                return Promise.all(categoryDeletes);
            });
        })
        .then(function() {
            closeDangerModal();
            showToast(I18n.get('settings.dataReset'), 'success');
        })
        .catch(function() {
            showToast(I18n.get('common.connectionError'), 'error');
        });
}

function deleteOwnAccount() {
    fetch('/api/users/' + parseInt(user.id), {
        method: 'DELETE',
        headers: getHeaders()
    })
        .then(function(r) {
            if (r.ok || r.status === 204) {
                showToast(I18n.get('settings.accountDeleted'), 'success');
                setTimeout(function() {
                    AuthManager.logout();
                }, 1500);
            } else {
                return r.json().then(function(err) {
                    var errEl = document.getElementById('dangerError');
                    errEl.textContent = err.message || I18n.get('settings.errUpdate');
                    errEl.classList.add('is-visible');
                });
            }
        })
        .catch(function() {
            showToast(I18n.get('common.connectionError'), 'error');
        });
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
        closeDangerModal();
    }
});

// ==================== Language Change Callback ====================

I18n.onLanguageChange(function() {
    // Re-render dynamic danger modal texts if modal is open
    if (dangerAction) {
        var titles = {
            clearOrders: I18n.get('settings.confirmClearOrders'),
            resetData: I18n.get('settings.confirmResetData'),
            deleteAccount: I18n.get('settings.confirmDeleteAccount')
        };
        var texts = {
            clearOrders: I18n.get('settings.confirmClearOrdersText'),
            resetData: I18n.get('settings.confirmResetDataText'),
            deleteAccount: I18n.get('settings.confirmDeleteAccountText')
        };
        document.getElementById('dangerModalTitle').textContent = titles[dangerAction] || '';
        document.getElementById('dangerModalText').textContent = texts[dangerAction] || '';
    }
});

// ==================== Initialize ====================

loadStoreSettings();
loadNotificationPreferences();
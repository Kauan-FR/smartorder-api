/**
 * Admin Topbar — theme toggle, language selector, notifications, global search.
 * Loaded in all admin pages via: <script th:src="@{/js/admin-topbar.js}"></script>
 *
 * @author Kauan Santos Ferreira
 * @since 2026
 */

// ==================== Theme Toggle ====================

/**
 * Toggles between light and dark theme.
 * Persists choice in localStorage under 'smartorder-theme'.
 */
function toggleTheme() {
    var current = document.documentElement.getAttribute('data-theme');
    var next = current === 'dark' ? 'light' : 'dark';
    document.documentElement.setAttribute('data-theme', next);
    localStorage.setItem('smartorder-theme', next);
}

/**
 * Initializes theme from localStorage or system preference.
 * Called immediately on script load.
 */
function initTheme() {
    var saved = localStorage.getItem('smartorder-theme');
    if (saved) {
        document.documentElement.setAttribute('data-theme', saved);
    } else {
        // Detect system preference
        var prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
        var theme = prefersDark ? 'dark' : 'light';
        document.documentElement.setAttribute('data-theme', theme);
        localStorage.setItem('smartorder-theme', theme);
    }
}

initTheme();

// ==================== Language Selector ====================

var LANG_LABELS = { en: 'EN', pt: 'PT', es: 'ES', fr: 'FR' };

/**
 * Toggles the language dropdown menu.
 */
function toggleLangDropdown() {
    var menu = document.getElementById('langMenu');
    menu.classList.toggle('is-open');

    // Close notifications if open
    var notifDropdown = document.getElementById('notifDropdown');
    if (notifDropdown) notifDropdown.classList.remove('is-open');
}

/**
 * Sets the selected language, updates the UI, and stores the preference.
 * In a real application, this would trigger i18n translation of the entire interface.
 * For this showcase, it updates the label and shows a toast.
 */
function setLanguage(lang, element) {
    var items = document.querySelectorAll('.topbar__dropdown-item');
    items.forEach(function(item) {
        item.classList.remove('topbar__dropdown-item--active');
    });
    element.classList.add('topbar__dropdown-item--active');

    document.getElementById('currentLangLabel').textContent = LANG_LABELS[lang] || lang.toUpperCase();

    // Apply translations
    I18n.setLanguage(lang);

    document.getElementById('langMenu').classList.remove('is-open');

    if (typeof showToast === 'function') {
        showToast(I18n.get('lang.changed') + ' ' + I18n.get('lang.' + lang), 'success');
    }
}

/**
 * Initializes language from localStorage.
 */
function initLanguage() {
    I18n.init();
    var saved = localStorage.getItem('smartorder-lang');
    if (saved && LANG_LABELS[saved]) {
        document.getElementById('currentLangLabel').textContent = LANG_LABELS[saved];
        var items = document.querySelectorAll('.topbar__dropdown-item');
        items.forEach(function(item) {
            item.classList.remove('topbar__dropdown-item--active');
            if (item.getAttribute('data-lang') === saved) {
                item.classList.add('topbar__dropdown-item--active');
            }
        });
    }
    I18n.apply();
}
initLanguage();

// ==================== Notifications ====================

/**
 * Toggles the notifications dropdown.
 */
function toggleNotifications() {
    var dropdown = document.getElementById('notifDropdown');
    dropdown.classList.toggle('is-open');

    // Close language if open
    var langMenu = document.getElementById('langMenu');
    if (langMenu) langMenu.classList.remove('is-open');
}

/**
 * Clears all notifications from the list.
 */
function clearNotifications() {
    var list = document.getElementById('notifList');
    list.innerHTML = '<div class="topbar__notif-empty">'
        + '<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M18 8A6 6 0 006 8c0 7-3 9-3 9h18s-3-2-3-9"/><path d="M13.73 21a2 2 0 01-3.46 0"/></svg>'
        + '<span>' + I18n.get('topbar.notifEmpty') + '</span>'
        + '</div>';

    // Hide badge
    var badge = document.getElementById('notifBadge');
    badge.style.display = 'none';
    badge.textContent = '0';
}

/**
 * Adds a notification to the dropdown.
 * Can be called from other scripts to push notifications.
 *
 * @param {string} text - The notification message
 * @param {string} icon - Icon background color (e.g., 'var(--color-info-light)')
 * @param {string} iconColor - Icon stroke color (e.g., 'var(--color-info-text)')
 */
function addNotification(text, icon, iconColor) {
    var list = document.getElementById('notifList');

    // Remove "no notifications" placeholder
    var empty = list.querySelector('.topbar__notif-empty');
    if (empty) empty.remove();

    // Create notification item
    var item = document.createElement('div');
    item.className = 'topbar__notif-item';

    var now = new Date();
    var timeStr = now.getHours().toString().padStart(2, '0') + ':' + now.getMinutes().toString().padStart(2, '0');

    item.innerHTML = '<div class="topbar__notif-icon" style="background-color:' + (icon || 'var(--color-info-light)') + ';">'
        + '<svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="' + (iconColor || 'var(--color-info-text)') + '" stroke-width="2"><circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="12"/><line x1="12" y1="16" x2="12.01" y2="16"/></svg>'
        + '</div>'
        + '<div class="topbar__notif-content">'
        + '<p class="topbar__notif-text">' + text + '</p>'
        + '<p class="topbar__notif-time">' + timeStr + '</p>'
        + '</div>';

    // Insert at top
    list.insertBefore(item, list.firstChild);

    // Update badge
    var badge = document.getElementById('notifBadge');
    var count = parseInt(badge.textContent || '0') + 1;
    badge.textContent = count;
    badge.style.display = 'flex';
}

// ==================== Global Search ====================

var searchTimeout = null;

/**
 * Handles global search input with debounce.
 * Searches across products, categories, users, and orders.
 */
function initGlobalSearch() {
    var input = document.getElementById('globalSearch');
    var results = document.getElementById('globalSearchResults');

    if (!input) return;

    input.addEventListener('input', function() {
        var query = input.value.trim().toLowerCase();

        clearTimeout(searchTimeout);

        if (query.length < 2) {
            results.classList.remove('is-open');
            return;
        }

        searchTimeout = setTimeout(function() {
            performGlobalSearch(query);
        }, 300);
    });

    // Close on click outside
    document.addEventListener('click', function(e) {
        if (!input.contains(e.target) && !results.contains(e.target)) {
            results.classList.remove('is-open');
        }
    });

    // Close on ESC
    input.addEventListener('keydown', function(e) {
        if (e.key === 'Escape') {
            results.classList.remove('is-open');
            input.blur();
        }
    });
}

/**
 * Performs search across multiple entities and renders grouped results.
 */
function performGlobalSearch(query) {
    var token = AuthManager.getToken();
    if (!token) return;

    var headers = { 'Authorization': 'Bearer ' + token, 'Content-Type': 'application/json' };
    var results = document.getElementById('globalSearchResults');

    // Search in parallel
    Promise.all([
        fetch('/api/products', { headers: headers }).then(function(r) { return r.json(); }).catch(function() { return []; }),
        fetch('/api/categories', { headers: headers }).then(function(r) { return r.json(); }).catch(function() { return []; }),
        fetch('/api/users', { headers: headers }).then(function(r) { return r.json(); }).catch(function() { return []; }),
        fetch('/api/orders', { headers: headers }).then(function(r) { return r.json(); }).catch(function() { return []; })
    ]).then(function(data) {
        var products = (Array.isArray(data[0]) ? data[0] : (data[0].content || [])).filter(function(p) {
            return p.name && p.name.toLowerCase().includes(query);
        }).slice(0, 3);

        var categories = (Array.isArray(data[1]) ? data[1] : (data[1].content || [])).filter(function(c) {
            return c.name && c.name.toLowerCase().includes(query);
        }).slice(0, 3);

        var users = (Array.isArray(data[2]) ? data[2] : (data[2].content || [])).filter(function(u) {
            return (u.name && u.name.toLowerCase().includes(query)) || (u.email && u.email.toLowerCase().includes(query));
        }).slice(0, 3);

        var orders = (Array.isArray(data[3]) ? data[3] : (data[3].content || [])).filter(function(o) {
            return String(o.id).includes(query) || (o.user && o.user.name && o.user.name.toLowerCase().includes(query));
        }).slice(0, 3);

        var html = '';
        var hasResults = false;

        if (products.length > 0) {
            hasResults = true;
            html += '<div class="topbar__search-group"><p class="topbar__search-group-label">' + I18n.get('products.title') + '</p>';
            products.forEach(function(p) {
                html += '<div class="topbar__search-result" onclick="window.location.href=\'/admin/products\'">'
                    + '<svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 16V8a2 2 0 00-1-1.73l-7-4a2 2 0 00-2 0l-7 4A2 2 0 003 8v8a2 2 0 001 1.73l7 4a2 2 0 002 0l7-4A2 2 0 0021 16z"/></svg>'
                    + '<span>' + escapeSearchHtml(p.name) + '</span></div>';
            });
            html += '</div>';
        }

        if (categories.length > 0) {
            hasResults = true;
            html += '<div class="topbar__search-group"><p class="topbar__search-group-label">'+ I18n.get('categories.title') +'</p>';
            categories.forEach(function(c) {
                html += '<div class="topbar__search-result" onclick="window.location.href=\'/admin/categories\'">'
                    + '<svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20.59 13.41l-7.17 7.17a2 2 0 01-2.83 0L2 12V2h10l8.59 8.59a2 2 0 010 2.82z"/><line x1="7" y1="7" x2="7.01" y2="7"/></svg>'
                    + '<span>' + escapeSearchHtml(c.name) + '</span></div>';
            });
            html += '</div>';
        }

        if (users.length > 0) {
            hasResults = true;
            html += '<div class="topbar__search-group"><p class="topbar__search-group-label">'+ I18n.get('users.title') +'</p>';
            users.forEach(function(u) {
                html += '<div class="topbar__search-result" onclick="window.location.href=\'/admin/users\'">'
                    + '<svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 21v-2a4 4 0 00-4-4H8a4 4 0 00-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>'
                    + '<span>' + escapeSearchHtml(u.name) + '</span></div>';
            });
            html += '</div>';
        }

        if (orders.length > 0) {
            hasResults = true;
            html += '<div class="topbar__search-group"><p class="topbar__search-group-label">'+ I18n.get('orders.title') +'</p>';
            orders.forEach(function(o) {
                html += '<div class="topbar__search-result" onclick="window.location.href=\'/admin/orders\'">'
                    + '<svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M14 2H6a2 2 0 00-2 2v16a2 2 0 002 2h12a2 2 0 002-2V8z"/><polyline points="14 2 14 8 20 8"/></svg>'
                    + '<span>#' + o.id + ' — ' + (o.user ? escapeSearchHtml(o.user.name) : I18n.get('common.unknown')) + '</span></div>';
            });
            html += '</div>';
        }

        if (!hasResults) {
            html = '<div class="topbar__search-no-results">No results found for "' + escapeSearchHtml(query) + '"</div>';
        }

        results.innerHTML = html;
        results.classList.add('is-open');
    });
}

function escapeSearchHtml(str) {
    var div = document.createElement('div');
    div.textContent = str;
    return div.innerHTML;
}

initGlobalSearch();

// ==================== Close Dropdowns on Outside Click ====================

document.addEventListener('click', function(e) {
    // Close language dropdown
    var langDropdown = document.getElementById('langDropdown');
    var langMenu = document.getElementById('langMenu');
    if (langDropdown && langMenu && !langDropdown.contains(e.target)) {
        langMenu.classList.remove('is-open');
    }

    // Close notifications dropdown
    var notifBtn = document.getElementById('notifBtn');
    var notifDropdown = document.getElementById('notifDropdown');
    if (notifBtn && notifDropdown && !notifBtn.contains(e.target) && !notifDropdown.contains(e.target)) {
        notifDropdown.classList.remove('is-open');
    }
});
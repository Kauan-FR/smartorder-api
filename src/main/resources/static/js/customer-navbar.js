/**
 * Customer Navbar — handles store navbar interactions.
 * Features: categories dropdown, profile menu, cart badge, language selector,
 * theme toggle, search, and logout.
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
(function () {
    'use strict';

    var API_BASE = '';

    /* ==========================================================================
       INIT
       ========================================================================== */

    document.addEventListener('DOMContentLoaded', function () {
        loadUserInfo();
        loadCategories();
        loadCartBadge();
        setupCategoriesDropdown();
        setupProfileDropdown();
        setupLanguageSelector();
        setupThemeToggle();
        setupSearch();
        setupLogout();
        setupOutsideClickClose();

        if (typeof I18n !== 'undefined') {
            I18n.apply();
            I18n.onLanguageChange(function () {
                I18n.apply();
            });
        }
    });

    /* ==========================================================================
       USER INFO
       ========================================================================== */

    function loadUserInfo() {
        var user = AuthManager.getUser();
        if (!user) return;

        var initial = (user.name || 'U').charAt(0).toUpperCase();

        var avatar = document.getElementById('profileAvatar');
        var avatarLg = document.getElementById('profileAvatarLg');
        var nameEl = document.getElementById('profileName');
        var emailEl = document.getElementById('profileEmail');

        if (avatar) avatar.textContent = initial;
        if (avatarLg) avatarLg.textContent = initial;
        if (nameEl) nameEl.textContent = user.name || 'User';
        if (emailEl) emailEl.textContent = user.email || '';
    }

    /* ==========================================================================
       CATEGORIES
       ========================================================================== */

    function loadCategories() {
        fetch(API_BASE + '/api/categories')
            .then(function (res) {
                if (!res.ok) throw new Error(I18n.get('storeNavbarJs.failedCategories'));
                return res.json();
            })
            .then(function (categories) {
                renderCategories(categories);
            })
            .catch(function () {
                showToast(I18n.get('storeNavbarJs.failedCategories'), 'error');
            });
    }

    function renderCategories(categories) {
        var list = document.getElementById('categoriesList');
        if (!list) return;

        if (!categories || categories.length === 0) {
            list.innerHTML = '';
            return;
        }

        var html = categories.map(function (cat) {
            return '<a href="/store?category=' + cat.id + '" class="customer-navbar__categories-link">' +
                escapeHtml(cat.name) +
                '</a>';
        }).join('');

        list.innerHTML = html;
    }

    function setupCategoriesDropdown() {
        var trigger = document.getElementById('categoriesTrigger');
        var dropdown = document.getElementById('categoriesDropdown');
        if (!trigger || !dropdown) return;

        trigger.addEventListener('click', function (e) {
            e.stopPropagation();
            closeProfileDropdown();
            toggleDropdown(dropdown);
        });
    }

    /* ==========================================================================
       CART BADGE
       ========================================================================== */

    function loadCartBadge() {
        var token = AuthManager.getToken();
        if (!token) {
            hideCartBadge();
            return;
        }

        fetch(API_BASE + '/api/cart', {
            headers: { 'Authorization': 'Bearer ' + token }
        })
            .then(function (res) {
                if (!res.ok) throw new Error(I18n.get('storeNavbarJs.failedCart'));
                return res.json();
            })
            .then(function (items) {
                var total = items.reduce(function (sum, item) {
                    return sum + (item.quantity || 0);
                }, 0);
                updateCartBadge(total);
            })
            .catch(function () {
                hideCartBadge();
            });
    }

    function updateCartBadge(count) {
        var badge = document.getElementById('cartBadge');
        if (!badge) return;

        if (count > 0) {
            badge.textContent = count > 99 ? '99+' : count;
            badge.hidden = false;
        } else {
            badge.hidden = true;
        }
    }

    function hideCartBadge() {
        var badge = document.getElementById('cartBadge');
        if (badge) badge.hidden = true;
    }

    // Expose globally so other scripts can refresh badge after add/remove
    window.refreshCartBadge = loadCartBadge;

    /* ==========================================================================
       PROFILE DROPDOWN
       ========================================================================== */

    function setupProfileDropdown() {
        var trigger = document.getElementById('profileTrigger');
        var dropdown = document.getElementById('profileDropdown');
        if (!trigger || !dropdown) return;

        trigger.addEventListener('click', function (e) {
            e.stopPropagation();
            closeCategoriesDropdown();
            toggleDropdown(dropdown);
        });
    }

    function closeProfileDropdown() {
        var dropdown = document.getElementById('profileDropdown');
        if (dropdown) dropdown.hidden = true;
        var lang = document.getElementById('customerLangDropdown');
        if (lang) lang.hidden = true;
    }

    function closeCategoriesDropdown() {
        var dropdown = document.getElementById('categoriesDropdown');
        if (dropdown) dropdown.hidden = true;
        var lang = document.getElementById('customerLangDropdown');
        if (lang) lang.hidden = true;
    }

    function toggleDropdown(el) {
        el.hidden = !el.hidden;
    }

    /* ==========================================================================
       LANGUAGE SELECTOR
       ========================================================================== */

    function setupLanguageSelector() {
        var btn = document.getElementById('customerLangBtn');
        var dropdown = document.getElementById('customerLangDropdown');
        if (!btn || !dropdown) return;

        btn.addEventListener('click', function (e) {
            e.stopPropagation();
            closeCategoriesDropdown();
            closeProfileDropdown();
            toggleDropdown(dropdown);
        });

        var items = dropdown.querySelectorAll('.customer-navbar__lang-item');
        items.forEach(function (item) {
            item.addEventListener('click', function () {
                var lang = item.getAttribute('data-lang');
                I18n.setLanguage(lang);
                dropdown.hidden = true;
                showToast(I18n.get('storeNavbarJs.languageChanged').replace('{0}', lang.toUpperCase()), 'info');
            });
        });
    }

    /* ==========================================================================
       THEME TOGGLE
       ========================================================================== */

    function setupThemeToggle() {
        var btn = document.getElementById('customerThemeBtn');
        if (!btn) return;

        applyStoredTheme();

        btn.addEventListener('click', function () {
            var current = document.documentElement.getAttribute('data-theme') || 'light';
            var next = current === 'light' ? 'dark' : 'light';
            document.documentElement.setAttribute('data-theme', next);
            localStorage.setItem('smartorder-theme', next);
        });
    }

    function applyStoredTheme() {
        var stored = localStorage.getItem('smartorder-theme');
        if (stored) {
            document.documentElement.setAttribute('data-theme', stored);
        } else if (window.matchMedia('(prefers-color-scheme: dark)').matches) {
            document.documentElement.setAttribute('data-theme', 'dark');
        }
    }

    /* ==========================================================================
       SEARCH
       ========================================================================== */

    function setupSearch() {
        var input = document.getElementById('storeSearchInput');
        var btn = document.getElementById('storeSearchBtn');
        if (!input || !btn) return;

        btn.addEventListener('click', function () {
            submitSearch(input.value);
        });

        input.addEventListener('keydown', function (e) {
            if (e.key === 'Enter') {
                e.preventDefault();
                submitSearch(input.value);
            }
        });
    }

    function submitSearch(query) {
        var trimmed = (query || '').trim();
        if (!trimmed) return;
        window.location.href = '/store?search=' + encodeURIComponent(trimmed);
    }

    /* ==========================================================================
       LOGOUT
       ========================================================================== */

    function setupLogout() {
        var btn = document.getElementById('logoutBtn');
        if (!btn) return;

        btn.addEventListener('click', function () {
            if (confirm(I18n.get('storeNavbarJs.confirmLogout'))) {
                AuthManager.logout();
            }
        });
    }

    /* ==========================================================================
       OUTSIDE CLICK CLOSE
       ========================================================================== */

    function setupOutsideClickClose() {
        document.addEventListener('click', function (e) {
            var dropdowns = [
                { el: document.getElementById('categoriesDropdown'), trigger: document.getElementById('categoriesTrigger') },
                { el: document.getElementById('profileDropdown'), trigger: document.getElementById('profileTrigger') },
                { el: document.getElementById('customerLangDropdown'), trigger: document.getElementById('customerLangBtn') }
            ];

            dropdowns.forEach(function (d) {
                if (d.el && !d.el.hidden && !d.el.contains(e.target) && !d.trigger.contains(e.target)) {
                    d.el.hidden = true;
                }
            });
        });
    }

    /* ==========================================================================
       HELPERS
       ========================================================================== */

    function escapeHtml(text) {
        var div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }
})();
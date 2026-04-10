/**
 * i18n Engine — handles language switching across the entire admin panel.
 * Reads data-i18n attributes from HTML elements and replaces text content.
 *
 * Usage in HTML:
 *   <span data-i18n="sidebar.dashboard">Dashboard</span>
 *   <input data-i18n-placeholder="topbar.searchPlaceholder" placeholder="Search...">
 *   <button data-i18n="common.save">Save</button>
 *
 * Supports:
 *   - data-i18n           → replaces textContent
 *   - data-i18n-placeholder → replaces placeholder attribute
 *   - data-i18n-title      → replaces title attribute
 *
 * @author Kauan Santos Ferreira
 * @since 2026
 */

var I18n = (function() {

    // Available languages — each file loaded via <script> sets window.LANG_xx
    var languages = {};
    var currentLang = 'en';

    /**
     * Registers a language pack.
     * Called by each lang file: I18n.register('en', { ... })
     */
    function register(lang, translations) {
        languages[lang] = translations;
    }

    /**
     * Retrieves a translation by dot-notation key.
     * Example: get('sidebar.dashboard') → 'Dashboard'
     */
    function get(key) {
        var parts = key.split('.');
        var obj = languages[currentLang];

        if (!obj) return key;

        for (var i = 0; i < parts.length; i++) {
            obj = obj[parts[i]];
            if (obj === undefined) {
                // Fallback to English
                var fallback = languages['en'];
                for (var j = 0; j < parts.length; j++) {
                    fallback = fallback ? fallback[parts[j]] : undefined;
                }
                return fallback || key;
            }
        }

        return obj;
    }

    /**
     * Applies translations to all elements with data-i18n attributes.
     * Should be called after DOM is ready and after language change.
     */
    function apply() {
        // Translate text content
        var elements = document.querySelectorAll('[data-i18n]');
        elements.forEach(function(el) {
            var key = el.getAttribute('data-i18n');
            var translation = get(key);
            if (translation && translation !== key) {
                el.textContent = translation;
            }
        });

        // Translate placeholders
        var placeholders = document.querySelectorAll('[data-i18n-placeholder]');
        placeholders.forEach(function(el) {
            var key = el.getAttribute('data-i18n-placeholder');
            var translation = get(key);
            if (translation && translation !== key) {
                el.placeholder = translation;
            }
        });

        // Translate title attributes
        var titles = document.querySelectorAll('[data-i18n-title]');
        titles.forEach(function(el) {
            var key = el.getAttribute('data-i18n-title');
            var translation = get(key);
            if (translation && translation !== key) {
                el.title = translation;
            }
        });
    }

    /**
     * Sets the current language and applies translations.
     */
    function setLanguage(lang) {
        if (!languages[lang]) {
            console.warn('[i18n] Language not registered: ' + lang);
            return;
        }
        currentLang = lang;
        localStorage.setItem('smartorder-lang', lang);
        apply();
    }

    /**
     * Returns the current language code.
     */
    function getCurrentLang() {
        return currentLang;
    }

    /**
     * Initializes i18n from localStorage or defaults to 'en'.
     * Called automatically on script load.
     */
    function init() {
        var saved = localStorage.getItem('smartorder-lang');
        if (saved && languages[saved]) {
            currentLang = saved;
        }
    }

    return {
        register: register,
        get: get,
        apply: apply,
        setLanguage: setLanguage,
        getCurrentLang: getCurrentLang,
        init: init
    };

})();
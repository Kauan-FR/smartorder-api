/**
 * Theme Manager for SmartOrder.
 * Handles dark/light mode switching with localStorage persistence.
 *
 * @author Kauan Santos Ferreira
 * @since 2026
 */
const ThemeManager = {
    STORAGE_KEY: 'smartorder-theme',
    LIGHT: 'light',
    DARK: 'dark',

    /**
     * Initializes the theme from localStorage or system preference.
     */
    init() {
        const saved = localStorage.getItem(this.STORAGE_KEY);
        if (saved) {
            this.apply(saved);
        } else {
            const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
            this.apply(prefersDark ? this.DARK : this.LIGHT);
        }

        this.updateToggleUI();
    },

    /**
     * Returns the current active theme.
     * @returns {string} 'light' or 'dark'
     */
    current() {
        return document.documentElement.getAttribute('data-theme') || this.LIGHT;
    },

    /**
     * Applies a theme to the document.
     * @param {string} theme - 'light' or 'dark'
     */
    apply(theme) {
        document.documentElement.setAttribute('data-theme', theme);
        localStorage.setItem(this.STORAGE_KEY, theme);
        this.updateToggleUI();
    },

    /**
     * Toggles between light and dark themes.
     */
    toggle() {
        const next = this.current() === this.LIGHT ? this.DARK : this.LIGHT;
        this.apply(next);
    },

    /**
     * Sets a specific theme.
     * @param {string} theme - 'light' or 'dark'
     */
    set(theme) {
        if (theme === this.LIGHT || theme === this.DARK) {
            this.apply(theme);
        }
    },

    /**
     * Updates any toggle UI elements to reflect the current theme.
     */
    updateToggleUI() {
        const current = this.current();

        document.querySelectorAll('[data-theme-option]').forEach(el => {
            const isActive = el.getAttribute('data-theme-option') === current;
            el.classList.toggle('theme-toggle__option--active', isActive);
        });
    }
};

document.addEventListener('DOMContentLoaded', () => ThemeManager.init());

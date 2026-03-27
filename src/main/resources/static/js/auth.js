/**
 * Auth Manager for SmartOrder.
 * Handles JWT token storage, retrieval, and authenticated API calls.
 *
 * @author Kauan Santos Ferreira
 * @since 2026
 */
const AuthManager = {
    TOKEN_KEY: 'smartorder-token',
    USER_KEY: 'smartorder-user',
    EXPIRY_CHECK_INTERVAL: 30000,

    /**
     * Returns the stored JWT token.
     * @returns {string|null}
     */
    getToken() {
        return localStorage.getItem(this.TOKEN_KEY);
    },

    /**
     * Returns the stored user data.
     * @returns {object|null} { name, email, role }
     */
    getUser() {
        const data = localStorage.getItem(this.USER_KEY);
        return data ? JSON.parse(data) : null;
    },

    /**
     * Returns the user's role.
     * @returns {string|null} 'ADMIN' or 'CUSTOMER'
     */
    getRole() {
        const user = this.getUser();
        return user ? user.role : null;
    },

    /**
     * Returns the user's display initials (first letter of first + last name).
     * @returns {string} e.g. 'KF'
     */
    getInitials() {
        const user = this.getUser();
        if (!user || !user.name) return '??';
        const parts = user.name.trim().split(/\s+/);
        if (parts.length === 1) return parts[0].charAt(0).toUpperCase();
        return (parts[0].charAt(0) + parts[parts.length - 1].charAt(0)).toUpperCase();
    },

    /**
     * Checks if user is authenticated.
     * @returns {boolean}
     */
    isAuthenticated() {
        return !!this.getToken();
    },

    /**
     * Checks if authenticated user is admin.
     * @returns {boolean}
     */
    isAdmin() {
        return this.getRole() === 'ADMIN';
    },

    // NEW: Decodes the JWT payload and checks expiration
    /**
     * Checks if the stored JWT token has expired.
     * Decodes the Base64 payload and compares the 'exp' claim against current time.
     *
     * @returns {boolean} true if token is expired or invalid, false if still valid
     */
    isTokenExpired() {
        var token = this.getToken();
        if (!token) return true;

        try {
            var parts = token.split('.');
            if (parts.length !== 3) return true;

            var payload = JSON.parse(atob(parts[1]));
            if (!payload.exp) return true;

            // exp is in seconds, Date.now() is in milliseconds
            return (payload.exp * 1000) < Date.now();
        } catch (e) {
            return true;
        }
    },

    // NEW: Starts periodic expiration check
    /**
     * Starts a periodic interval that checks token expiration.
     * When the token expires, clears auth data and redirects to login.
     * Only runs on protected pages (not login/register/store).
     */
    startExpiryCheck() {
        var self = this;
        this._expiryInterval = setInterval(function() {
            if (self.isTokenExpired()) {
                clearInterval(self._expiryInterval);
                self.logout();
            }
        }, this.EXPIRY_CHECK_INTERVAL);
    },

    /**
     * Stores auth data after successful login.
     * @param {string} token - JWT token
     * @param {object} user - { name, email, role }
     */
    login(token, user) {
        localStorage.setItem(this.TOKEN_KEY, token);
        localStorage.setItem(this.USER_KEY, JSON.stringify(user));
    },

    /**
     * Clears auth data and redirects to login page.
     */
    logout() {
        localStorage.removeItem(this.TOKEN_KEY);
        localStorage.removeItem(this.USER_KEY);
        window.location.href = '/login';
    },

    /**
     * Makes an authenticated fetch request with JWT in Authorization header.
     * Automatically redirects to login on 401 responses.
     *
     * @param {string} url - API endpoint
     * @param {object} options - fetch options
     * @returns {Promise<Response>}
     */
    async fetch(url, options = {}) {
        const token = this.getToken();

        const headers = {
            'Content-Type': 'application/json',
            ...options.headers
        };

        if (token) {
            headers['Authorization'] = `Bearer ${token}`;
        }

        const response = await fetch(url, { ...options, headers });

        if (response.status === 401) {
            this.logout();
            return;
        }

        return response;
    },

    /**
     * Checks auth status on page load.
     * Redirects unauthenticated users away from protected pages.
     * Redirects authenticated users away from login page.
     */
    guard() {
        const path = window.location.pathname;
        const isPublic = ['/login', '/register'].includes(path) ||
                         path.startsWith('/store');
        const isAdminPage = path.startsWith('/admin');

        if (!isPublic && !this.isAuthenticated()) {
            window.location.href = '/login';
            return;
        }

        if (this.isAuthenticated() && ['/login', '/register', '/'].includes(path)) {
            window.location.href = this.isAdmin() ? '/admin/dashboard' : '/store';
            return;
        }

        if (isAdminPage && !this.isAdmin()) {
            window.location.href = '/store';
            return;
        }
    }
};

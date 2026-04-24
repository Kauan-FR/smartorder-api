/**
 * Shows a floating toast notification.
 * Shared between admin and store pages.
 *
 * @param {string} message - The message to display
 * @param {string} type - Toast type: 'success', 'error', 'info', 'warning'
 */
function showToast(message, type) {
    var toast = document.getElementById('globalToast');
    if (!toast) {
        toast = document.createElement('div');
        toast.id = 'globalToast';
        toast.className = 'global-toast';
        document.body.appendChild(toast);
    }
    toast.textContent = message;
    toast.className = 'global-toast global-toast--' + type + ' is-visible';
    setTimeout(function () {
        toast.classList.remove('is-visible');
    }, 3000);
}
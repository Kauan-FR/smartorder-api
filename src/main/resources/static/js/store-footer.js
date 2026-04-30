/**
 * Store Footer
 *
 * Handles mobile accordion toggling and dynamic copyright year.
 * Accordion only activates on mobile breakpoint (<= 768px).
 */
(function () {
    'use strict';

    const MOBILE_BREAKPOINT = 768;

    // ===== Set current year =====
    const yearEl = document.getElementById('storeFooterYear');
    if (yearEl) {
        yearEl.textContent = new Date().getFullYear();
    }

    // ===== Accordion =====
    const triggers = document.querySelectorAll('[data-accordion-trigger]');

    triggers.forEach(trigger => {
        trigger.addEventListener('click', () => {
            // Only act as accordion on mobile
            if (window.innerWidth > MOBILE_BREAKPOINT) return;

            const column = trigger.closest('[data-accordion]');
            if (!column) return;

            column.classList.toggle('is-open');
        });
    });

    // ===== Reset accordion state when crossing breakpoint =====
    let lastWasMobile = window.innerWidth <= MOBILE_BREAKPOINT;
    window.addEventListener('resize', () => {
        const isMobile = window.innerWidth <= MOBILE_BREAKPOINT;
        if (lastWasMobile !== isMobile) {
            // Going from mobile -> desktop: clear all open states
            // (desktop CSS ignores them, but keeps DOM clean)
            if (!isMobile) {
                document.querySelectorAll('[data-accordion].is-open')
                    .forEach(c => c.classList.remove('is-open'));
            }
            lastWasMobile = isMobile;
        }
    });
})();
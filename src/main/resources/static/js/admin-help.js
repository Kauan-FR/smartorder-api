/**
 * Help system — shared logic for Help Center, Release Notes, Terms & Policies, Report a Bug.
 * Loaded in all admin pages via: <script th:src="@{/js/admin-help.js}"></script>
 *
 * @author Kauan Santos Ferreira
 * @since 2026
 */

// ==================== Open Help Modals ====================

function openHelpCenter(event) {
    event.preventDefault();
    closeProfilePopup();
    document.getElementById('helpCenterOverlay').classList.add('is-open');
}

function openReleaseNotes(event) {
    event.preventDefault();
    closeProfilePopup();
    document.getElementById('releaseNotesOverlay').classList.add('is-open');
}

function openTermsPolicies(event) {
    event.preventDefault();
    closeProfilePopup();
    document.getElementById('termsPoliciesOverlay').classList.add('is-open');
}

function openReportBug(event) {
    event.preventDefault();
    closeProfilePopup();
    document.getElementById('reportBugOverlay').classList.add('is-open');
}

// ==================== Close Help Modals ====================

function closeHelpModal(overlayId) {
    document.getElementById(overlayId).classList.remove('is-open');
}

function closeProfilePopup() {
    var popup = document.getElementById('profilePopup');
    if (popup) {
        popup.classList.remove('is-open');
    }
}

// ==================== FAQ Accordion ====================

function toggleFaq(button) {
    var item = button.closest('.help-faq__item');
    var isOpen = item.classList.contains('is-open');

    // Close all FAQ items first
    var allItems = document.querySelectorAll('.help-faq__item');
    allItems.forEach(function(faqItem) {
        faqItem.classList.remove('is-open');
    });

    // Toggle the clicked item (if it was closed, open it)
    if (!isOpen) {
        item.classList.add('is-open');
    }
}

// ==================== Terms & Policies Tabs ====================

function switchTermsTab(button, contentId) {
    // Remove active from all tabs
    var allTabs = document.querySelectorAll('.help-tabs__tab');
    allTabs.forEach(function(tab) {
        tab.classList.remove('help-tabs__tab--active');
    });

    // Remove active from all content
    var allContent = document.querySelectorAll('.help-terms__content');
    allContent.forEach(function(content) {
        content.classList.remove('help-terms__content--active');
    });

    // Activate clicked tab and its content
    button.classList.add('help-tabs__tab--active');
    document.getElementById(contentId).classList.add('help-terms__content--active');
}

// ==================== Report a Bug — Submit ====================

function submitBugReport() {
    var title = document.getElementById('bugTitle').value.trim();
    var category = document.getElementById('bugCategory').value;
    var description = document.getElementById('bugDescription').value.trim();

    // Validate required fields
    if (!title) {
        document.getElementById('bugTitle').focus();
        return;
    }
    if (!category) {
        document.getElementById('bugCategory').focus();
        return;
    }
    if (!description) {
        document.getElementById('bugDescription').focus();
        return;
    }

    // In a real system, this would send an email or create a ticket via API.
    // For this showcase, we simulate the submission.
    console.log('Bug Report Submitted:', { title: title, category: category, description: description });

    // Clear form
    document.getElementById('bugTitle').value = '';
    document.getElementById('bugCategory').selectedIndex = 0;
    document.getElementById('bugDescription').value = '';

    // Close modal
    closeHelpModal('reportBugOverlay');

    // Show success toast (uses showToast from each admin page)
    if (typeof showToast === 'function') {
        showToast('Bug report submitted successfully! Our team will review it.', 'success');
    }
}

// ==================== Close Modals with ESC Key ====================

document.addEventListener('keydown', function(event) {
    if (event.key === 'Escape') {
        var overlays = document.querySelectorAll('.help-modal-overlay.is-open');
        overlays.forEach(function(overlay) {
            overlay.classList.remove('is-open');
        });
    }
});

// ==================== Close Modals on Overlay Click ====================

document.addEventListener('click', function(event) {
    if (event.target.classList.contains('help-modal-overlay')) {
        event.target.classList.remove('is-open');
    }
});
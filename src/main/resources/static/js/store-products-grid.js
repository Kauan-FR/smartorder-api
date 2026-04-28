/**
 * Store Products Grid
 *
 * Loads paginated products and renders cards in an infinite scroll layout.
 * Fetches favorite product IDs once on init to mark active hearts.
 * Uses IntersectionObserver with a debounced loader to prevent backend hammering.
 */
(function () {
    'use strict';

    // ===== Config =====
    const PAGE_SIZE = 12;
    const SCROLL_DEBOUNCE_MS = 800;
    const LOCALE_MAP = { en: 'en-US', pt: 'pt-BR', es: 'es-ES', fr: 'fr-FR' };
    const CURRENCY_MAP = { en: 'USD', pt: 'BRL', es: 'EUR', fr: 'EUR' };

    // ===== State =====
    let currentPage = 0;
    let totalPages = Infinity;
    let isLoading = false;
    let lastLoadAt = 0;
    let favoriteIds = new Set();
    let observer = null;

    // ===== DOM refs =====
    const grid = document.getElementById('productGrid');
    const sentinel = document.getElementById('productGridSentinel');
    const spinner = document.getElementById('productGridSpinner');
    const endMessage = document.getElementById('productGridEndMessage');

    if (!grid || !sentinel) return;

    // ===== Init =====
    init();

    async function init() {
        await loadFavoriteIds();
        setupObserver();

        // Re-render cards when language changes (price formatting + i18n labels)
        if (window.I18n && typeof I18n.onLanguageChange === 'function') {
            I18n.onLanguageChange(reRenderAll);
        }
    }

    // ===== Favorites =====
    async function loadFavoriteIds() {
        const token = window.AuthManager && AuthManager.getToken();
        if (!token) return;

        try {
            const res = await fetch('/api/favorites', {
                headers: { 'Authorization': 'Bearer ' + token }
            });
            if (!res.ok) return;
            const list = await res.json();
            favoriteIds = new Set(list.map(f => f.productId));
        } catch (err) {
            console.warn('[products-grid] Failed to load favorites', err);
        }
    }

    // ===== Observer setup =====
    function setupObserver() {
        observer = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    requestLoad();
                }
            });
        }, {
            rootMargin: '200px 0px'
        });

        observer.observe(sentinel);
    }

    // ===== Debounced loader =====
    function requestLoad() {
        if (isLoading) return;
        if (currentPage >= totalPages) return;

        const now = Date.now();
        const elapsed = now - lastLoadAt;

        if (elapsed < SCROLL_DEBOUNCE_MS) {
            setTimeout(requestLoad, SCROLL_DEBOUNCE_MS - elapsed);
            return;
        }

        loadNextPage();
    }

    async function loadNextPage() {
        isLoading = true;
        lastLoadAt = Date.now();
        if (spinner) spinner.hidden = false;

        try {
            const res = await fetch(`/api/products/paged?page=${currentPage}&size=${PAGE_SIZE}`);
            if (!res.ok) throw new Error('HTTP ' + res.status);

            const page = await res.json();
            const products = page.content || [];
            totalPages = page.totalPages ?? 0;

            if (currentPage === 0 && products.length === 0) {
                renderEmptyState();
                stopObserving();
                return;
            }

            products.forEach(p => grid.appendChild(buildCard(p)));
            currentPage++;

            if (currentPage >= totalPages) {
                stopObserving();
                if (endMessage) endMessage.hidden = false;
            }
        } catch (err) {
            console.error('[products-grid] Failed to load page', err);
            if (window.showToast) {
                showToast(I18n.get('storeCardJs.loadError') || 'Failed to load products', 'error');
            }
        } finally {
            isLoading = false;
            if (spinner) spinner.hidden = true;
        }
    }

    function stopObserving() {
        if (observer && sentinel) {
            observer.unobserve(sentinel);
            sentinel.style.display = 'none';
        }
    }

    function renderEmptyState() {
        const empty = document.createElement('div');
        empty.className = 'product-grid__empty';
        empty.textContent = I18n.get('storeCardJs.empty') || 'No products available yet';
        grid.appendChild(empty);
    }

    // ===== Card builder =====
    function buildCard(product) {
        const card = document.createElement('article');
        card.className = 'product-card';
        card.dataset.productId = product.id;
        card.addEventListener('click', (e) => {
            // Avoid navigation when clicking on action buttons
            if (e.target.closest('.product-card__favorite, .product-card__cart-btn')) return;
            window.location.href = `/store/product/${product.id}`;
        });

        card.innerHTML = renderCardHTML(product);
        attachCardEvents(card, product);
        return card;
    }

    function renderCardHTML(product) {
        const hasDiscount = product.discountPercent && product.discountPercent > 0;
        const finalPrice = product.finalPrice ?? product.price;
        const isFav = favoriteIds.has(product.id);

        const dealBanner = hasDiscount ? `
            <div class="product-card__deal-banner">
                <span class="product-card__deal-banner-label" data-i18n="storeCardJs.welcomeDeal">Welcome Deal</span>
            </div>
        ` : '';

        const priceBlock = hasDiscount ? `
            <span class="product-card__price-final">${formatPrice(finalPrice)}</span>
            <span class="product-card__price-original">${formatPrice(product.price)}</span>
            <span class="product-card__price-discount">-${product.discountPercent}%</span>
        ` : `
            <span class="product-card__price-final product-card__price-final--no-discount">${formatPrice(product.price)}</span>
        `;

        return `
            <div class="product-card__image-wrapper">
                <img class="product-card__image"
                     src="${escapeHtml(product.imageUrl || '/img/product-placeholder.png')}"
                     alt="${escapeHtml(product.name)}"
                     loading="lazy">
                <button type="button" class="product-card__favorite ${isFav ? 'is-active' : ''}"
                        data-action="favorite"
                        aria-label="Toggle favorite">
                    ${HEART_SVG}
                </button>
                <button type="button" class="product-card__cart-btn"
                        data-action="cart"
                        aria-label="Add to cart">
                    ${CART_SVG}
                </button>
                ${dealBanner}
            </div>
            <div class="product-card__body">
                <h3 class="product-card__name">${escapeHtml(product.name)}</h3>
                <div class="product-card__price-row">
                    ${priceBlock}
                </div>
                <div class="product-card__rating-row">
                    ${renderStars(product.averageRating)}
                    <span class="product-card__rating-value">${formatRating(product.averageRating)}</span>
                    <span class="product-card__separator">|</span>
                    <span class="product-card__review-count">
                        ${product.reviewCount || 0} <span data-i18n="storeCardJs.reviews">reviews</span>
                    </span>
                </div>
            </div>
        `;
    }

    function attachCardEvents(card, product) {
        const favBtn = card.querySelector('[data-action="favorite"]');
        const cartBtn = card.querySelector('[data-action="cart"]');

        if (favBtn) {
            favBtn.addEventListener('click', (e) => {
                e.stopPropagation();
                toggleFavorite(product.id, favBtn);
            });
        }

        if (cartBtn) {
            cartBtn.addEventListener('click', (e) => {
                e.stopPropagation();
                // Hook real ao integrar com CartItem (próxima etapa)
                if (window.showToast) {
                    showToast(I18n.get('storeCardJs.cartSoon') || 'Cart integration coming soon', 'info');
                }
            });
        }

        // Apply i18n to any data-i18n inside the freshly built card
        if (window.I18n && typeof I18n.apply === 'function') {
            I18n.apply(card);
        }
    }

    // ===== Favorite toggle =====
    async function toggleFavorite(productId, btn) {
        const token = window.AuthManager && AuthManager.getToken();
        if (!token) {
            if (window.showToast) {
                showToast(I18n.get('storeCardJs.loginRequired') || 'Please log in to favorite', 'info');
            }
            return;
        }

        const isFav = favoriteIds.has(productId);
        const method = isFav ? 'DELETE' : 'POST';

        // Optimistic UI
        btn.classList.toggle('is-active');

        try {
            const res = await fetch(`/api/favorites/${productId}`, {
                method,
                headers: { 'Authorization': 'Bearer ' + token }
            });
            if (!res.ok) throw new Error('HTTP ' + res.status);

            if (isFav) favoriteIds.delete(productId);
            else favoriteIds.add(productId);
        } catch (err) {
            // Revert on error
            btn.classList.toggle('is-active');
            console.error('[products-grid] Favorite toggle failed', err);
            if (window.showToast) {
                showToast(I18n.get('storeCardJs.favoriteError') || 'Failed to update favorite', 'error');
            }
        }
    }

    // ===== Re-render on language change =====
    function reRenderAll() {
        const cards = grid.querySelectorAll('.product-card');
        cards.forEach(card => {
            // Re-apply i18n is enough; price formatting will only update if we re-render fully
            // Since locale affects currency, we re-render the price row
            const productId = Number(card.dataset.productId);
            // Lightweight: just re-apply i18n labels
            if (window.I18n && typeof I18n.apply === 'function') {
                I18n.apply(card);
            }
        });
    }

    // ===== Helpers =====
    function formatPrice(value) {
        if (value == null) return '';
        const lang = (window.I18n && I18n.getCurrentLanguage && I18n.getCurrentLanguage()) || 'pt';
        const locale = LOCALE_MAP[lang] || 'pt-BR';
        const currency = CURRENCY_MAP[lang] || 'BRL';
        try {
            return new Intl.NumberFormat(locale, {
                style: 'currency',
                currency,
                minimumFractionDigits: 2
            }).format(value);
        } catch {
            return 'R$ ' + Number(value).toFixed(2).replace('.', ',');
        }
    }

    function formatRating(rating) {
        if (rating == null || rating === 0) return '0.0';
        return Number(rating).toFixed(1);
    }

    function renderStars(rating) {
        const r = Number(rating) || 0;
        let html = '<span class="product-card__stars">';
        for (let i = 1; i <= 5; i++) {
            const filled = i <= Math.round(r);
            html += `<svg class="product-card__star ${filled ? 'is-filled' : ''}" viewBox="0 0 24 24" fill="currentColor"><path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/></svg>`;
        }
        html += '</span>';
        return html;
    }

    function escapeHtml(str) {
        if (str == null) return '';
        return String(str)
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
            .replace(/"/g, '&quot;')
            .replace(/'/g, '&#39;');
    }

    // ===== SVG icons =====
    const HEART_SVG = `<svg viewBox="0 0 24 24"><path d="M12.1 18.55l-.1.1-.1-.1C7.14 14.24 4 11.39 4 8.5 4 6.5 5.5 5 7.5 5c1.54 0 3.04.99 3.57 2.36h1.86C13.46 5.99 14.96 5 16.5 5c2 0 3.5 1.5 3.5 3.5 0 2.89-3.14 5.74-7.9 10.05z"/></svg>`;

    const CART_SVG = `<svg viewBox="0 0 24 24" fill="currentColor"><path d="M7 18c-1.1 0-2 .9-2 2s.9 2 2 2 2-.9 2-2-.9-2-2-2zM1 2v2h2l3.6 7.59-1.35 2.45c-.16.28-.25.61-.25.96 0 1.1.9 2 2 2h12v-2H7.42c-.14 0-.25-.11-.25-.25l.03-.12.9-1.63h7.45c.75 0 1.41-.41 1.75-1.03l3.58-6.49A1.003 1.003 0 0 0 20 4H5.21l-.94-2H1zm16 16c-1.1 0-2 .9-2 2s.9 2 2 2 2-.9 2-2-.9-2-2-2z"/></svg>`;
})();
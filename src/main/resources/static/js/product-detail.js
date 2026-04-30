/**
 * Product Detail Page
 *
 * Handles:
 * - Loading product data + populating UI
 * - Image gallery with thumbs and Amazon-style hover-zoom
 * - Quantity selector with stock-aware bounds
 * - Favorite toggle (with anonymous user fallback)
 * - Add to cart (placeholder - integration in next phase)
 * - Talk to seller (placeholder - WebSocket chat in next phase)
 * - Tabs (Description / Reviews)
 * - Reviews list with sort + like + inline form
 * - Purchase-gated review form (only buyers can review)
 * - Locale-aware price formatting
 * - I18n re-render on language change
 */
(function () {
    'use strict';

    // ===== Config =====
    const ZOOM_FACTOR = 2.5;
    const LENS_SIZE = 150;
    const LOCALE_MAP = { en: 'en-US', pt: 'pt-BR', es: 'es-ES', fr: 'fr-FR' };
    const CURRENCY_MAP = { en: 'USD', pt: 'BRL', es: 'EUR', fr: 'EUR' };
    const LOW_STOCK_THRESHOLD = 10;

    // ===== State =====
    const productId = Number(document.querySelector('meta[name="product-id"]')?.content);
    let product = null;
    let imageList = [];
    let currentImageIndex = 0;
    let isFavorited = false;
    let canReview = false;
    let reviews = [];
    let likedReviewIds = new Set();
    let currentSort = 'recent';

    // ===== DOM refs =====
    const loading = document.getElementById('productDetailLoading');
    const errorEl = document.getElementById('productDetailError');
    const content = document.getElementById('productDetailContent');

    if (!productId) {
        showError();
        return;
    }

    // ===== Init =====
    init();

    async function init() {
        try {
            await loadProduct();
            await loadFavoriteStatus();
            await loadPurchaseStatus();
            await loadReviews();
            renderAll();
            attachStaticEvents();
            setupLanguageReRender();
        } catch (err) {
            console.error('[product-detail] Init failed', err);
            showError();
        }
    }

    function showError() {
        if (loading) loading.hidden = true;
        if (content) content.hidden = true;
        if (errorEl) errorEl.hidden = false;
    }

    function showContent() {
        if (loading) loading.hidden = true;
        if (errorEl) errorEl.hidden = true;
        if (content) content.hidden = false;
    }

    // ====================================================================
    //   DATA LOADING
    // ====================================================================

    async function loadProduct() {
        const res = await fetch(`/api/products/${productId}`);
        if (!res.ok) throw new Error('Product not found');
        product = await res.json();

        // Single image for now; future: ProductImage entity gives multiple
        imageList = product.imageUrl ? [product.imageUrl] : ['/img/product-placeholder.png'];
    }

    async function loadFavoriteStatus() {
        const token = window.AuthManager && AuthManager.getToken();
        if (!token) return;

        try {
            const res = await fetch(`/api/favorites/${productId}/check`, {
                headers: { 'Authorization': 'Bearer ' + token }
            });
            if (res.ok) isFavorited = await res.json();
        } catch (err) {
            // Anonymous or expired token - silent fail
        }
    }

    async function loadPurchaseStatus() {
        const token = window.AuthManager && AuthManager.getToken();
        if (!token) {
            canReview = false;
            return;
        }

        try {
            const res = await fetch(`/api/orders/has-purchased/${productId}`, {
                headers: { 'Authorization': 'Bearer ' + token }
            });
            if (res.ok) canReview = await res.json();
        } catch (err) {
            canReview = false;
        }
    }

    async function loadReviews() {
        try {
            const res = await fetch(`/api/reviews/product/${productId}`);
            if (res.ok) reviews = await res.json();
        } catch (err) {
            console.warn('[product-detail] Failed to load reviews', err);
            reviews = [];
        }

// Load liked review IDs if logged in
        const token = window.AuthManager && AuthManager.getToken();
        if (token) {
            try {
                const res = await fetch('/api/reviews/my-likes', {
                    headers: { 'Authorization': 'Bearer ' + token }
                });
                if (res.ok) {
                    const ids = await res.json();
                    likedReviewIds = new Set(ids);
                }
            } catch {
                // silent - either endpoint not implemented yet or anonymous
            }
        }
    }

    // ====================================================================
    //   RENDERING
    // ====================================================================

    function renderAll() {
        renderBreadcrumb();
        renderGallery();
        renderHeader();
        renderPrice();
        renderQuantity();
        renderFavoriteButton();
        renderTabs();
        renderReviewsHeader();
        renderReviewFormArea();
        renderReviewsList();
        document.title = `${product.name} · SmartOrder`;
        showContent();
    }

    function renderBreadcrumb() {
        const productEl = document.getElementById('breadcrumbProduct');
        const categoryEl = document.getElementById('breadcrumbCategory');

        if (productEl) productEl.textContent = product.name;

        if (categoryEl && product.category) {
            categoryEl.querySelector('span').textContent = product.category.name;
            // Future: link to category filter page
            // categoryEl.href = `/store/category/${product.category.id}`;
        }
    }

    function renderGallery() {
        const thumbs = document.getElementById('productThumbs');
        const mainImage = document.getElementById('mainImage');

        if (!thumbs || !mainImage) return;

        thumbs.innerHTML = '';
        imageList.forEach((url, idx) => {
            const btn = document.createElement('button');
            btn.type = 'button';
            btn.className = 'product-detail__thumb' + (idx === 0 ? ' is-active' : '');
            btn.dataset.index = idx;
            btn.innerHTML = `<img src="${escapeHtml(url)}" alt="${escapeHtml(product.name)} ${idx + 1}">`;
            btn.addEventListener('click', () => switchImage(idx));
            thumbs.appendChild(btn);
        });

        mainImage.src = imageList[0];
        mainImage.alt = product.name;

        setupZoom();
    }

    function switchImage(idx) {
        currentImageIndex = idx;
        const mainImage = document.getElementById('mainImage');
        const thumbs = document.querySelectorAll('.product-detail__thumb');

        if (mainImage) mainImage.src = imageList[idx];
        thumbs.forEach((t, i) => t.classList.toggle('is-active', i === idx));
    }

    function renderHeader() {
        const nameEl = document.getElementById('productName');
        const starsEl = document.getElementById('productStars');
        const ratingValueEl = document.getElementById('productRatingValue');
        const reviewCountEl = document.getElementById('productReviewCount');

        if (nameEl) nameEl.textContent = product.name;
        if (starsEl) starsEl.innerHTML = renderStarsHTML(product.averageRating || 0);
        if (ratingValueEl) ratingValueEl.textContent = formatRating(product.averageRating);
        if (reviewCountEl) reviewCountEl.textContent = product.reviewCount || 0;
    }

    function renderPrice() {
        const finalEl = document.getElementById('productPriceFinal');
        const originalEl = document.getElementById('productPriceOriginal');
        const discountEl = document.getElementById('productPriceDiscount');
        const stockEl = document.getElementById('productStock');

        const hasDiscount = product.discountPercent && product.discountPercent > 0;
        const finalPrice = product.finalPrice ?? product.price;

        if (finalEl) {
            finalEl.textContent = formatPrice(finalPrice);
            finalEl.classList.toggle('product-detail__price-final--no-discount', !hasDiscount);
        }

        if (originalEl) {
            originalEl.hidden = !hasDiscount;
            if (hasDiscount) originalEl.textContent = formatPrice(product.price);
        }

        if (discountEl) {
            discountEl.hidden = !hasDiscount;
            if (hasDiscount) discountEl.textContent = `-${product.discountPercent}%`;
        }

        if (stockEl) renderStock(stockEl);
    }

    function renderStock(el) {
        const stock = product.stockQuantity || 0;
        el.classList.remove('product-detail__stock--low', 'product-detail__stock--out');

        if (stock === 0) {
            el.textContent = I18n.get('productDetailJs.stockOut') || 'Out of stock';
            el.classList.add('product-detail__stock--out');
        } else if (stock <= LOW_STOCK_THRESHOLD) {
            const tpl = I18n.get('productDetailJs.stockLow') || 'Only {n} left in stock';
            el.textContent = tpl.replace('{n}', stock);
            el.classList.add('product-detail__stock--low');
        } else {
            const tpl = I18n.get('productDetailJs.stockAvailable') || 'In stock · {n} units available';
            el.textContent = tpl.replace('{n}', stock);
        }
    }

    function renderQuantity() {
        const input = document.getElementById('qtyInput');
        const decrease = document.getElementById('qtyDecrease');
        const increase = document.getElementById('qtyIncrease');
        const cartBtn = document.getElementById('addToCartBtn');

        if (!input || !decrease || !increase) return;

        const stock = product.stockQuantity || 0;
        input.max = stock;

        if (stock === 0) {
            input.value = 0;
            input.disabled = true;
            decrease.disabled = true;
            increase.disabled = true;
            if (cartBtn) cartBtn.disabled = true;
            return;
        }

        decrease.addEventListener('click', () => {
            const v = Math.max(1, Number(input.value) - 1);
            input.value = v;
        });

        increase.addEventListener('click', () => {
            const v = Math.min(stock, Number(input.value) + 1);
            input.value = v;
        });

        input.addEventListener('input', () => {
            let v = Number(input.value);
            if (isNaN(v) || v < 1) v = 1;
            if (v > stock) v = stock;
            input.value = v;
        });
    }

    function renderFavoriteButton() {
        const btn = document.getElementById('favoriteBtn');
        if (btn) btn.classList.toggle('is-active', isFavorited);
    }

    function renderTabs() {
        const tabCount = document.getElementById('reviewsTabCount');
        const description = document.getElementById('productDescription');

        if (tabCount) tabCount.textContent = reviews.length;
        if (description) {
            description.textContent = product.description ||
                I18n.get('productDetailJs.empty') || 'No description provided.';
        }
    }

    function renderReviewsHeader() {
        const totalEl = document.getElementById('reviewsCountTotal');
        if (totalEl) totalEl.textContent = reviews.length;
    }

    function renderReviewFormArea() {
        const area = document.getElementById('reviewFormArea');
        if (!area) return;

        const token = window.AuthManager && AuthManager.getToken();

        if (!token) {
            area.innerHTML = `
                <div class="product-detail__review-notice">
                    <span data-i18n="productDetail.reviews.loginNotice">Please log in to leave a review.</span>
                    <a href="/login" data-i18n="productDetail.reviews.loginLink">Log in</a>
                </div>
            `;
            applyI18n(area);
            return;
        }

        if (!canReview) {
            area.innerHTML = `
                <div class="product-detail__review-notice">
                    <span data-i18n="productDetail.reviews.purchaseNotice">You can only review products you have purchased.</span>
                </div>
            `;
            applyI18n(area);
            return;
        }

        area.innerHTML = `
            <form class="product-detail__review-form" id="reviewForm">
                <h3 class="product-detail__review-form-title" data-i18n="productDetail.reviews.formTitle">
                    Write a review
                </h3>
                <div class="product-detail__review-stars-input" id="reviewStarsInput" role="radiogroup" aria-label="Rating">
                    ${[1, 2, 3, 4, 5].map(n => `
                        <button type="button" data-value="${n}" aria-label="${n} stars">
                            ${STAR_SVG}
                        </button>
                    `).join('')}
                </div>
                <textarea
                    class="product-detail__review-textarea"
                    id="reviewTextarea"
                    data-i18n-placeholder="productDetail.reviews.formPlaceholder"
                    placeholder="Share your thoughts about this product..."
                    maxlength="500"></textarea>
                <div class="product-detail__review-form-actions">
                    <button type="submit" class="product-detail__review-submit" id="reviewSubmit" disabled>
                        <span data-i18n="productDetail.formSubmit">Submit Review</span>
                    </button>
                </div>
            </form>
        `;

        applyI18n(area);
        attachReviewFormEvents();
    }

    function renderReviewsList() {
        const list = document.getElementById('reviewsList');
        const empty = document.getElementById('reviewsEmpty');

        if (!list) return;

        if (reviews.length === 0) {
            list.innerHTML = '';
            if (empty) empty.hidden = false;
            return;
        }

        if (empty) empty.hidden = true;

        const sorted = sortReviews(reviews, currentSort);
        list.innerHTML = sorted.map(review => renderReviewItem(review)).join('');

        // Attach like handlers
        list.querySelectorAll('.review-item__like-btn').forEach(btn => {
            btn.addEventListener('click', () => toggleReviewLike(Number(btn.dataset.reviewId), btn));
        });
    }

    function renderReviewItem(review) {
        const isLiked = likedReviewIds.has(review.id);
        const userName = review.user?.name || 'Anonymous';
        const initials = getInitials(userName);
        const date = formatDate(review.createdAt);
        const likes = review.likesCount || 0;

        return `
        <article class="review-item" data-review-id="${review.id}">
            <header class="review-item__header">
                <div class="review-item__avatar">${escapeHtml(initials)}</div>
                <div class="review-item__author">
                    <p class="review-item__name">${escapeHtml(userName)}</p>
                    <span class="review-item__date">${date}</span>
                </div>
                <div class="review-item__stars">${renderStarsHTML(review.rating)}</div>
            </header>
            <p class="review-item__text">${escapeHtml(review.comment || '')}</p>
            <footer class="review-item__footer">
                <button type="button"
                        class="review-item__like-btn ${isLiked ? 'is-liked' : ''}"
                        data-review-id="${review.id}">
                    ${THUMBS_UP_SVG}
                    <span data-likes="${review.id}">${likes}</span>
                </button>
            </footer>
        </article>
    `;
    }

    function sortReviews(list, sort) {
        const copy = [...list];
        switch (sort) {
            case 'liked':
                return copy.sort((a, b) => (b.likesCount || 0) - (a.likesCount || 0));
            case 'rating':
                return copy.sort((a, b) => (b.rating || 0) - (a.rating || 0));
            case 'recent':
            default:
                return copy.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));
        }
    }

    // ====================================================================
    //   EVENTS (static elements that don't get rebuilt)
    // ====================================================================

    function attachStaticEvents() {
        // Favorite button
        const favBtn = document.getElementById('favoriteBtn');
        if (favBtn) favBtn.addEventListener('click', toggleFavorite);

        // Add to cart button
        const cartBtn = document.getElementById('addToCartBtn');
        if (cartBtn) cartBtn.addEventListener('click', handleAddToCart);

        // Chat button
        const chatBtn = document.getElementById('chatBtn');
        if (chatBtn) chatBtn.addEventListener('click', handleTalkToSeller);

        // Tabs
        document.querySelectorAll('.product-detail__tab').forEach(tab => {
            tab.addEventListener('click', () => switchTab(tab.dataset.tab));
        });

        // Review link in header (jumps to reviews tab)
        const reviewLink = document.getElementById('productReviewLink');
        if (reviewLink) {
            reviewLink.addEventListener('click', (e) => {
                e.preventDefault();
                switchTab('reviews');
                document.getElementById('panelReviews')?.scrollIntoView({ behavior: 'smooth' });
            });
        }

        // Reviews sort
        const sortSelect = document.getElementById('reviewsSort');
        if (sortSelect) {
            sortSelect.addEventListener('change', () => {
                currentSort = sortSelect.value;
                renderReviewsList();
            });
        }
    }

    function switchTab(tabName) {
        document.querySelectorAll('.product-detail__tab').forEach(t => {
            t.classList.toggle('is-active', t.dataset.tab === tabName);
        });
        document.querySelectorAll('.product-detail__tab-panel').forEach(p => {
            const expectedId = 'panel' + capitalize(tabName);
            p.classList.toggle('is-active', p.id === expectedId);
        });
    }

    // ====================================================================
    //   ACTIONS
    // ====================================================================

    async function toggleFavorite() {
        const token = window.AuthManager && AuthManager.getToken();
        if (!token) {
            if (window.showToast) {
                showToast(I18n.get('productDetailJs.favoriteLoginRequired') || 'Please log in to favorite', 'info');
            }
            return;
        }

        const btn = document.getElementById('favoriteBtn');
        if (btn) btn.classList.toggle('is-active');

        try {
            const method = isFavorited ? 'DELETE' : 'POST';
            const res = await fetch(`/api/favorites/${productId}`, {
                method,
                headers: { 'Authorization': 'Bearer ' + token }
            });
            if (!res.ok) throw new Error('HTTP ' + res.status);
            isFavorited = !isFavorited;
        } catch (err) {
            if (btn) btn.classList.toggle('is-active');
            if (window.showToast) {
                showToast(I18n.get('productDetailJs.favoriteError') || 'Failed to update favorite', 'error');
            }
        }
    }

    function handleAddToCart() {
        const qty = Number(document.getElementById('qtyInput')?.value || 1);
        // Real cart integration in next phase
        if (window.showToast) {
            const tpl = I18n.get('productDetailJs.cartSoon') || 'Cart integration coming soon ({n} units)';
            showToast(tpl.replace('{n}', qty), 'info');
        }
    }

    function handleTalkToSeller() {
        // Real WebSocket chat in next phase
        if (window.showToast) {
            showToast(I18n.get('productDetailJs.chatSoon') || 'Live chat coming soon', 'info');
        }
    }

    // ====================================================================
    //   REVIEW FORM
    // ====================================================================

    function attachReviewFormEvents() {
        const form = document.getElementById('reviewForm');
        const textarea = document.getElementById('reviewTextarea');
        const submitBtn = document.getElementById('reviewSubmit');
        const starsContainer = document.getElementById('reviewStarsInput');

        if (!form || !textarea || !submitBtn || !starsContainer) return;

        let selectedRating = 0;

        // Star selection
        const starButtons = starsContainer.querySelectorAll('button');

        starButtons.forEach(btn => {
            btn.addEventListener('mouseenter', () => {
                const value = Number(btn.dataset.value);
                starButtons.forEach((b, i) => {
                    b.classList.toggle('is-hover', i < value);
                });
            });

            btn.addEventListener('mouseleave', () => {
                starButtons.forEach(b => b.classList.remove('is-hover'));
            });

            btn.addEventListener('click', () => {
                selectedRating = Number(btn.dataset.value);
                starButtons.forEach((b, i) => {
                    b.classList.toggle('is-filled', i < selectedRating);
                });
                updateSubmitState();
            });
        });

        textarea.addEventListener('input', updateSubmitState);

        function updateSubmitState() {
            const hasRating = selectedRating > 0;
            const hasText = textarea.value.trim().length > 0;
            submitBtn.disabled = !(hasRating && hasText);
        }

        form.addEventListener('submit', async (e) => {
            e.preventDefault();
            await submitReview(selectedRating, textarea.value.trim());
        });
    }

    async function submitReview(rating, comment) {
        const token = window.AuthManager && AuthManager.getToken();
        if (!token) return;

        const submitBtn = document.getElementById('reviewSubmit');
        if (submitBtn) submitBtn.disabled = true;

        try {
            const res = await fetch('/api/reviews', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + token
                },
                body: JSON.stringify({
                    productId,
                    rating,
                    comment
                })
            });

            if (!res.ok) {
                const errorData = await res.json().catch(() => ({}));
                throw new Error(errorData.message || 'HTTP ' + res.status);
            }

            const newReview = await res.json();
            reviews.unshift(newReview);

            // Update product aggregates locally
            const newCount = (product.reviewCount || 0) + 1;
            const oldAvg = product.averageRating || 0;
            const newAvg = ((oldAvg * (newCount - 1)) + rating) / newCount;
            product.reviewCount = newCount;
            product.averageRating = newAvg;

            renderHeader();
            renderTabs();
            renderReviewsHeader();
            renderReviewFormArea();
            renderReviewsList();

            if (window.showToast) {
                showToast(I18n.get('productDetailJs.reviewSuccess') || 'Review submitted!', 'success');
            }
        } catch (err) {
            console.error('[product-detail] Review submit failed', err);
            if (window.showToast) {
                showToast(I18n.get('productDetailJs.reviewError') || 'Failed to submit review', 'error');
            }
            if (submitBtn) submitBtn.disabled = false;
        }
    }

    // ====================================================================
    //   REVIEW LIKES
    // ====================================================================

    async function toggleReviewLike(reviewId, btn) {
        const token = window.AuthManager && AuthManager.getToken();
        if (!token) {
            if (window.showToast) {
                showToast(I18n.get('productDetailJs.likeLoginRequired') || 'Please log in to like reviews', 'info');
            }
            return;
        }

        const isLiked = likedReviewIds.has(reviewId);
        const method = isLiked ? 'DELETE' : 'POST';

        // Optimistic UI
        btn.classList.toggle('is-liked');
        const counter = document.querySelector(`[data-likes="${reviewId}"]`);
        if (counter) {
            const current = Number(counter.textContent || 0);
            counter.textContent = isLiked ? current - 1 : current + 1;
        }

        try {
            const res = await fetch(`/api/reviews/${reviewId}/like`, {
                method,
                headers: { 'Authorization': 'Bearer ' + token }
            });
            if (!res.ok) throw new Error('HTTP ' + res.status);

            if (isLiked) likedReviewIds.delete(reviewId);
            else likedReviewIds.add(reviewId);
        } catch (err) {
            // Revert
            btn.classList.toggle('is-liked');
            if (counter) {
                const current = Number(counter.textContent || 0);
                counter.textContent = isLiked ? current + 1 : current - 1;
            }
        }
    }

    // ====================================================================
    //   ZOOM (Amazon-style)
    // ====================================================================

    function setupZoom() {
        const wrapper = document.getElementById('mainImageWrapper');
        const lens = document.getElementById('zoomLens');
        const panel = document.getElementById('zoomPanel');
        const gallery = document.querySelector('.product-detail__gallery');
        const mainImage = document.getElementById('mainImage');

        if (!wrapper || !lens || !panel || !gallery || !mainImage) return;

        // Disable zoom on touch devices
        if (window.matchMedia('(hover: none)').matches) return;

        wrapper.addEventListener('mouseenter', activateZoom);
        wrapper.addEventListener('mouseleave', deactivateZoom);
        wrapper.addEventListener('mousemove', moveZoom);

        function activateZoom() {
            const rect = wrapper.getBoundingClientRect();
            panel.style.backgroundImage = `url("${mainImage.src}")`;
            panel.style.backgroundSize = `${rect.width * ZOOM_FACTOR}px ${rect.height * ZOOM_FACTOR}px`;
            wrapper.classList.add('is-zooming');
            gallery.classList.add('is-zooming');
        }

        function deactivateZoom() {
            wrapper.classList.remove('is-zooming');
            gallery.classList.remove('is-zooming');
        }

        function moveZoom(e) {
            const rect = wrapper.getBoundingClientRect();
            let x = e.clientX - rect.left;
            let y = e.clientY - rect.top;

            // Lens position (centered on cursor, clamped to image bounds)
            let lensX = x - LENS_SIZE / 2;
            let lensY = y - LENS_SIZE / 2;
            lensX = Math.max(0, Math.min(rect.width - LENS_SIZE, lensX));
            lensY = Math.max(0, Math.min(rect.height - LENS_SIZE, lensY));

            lens.style.left = lensX + 'px';
            lens.style.top = lensY + 'px';

            // Background position on the panel (mirrors lens position * factor)
            const bgX = -lensX * ZOOM_FACTOR;
            const bgY = -lensY * ZOOM_FACTOR;
            panel.style.backgroundPosition = `${bgX}px ${bgY}px`;
        }
    }

    // ====================================================================
    //   I18N RE-RENDER
    // ====================================================================

    function setupLanguageReRender() {
        if (window.I18n && typeof I18n.onLanguageChange === 'function') {
            I18n.onLanguageChange(() => {
                renderPrice();
                renderReviewFormArea();
                renderReviewsList();
            });
        }
    }

    function applyI18n(scope) {
        if (window.I18n && typeof I18n.apply === 'function') {
            I18n.apply(scope);
        }
    }

    // ====================================================================
    //   HELPERS
    // ====================================================================

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

    function formatDate(iso) {
        if (!iso) return '';
        try {
            const lang = (window.I18n && I18n.getCurrentLanguage && I18n.getCurrentLanguage()) || 'pt';
            const locale = LOCALE_MAP[lang] || 'pt-BR';
            return new Intl.DateTimeFormat(locale, {
                year: 'numeric',
                month: 'short',
                day: 'numeric'
            }).format(new Date(iso));
        } catch {
            return iso.substring(0, 10);
        }
    }

    function renderStarsHTML(rating) {
        const r = Number(rating) || 0;
        let html = '';
        for (let i = 1; i <= 5; i++) {
            const filled = i <= Math.round(r);
            html += `<svg class="${filled ? 'is-filled' : ''}" viewBox="0 0 24 24" fill="currentColor"><path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/></svg>`;
        }
        return html;
    }

    function getInitials(name) {
        return name
            .split(' ')
            .filter(Boolean)
            .slice(0, 2)
            .map(p => p.charAt(0).toUpperCase())
            .join('');
    }

    function capitalize(str) {
        return str.charAt(0).toUpperCase() + str.slice(1);
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

    // ===== Inline SVG icons =====
    const STAR_SVG = `<svg viewBox="0 0 24 24" fill="currentColor"><path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/></svg>`;

    const THUMBS_UP_SVG = `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M14 9V5a3 3 0 0 0-3-3l-4 9v11h11.28a2 2 0 0 0 2-1.7l1.38-9a2 2 0 0 0-2-2.3zM7 22H4a2 2 0 0 1-2-2v-7a2 2 0 0 1 2-2h3"/></svg>`;
})();
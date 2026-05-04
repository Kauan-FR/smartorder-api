(function () {
    'use strict';

    // ===== Config =====
    const LOCALE_MAP = { en: 'en-US', pt: 'pt-BR', es: 'es-ES', fr: 'fr-FR' };
    const CURRENCY_MAP = { en: 'USD', pt: 'BRL', es: 'EUR', fr: 'EUR' };
    const QTY_DEBOUNCE_MS = 400;
    const REMOVE_ANIMATION_MS = 200;

    // ===== State =====
    let cartItems = [];
    const pendingQtyUpdates = new Map(); // itemId -> { timeout, originalQty }

    // ===== DOM refs =====
    const loading = document.getElementById('cartLoading');
    const empty = document.getElementById('cartEmpty');
    const content = document.getElementById('cartContent');
    const itemsList = document.getElementById('cartItemsList');
    const subtitleCount = document.querySelector('[data-cart-count]');
    const summarySubtotal = document.getElementById('summarySubtotal');
    const summaryTotal = document.getElementById('summaryTotal');
    const clearBtn = document.getElementById('cartClearBtn');
    const checkoutBtn = document.getElementById('cartCheckoutBtn');

    // ===== Auth guard =====
    const token = window.AuthManager && AuthManager.getToken();
    if (!token) {
        window.location.href = '/login';
        return;
    }

    // ===== Init =====
    init();

    function init() {
        loadCart();
        attachStaticEvents();
        setupLanguageReRender();
    }

    // ====================================================================
    //   DATA LOADING
    // ====================================================================

    function loadCart() {
        showLoading();

        fetch('/api/cart', {
            headers: { 'Authorization': 'Bearer ' + token }
        })
            .then((res) => {
                if (res.status === 401) {
                    window.location.href = '/login';
                    return null;
                }
                if (!res.ok) throw new Error('HTTP ' + res.status);
                return res.json();
            })
            .then((items) => {
                if (!items) return;
                cartItems = items;
                renderAll();
            })
            .catch((err) => {
                console.error('[cart] Failed to load', err);
                showToast(I18n.get('cartPageJs.loginRequired') || 'Could not load your cart', 'error');
            });
    }

    // ====================================================================
    //   STATE TRANSITIONS
    // ====================================================================

    function showLoading() {
        if (loading) loading.hidden = false;
        if (empty) empty.hidden = true;
        if (content) content.hidden = true;
    }

    function showEmpty() {
        if (loading) loading.hidden = true;
        if (empty) empty.hidden = false;
        if (content) content.hidden = true;
    }

    function showContent() {
        if (loading) loading.hidden = true;
        if (empty) empty.hidden = true;
        if (content) content.hidden = false;
    }

    // ====================================================================
    //   RENDERING
    // ====================================================================

    function renderAll() {
        if (cartItems.length === 0) {
            showEmpty();
            updateSubtitle(0);
            return;
        }

        showContent();
        renderItems();
        renderSummary();
        updateSubtitle(getTotalQuantity());
    }

    function renderItems() {
        if (!itemsList) return;
        itemsList.innerHTML = cartItems.map(buildItemHTML).join('');
        attachItemEvents();
        applyI18n(itemsList);
    }

    function buildItemHTML(item) {
        const product = item.product;
        const hasDiscount = product.discountPercent && product.discountPercent > 0;
        const finalPrice = product.finalPrice ?? product.price;
        const subtotal = finalPrice * item.quantity;
        const stock = product.stockQuantity || 0;

        const priceBlock = hasDiscount
            ? `
                ${formatPrice(finalPrice)}
                <span class="cart-item__price-original">${formatPrice(product.price)}</span>
            `
            : formatPrice(finalPrice);

        const categoryBlock = product.category
            ? `<span class="cart-item__category">${escapeHtml(product.category.name)}</span>`
            : '';

        return `
            <li class="cart-item" data-item-id="${item.id}" data-product-id="${product.id}">
                <div class="cart-item__product">
                    <img class="cart-item__image"
                         src="${escapeHtml(product.imageUrl || '/img/product-placeholder.png')}"
                         alt="${escapeHtml(product.name)}"
                         data-action="navigate"
                         loading="lazy">
                    <div class="cart-item__info">
                        <p class="cart-item__name" data-action="navigate">${escapeHtml(product.name)}</p>
                        ${categoryBlock}
                    </div>
                </div>

                <div class="cart-item__price" data-label-i18n="cartPageJs.colPrice">
                    ${priceBlock}
                </div>

                <div class="cart-item__qty">
                    <button type="button" class="cart-item__qty-btn"
                            data-action="qty-decrease"
                            ${item.quantity <= 1 ? 'disabled' : ''}
                            aria-label="Decrease quantity">−</button>
                    <input type="number"
                           class="cart-item__qty-input"
                           data-action="qty-input"
                           value="${item.quantity}"
                           min="1"
                           max="${stock}"
                           aria-label="Quantity">
                    <button type="button" class="cart-item__qty-btn"
                            data-action="qty-increase"
                            ${item.quantity >= stock ? 'disabled' : ''}
                            aria-label="Increase quantity">+</button>
                </div>

                <div class="cart-item__subtotal" data-label-i18n="cartPageJs.colSubtotal">
                    ${formatPrice(subtotal)}
                </div>

                <div class="cart-item__actions">
                    <button type="button" class="cart-item__remove-btn"
                            data-action="remove"
                            aria-label="Remove item">
                        ${TRASH_SVG}
                    </button>
                </div>
            </li>
        `;
    }

    function renderSummary() {
        const subtotal = getCartSubtotal();
        if (summarySubtotal) summarySubtotal.textContent = formatPrice(subtotal);
        if (summaryTotal) summaryTotal.textContent = formatPrice(subtotal);
    }

    function updateSubtitle(count) {
        if (subtitleCount) subtitleCount.textContent = count;
    }

    // ====================================================================
    //   EVENTS
    // ====================================================================

    function attachStaticEvents() {
        if (clearBtn) clearBtn.addEventListener('click', handleClearCart);
        if (checkoutBtn) checkoutBtn.addEventListener('click', handleCheckout);
    }

    function attachItemEvents() {
        const items = itemsList.querySelectorAll('.cart-item');

        items.forEach((li) => {
            const itemId = Number(li.dataset.itemId);
            const productId = Number(li.dataset.productId);

            // Navigate to product page (image + name)
            li.querySelectorAll('[data-action="navigate"]').forEach((el) => {
                el.addEventListener('click', () => {
                    window.location.href = `/store/product/${productId}`;
                });
            });

            // Quantity controls
            const decreaseBtn = li.querySelector('[data-action="qty-decrease"]');
            const increaseBtn = li.querySelector('[data-action="qty-increase"]');
            const qtyInput = li.querySelector('[data-action="qty-input"]');

            if (decreaseBtn) {
                decreaseBtn.addEventListener('click', () => changeQuantity(itemId, -1));
            }
            if (increaseBtn) {
                increaseBtn.addEventListener('click', () => changeQuantity(itemId, 1));
            }
            if (qtyInput) {
                qtyInput.addEventListener('change', () => setQuantity(itemId, Number(qtyInput.value)));
            }

            // Remove button
            const removeBtn = li.querySelector('[data-action="remove"]');
            if (removeBtn) {
                removeBtn.addEventListener('click', () => handleRemoveItem(itemId));
            }
        });
    }

    // ====================================================================
    //   QUANTITY ACTIONS (with debounce + optimistic UI)
    // ====================================================================

    function changeQuantity(itemId, delta) {
        const item = cartItems.find((i) => i.id === itemId);
        if (!item) return;

        const newQty = item.quantity + delta;
        setQuantity(itemId, newQty);
    }

    function setQuantity(itemId, newQty) {
        const item = cartItems.find((i) => i.id === itemId);
        if (!item) return;

        const stock = item.product.stockQuantity || 0;
        const clampedQty = Math.max(1, Math.min(stock, newQty));

        if (clampedQty === item.quantity) {
            // No-op (already at this quantity); just resync the input visually
            const input = itemsList.querySelector(`[data-item-id="${itemId}"] [data-action="qty-input"]`);
            if (input) input.value = clampedQty;
            return;
        }

        // Save original quantity for rollback if backend rejects
        const pending = pendingQtyUpdates.get(itemId);
        const originalQty = pending ? pending.originalQty : item.quantity;

        // Optimistic update — change local state immediately
        item.quantity = clampedQty;
        renderItems();
        renderSummary();
        updateSubtitle(getTotalQuantity());

        // Debounce backend call
        if (pending) clearTimeout(pending.timeout);

        const timeout = setTimeout(() => {
            commitQuantityChange(itemId, clampedQty, originalQty);
            pendingQtyUpdates.delete(itemId);
        }, QTY_DEBOUNCE_MS);

        pendingQtyUpdates.set(itemId, { timeout, originalQty });
    }

    function commitQuantityChange(itemId, newQty, originalQty) {
        const item = cartItems.find((i) => i.id === itemId);
        if (!item) return;

        fetch(`/api/cart/${itemId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token,
            },
            body: JSON.stringify({ productId: item.product.id, quantity: newQty }),
        })
            .then((res) => res.json().then((body) => ({ status: res.status, body })))
            .then(({ status, body }) => {
                if (status === 200) {
                    window.dispatchEvent(new CustomEvent('cart:updated'));
                    return;
                }

                // Rollback on any error
                const it = cartItems.find((i) => i.id === itemId);
                if (it) it.quantity = originalQty;
                renderItems();
                renderSummary();
                updateSubtitle(getTotalQuantity());

                if (status === 409) {
                    showToast(I18n.get('cartPageJs.stockExceeded') || 'Not enough units in stock', 'error');
                } else {
                    showToast(I18n.get('cartPageJs.quantityError') || 'Could not update quantity', 'error');
                }
            })
            .catch(() => {
                const it = cartItems.find((i) => i.id === itemId);
                if (it) it.quantity = originalQty;
                renderItems();
                renderSummary();
                updateSubtitle(getTotalQuantity());
                showToast(I18n.get('cartPageJs.quantityError') || 'Could not update quantity', 'error');
            });
    }

    // ====================================================================
    //   REMOVE ITEM
    // ====================================================================

    function handleRemoveItem(itemId) {
        const li = itemsList.querySelector(`[data-item-id="${itemId}"]`);
        if (!li) return;

        // Animate out
        li.classList.add('is-removing');

        setTimeout(() => {
            fetch(`/api/cart/${itemId}`, {
                method: 'DELETE',
                headers: { 'Authorization': 'Bearer ' + token }
            })
                .then((res) => {
                    if (res.status === 204) {
                        cartItems = cartItems.filter((i) => i.id !== itemId);
                        renderAll();
                        showToast(I18n.get('cartPageJs.itemRemoved') || 'Item removed from cart', 'success');
                        window.dispatchEvent(new CustomEvent('cart:updated'));
                        return;
                    }
                    throw new Error('HTTP ' + res.status);
                })
                .catch(() => {
                    li.classList.remove('is-removing');
                    showToast(I18n.get('cartPageJs.removeError') || 'Could not remove item', 'error');
                });
        }, REMOVE_ANIMATION_MS);
    }

    // ====================================================================
    //   CLEAR CART
    // ====================================================================

    function handleClearCart() {
        const confirmMsg = I18n.get('cartPageJs.clearConfirm') ||
            'Are you sure you want to remove all items from your cart?';
        if (!confirm(confirmMsg)) return;

        clearBtn.disabled = true;

        fetch('/api/cart', {
            method: 'DELETE',
            headers: { 'Authorization': 'Bearer ' + token }
        })
            .then((res) => {
                if (res.status === 204) {
                    cartItems = [];
                    renderAll();
                    showToast(I18n.get('cartPageJs.cartCleared') || 'Cart cleared', 'success');
                    window.dispatchEvent(new CustomEvent('cart:updated'));
                    return;
                }
                throw new Error('HTTP ' + res.status);
            })
            .catch(() => {
                showToast(I18n.get('cartPageJs.clearError') || 'Could not clear cart', 'error');
            })
            .finally(() => {
                clearBtn.disabled = false;
            });
    }

    // ====================================================================
    //   CHECKOUT (placeholder)
    // ====================================================================

    function handleCheckout() {
        showToast(I18n.get('cartPageJs.checkoutSoon') || 'Checkout coming soon', 'info');
    }

    // ====================================================================
    //   I18N RE-RENDER
    // ====================================================================

    function setupLanguageReRender() {
        if (window.I18n && typeof I18n.onLanguageChange === 'function') {
            I18n.onLanguageChange(() => {
                renderItems();
                renderSummary();
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

    function getCartSubtotal() {
        return cartItems.reduce((sum, item) => {
            const price = item.product.finalPrice ?? item.product.price;
            return sum + price * item.quantity;
        }, 0);
    }

    function getTotalQuantity() {
        return cartItems.reduce((sum, item) => sum + (item.quantity || 0), 0);
    }

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
    const TRASH_SVG = `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="3 6 5 6 21 6"/><path d="M19 6l-2 14a2 2 0 0 1-2 2H9a2 2 0 0 1-2-2L5 6"/><path d="M10 11v6M14 11v6"/><path d="M9 6V4a2 2 0 0 1 2-2h2a2 2 0 0 1 2 2v2"/></svg>`;
})();
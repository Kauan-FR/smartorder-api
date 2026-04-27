/**
 * Store Carousel — fetches featured, deals, and low-stock products from the API
 * and renders them as a hero carousel on the storefront home page.
 *
 * Features:
 * - Parallel fetch of 3 endpoints with deduplication (Deal > Low Stock > Featured)
 * - Autoplay every 5s with pause on hover
 * - Manual navigation via arrows and dots
 * - Mobile swipe support
 * - Hides itself if no products are available
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
(function () {
    'use strict';

    var AUTOPLAY_INTERVAL = 5000;
    var BADGE_PRIORITY = { deal: 3, lowStock: 2, featured: 1 };

    var state = {
        slides: [],
        currentIndex: 0,
        autoplayTimer: null,
        isPaused: false,
        touchStartX: 0,
        touchEndX: 0
    };

    /* ==========================================================================
       INIT
       ========================================================================== */

    document.addEventListener('DOMContentLoaded', function () {
        loadCarouselProducts();
    });

    function setupLanguageRefresh() {
        if (typeof I18n !== 'undefined' && I18n.onLanguageChange) {
            I18n.onLanguageChange(function () {
                renderCarousel();
            });
        }
    }

    /* ==========================================================================
       FETCH & MERGE
       ========================================================================== */

    function loadCarouselProducts() {
        Promise.all([
            fetchSafe('/api/products/featured'),
            fetchSafe('/api/products/deals'),
            fetchSafe('/api/products/low-stock')
        ])
            .then(function (results) {
                var featured = results[0] || [];
                var deals = results[1] || [];
                var lowStock = results[2] || [];

                var merged = mergeProducts(featured, deals, lowStock);

                if (merged.length === 0) {
                    return;
                }

                state.slides = merged;
                renderCarousel();
                setupControls();
                startAutoplay();
                showCarousel();
                setupLanguageRefresh();
            })
            .catch(function () {
                showToast(I18n.get('storeCarouselJs.failedLoad'), 'error');
            });
    }

    function fetchSafe(url) {
        return fetch(url)
            .then(function (res) {
                if (!res.ok) return [];
                return res.json();
            })
            .catch(function () {
                return [];
            });
    }

    /**
     * Merges products from 3 sources, applying badge priority (Deal > Low Stock > Featured)
     * when a product appears in more than one category.
     */
    function mergeProducts(featured, deals, lowStock) {
        var byId = {};

        featured.forEach(function (p) {
            byId[p.id] = { product: p, badge: 'featured' };
        });

        lowStock.forEach(function (p) {
            if (!byId[p.id] || BADGE_PRIORITY.lowStock > BADGE_PRIORITY[byId[p.id].badge]) {
                byId[p.id] = { product: p, badge: 'lowStock' };
            }
        });

        deals.forEach(function (p) {
            if (!byId[p.id] || BADGE_PRIORITY.deal > BADGE_PRIORITY[byId[p.id].badge]) {
                byId[p.id] = { product: p, badge: 'deal' };
            }
        });

        return shuffle(Object.values(byId));
    }

    function shuffle(array) {
        var arr = array.slice();
        for (var i = arr.length - 1; i > 0; i--) {
            var j = Math.floor(Math.random() * (i + 1));
            var temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
        return arr;
    }

    /* ==========================================================================
       RENDER
       ========================================================================== */

    function renderCarousel() {
        var track = document.getElementById('carouselTrack');
        var dotsContainer = document.getElementById('carouselDots');
        if (!track || !dotsContainer) return;

        track.innerHTML = state.slides.map(renderSlide).join('');
        dotsContainer.innerHTML = state.slides.map(renderDot).join('');

        updateActiveDot();
    }

    function renderSlide(slide) {
        var p = slide.product;
        var badge = renderBadge(slide.badge);
        var price = renderPrice(p);
        var rating = renderRating(p);
        var image = p.imageUrl
            ? '<img src="' + escapeAttr(p.imageUrl) + '" alt="' + escapeAttr(p.name) + '">'
            : '';

        return '<div class="store-carousel__slide" data-product-id="' + p.id + '">' +
            '<div class="store-carousel__slide-content">' +
            badge +
            '<h2 class="store-carousel__slide-name">' + escapeHtml(p.name) + '</h2>' +
            '<p class="store-carousel__slide-description">' + escapeHtml(p.description || '') + '</p>' +
            price +
            rating +
            '</div>' +
            '<div class="store-carousel__slide-image">' + image + '</div>' +
            '</div>';
    }

    function renderBadge(type) {
        var labelKey = type === 'deal' ? 'badgeDeal'
            : type === 'lowStock' ? 'badgeLowStock'
                : 'badgeFeatured';
        var className = type === 'lowStock' ? 'low-stock' : type;

        return '<span class="store-carousel__badge store-carousel__badge--' + className + '">' +
            I18n.get('storeCarousel.' + labelKey) +
            '</span>';
    }

    function renderPrice(p) {
        var hasDiscount = p.discountPercent && p.discountPercent > 0;
        var finalPrice = formatPrice(p.finalPrice != null ? p.finalPrice : p.price);
        var originalPrice = hasDiscount
            ? '<span class="store-carousel__slide-price-original">' + formatPrice(p.price) + '</span>'
            : '';

        return '<div class="store-carousel__slide-price">' +
            '<span class="store-carousel__slide-price-final">' + finalPrice + '</span>' +
            originalPrice +
            '</div>';
    }

    function renderRating(p) {
        var rating = p.averageRating != null ? p.averageRating : 0;
        var count = p.reviewCount != null ? p.reviewCount : 0;
        var stars = renderStars(rating);

        var countText = count > 0
            ? '(' + count + ' ' + I18n.get('storeCarousel.reviews') + ')'
            : I18n.get('storeCarousel.noReviews');

        return '<div class="store-carousel__slide-rating">' +
            '<span class="store-carousel__slide-rating-stars">' + stars + '</span>' +
            '<span class="store-carousel__slide-rating-count">' + countText + '</span>' +
            '</div>';
    }

    function renderStars(rating) {
        var rounded = Math.round(rating);
        var full = '★'.repeat(rounded);
        var empty = '☆'.repeat(5 - rounded);
        return full + empty;
    }

    function renderDot(_, index) {
        return '<button class="store-carousel__dot" data-index="' + index + '" type="button"></button>';
    }

    function showCarousel() {
        var carousel = document.getElementById('storeCarousel');
        if (carousel) carousel.hidden = false;
    }

    /* ==========================================================================
       NAVIGATION
       ========================================================================== */

    function goTo(index) {
        if (state.slides.length === 0) return;

        if (index < 0) index = state.slides.length - 1;
        if (index >= state.slides.length) index = 0;

        state.currentIndex = index;
        var track = document.getElementById('carouselTrack');
        if (track) {
            track.style.transform = 'translateX(-' + (index * 100) + '%)';
        }
        updateActiveDot();
    }

    function next() {
        goTo(state.currentIndex + 1);
    }

    function prev() {
        goTo(state.currentIndex - 1);
    }

    function updateActiveDot() {
        var dots = document.querySelectorAll('.store-carousel__dot');
        dots.forEach(function (dot, i) {
            dot.classList.toggle('store-carousel__dot--active', i === state.currentIndex);
        });
    }

    /* ==========================================================================
       CONTROLS
       ========================================================================== */

    function setupControls() {
        var prevBtn = document.getElementById('carouselPrev');
        var nextBtn = document.getElementById('carouselNext');
        var carousel = document.getElementById('storeCarousel');
        var track = document.getElementById('carouselTrack');

        if (prevBtn) prevBtn.addEventListener('click', function () { resetAutoplay(); prev(); });
        if (nextBtn) nextBtn.addEventListener('click', function () { resetAutoplay(); next(); });

        var dotsContainer = document.getElementById('carouselDots');
        if (dotsContainer) {
            dotsContainer.addEventListener('click', function (e) {
                if (e.target.classList.contains('store-carousel__dot')) {
                    var index = parseInt(e.target.getAttribute('data-index'), 10);
                    resetAutoplay();
                    goTo(index);
                }
            });
        }

        if (carousel) {
            carousel.addEventListener('mouseenter', function () { state.isPaused = true; });
            carousel.addEventListener('mouseleave', function () { state.isPaused = false; });
        }

        if (track) {
            track.addEventListener('click', function (e) {
                var slide = e.target.closest('.store-carousel__slide');
                if (slide) {
                    var productId = slide.getAttribute('data-product-id');
                    // TODO: replace with real product detail page when available
                    window.location.href = '/store/product/' + productId;
                }
            });

            track.addEventListener('touchstart', function (e) {
                state.touchStartX = e.changedTouches[0].screenX;
            }, { passive: true });

            track.addEventListener('touchend', function (e) {
                state.touchEndX = e.changedTouches[0].screenX;
                handleSwipe();
            }, { passive: true });
        }
    }

    function handleSwipe() {
        var diff = state.touchStartX - state.touchEndX;
        var threshold = 50;
        if (Math.abs(diff) < threshold) return;
        resetAutoplay();
        if (diff > 0) next();
        else prev();
    }

    /* ==========================================================================
       AUTOPLAY
       ========================================================================== */

    function startAutoplay() {
        stopAutoplay();
        state.autoplayTimer = setInterval(function () {
            if (!state.isPaused) next();
        }, AUTOPLAY_INTERVAL);
    }

    function stopAutoplay() {
        if (state.autoplayTimer) {
            clearInterval(state.autoplayTimer);
            state.autoplayTimer = null;
        }
    }

    function resetAutoplay() {
        stopAutoplay();
        startAutoplay();
    }

    /* ==========================================================================
       HELPERS
       ========================================================================== */

    function formatPrice(value) {
        if (value == null) return '';
        return 'R$ ' + Number(value).toFixed(2).replace('.', ',');
    }

    function escapeHtml(text) {
        var div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }

    function escapeAttr(text) {
        return String(text).replace(/"/g, '&quot;').replace(/'/g, '&#39;');
    }
})();
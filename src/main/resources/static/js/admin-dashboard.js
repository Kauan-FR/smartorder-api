/**
 * Admin Dashboard — loads metrics, orders table, and status bars from the API.
 *
 * @author Kauan Santos Ferreira
 * @since 2026
 */

// ==================== Auth Guard ====================

AuthManager.guard();

// ==================== Populate User Info ====================

var user = AuthManager.getUser();
if (user) {
    var initials = AuthManager.getInitials();
    document.getElementById('sidebarName').textContent = user.name;
    document.getElementById('sidebarAvatar').textContent = initials;
    document.getElementById('popupName').textContent = user.name;
    document.getElementById('popupAvatar').textContent = initials;
    document.getElementById('welcomeText').textContent = 'Welcome back, ' + user.name.split(' ')[0];
}

// ==================== Sidebar ====================

function toggleSidebar() {
    var sidebar = document.getElementById('sidebar');
    sidebar.classList.toggle('is-collapsed');
    localStorage.setItem('smartorder-sidebar', sidebar.classList.contains('is-collapsed') ? 'collapsed' : 'expanded');
}

// Restore sidebar state
if (localStorage.getItem('smartorder-sidebar') === 'collapsed') {
    document.getElementById('sidebar').classList.add('is-collapsed');
}

// ==================== Profile Popup ====================

function toggleProfilePopup() {
    var popup = document.getElementById('profilePopup');
    popup.classList.toggle('is-open');
}

// Close popup when clicking outside
document.addEventListener('click', function(e) {
    var popup = document.getElementById('profilePopup');
    var trigger = document.getElementById('profileTrigger');
    if (popup.classList.contains('is-open') && !trigger.contains(e.target) && !popup.contains(e.target)) {
        popup.classList.remove('is-open');
    }
});

// ==================== Sign Out ====================

function signOut() {
    AuthManager.logout();
}

// ==================== Status Helpers ====================

function getStatusClass(status) {
    var map = {
        'PENDING': 'badge-warning',
        'CONFIRMED': 'badge-info',
        'SHIPPED': 'badge-info',
        'DELIVERED': 'badge-success',
        'CANCELLED': 'badge-danger'
    };
    return map[status] || 'badge-info';
}

function formatStatus(status) {
    var map = {
        'PENDING': 'Pending',
        'CONFIRMED': 'Confirmed',
        'SHIPPED': 'Shipped',
        'DELIVERED': 'Delivered',
        'CANCELLED': 'Cancelled'
    };
    return map[status] || status;
}

function getStatusColor(status) {
    var map = {
        'PENDING': '#EF9F27',
        'CONFIRMED': '#85B7EB',
        'SHIPPED': '#AFA9EC',
        'DELIVERED': '#5DCAA5',
        'CANCELLED': '#F09595'
    };
    return map[status] || '#85B7EB';
}

// ==================== Load Dashboard Data ====================

function loadDashboard() {
    var token = AuthManager.getToken();
    var headers = { 'Authorization': 'Bearer ' + token, 'Content-Type': 'application/json' };

    // Fetch orders count
    fetch('/api/orders', { headers: headers })
        .then(function(r) { return r.json(); })
        .then(function(data) {
            var orders = Array.isArray(data) ? data : (data.content || []);
            document.getElementById('metricOrders').textContent = orders.length;
        })
        .catch(function() { document.getElementById('metricOrders').textContent = '0'; });

    // Fetch users count
    fetch('/api/users', { headers: headers })
        .then(function(r) { return r.json(); })
        .then(function(data) {
            var users = Array.isArray(data) ? data : (data.content || []);
            document.getElementById('metricUsers').textContent = users.length;
        })
        .catch(function() { document.getElementById('metricUsers').textContent = '0'; });

    // Fetch orders count
    fetch('/api/orders', { headers: headers })
        .then(function(r) { return r.json(); })
        .then(function(data) {
            var orders = Array.isArray(data) ? data : (data.content || []);
            document.getElementById('metricOrders').textContent = orders.length;
        })
        .catch(function() { document.getElementById('metricOrders').textContent = '0'; });

    // Fetch products count
    fetch('/api/products', { headers: headers })
        .then(function(r) { return r.json(); })
        .then(function(data) {
            var products = Array.isArray(data) ? data : (data.content || []);
            document.getElementById('metricProducts').textContent = products.length;
        })
        .catch(function() { document.getElementById('metricProducts').textContent = '0'; });

    // Fetch recent orders for table
    fetch('/api/orders?size=5&sort=id,desc', { headers: headers })
        .then(function(r) { return r.json(); })
        .then(function(data) {
            var orders = Array.isArray(data) ? data : (data.content || []);
            renderOrdersTable(orders);
            renderStatusBars(orders);
        })
        .catch(function() {
            document.getElementById('ordersTableBody').innerHTML = '<tr><td colspan="4" style="text-align:center;padding:24px;color:var(--text-tertiary);">Failed to load orders</td></tr>';
        });

    // Fetch revenue (sum of all orders totalAmount)
    fetch('/api/orders?size=1000', { headers: headers })
        .then(function(r) { return r.json(); })
        .then(function(data) {
            var orders = Array.isArray(data) ? data : (data.content || []);
            var total = 0;
            orders.forEach(function(order) {
                if (order.totalAmount) {
                    total += parseFloat(order.totalAmount);
                }
            });
            if (total >= 1000) {
                document.getElementById('metricRevenue').textContent = 'R$ ' + (total / 1000).toFixed(1) + 'k';
            } else {
                document.getElementById('metricRevenue').textContent = 'R$ ' + total.toFixed(2);
            }
        })
        .catch(function() { document.getElementById('metricRevenue').textContent = 'R$ 0';
        });
}

// ==================== Render Orders Table ====================

function renderOrdersTable(orders) {
    var tbody = document.getElementById('ordersTableBody');

    if (orders.length === 0) {
        tbody.innerHTML = '<tr><td colspan="4" style="text-align:center;padding:24px;color:var(--text-tertiary);">No orders yet</td></tr>';
        return;
    }

    var html = '';
    orders.forEach(function(order) {
        var statusClass = getStatusClass(order.status);
        html += '<tr>'
            + '<td class="table__cell--primary">#' + order.id + '</td>'
            + '<td>' + (order.user ? order.user.name : 'Unknown') + '</td>'
            + '<td>R$ ' + (order.totalAmount ? order.totalAmount.toFixed(2) : '0.00') + '</td>'
            + '<td><span class="badge ' + statusClass + '">' + formatStatus(order.status) + '</span></td>'
            + '</tr>';
    });
    tbody.innerHTML = html;
}

// ==================== Render Status Bars ====================

function renderStatusBars(orders) {
    var statusBarsEl = document.getElementById('statusBars');
    var statusCount = {};
    var total = 0;

    // Count all orders by status (fetch all for accurate count)
    var token = AuthManager.getToken();
    var headers = { 'Authorization': 'Bearer ' + token, 'Content-Type': 'application/json' };

    fetch('/api/orders?size=1000', { headers: headers })
        .then(function(r) { return r.json(); })
        .then(function(data) {
            var allOrders = Array.isArray(data) ? data : (data.content || []);
            total = allOrders.length;

            allOrders.forEach(function(order) {
                statusCount[order.status] = (statusCount[order.status] || 0) + 1;
            });

            var statusOrder = ['DELIVERED', 'CONFIRMED', 'SHIPPED', 'PENDING', 'CANCELLED'];
            var html = '';

            statusOrder.forEach(function(status) {
                var count = statusCount[status] || 0;
                var percent = total > 0 ? Math.round((count / total) * 100) : 0;
                var color = getStatusColor(status);

                html += '<div class="status-bar">'
                    + '<div class="status-bar__header">'
                    + '<span class="status-bar__label">' + formatStatus(status) + '</span>'
                    + '<span class="status-bar__value">' + count + '</span>'
                    + '</div>'
                    + '<div class="status-bar__track">'
                    + '<div class="status-bar__fill" style="width:' + percent + '%;background-color:' + color + ';"></div>'
                    + '</div>'
                    + '</div>';
            });

            statusBarsEl.innerHTML = html;
        })
        .catch(function() {
            statusBarsEl.innerHTML = '<p style="color:var(--text-tertiary);font-size:var(--text-sm);">Failed to load</p>';
        });
}

// ==================== Initialize ====================

loadDashboard();
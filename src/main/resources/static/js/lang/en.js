/**
 * English translations for SmartOrder admin panel.
 *
 * @author Kauan Santos Ferreira
 * @since 2026
 */

I18n.register('en', {

    // ==================== Sidebar ====================
    sidebar: {
        brand: 'SmartOrder',
        sectionMain: 'Main',
        dashboard: 'Dashboard',
        categories: 'Categories',
        products: 'Products',
        users: 'Users',
        addresses: 'Addresses',
        sectionSales: 'Sales',
        orders: 'Orders',
        orderItems: 'Order items'
    },

    // ==================== Profile Popup(Sidebar) ====================
    popup: {
        profile: 'Profile',
        settings: 'Settings',
        help: 'Help',
        signOut: 'Sign out'
    },

    // ==================== Help Submenu(Sidebar) ====================
    helpMenu: {
        helpCenter: 'Help center',
        releaseNotes: 'Release notes',
        termsAndPolicies: 'Terms & policies',
        reportBug: 'Report a bug'
    },

    // ==================== Topbar ====================
    topbar: {
        searchPlaceholder: 'Search...',
        language: 'Language',
        theme: 'Toggle theme',
        notifications: 'Notifications',
        notifClearAll: 'Clear all',
        notifEmpty: 'No notifications yet'
    },

    // ======================= Help Modals =======================
    // ==================== Help Center Modal(Help Modals) ====================
    helpCenter: {
        title: 'Help Center',
        description: 'Find answers to frequently asked questions about the SmartOrder admin panel.',
        faq1q: 'How do I add a new product?',
        faq1a: 'Navigate to Products in the sidebar, click the "New product" button in the top right corner, fill in the product details (name, description, price, stock, category, and image URL), then click "Save". You can also set a discount percentage and mark the product as featured.',
        faq2q: 'How do I change my password?',
        faq2a: 'Go to Profile from the popup menu at the bottom of the sidebar. In the security section, click "Change password", enter your current password followed by the new password and confirmation, then save.',
        faq3q: 'What do the order statuses mean?',
        faq3aTitle: 'Order statuses:',
        faq3aPending: 'Pending — Order was placed but not yet confirmed by the store.',
        faq3aConfirmed: 'Confirmed — Store accepted the order and is preparing it.',
        faq3aShipped: 'Shipped — Order has been dispatched for delivery.',
        faq3aDelivered: 'Delivered — Order successfully delivered to the customer.',
        faq3aCancelled: 'Cancelled — Order was cancelled by the store or customer.',
        faq4q: 'How do I create a new category?',
        faq4a: 'Navigate to Categories in the sidebar, click "New category", enter the category name and an optional description, then click "Save". Products can then be assigned to this category.',
        faq5q: 'Can I delete a user account?',
        faq5a: 'Yes. Go to Users, find the user in the table, and click the delete icon. A confirmation dialog will appear. Note that you cannot delete your own account from the admin panel for security reasons.',
        faq6q: 'How do I set up a product deal/promotion?',
        faq6a: 'Edit the product and fill in the Discount % field (e.g., 30 for 30% off). Optionally set a Deal expires at date. The final price will be calculated automatically. You can also set Initial stock to show a progress bar of how much has been sold.',
        faq7q: 'How does the search bar work?',
        faq7a: 'The search bar at the top of each CRUD page filters the table in real-time by name. Simply type and the results will update instantly — no need to press Enter.'
    },

    // ==================== Release Notes Modal(Help Modals) ====================
    releaseNotes: {
        title: 'Release Notes',
        description: 'Track what\'s new and improved in SmartOrder.',
        latest: 'Latest',
        v120: 'Real-time chat via WebSocket (STOMP + SockJS)',
        v120b: 'Product reviews with star ratings and like system',
        v120c: 'Favorites system for customers',
        v120d: 'Shopping cart persisted in database',
        v120e: 'Quick replies for sellers in chat',
        v110: 'Admin dashboard with metrics and status bars',
        v110b: 'Full CRUD for all entities (categories, products, users, addresses, orders, order items)',
        v110c: 'Dark/light theme with system preference detection',
        v110d: 'Collapsible sidebar with state persistence',
        v100: 'Initial release with Spring Boot 4.0 backend',
        v100b: 'JWT authentication with role-based access',
        v100c: 'Swagger/OpenAPI documentation',
        v100d: 'Flyway database migrations (24 migrations)',
        january: 'January 2026',
        february: 'February 2026',
        march: 'March 2026',
        april: 'April 2026',
        may: 'May 2026',
        june: 'June 2026',
        july: 'July 2026',
        august: 'August 2026',
        september: 'September 2026',
        october: 'October 2026',
        november: 'November 2026',
        december: 'December 2026',
    },

    // ==================== Terms & Policies Modal(Help Modals) ====================
    terms: {
        title: 'Terms & Policies',
        tabTerms: 'Terms of Use',
        tabPrivacy: 'Privacy Policy',
        tabCookie: 'Cookie Policy',
        lastUpdated: 'Last updated: March 2026',
        // Terms of Use
        t1title: '1. Acceptance of Terms',
        t1text: 'By accessing and using the SmartOrder platform, you agree to be bound by these Terms of Use. If you do not agree with any part of these terms, you must not use the platform.',
        t2title: '2. Use of the Platform',
        t2text: 'SmartOrder provides e-commerce management tools for authorized administrators and customers. You agree to use the platform only for lawful purposes and in accordance with these terms. You are responsible for maintaining the confidentiality of your account credentials.',
        t3title: '3. User Accounts',
        t3text: 'You must provide accurate and complete information when creating an account. Each user is responsible for all activities that occur under their account. Administrators have elevated privileges and must exercise them responsibly.',
        t4title: '4. Intellectual Property',
        t4text: 'All content, designs, and functionality of SmartOrder are the property of the platform owner. You may not reproduce, distribute, or create derivative works without explicit written permission.',
        t5title: '5. Limitation of Liability',
        t5text: 'SmartOrder is provided "as is" without warranties of any kind. We shall not be liable for any indirect, incidental, or consequential damages arising from your use of the platform.',
        // Privacy Policy
        p1title: '1. Information We Collect',
        p1text: 'We collect information you provide directly: name, email address, phone number, and delivery addresses. We also collect usage data such as login timestamps and order history to improve the platform experience.',
        p2title: '2. How We Use Your Information',
        p2text: 'Your information is used to process orders, manage your account, communicate important updates, and improve our services. We do not sell or share your personal data with third parties for marketing purposes.',
        p3title: '3. Data Security',
        p3text: 'We implement industry-standard security measures including JWT authentication, password encryption (BCrypt), and HTTPS encryption to protect your data. However, no method of transmission over the internet is 100% secure.',
        p4title: '4. Your Rights (LGPD)',
        p4text: 'In accordance with Brazil\'s General Data Protection Law (LGPD), you have the right to access, correct, delete, or export your personal data. You may request account deletion through your profile settings or by contacting support.',
        // Cookie Policy
        c1title: '1. What Are Cookies',
        c1text: 'Cookies are small text files stored on your device when you visit our platform. We use them to remember your preferences and improve your experience.',
        c2title: '2. Cookies We Use',
        c2textEssential: 'Essential cookies: Required for authentication (JWT token storage) and basic platform functionality. These cannot be disabled.',
        c2textPreference: 'Preference cookies: Store your theme preference (dark/light mode) and sidebar state. These enhance your experience but are not strictly necessary.',
        c3title: '3. Managing Cookies',
        c3text: 'You can manage cookies through your browser settings. Note that disabling essential cookies will prevent you from logging into the platform.'
    },

    // ==================== Report a Bug Modal(Help Modals) ====================
    reportBug: {
        title: 'Report a Bug',
        description: 'Found something wrong? Let us know and we\'ll fix it as soon as possible.',
        labelTitle: 'Title',
        placeholderTitle: 'Brief description of the issue',
        labelCategory: 'Category',
        categoryDefault: 'Select a category',
        categoryUi: 'UI / Visual issue',
        categoryFunc: 'Functionality not working',
        categoryPerf: 'Performance / Slow loading',
        categoryData: 'Data not saving / displaying',
        categoryAuth: 'Login / Authentication issue',
        categoryOther: 'Other',
        labelDescription: 'Description',
        placeholderDescription: 'Describe what happened, what you expected, and steps to reproduce...',
        btnCancel: 'Cancel',
        btnSubmit: 'Submit report',
        successMessage: 'Bug report submitted successfully! Our team will review it.'
    },

    // ====================== Help JS ======================
    helpJs: {
        msgSubmitted: 'Bug report submitted successfully! Our team will review it.'
    },

    // ==================== Dashboard ====================
    dashboard: {
        title: 'Dashboard',
        welcome: 'Welcome back',
        totalOrders: 'Total orders',
        revenue: 'Revenue',
        users: 'Users',
        products: 'Products',
        recentOrders: 'Recent orders',
        ordersByStatus: 'Orders by status',
        quickActions: 'Quick actions',
        newProduct: 'New product',
        newCategory: 'New category',
        addAdminUser: 'Add admin user',
        loadingOrders: 'Loading orders...'
    },

    // ====================== Dashboard JS ======================
    dashboardJs: {
        welcome: 'Welcome back',
        failedOrders: 'Failed to load orders',
        noOrders: 'No orders yet',
        failedStatus: 'Failed to load'
    },

    // ==================== Categories Page ====================
    categories: {
        title: 'Categories',
        subtitle: 'Manage product categories',
        newCategory: 'New category',
        thName: 'Name',
        thDescription: 'Description',
        thActions: 'Actions',
        formName: 'Name',
        formDescription: 'Description',
        placeholderDescription: 'Description of this category...',
        placeholderEG: 'e.g. Electronics',
        createTitle: 'New category',
        searchCategories: 'Search categories...',
        loadingCategories: 'Loading categories...',
        deleteCategory: 'Delete category',
        deleteCategoryText: 'Are you sure you want to delete this category? This will not delete the products assigned to it, but they will become uncategorized.'
    },

    // ==================== Categories JS ====================
    categoriesJs: {
        failedCategories: 'Failed to load categories',
        noCategories: 'No categories yet',
        createCategoriesText: 'Create your first category to organize products',
        createCategories: 'Create category',
        editTitle: 'Edit category',
        categoryRequired: 'Category name is required.',
        categoryUpdate: 'Category updated successfully!',
        failedCategoryUpdate: 'Failed to update category',
        failedCategoryCreate: 'Failed to create category',
        failedCategoryDelete: 'Failed to delete category',
        createSuccessCategories: 'Category created successfully',
        deleteSuccessCategories: 'Category deleted successfully',
    },

    // ==================== Products Page ====================
    products: {
        title: 'Products',
        subtitle: 'Manage store products',
        newProduct: 'New product',
        thImage: 'Image',
        thName: 'Name',
        thPrice: 'Price',
        thStock: 'Stock',
        thCategory: 'Category',
        thActive: 'Active',
        thActions: 'Actions',
        formName: 'Name',
        formDescription: 'Description',
        formPrice: 'Price ($)',
        formStock: 'Stock quantity',
        formImageUrl: 'Image URL',
        formCategory: 'Category',
        formActive: 'Active',
        formDiscount: 'Discount %',
        formInitialStock: 'Initial stock',
        formDealExpires: 'Deal expires at',
        formFeatured: 'Featured',
        selectCategory: 'Select a category',
        searchProduct: 'Search products...',
        loadingProducts: 'Loading products...',
        deleteProduct: 'Delete product',
        deleteProductText: 'Are you sure you want to delete this product? This action cannot be undone.',
        thDiscount: 'Discount',
        thFeatured: 'Featured',
        featuredYes: 'Yes',
        featuredNo: 'No'
    },

    // ==================== Products JS ====================
    productsJs: {
        failedProducts: 'Failed to load products',
        noProducts: 'No products yet',
        createProductsText: 'Create your first product to get started',
        createProducts: 'Create product',
        editTitle: 'Edit product',
        requiredName: 'Product name is required.',
        requiredPrice: 'Price must be zero or a positive value.',
        requiredStock: 'Stock quantity must be zero or a positive value.',
        requiredDiscount: 'Discount percentage must be between 0 and 100.',
        requiredInitialStock: 'Initial stock must be zero or a positive value.',
        selectCategory: 'Please select a category.',
        failedProductUpdate: 'Failed to update product',
        failedProductCreate: 'Failed to create product',
        failedProductDelete: 'Failed to delete product',
        updateSuccessProduct: 'Product updated successfully!',
        createSuccessProducts: 'Product created successfully',
        deleteSuccessProducts: 'Product deleted successfully',
    },

    // ==================== Users Page ====================
    users: {
        title: 'Users',
        subtitle: 'Manage system users',
        newUser: 'New user',
        thName: 'Name',
        thEmail: 'Email',
        thRole: 'Role',
        thPhone: 'Phone',
        thCreated: 'Created at',
        thActions: 'Actions',
        formName: 'Name',
        formEmail: 'Email',
        formPassword: 'Password',
        formRole: 'Role',
        formPhone: 'Phone',
        formProfileImage: 'Profile image URL',
        selectRole: 'Select a role',
        searchUser: 'Search users...',
        changePassword: 'Change password',
        newPassword: 'New password',
        loadingUsers: 'Loading users...',
        placeholderEGName: 'e.g. John Doe',
        placeholderEGEmail: 'e.g. john@gmail.com',
        enterPass: 'Enter password',
        enterPassNew: 'Enter new password',
        deleteUser: 'Delete user',
        deleteUserText: 'Are you sure you want to delete this user? This action cannot be undone.',
        cannotDeleteSelf: 'You cannot delete your own account.'
    },

    // ==================== Users JS ====================
    usersJs: {
        failedUsers: 'Failed to load users',
        failedUsersUpdate: 'Failed to update users',
        failedUsersCreate: 'Failed to create users',
        failedUsersDelete: 'Failed to delete user',
        failedUserPassword: 'Failed to update password',
        usersUpdate: 'Users updated successfully',
        noUsers: 'No users yet',
        deleteUser: 'User deleted successfully',
        createUsersText: 'Create your first user to get started',
        createUsers: 'Create user',
        usersCreate: 'Users created successfully',
        editTitle: 'Edit user',
        requestPassword: 'Requested password',
        requiredPassword: 'New password is required.',
        updatePassword: 'Password updated successfully',

    },

    // ==================== Addresses Page ====================
        addresses: {
            title: 'Addresses',
            subtitle: 'Manage user addresses',
            newAddress: 'New address',
            thStreet: 'Street',
            thNumber: 'Number',
            thCity: 'City',
            thState: 'State',
            thUser: 'User',
            thActions: 'Actions',
            formStreet: 'Street',
            formNumber: 'Number',
            formComplement: 'Complement',
            formCity: 'City',
            formState: 'State',
            formZipCode: 'ZIP Code',
            formCountry: 'Country',
            formUser: 'User',
            selectUser: 'Select a user',
            createTitle: 'New address',
            deleteAddresses: 'Delete address',
            deleteAddressText: 'Are you sure you want to delete this address? This action cannot be undone.'
        },

       // ==================== Addresses JS ====================
        addressesJs: {
            loading: 'Loading addresses...',
            failedLoad: 'Failed to load addresses',
            noAddresses: 'No addresses yet',
            createFirst: 'Create your first address to get started',
            createAddressBtn: 'Create address',
            editTitle: 'Edit address',
            streetRequired: 'Street is required.',
            numberRequired: 'Number is required.',
            cityRequired: 'City is required.',
            stateRequired: 'State is required.',
            zipRequired: 'Zip code is required.',
            countryRequired: 'Country is required.',
            userRequired: 'Please select a user.',
            failedUpdate: 'Failed to update address',
            failedCreate: 'Failed to create address',
            failedDelete: 'Failed to delete address',
            updateSuccess: 'Address updated successfully',
            createSuccess: 'Address created successfully',
            deleteSuccess: 'Address deleted successfully',
            searchPlaceholder: 'Search addresses...'
        },

    // ==================== Orders Page ====================
    orders: {
        title: 'Orders',
        subtitle: 'Manage customer orders',
        newOrder: 'New order',
        thDate: 'Date',
        thCustomer: 'Customer',
        thTotal: 'Total',
        thStatus: 'Status',
        thActions: 'Actions',
        formStatus: 'Status',
        formTotal: 'Total amount',
        formUser: 'User',
        formAddress: 'Address',
        selectStatus: 'Select status',
        selectUser: 'Select a user',
        selectAddress: 'Select an address',
        createTitle: 'New order',
        searchPlaceholder: 'Search orders...',
        loadingOrders: 'Loading orders...',
        totalAmount: 'Total amount (R$)',
        updateStatus: 'Update status',
        newStatus: 'New status',
        statusPending: 'Pending',
        statusConfirmed: 'Confirmed',
        statusShipped: 'Shipped',
        statusDelivered: 'Delivered',
        statusCancelled: 'Cancelled',
        deleteConfirmTitle: 'Delete order',
        deleteConfirmText: 'Are you sure you want to delete this order? This will also delete all its items. This action cannot be undone.'
    },

    // ==================== Orders JS ====================
    ordersJs: {
        failedLoad: 'Failed to load orders',
        noOrders: 'No orders yet',
        firstOrderText: 'Create your first order to get started',
        createOrder: 'Create order',
        clickStatus: 'Click to change status',
        selectUserPlaceholder: 'Select a user...',
        selectUserFirst: 'Select a user first...',
        selectAddressPlaceholder: 'Select an address...',
        loading: 'Loading...',
        editTitle: 'Edit order',
        noAddresses: 'No addresses for this user',
        failedAddresses: 'Failed to load addresses',
        requiredUser: 'Please select a user.',
        requiredAddress: 'Please select an address.',
        requiredStatus: 'Please select a status.',
        invalidTotal: 'Total amount must be zero or positive.',
        updateSuccess: 'Order updated successfully',
        createSuccess: 'Order created successfully',
        deleteSuccess: 'Order deleted successfully',
        statusUpdateSuccess: 'Status updated to ',
        failedUpdate: 'Failed to update order',
        failedCreate: 'Failed to create order',
        failedDelete: 'Failed to delete order',
        failedStatus: 'Failed to update status',
        connectionError: 'Connection error',
        deleteConfirmTitle: 'Delete order',
        deleteConfirmText: 'Are you sure you want to delete this order? This will also delete all its items. This action cannot be undone.'
    },

    // ==================== Order Items Page ====================
    orderItems: {
        title: 'Order Items',
        subtitle: 'Manage order line items',
        newItem: 'New item',
        thOrder: 'Order',
        thProduct: 'Product',
        thQuantity: 'Quantity',
        thPrice: 'Price',
        thSubtotal: 'Subtotal',
        loadingItems: 'Loading items...',
        thActions: 'Actions',
        formOrder: 'Order',
        formProduct: 'Product',
        formQuantity: 'Quantity',
        formPrice: 'Unit price',
        formSubtotal: 'Subtotal',
        unitPrice: 'Unit price (R$)',
        selectOrder: 'Select an order',
        selectProduct: 'Select a product',
        createTitle: 'New order item',
        deleteItem: 'Delete item',
        deleteItemText: 'Are you sure you want to delete this item? This action cannot be undone.'
    },

    // ==================== Order Items JS ====================
    orderItemsJs: {
        failedLoad: 'Failed to load items',
        noItems: 'No items yet',
        firstItemText: 'Add items to orders to get started',
        addItem: 'Add item',
        orderLabel: 'Order #',
        selectOrder: 'Select an order...',
        selectProduct: 'Select a product...',
        searchPlaceholder: 'Search items...',
        requiredOrder: 'Please select an order.',
        requiredProduct: 'Please select a product.',
        requiredQuantity: 'Quantity must be at least 1.',
        invalidPrice: 'Price must be zero or positive.',
        updateSuccess: 'Item updated successfully',
        createSuccess: 'Item created successfully',
        deleteSuccess: 'Item deleted successfully',
        failedUpdate: 'Failed to update item',
        failedCreate: 'Failed to create item',
        failedDelete: 'Failed to delete item',
        connectionError: 'Connection error',
        createTitle: 'New item',
        editTitle: 'Edit item',
        deleteConfirmText: 'Are you sure you want to delete "{label}"? This action cannot be undone.'
    },

    // ==================== Language Names ====================
    lang: {
        en: 'English',
        pt: 'Português',
        es: 'Español',
        fr: 'Français',
        changed: 'Language changed to'
    },

    // ==================== Settings Page ====================
    settings: {
        title: 'Settings',
        subtitle: 'Manage your store, account, and system preferences',

        tabStore: 'Store',
        tabNotifications: 'Notifications',
        tabAccount: 'Account',
        tabSecurity: 'Security',
        tabDanger: 'Danger zone',

        // Store
        storeTitle: 'Store information',
        storeDesc: "Configure your store's public information and regional preferences.",
        storeName: 'Store name',
        storeDescription: 'Store description',
        storeLogo: 'Logo URL',
        currency: 'Currency',
        timezone: 'Timezone',
        storeSaved: 'Store settings saved successfully',
        errStoreName: 'Store name is required',

        // Notifications
        notifTitle: 'Notification preferences',
        notifDesc: 'Choose which notifications you want to receive in the admin panel.',
        notifDisableAll: 'Disable all notifications',
        notifDisableAllDesc: 'Turn off every notification type at once.',
        notifNewOrders: 'New orders',
        notifNewOrdersDesc: 'Get notified when a customer places a new order.',
        notifNewUsers: 'New users',
        notifNewUsersDesc: 'Get notified when a new user registers.',
        notifLowStock: 'Low stock alerts',
        notifLowStockDesc: "Alert when a product's stock falls below a threshold.",
        notifOrderStatus: 'Order status changes',
        notifOrderStatusDesc: "Get updates when an order's status is changed.",
        notifNewReviews: 'New product reviews',
        notifNewReviewsDesc: 'Get notified when customers review products.',
        notifNewMessages: 'New chat messages',
        notifNewMessagesDesc: 'Get notified when a customer sends you a message.',
        notifSaved: 'Notification preferences saved',

        // Account
        accountTitle: 'Account information',
        accountDesc: 'Update your personal information and profile photo.',
        profilePhoto: 'Profile photo',
        profilePhotoDesc: 'Add a URL to display your profile image across the panel.',
        accountName: 'Full name',
        accountEmail: 'Email',
        accountSaved: 'Account information updated',
        errName: 'Name is required',
        errEmail: 'Email is required',
        errUpdate: 'Failed to update',

        // Security
        securityTitle: 'Change password',
        securityDesc: 'Update your password. Use at least 8 characters with a mix of letters and numbers.',
        currentPassword: 'Current password',
        newPassword: 'New password',
        confirmPassword: 'Confirm new password',
        updatePassword: 'Update password',
        passUpdated: 'Password updated successfully',
        errCurrentPass: 'Current password is required',
        errNewPassLen: 'New password must have at least 8 characters',
        errPassMatch: 'New password and confirmation do not match',

        // Danger
        dangerTitle: 'Danger zone',
        dangerDesc: 'These actions are irreversible. Proceed with caution.',
        clearOrders: 'Clear all orders',
        clearOrdersDesc: 'Permanently delete every order and order item in the system.',
        clearData: 'Clear data',
        resetData: 'Reset test data',
        resetDataDesc: 'Delete all products, categories, and orders. The default users will remain.',
        resetButton: 'Reset',
        deleteAccount: 'Delete my account',
        deleteAccountDesc: 'Remove your admin account permanently. You will be signed out.',
        typeConfirm: 'Type CONFIRM to proceed:',
        errTypeConfirm: 'You must type CONFIRM exactly',
        confirmClearOrders: 'Clear all orders?',
        confirmClearOrdersText: 'This will permanently delete all orders and order items. This action cannot be undone.',
        confirmResetData: 'Reset all test data?',
        confirmResetDataText: 'This will delete all orders, products, and categories. Users will remain. This action cannot be undone.',
        confirmDeleteAccount: 'Delete your account?',
        confirmDeleteAccountText: 'Your admin account will be permanently removed and you will be signed out. This action cannot be undone.',
        ordersCleared: 'All orders have been cleared',
        dataReset: 'Test data has been reset',
        accountDeleted: 'Account deleted. Signing out...'
    },

    // ==================== Profile Page ====================
    profile: {
        tabAccount: 'Account',
        tabSecurity: 'Security',

        accountTitle: 'Account information',
        accountDesc: 'Update your personal information and profile photo.',
        profilePhoto: 'Profile photo',
        profilePhotoDesc: 'Add a URL to display your profile image across the panel.',
        accountName: 'Full name',
        accountEmail: 'Email',
        accountPhone: 'Phone',
        accountSaved: 'Account information updated',
        errName: 'Name is required',
        errEmail: 'Email is required',
        errUpdate: 'Failed to update',
        repeat: 'Repeat new password',
        least: 'At least 8 characters',

        securityTitle: 'Change password',
        securityDesc: 'Update your password. Use at least 8 characters with a mix of letters and numbers.',
        currentPassword: 'Current password',
        newPassword: 'New password',
        confirmPassword: 'Confirm new password',
        updatePassword: 'Update password',
        passUpdated: 'Password updated successfully',
        errCurrentPass: 'Current password is required',
        errNewPassLen: 'New password must have at least 8 characters',
        errPassMatch: 'New password and confirmation do not match'
    },

    // ==================== Register ====================
    register: {
        title: 'Create your account',
        subtitle: 'Join SmartOrder and start shopping',
        name: 'Full Name',
        namePlaceholder: 'Enter your full name',
        email: 'Email',
        emailPlaceholder: 'Enter your email',
        password: 'Password',
        passwordPlaceholder: 'Create a password',
        confirmPassword: 'Confirm Password',
        confirmPasswordPlaceholder: 'Confirm your password',
        submit: 'Create Account',
        hasAccount: 'Already have an account?',
        login: 'Sign in',
    },

    // ==================== Register JS ====================
    registerJs: {
        nameRequired: 'Full name is required.',
        emailInvalid: 'Enter a valid email.',
        emailTaken: 'Email already in use.',
        passwordShort: 'Password must be at least 6 characters.',
        passwordMismatch: 'Passwords do not match.',
        invalidData: 'Invalid data. Check the fields.',
        server: 'Internal error. Please try again.',
        connection: 'Connection error. Please try again.'
    },

    // ==================== Store Nav Bar ====================
    storeNavbar: {
        categories: 'Categories',
        allProducts: 'All Products',
        searchPlaceholder: 'Search products...',
        language: 'Language',
        theme: 'Theme',
        cart: 'Cart',
        myAccount: 'My Account',
        myOrders: 'My Orders',
        favorites: 'Favorites',
        logout: 'Logout'
    },

    // ==================== Store Nav Bar Js ====================
    storeNavbarJs: {
        failedCategories: 'Failed to load categories',
        confirmLogout: 'Are you sure you want to logout?',
        languageChanged: 'Language changed to {0}',
        failedCart: 'Failed to load cart'
    },

    // ==================== Store Carousel ====================
    storeCarousel: {
        previous: 'Previous',
        next: 'Next',
        badgeDeal: 'Deal',
        badgeLowStock: 'Almost Gone',
        badgeFeatured: 'Featured',
        reviews: 'reviews',
        noReviews: 'No reviews yet'
    },

    // ==================== Store Carousel Js ====================
    storeCarouselJs: {
        failedLoad: 'Failed to load carousel products'
    },

    // ==================== Store Cards ====================
    storeCard: {
        title: 'All Products',
        end: "You've seen all products",
    },

    // ==================== Store Carousel Js ====================
    storeCardJs: {
        welcomeDeal: 'Welcome Deal',
        reviews: 'reviews',
        empty: 'No products available yet',
        loadError: 'Failed to load products',
        loginRequired: 'Please log in to favorite',
        favoriteError: 'Failed to update favorite',
        cartSoon: 'Cart integration coming soon',
    },

    // ==================== Common ====================
    common: {
        save: 'Save',
        cancel: 'Cancel',
        delete: 'Delete',
        edit: 'Edit',
        create: 'Create',
        close: 'Close',
        confirm: 'Confirm',
        yes: 'Yes',
        no: 'No',
        loading: 'Loading...',
        error: 'Error',
        success: 'success',
        unknown: 'Unknown',
        footer: 'SmartOrder API v1.0 — Admin panel',
        new: 'New',
        search: 'Search...',
        noResults: 'No results found',
        confirmDelete: 'Are you sure you want to delete',
        confirmDeleteText: 'This action cannot be undone.',
        deleteSuccess: 'deleted successfully!',
        createSuccess: 'created successfully!',
        updateSuccess: 'updated successfully!',
        errorLoad: 'Failed to load data',
        actions: 'Actions',
        showing: 'Showing',
        of: 'of',
        results: 'results',
        errorConnect: 'Connection error',
        sure: 'Are you sure you want to delete',
        undone: 'This action cannot be undone',
        admin: 'Admin',
        customer: 'Customer',
        saveChanges: 'Save changes',
        connectionError: 'Connection error. Please try again.',
    }

});
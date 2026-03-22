package com.kauanferreira.smartorder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller responsible for serving Thymeleaf view pages.
 *
 * <p>This controller is separate from the REST API controllers
 * and handles page navigation for both admin and customer interfaces.
 * Authentication and role-based access are enforced via Spring Security
 * and JWT tokens stored in localStorage.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
@Controller
public class PageController {

    // ==================== Auth Pages ====================

    /**
     * Serves the login page.
     *
     * @return the login template path
     */
    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    /**
     * Serves the registration page.
     *
     * @return the register template path
     */
    @GetMapping("/register")
    public String registerPage() {
        return "auth/register";
    }

    // ==================== Admin Pages ====================

    /**
     * Serves the admin dashboard page.
     *
     * @return the admin dashboard template path
     */
    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "admin/dashboard";
    }

    /**
     * Serves the admin categories management page.
     *
     * @return the admin categories template path
     */
    @GetMapping("/admin/categories")
    public String adminCategories() {
        return "admin/categories";
    }

    /**
     * Serves the admin products management page.
     *
     * @return the admin products template path
     */
    @GetMapping("/admin/products")
    public String adminProducts() {
        return "admin/products";
    }

    /**
     * Serves the admin users management page.
     *
     * @return the admin users template path
     */
    @GetMapping("/admin/users")
    public String adminUsers() {
        return "admin/users";
    }

    /**
     * Serves the admin addresses management page.
     *
     * @return the admin addresses template path
     */
    @GetMapping("/admin/addresses")
    public String adminAddresses() {
        return "admin/addresses";
    }

    /**
     * Serves the admin orders management page.
     *
     * @return the admin orders template path
     */
    @GetMapping("/admin/orders")
    public String adminOrders() {
        return "admin/orders";
    }

    /**
     * Serves the admin order items management page.
     *
     * @return the admin order items template path
     */
    @GetMapping("/admin/order-items")
    public String adminOrderItems() {
        return "admin/order-items";
    }

    // ==================== Store Pages ====================

    /**
     * Serves the customer store home page (product showcase).
     *
     * @return the store home template path
     */
    @GetMapping("/store")
    public String storeHome() {
        return "store/home";
    }

    /**
     * Serves the shopping cart page.
     *
     * @return the cart template path
     */
    @GetMapping("/store/cart")
    public String cart() {
        return "store/cart";
    }

    // ==================== Profile & Settings ====================

    /**
     * Serves the user profile page (adapts based on role).
     *
     * @return the profile template path
     */
    @GetMapping("/profile")
    public String profile() {
        return "profile/profile";
    }

    /**
     * Serves the settings page.
     *
     * @return the settings template path
     */
    @GetMapping("/settings")
    public String settings() {
        return "settings/settings";
    }

    // ==================== Root Redirect ====================

    /**
     * Redirects root URL to login page.
     *
     * @return redirect to login
     */
    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }
}
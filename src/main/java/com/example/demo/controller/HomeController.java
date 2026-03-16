package com.example.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // 1. Trang chủ: Tự động chuyển hướng vào danh sách sản phẩm
    @GetMapping("/")
    public String index() {
        return "redirect:/products";
    }

    // 2. Trang đăng nhập tùy chỉnh (Custom Login)
    @GetMapping("/login")
    public String login() {
        return "login"; // Trả về file src/main/resources/templates/login.html
    }

    // 3. Trang thông báo lỗi 403 (Access Denied)
    // Khi User thường cố tình vào trang của ADMIN, Spring Security sẽ đá về đây
    @GetMapping("/403")
    public String accessDenied() {
        return "403"; // Trả về file src/main/resources/templates/403.html
    }
}
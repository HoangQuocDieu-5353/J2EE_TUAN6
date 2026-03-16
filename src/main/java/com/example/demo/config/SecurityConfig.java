package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login").permitAll()
                        // Yêu cầu quyền ADMIN cho trang thêm sản phẩm
                        .requestMatchers("/product/add", "/products/create").hasRole("ADMIN")
                        // Mọi người đã đăng nhập đều xem được danh sách sản phẩm
                        .requestMatchers("/products").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/products", true))
                .exceptionHandling(ex -> ex.accessDeniedPage("/403")) // Trang lỗi khi user vào khu vực của admin
                .logout(logout -> logout.permitAll());

        return http.build();
    }
}
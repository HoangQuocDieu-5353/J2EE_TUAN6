package com.example.demo.repository;

import com.example.demo.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    // JpaRepository đã cung cấp sẵn các hàm như save(), findAll(), findById()
    // nên bạn không cần phải viết thêm gì vào đây để làm chức năng thanh toán (Câu 7).
}
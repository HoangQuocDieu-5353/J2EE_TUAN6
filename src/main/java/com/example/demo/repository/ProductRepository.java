package com.example.demo.repository;

import com.example.demo.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    // Câu truy vấn kết hợp: Tìm theo tên (tương đối) và Lọc theo Category
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:keyword% AND (:categoryId IS NULL OR p.category.id = :categoryId)")
    Page<Product> searchAndFilter(
            @Param("keyword") String keyword,
            @Param("categoryId") Integer categoryId,
            Pageable pageable);
}
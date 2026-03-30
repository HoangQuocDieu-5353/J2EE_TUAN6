package com.example.demo.model;
import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Entity
@Data
@Table(name = "orders") // Tránh lỗi từ khóa reserve 'order' trong SQL
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Date orderDate;
    private Long totalAmount;
    private String customerName;
    private String phone;
    private String address;
}
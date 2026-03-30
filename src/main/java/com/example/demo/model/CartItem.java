package com.example.demo.model;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartItem {
    private Product product;
    private int quantity;

    public Long getSubTotal() {
        return product.getPrice() * quantity;
    }
}
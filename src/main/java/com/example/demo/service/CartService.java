package com.example.demo.service;

import com.example.demo.model.CartItem;
import com.example.demo.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;

@Service
@SessionScope
public class CartService {
    private List<CartItem> cart = new ArrayList<>();

    public void addToCart(Product product, int quantity) {
        for (CartItem item : cart) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        cart.add(new CartItem(product, quantity));
    }

    public List<CartItem> getCart() {
        return cart;
    }

    public Long getTotalPrice() {
        return cart.stream().mapToLong(CartItem::getSubTotal).sum();
    }

    public void clearCart() {
        cart.clear();
    }
    // Hàm cập nhật số lượng mới
    public void updateQuantity(Integer productId, int quantity) {
        for (CartItem item : cart) {
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(quantity); // Set số lượng mới
                return;
            }
        }
    }
}
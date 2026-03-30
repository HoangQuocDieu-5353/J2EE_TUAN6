package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final ProductService productService;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    // CÂU 5: Thêm vào giỏ hàng
    @PostMapping("/add")
    public String addToCart(@RequestParam Integer productId, @RequestParam(defaultValue = "1") int quantity) {
        Product product = productService.get(productId);
        if (product != null) {
            cartService.addToCart(product, quantity);
        }
        return "redirect:/cart";
    }

    // CÂU 6: Xem giỏ hàng
    @GetMapping
    public String viewCart(Model model) {
        model.addAttribute("cartItems", cartService.getCart());
        model.addAttribute("totalPrice", cartService.getTotalPrice());
        return "cart/view"; // Cần tạo file view này
    }

    // CÂU 7: Đặt hàng (Checkout)
    @GetMapping("/checkout")
    public String checkoutForm(Model model) {
        if (cartService.getCart().isEmpty()) {
            return "redirect:/cart";
        }
        model.addAttribute("totalPrice", cartService.getTotalPrice());
        return "cart/checkout"; // Trả về trang nhập thông tin
    }

    // 2. XỬ LÝ NÚT "ĐẶT HÀNG" SAU KHI ĐIỀN ĐỊA CHỈ (POST)
    @PostMapping("/checkout")
    public String processCheckout(@RequestParam String customerName,
                                  @RequestParam String phone,
                                  @RequestParam String address) {
        if (cartService.getCart().isEmpty()) return "redirect:/cart";

        // Lưu Order có kèm thông tin khách hàng
        Order order = new Order();
        order.setOrderDate(new Date());
        order.setTotalAmount(cartService.getTotalPrice());
        order.setCustomerName(customerName);
        order.setPhone(phone);
        order.setAddress(address);
        orderRepository.save(order);

        // Lưu OrderDetail
        for (CartItem item : cartService.getCart()) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(item.getProduct());
            detail.setQuantity(item.getQuantity());
            detail.setPrice(item.getProduct().getPrice());
            orderDetailRepository.save(detail);
        }

        // Xóa giỏ hàng
        cartService.clearCart();

        // Chuyển hướng sang trang báo thành công
        return "redirect:/cart/success";
    }
    // Hàm đón request khi bấm nút (+) hoặc (-)
    @PostMapping("/update")
    public String updateCart(@RequestParam Integer productId, @RequestParam int quantity) {
        // Cập nhật số lượng
        cartService.updateQuantity(productId, quantity);

        // Lệnh redirect này cực kỳ quan trọng:
        // Cập nhật xong nó sẽ lập tức đá bạn quay ngược lại đúng trang Giỏ hàng
        // để hiển thị tổng tiền mới (cảm giác giống như trang tự động chớp 1 cái rồi update).
        return "redirect:/cart";
    }

    // 3. TRANG THÔNG BÁO THÀNH CÔNG
    @GetMapping("/success")
    public String orderSuccess() {
        return "cart/success";
    }
}
package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    // LIST PRODUCT
    @GetMapping
    public String index(
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name = "categoryId", required = false) Integer categoryId,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "sort", defaultValue = "asc") String sort,
            Model model) {

        // 1. Cấu hình sắp xếp (Câu 3)
        Sort sortOrder = sort.equalsIgnoreCase("desc") ? Sort.by("price").descending() : Sort.by("price").ascending();

        // 2. Cấu hình phân trang (Câu 2 - Mỗi trang 5 SP)
        Pageable pageable = PageRequest.of(page - 1, 5, sortOrder);

        // 3. Lấy dữ liệu (Câu 1, 4)
        Page<Product> productPage = productService.getProductsWithFilter(keyword, categoryId, pageable);

        model.addAttribute("productPage", productPage);
        model.addAttribute("categories", categoryService.getAll()); // Đổ data cho Dropdown
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("sort", sort);

        return "product/products";
    }

    // FORM CREATE
    @GetMapping("/create")
    public String createForm(Model model) {

        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAll());

        return "product/create";
    }

    // CREATE PRODUCT
    @PostMapping("/create")
    public String create(
            @Valid @ModelAttribute Product product,
            BindingResult result,
            @RequestParam("imageFile") MultipartFile file,
            @RequestParam("categoryId") int categoryId,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAll());
            return "product/create";
        }

        product.setCategory(categoryService.get(categoryId));

        productService.uploadImage(product, file);

        productService.add(product);

        return "redirect:/products";
    }

    // FORM EDIT
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable int id, Model model) {

        model.addAttribute("product", productService.get(id));
        model.addAttribute("categories", categoryService.getAll());

        return "product/edit";
    }

    // UPDATE PRODUCT
    @PostMapping("/edit")
    public String edit(
            @Valid @ModelAttribute Product product,
            BindingResult result,
            @RequestParam("imageFile") MultipartFile file,
            @RequestParam("categoryId") int categoryId,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAll());
            return "product/edit";
        }

        product.setCategory(categoryService.get(categoryId));

        productService.uploadImage(product, file);

        productService.update(product);

        return "redirect:/products";
    }

    // DELETE
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {

        productService.delete(id);

        return "redirect:/products";
    }
}
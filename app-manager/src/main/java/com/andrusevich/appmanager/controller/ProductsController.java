package com.andrusevich.appmanager.controller;

import com.andrusevich.appmanager.controller.payload.NewProductPayload;
import com.andrusevich.appmanager.entity.Product;
import com.andrusevich.appmanager.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("home/products")
public class ProductsController {

    private final ProductService service;


    @GetMapping("all")
    public String getAllProducts(Model model) {
        model.addAttribute("products", this.service.findAllProducts());
        return "home/products/all";
    }

    @GetMapping("new-product")
    public String getNewProductPage() {
        return "home/products/new-product";
    }

    @PostMapping("create")
    public String createProduct(@Valid NewProductPayload payload, BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("payload", payload);
            model.addAttribute("errors", bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList());
            return "home/products/new-product";
        } else {
            Product product = this.service.createProduct(payload.name(), payload.description());
            return "redirect:/home/products/%d".formatted(product.getId());
        }
    }

}

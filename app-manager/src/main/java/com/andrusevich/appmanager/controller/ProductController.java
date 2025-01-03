package com.andrusevich.appmanager.controller;

import com.andrusevich.appmanager.controller.payload.EditProductPayload;
import com.andrusevich.appmanager.controller.payload.NewProductPayload;
import com.andrusevich.appmanager.entity.Product;
import com.andrusevich.appmanager.service.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
@RequestMapping("home/products/{productId:\\d+}")
public class ProductController {

    private final ProductService service;

    @ModelAttribute("product")
    public Product product(@PathVariable("productId") int productId) {
        return this.service.getProductById(productId).orElseThrow(() -> new NoSuchElementException("No products found"));
    }

    @GetMapping
    public String getProductById() {
        return "home/products/product";
    }

    @GetMapping("edit")
    public String getProductEditPage() {
        return "home/products/edit";
    }

    @PostMapping("edit")
    public String editProduct(@ModelAttribute(name = "product", binding = false) Product product, @Valid EditProductPayload payload,
                              BindingResult bindingResult,
                              Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("payload", payload);
            model.addAttribute("errors", bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList());
            this.service.editProduct(product.getId(), payload.name(), payload.description());
            return "home/products/edit";
        } else {
            this.service.editProduct(product.getId(), payload.name(), payload.description());
            return "redirect:/home/products/%d".formatted(product.getId());
        }
    }

    @PostMapping("delete")
    public String deleteProduct(@ModelAttribute("product") Product product) {
        this.service.deleteProduct(product.getId());
        return "redirect:/home/products/all";
    }

    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementException(NoSuchElementException e, Model model,
                                               HttpServletResponse response) {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        model.addAttribute("error", e.getMessage());
        return "errors/404";
    }
}

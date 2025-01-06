package com.andrusevich.appmanager.controller;

import com.andrusevich.appmanager.client.BadRequestException;
import com.andrusevich.appmanager.client.ProductsRestClient;
import com.andrusevich.appmanager.controller.payload.EditProductPayload;
import com.andrusevich.appmanager.entity.Product;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
@RequestMapping("home/products/{productId:\\d+}")
public class ProductController {

    private final ProductsRestClient client;

    @ModelAttribute("product")
    public Product product(@PathVariable("productId") int productId) {
        return this.client.getProductById(productId).orElseThrow(() -> new NoSuchElementException("No products found"));
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
    public String editProduct(@ModelAttribute(name = "product", binding = false) Product product,
                              EditProductPayload payload,
                              Model model) {
        try {
            this.client.editProduct(product.id(), payload.name(), payload.description());
            return "redirect:/home/products/%d".formatted(product.id());
        } catch (BadRequestException exception) {
            model.addAttribute("payload", payload);
            model.addAttribute("errors", exception.getErrors());
            return "home/products/edit";
        }

    }

    @PostMapping("delete")
    public String deleteProduct(@ModelAttribute("product") Product product) {
        this.client.deleteProductById(product.id());
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

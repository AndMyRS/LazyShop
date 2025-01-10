package com.andrusevich.appmanager.controller;

import com.andrusevich.appmanager.client.BadRequestException;
import com.andrusevich.appmanager.client.ProductsRestClient;
import com.andrusevich.appmanager.controller.payload.NewProductPayload;
import com.andrusevich.appmanager.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("home/products")
public class ProductsController {

    private final ProductsRestClient client;


    @GetMapping("all")
    public String getAllProducts(Model model, @RequestParam(name = "filter", required = false) String filter) {
        model.addAttribute("products", this.client.getAllProducts(filter));
        model.addAttribute("filter", filter);
        return "home/products/all";
    }

    @GetMapping("new-product")
    public String getNewProductPage() {
        return "home/products/new-product";
    }

    @PostMapping("create")
    public String createProduct(NewProductPayload payload, Model model) {
        try {
            Product product = this.client.createProduct(payload.name(), payload.description());
            return "redirect:/home/products/%d".formatted(product.id());
        } catch (BadRequestException exception) {
            model.addAttribute("payload", payload);
            model.addAttribute("errors", exception.getErrors());
            return "home/products/new-product";
        }
    }
}

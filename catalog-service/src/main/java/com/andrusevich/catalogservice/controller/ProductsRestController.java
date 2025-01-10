package com.andrusevich.catalogservice.controller;

import com.andrusevich.catalogservice.controller.payload.NewProductPayload;
import com.andrusevich.catalogservice.entity.Product;
import com.andrusevich.catalogservice.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("home-api/products")
public class ProductsRestController {

    private final ProductService service;

    @GetMapping
    public Iterable<Product> getProducts(@RequestParam(name = "filter", required = false) String filter) {
        return this.service.findAllProducts(filter);
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody NewProductPayload payload,
                                                 BindingResult bindingResult,
                                                 UriComponentsBuilder uriComponentsBuilder) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
            throw exception;
        } else {
            throw new BindException(bindingResult);
            }
        } else {
            Product product = this.service.createProduct(payload.name(), payload.description());
            return ResponseEntity.created(uriComponentsBuilder
                            .replacePath("/home-api/products/{productId}")
                            .build(Map.of("productId", product.getId())))
                    .body(product);
        }
    }
}

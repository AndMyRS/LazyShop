package com.andrusevich.catalogservice.controller;

import com.andrusevich.catalogservice.controller.payload.EditProductPayload;
import com.andrusevich.catalogservice.entity.Product;
import com.andrusevich.catalogservice.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("home-api/products/{productId:\\d+}")
public class ProductRestController {

    private final ProductService service;

    @ModelAttribute("product")
    public Product getProduct(@PathVariable("productId") int productId) {
        return this.service.getProductById(productId).orElseThrow(() -> new NoSuchElementException("No products found"));
    }

    @GetMapping
    public Product getProductById(@ModelAttribute("product") Product product) {
        return product;
    }

    @PatchMapping
    public ResponseEntity<?> editProductById(@PathVariable("productId") int productId,
                                                @Valid @RequestBody EditProductPayload payload,
                                                BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            this.service.editProduct(productId, payload.name(), payload.description());
            return ResponseEntity.noContent().build();
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteProductById(@PathVariable("productId") int productId) {
        this.service.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ProblemDetail> handleNoSuchElementException(NoSuchElementException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage()));
    }
}

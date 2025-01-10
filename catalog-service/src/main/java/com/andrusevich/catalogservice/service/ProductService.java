package com.andrusevich.catalogservice.service;

import com.andrusevich.catalogservice.entity.Product;

import java.util.Optional;

public interface ProductService {

    Iterable<Product> findAllProducts(String filter);

    Product createProduct(String name, String description);

    Optional<Product> getProductById(int productId);

    void editProduct(Integer id, String name, String description);

    void deleteProduct(Integer id);
}

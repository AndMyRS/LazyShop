package com.andrusevich.appmanager.client;

import com.andrusevich.appmanager.entity.Product;

import java.util.List;
import java.util.Optional;


public interface ProductsRestClient {

    List<Product> getAllProducts();

    Product createProduct(String name, String description);

    Optional<Product> getProductById(int productId);

    void editProduct(int productId, String name, String description);

    void deleteProductById(int productId);
}

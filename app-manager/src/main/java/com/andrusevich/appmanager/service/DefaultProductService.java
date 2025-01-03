package com.andrusevich.appmanager.service;

import com.andrusevich.appmanager.entity.Product;
import com.andrusevich.appmanager.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultProductService implements ProductService {

    private final ProductRepository repository;

    @Override
    public List<Product> findAllProducts() {
        return this.repository.findAll();
    }

    @Override
    public Product createProduct(String name, String description) {
        return this.repository.save(new Product(null, name, description));
    }

    @Override
    public Optional<Product> getProductById(int productId) {
        return this.repository.findById(productId);
    }

    @Override
    public void editProduct(Integer id, String name, String description) {
        this.repository.findById(id)
                .ifPresentOrElse(product -> {
                    product.setName(name);
                    product.setDescription(description);
                }, () -> {
                    throw new NoSuchElementException();
                });
    }

    @Override
    public void deleteProduct(Integer id) {
        this.repository.deleteById(id);
    }
}

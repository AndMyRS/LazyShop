package com.andrusevich.catalogservice.service;

import com.andrusevich.catalogservice.entity.Product;
import com.andrusevich.catalogservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultProductService implements ProductService {

    private final ProductRepository repository;

    @Override
    public Iterable<Product> findAllProducts(String filter) {
        if (filter != null && !filter.isBlank()) {
            return this.repository.findAllByNameLikeIgnoreCase("%" + filter + "%");
        } else return this.repository.findAll();
    }

    @Override
    @Transactional
    public Product createProduct(String name, String description) {
        return this.repository.save(new Product(null, name, description));
    }

    @Override
    public Optional<Product> getProductById(int productId) {
        return this.repository.findById(productId);
    }

    @Override
    @Transactional
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
    @Transactional
    public void deleteProduct(Integer id) {
        this.repository.deleteById(id);
    }
}

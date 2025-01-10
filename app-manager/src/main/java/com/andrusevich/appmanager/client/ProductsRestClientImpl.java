package com.andrusevich.appmanager.client;

import com.andrusevich.appmanager.controller.payload.EditProductPayload;
import com.andrusevich.appmanager.controller.payload.NewProductPayload;
import com.andrusevich.appmanager.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
public class ProductsRestClientImpl implements ProductsRestClient {

    private static final ParameterizedTypeReference<List<Product>> PRODUCTS_TYPE_REFERENCE =
            new ParameterizedTypeReference<>() {
            };

    private final RestClient client;

    @Override
    public List<Product> getAllProducts(String filter) {
        return this.client
                .get()
                .uri("home-api/products?filter={filter}", filter)
                .retrieve()
                .body(PRODUCTS_TYPE_REFERENCE);
    }

    @Override
    public Product createProduct(String name, String description) {
        try {
            return this.client
                    .post()
                    .uri("/home-api/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new NewProductPayload(name, description))
                    .retrieve()
                    .body(Product.class);
        } catch (HttpClientErrorException.BadRequest exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<String>) problemDetail.getProperties().get("errors"));
        }
    }

    @Override
    public Optional<Product> getProductById(int productId) {
        try {
            return Optional.ofNullable(this.client
                    .get()
                    .uri("/home-api/products/{productId}", productId)
                    .retrieve()
                    .body(Product.class));
        } catch (HttpClientErrorException.NotFound exception) {
            return Optional.empty();
        }
    }

    @Override
    public void editProduct(int productId, String name, String description) {
        try {
            this.client
                    .patch()
                    .uri("/home-api/products/{productId}", productId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new EditProductPayload(name, description))
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.BadRequest exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<String>) problemDetail.getProperties().get("errors"));
        }
    }

    @Override
    public void deleteProductById(int productId) {
        try {
            this.client
                    .delete()
                    .uri("/home-api/products/{productId}", productId)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.NotFound exception) {
            throw new NoSuchElementException(exception);
        }
    }
}

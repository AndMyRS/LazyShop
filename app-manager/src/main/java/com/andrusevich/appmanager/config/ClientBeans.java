package com.andrusevich.appmanager.config;

import com.andrusevich.appmanager.client.ProductsRestClientImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ClientBeans {

    @Bean
    public ProductsRestClientImpl productsRestClient() {
        return new ProductsRestClientImpl(RestClient.builder()
                .baseUrl("http://localhost:8081")
                .build());
    }
}

package com.andrusevich.appmanager.config;

import com.andrusevich.appmanager.client.ProductsRestClientImpl;
import com.andrusevich.appmanager.security.OAuthClientHttpRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.web.client.RestClient;

@Configuration
@EnableWebSecurity
public class ClientBeans {

    @Bean
    public ProductsRestClientImpl productsRestClientImpl(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository
    ) {
        String registrationId = "keycloak";
        return new ProductsRestClientImpl(RestClient.builder()
                .baseUrl("http://localhost:8081")
                .requestInterceptor(new OAuthClientHttpRequestInterceptor(
                        new DefaultOAuth2AuthorizedClientManager(
                                clientRegistrationRepository, oAuth2AuthorizedClientRepository), registrationId))
                .build());
    }
}

package com.example.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient otpWebClient(
            @Value("${otp.graphql-url}") String graphqlUrl
    ) {
        return WebClient.builder()
                .baseUrl(graphqlUrl)
                .build();
    }
}

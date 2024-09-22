package com.y.javachat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${ai.sever.base-url}")
    private String aiBaseUri;

    @Bean(name = "aiServerWebClientBuilder")
    public WebClient webClientBuilder() {
        return WebClient.builder().baseUrl(aiBaseUri).build();
    }
}

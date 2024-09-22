package com.y.javachat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean(name = "aiServerWebClientBuilder")
    public WebClient webClientBuilder() {
        return WebClient.create();
    }
}

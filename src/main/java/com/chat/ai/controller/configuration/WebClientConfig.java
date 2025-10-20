package com.chat.ai.controller.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${azure.translator.subscription-key}")
    private String subscriptionKey;

    @Value("${azure.translator.region}")
    private String region;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
            .baseUrl("https://api.cognitive.microsofttranslator.com")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader("Ocp-Apim-Subscription-Key", subscriptionKey)
            .defaultHeader("Ocp-Apim-Subscription-Region", region)
            .build();
    }
}

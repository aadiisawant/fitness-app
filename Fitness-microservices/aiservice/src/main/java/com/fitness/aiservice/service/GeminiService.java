package com.fitness.aiservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@Slf4j
public class GeminiService {

    private final WebClient webClient;
    private final String geminiAPIUrl;
    private final String geminiAPIKey;


    public GeminiService(
            WebClient.Builder webClientBuilder,
            @Value("${gemini.api.url}") String geminiAPIUrl,
            @Value("${gemini.api.key}") String geminiAPIKey
    ) {
        this.webClient = webClientBuilder.build();
        this.geminiAPIUrl = geminiAPIUrl;
        this.geminiAPIKey = geminiAPIKey;
        log.info("Inside Gemini constructor, Url:{}, key:{}", geminiAPIUrl, geminiAPIKey);
    }

    public String getRecommendations(String details) {

        Map<String, Object> requestBody = Map.of(
                "contents", new Object[]{
                        Map.of("parts", new Object[]{
                                Map.of("text", details)
                        })
                }
        );

        return webClient.post()
                .uri(geminiAPIUrl)
                .header("Content-Type", "application/json")
                .header("X-goog-api-key", geminiAPIKey)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}

package com.fitness.aiservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
//@RequiredArgsConstructor
public class GeminiService {
//    private final WebClient webClient;

//    @Value("${gemini.api.url}")
//    private String geminiAPIUrl;
//    @Value("${gemini.api.key}")
//    private String geminiAPIKey;

//    public GeminiService(WebClient.Builder webClientbuilder){
//        this.webClient = webClientbuilder.build();
//    }

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
    }

    public String getRecommendations(String details){
        Map<String, Object> requestBody = Map.of(
                "contents", new Object[]{
                        Map.of("parts", new Object[]{
                                 Map.of("text",details)
                        })
                }
        );
        String response = webClient.post()
                .uri(geminiAPIUrl)
                .header("Content-Type", "application/json")
                .header("X-goog-api-key", geminiAPIKey)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return response;
    }
}

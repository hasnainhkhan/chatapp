package com.chat.ai.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.chat.ai.DTO.ChatRequest;
import com.chat.ai.DTO.ChatResponse;
import com.chat.ai.DTO.ChatMessage;

import java.util.List;

@Service
public class AIService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private final WebClient webClient = WebClient.builder().build();

    public String askGeminiFlash(String prompt) {
        // Prepare the user message
        ChatMessage userMessage = new ChatMessage("user", prompt);

        // Prepare the request with the appropriate model and messages for Gemini Flash 2.0
        ChatRequest request = new ChatRequest();
        request.setModel("gemini-flash-2.0"); // Ensure the model name matches Gemini Flash 2.0
        request.setMessages(List.of(userMessage));

        // Make the API call to Gemini Flash 2.0 and retrieve the response
        ChatResponse response = webClient.post()
                .uri(apiUrl)
                .header("Authorization", "Bearer " + apiKey) // Add Bearer token for authentication
                .header("Content-Type", "application/json") // Specify content type
                .bodyValue(request) // Send the request body
                .retrieve()
                .bodyToMono(ChatResponse.class) // Convert response to ChatResponse object
                .block(); // Wait for the response

        // Check if the response is valid and return the message
        if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
            return response.getChoices().get(0).getMessage().getContent(); // Extract and return the content
        } else {
            return "Error: No valid response from Gemini Flash 2.0.";
        }
    }
}

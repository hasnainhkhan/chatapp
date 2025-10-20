package com.chat.ai.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

//For handling AI chat API (API controller)
@RestController
@RequestMapping("/api")
public class Controllers {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${gemini.api.url}")
    private String apiUrl;

    @Value("${gemini.api.key}")
    private String apiKey;

    @PostMapping("/chat")
    @ResponseBody // this sends raw response for AJAX
    public String chat(@RequestParam("prompt") String prompt) {
        if (prompt == null || prompt.trim().isEmpty()) {
            return "Prompt cannot be empty.";
        }

        // Gemini request body
        Map<String, Object> part = Map.of("text", prompt);
        Map<String, Object> content = Map.of("parts", List.of(part));
        Map<String, Object> body = Map.of("contents", List.of(content));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // ✅ Do not use Bearer
        String requestUrl = apiUrl + "?key=" + apiKey;

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    requestUrl,
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.getBody().get("candidates");
            if (candidates != null && !candidates.isEmpty()) {
                Map<String, Object> contentMap = (Map<String, Object>) candidates.get(0).get("content");
                List<Map<String, Object>> parts = (List<Map<String, Object>>) contentMap.get("parts");
                return (String) parts.get(0).get("text");
            } else {
                return "No response from AI.";
            }
        } catch (Exception e) {
            return "API error: " + e.getMessage();
        }
    }
}

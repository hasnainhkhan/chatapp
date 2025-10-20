package com.chat.ai.service;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AzureVisionService {

    @Value("${azure.vision.endpoint}")
    private String endpoint;

    @Value("${azure.vision.key}")
    private String subscriptionKey;

    public String extractTextFromImage(byte[] imageBytes) throws IOException {
        String uri = endpoint + "/vision/v3.2/ocr?language=unk&detectOrientation=true";

        HttpPost request = new HttpPost(uri);
        request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);
        request.setHeader("Content-Type", "application/octet-stream");
        request.setEntity(new ByteArrayEntity(imageBytes));

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();

        if (entity != null) {
            String jsonResponse = EntityUtils.toString(entity);
            return parseTextFromResponse(jsonResponse);
        }

        return "No text detected.";
    }

    private String parseTextFromResponse(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);
        StringBuilder extractedText = new StringBuilder();

        root.path("regions").forEach(region -> {
            region.path("lines").forEach(line -> {
                line.path("words").forEach(word -> {
                    extractedText.append(word.path("text").asText()).append(" ");
                });
                extractedText.append("\n");
            });
        });

        return extractedText.toString().trim();
    }
}

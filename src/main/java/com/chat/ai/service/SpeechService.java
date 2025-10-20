package com.chat.ai.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

import java.io.InputStream;

@Service
public class SpeechService {

    @Value("${azure.speech.key1}")
    private String azureSpeechKey1;

    @Value("${azure.speech.key2}")
    private String azureSpeechKey2;

    @Value("${azure.speech.region}")
    private String azureSpeechRegion;

    /**
     * Transcribes the provided audio file using Microsoft Azure Speech-to-Text.
     * Tries Key1 first; if unauthorized, tries Key2.
     */
    public String transcribe(MultipartFile audioFile) throws Exception {
        try {
            return callAzureSpeechApi(audioFile, azureSpeechKey1);
        } catch (Exception ex) {
            // If unauthorized (401), try key2
            if (isAuthenticationError(ex)) {
                return callAzureSpeechApi(audioFile, azureSpeechKey2);
            } else {
                throw ex;
            }
        }
    }

    private String callAzureSpeechApi(MultipartFile audioFile, String apiKey) throws Exception {
        String endpoint = String.format(
            "https://%s.stt.speech.microsoft.com/speech/recognition/conversation/cognitiveservices/v1?language=en-US",
            azureSpeechRegion
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("Ocp-Apim-Subscription-Key", apiKey);
        headers.set("Content-Type", "audio/wav");
        headers.set("Accept", "application/json");

        InputStream is = audioFile.getInputStream();
        byte[] audioBytes = is.readAllBytes();

        HttpEntity<byte[]> entity = new HttpEntity<>(audioBytes, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
            endpoint,
            HttpMethod.POST,
            entity,
            String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            JSONObject root = new JSONObject(response.getBody());
            return root.optString("DisplayText", "No transcript found.");
        } else {
            throw new Exception("Speech API error: " + response.getBody());
        }
    }

    private boolean isAuthenticationError(Exception ex) {
        String msg = ex.getMessage();
        return msg != null && (msg.contains("401") || msg.contains("Unauthorized"));
    }
}
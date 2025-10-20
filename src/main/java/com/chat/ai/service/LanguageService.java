package com.chat.ai.service;

import com.chat.ai.DTO.TranslationRequest;
import com.chat.ai.entities.LanguageTranslation;
import com.chat.ai.repositories.LanguageTranslationRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LanguageService {

    private final WebClient webClient;
    private final LanguageTranslationRepository repository;

    @Value("${azure.translator.api-version}")
    private String apiVersion;

    public LanguageTranslation translate(TranslationRequest request) {
        String route = "/translate?api-version=" + apiVersion +
                "&from=" + request.getSourceLanguage() +
                "&to=" + request.getTargetLanguage();

        Map<String, String> body = Map.of("Text", request.getOriginalText());

        // Azure Translate API returns a List of translations, so map accordingly
        List<Map<String, Object>> response = webClient.post()
                .uri(route)
                .bodyValue(List.of(body))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
                .block();

        // Parse translated text from response JSON
        String translatedText = "";
        if (response != null && !response.isEmpty()) {
            // response is a List with one item (for each input text)
            Map<String, Object> firstObj = response.get(0);
            if (firstObj.containsKey("translations")) {
                List<Map<String, String>> translations = (List<Map<String, String>>) firstObj.get("translations");
                if (!translations.isEmpty()) {
                    translatedText = translations.get(0).get("text");
                }
            }
        }

        LanguageTranslation translation = new LanguageTranslation();
        translation.setOriginalText(request.getOriginalText());
        translation.setTranslatedText(translatedText);
        translation.setSourceLanguage(request.getSourceLanguage());
        translation.setTargetLanguage(request.getTargetLanguage());

        return repository.save(translation);
    }

    public Map<String, Map<String, Object>> getSupportedLanguages() {
        String url = "/languages?api-version=" + apiVersion + "&scope=translation";

        Map<String, Object> response = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

        if (response != null && response.containsKey("translation")) {
            Object translationObj = response.get("translation");
            if (translationObj instanceof Map) {
                return (Map<String, Map<String, Object>>) translationObj;
            }
        }
        return Map.of();
    }
}

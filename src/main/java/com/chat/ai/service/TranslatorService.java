package com.chat.ai.service;

import com.chat.ai.DTO.TranslationRequest;
import com.chat.ai.entities.LanguageTranslation;
import com.chat.ai.repositories.LanguageTranslationRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TranslatorService {

    private final WebClient webClient;
    private final LanguageTranslationRepository repository;

    @Value("${azure.translator.api-version}")
    private String apiVersion;

    public LanguageTranslation translate(TranslationRequest request) {
        String route = "/translate?api-version=" + apiVersion +
                "&from=" + request.getSourceLanguage() +
                "&to=" + request.getTargetLanguage();

        Map<String, String> body = Map.of("Text", request.getOriginalText());

        // Call Azure Translator API and parse the response properly
        TranslationResponse[] response = webClient.post()
                .uri(route)
                .bodyValue(List.of(body))
                .retrieve()
                .bodyToMono(TranslationResponse[].class)
                .block();

        String translatedText = "";

        if (response != null && response.length > 0) {
            // Azure returns a list of translations, get the first one
            if (response[0].translations != null && !response[0].translations.isEmpty()) {
                translatedText = response[0].translations.get(0).text;
            }
        }

        LanguageTranslation translation = new LanguageTranslation();
        translation.setOriginalText(request.getOriginalText());
        translation.setTranslatedText(translatedText);
        translation.setSourceLanguage(request.getSourceLanguage());
        translation.setTargetLanguage(request.getTargetLanguage());

        return repository.save(translation);
    }

    // Define nested static classes matching Azure Translator API JSON response

    private static class TranslationResponse {
        public List<Translation> translations;
    }

    private static class Translation {
        public String text;
        public String to;
    }
}

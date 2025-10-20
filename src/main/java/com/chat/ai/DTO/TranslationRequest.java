package com.chat.ai.DTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TranslationRequest {
    private String originalText;
    private String sourceLanguage;
    private String targetLanguage;
}


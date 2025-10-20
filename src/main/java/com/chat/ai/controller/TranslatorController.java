package com.chat.ai.controller;

import com.chat.ai.DTO.TranslationRequest;
import com.chat.ai.service.LanguageService;
import com.chat.ai.service.TranslatorService;
import lombok.RequiredArgsConstructor;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/translate")
@RequiredArgsConstructor
public class TranslatorController {

    private final TranslatorService service;
    private final LanguageService languageService;

    @GetMapping("/data")
    public String home(Model model) {
        model.addAttribute("request", new TranslationRequest());
        model.addAttribute("languages", languageService.getSupportedLanguages());
        return "trindex";
    }

    @PostMapping("/translate")
    public String translateText(@ModelAttribute TranslationRequest request, Model model) {
        try {
            model.addAttribute("result", service.translate(request));
        } catch (Exception e) {
            model.addAttribute("error", "Translation failed: " + e.getMessage());
            model.addAttribute("request", request);
            return "trindex";
        }
        return "trresult";
    }
}

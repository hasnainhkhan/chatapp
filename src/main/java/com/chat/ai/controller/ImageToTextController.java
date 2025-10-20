package com.chat.ai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.chat.ai.service.AzureVisionService;

@Controller
public class ImageToTextController {

    @Autowired
    private AzureVisionService visionService;

    @GetMapping("/image-to-text")
    public String showForm() {
        return "image-upload";
    }

    @PostMapping("/image-to-text")
    public String handleUpload(@RequestParam("file") MultipartFile file, Model model) {
        try {
            byte[] imageData = file.getBytes();
            String extractedText = visionService.extractTextFromImage(imageData);
            model.addAttribute("text", extractedText);
        } catch (Exception e) {
            model.addAttribute("error", "Failed to extract text: " + e.getMessage());
        }
        return "image-result";
    }
}


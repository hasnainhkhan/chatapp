package com.chat.ai.controller;

import com.chat.ai.service.AzureTextToSpeechService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/text")
public class FileTextController {

    @Autowired
    private AzureTextToSpeechService ttsService;

    @GetMapping
    public String index() {
        return "filetext";
    }

    @PostMapping
    public String processText(
            @RequestParam(value = "textfile", required = false) MultipartFile file,
            @RequestParam(value = "manualtext", required = false) String manualText,
            Model model) {
        String resultText = "";

        try {
            if (file != null && !file.isEmpty()) {
                if (!file.getContentType().equals("text/plain")) {
                    model.addAttribute("error", "Only .txt files are allowed.");
                    return "filetext";
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                resultText = sb.toString();
            } else if (manualText != null && !manualText.trim().isEmpty()) {
                resultText = manualText.trim();
            } else {
                model.addAttribute("error", "Please upload a text file or enter some text.");
                return "filetext";
            }
            model.addAttribute("resultText", resultText);
        } catch (Exception e) {
            model.addAttribute("error", "Error processing input: " + e.getMessage());
        }
        return "filetext";
    }

    @PostMapping("/speech")
    public ResponseEntity<ByteArrayResource> generateSpeech(
            @RequestParam("text") String text) {
        try {
            byte[] audioBytes = ttsService.synthesizeSpeech(text);
            ByteArrayResource resource = new ByteArrayResource(audioBytes);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("audio/wav"));
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"speech.wav\"");
            headers.setContentLength(audioBytes.length);
            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
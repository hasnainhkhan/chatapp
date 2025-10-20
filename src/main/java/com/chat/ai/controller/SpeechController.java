package com.chat.ai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.chat.ai.entities.TranscriptionResult;
import com.chat.ai.repositories.TranscriptionResultRepository;
import com.chat.ai.service.SpeechService;

import java.util.Optional;

@Controller
public class SpeechController {

    @Autowired
    private SpeechService speechToTextService;

    @Autowired
    private TranscriptionResultRepository transcriptionResultRepository;

    @GetMapping("/data")
    public String index(Model model) {
        model.addAttribute("results", transcriptionResultRepository.findAll());
        return "txtspeech";
    }

    @PostMapping("/transcribe")
    public String transcribe(@RequestParam("file") MultipartFile file, Model model) {
        try {
            // Check if transcription for this file already exists
            Optional<TranscriptionResult> existingResult =
                transcriptionResultRepository.findByFileName(file.getOriginalFilename());

            if (existingResult.isPresent()) {
                model.addAttribute("success", "Transcription already exists for this file!");
                model.addAttribute("results", transcriptionResultRepository.findAll());
                return "txtspeech";
            }

            // Otherwise, do the transcription and save
            String transcript = speechToTextService.transcribe(file);
            TranscriptionResult result = new TranscriptionResult(file.getOriginalFilename(), transcript);
            transcriptionResultRepository.save(result);
            model.addAttribute("success", "Transcription successful!");
        } catch (Exception e) {
            model.addAttribute("error", "Transcription failed: " + e.getMessage());
        }
        model.addAttribute("results", transcriptionResultRepository.findAll());
        return "txtspeech";
    }
}
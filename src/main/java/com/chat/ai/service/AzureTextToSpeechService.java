package com.chat.ai.service;

import com.microsoft.cognitiveservices.speech.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AzureTextToSpeechService {

    @Value("${azure.speech.key1}")
    private String azureSpeechKey;

    @Value("${azure.speech.region}")
    private String azureSpeechRegion;

    public byte[] synthesizeSpeech(String text) throws Exception {
        SpeechConfig speechConfig = SpeechConfig.fromSubscription(azureSpeechKey, azureSpeechRegion);
        speechConfig.setSpeechSynthesisOutputFormat(SpeechSynthesisOutputFormat.Riff16Khz16BitMonoPcm);

        try (SpeechSynthesizer synthesizer = new SpeechSynthesizer(speechConfig, null)) {
            SpeechSynthesisResult result = synthesizer.SpeakText(text);

            if (result.getReason() == ResultReason.SynthesizingAudioCompleted) {
                return result.getAudioData();
            } else if (result.getReason() == ResultReason.Canceled) {
                SpeechSynthesisCancellationDetails cancellation =
                    SpeechSynthesisCancellationDetails.fromResult(result);
                throw new RuntimeException("Speech synthesis canceled: " + cancellation.getErrorDetails());
            } else {
                throw new RuntimeException("Speech synthesis failed: Reason: " + result.getReason());
            }
        }
    }
}
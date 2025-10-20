package com.chat.ai.entities;


import jakarta.persistence.*;

@Entity
public class TranscriptionResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    @Lob
    private String transcript;

    public TranscriptionResult() {}

    public TranscriptionResult(String fileName, String transcript) {
        this.fileName = fileName;
        this.transcript = transcript;
    }

    public Long getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public String getTranscript() {
        return transcript;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setTranscript(String transcript) {
        this.transcript = transcript;
    }
}

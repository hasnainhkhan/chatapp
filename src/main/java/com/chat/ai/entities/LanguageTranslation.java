package com.chat.ai.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class LanguageTranslation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String originalText;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String translatedText;

    @Column(nullable = false)
    private String sourceLanguage;

    @Column(nullable = false)
    private String targetLanguage;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
}



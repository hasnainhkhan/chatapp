package com.chat.ai.repositories;


import com.chat.ai.entities.LanguageTranslation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LanguageTranslationRepository extends JpaRepository<LanguageTranslation, UUID> {}


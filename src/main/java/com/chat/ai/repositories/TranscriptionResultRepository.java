package com.chat.ai.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chat.ai.entities.TranscriptionResult;

@Repository
public interface TranscriptionResultRepository extends JpaRepository<TranscriptionResult, Long> {
    // You can add custom query methods here if needed
	Optional<TranscriptionResult> findByFileName(String fileName);
	}

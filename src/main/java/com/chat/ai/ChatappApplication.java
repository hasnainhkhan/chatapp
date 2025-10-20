package com.chat.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;


@SpringBootApplication
public class ChatappApplication {

	public static void main(String[] args) {
		//.env file load 
		Dotenv dotenv = Dotenv.load();
        dotenv.entries().forEach(entry ->
            System.setProperty(entry.getKey(), entry.getValue())
        );
        System.out.println("Gemini Key: " + dotenv.get("GEMINI_API_KEY"));
        System.out.println("Gemini Key: " + dotenv.get("DB_USERNAME"));
        System.out.println("DB pass: " + dotenv.get("DB_PASSWORD"));
		SpringApplication.run(ChatappApplication.class, args);
	}

}

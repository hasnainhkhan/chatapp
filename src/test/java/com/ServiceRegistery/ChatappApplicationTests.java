package com.ServiceRegistery;  // better to match main app package: com.serviceregistery.chatapp

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.chat.ai.ChatappApplication;

@SpringBootTest(classes = ChatappApplication.class)
class ChatappApplicationTests {

    @Test
    void contextLoads() {
    }

}

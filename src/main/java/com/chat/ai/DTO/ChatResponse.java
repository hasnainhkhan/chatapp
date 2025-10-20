package com.chat.ai.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChatResponse {
    private List<Choice> choices;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Choice {
        private ChatMessage message;
    }
}

package com.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiAgentService {

    private final ChatClient chatClient;

    public String generateResponse(String message) {

        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }

}

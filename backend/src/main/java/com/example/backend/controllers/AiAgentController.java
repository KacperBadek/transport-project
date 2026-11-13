package com.example.backend.controllers;

import com.example.backend.services.AiAgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AiAgentController {

    private final AiAgentService aiService;

    @PostMapping("/api/ai/test")
    public String testAi(@RequestBody AiRequest request) {
        return aiService.generateResponse(request.message());
    }

    public record AiRequest(String message) {
    }

}

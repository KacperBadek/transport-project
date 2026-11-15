package com.example.backend.controllers;

import com.example.backend.model.dtos.journey.JourneyOptionDto;
import com.example.backend.services.JourneyAnalysisAgentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class JourneyAnalysisAgentController {

    private final JourneyAnalysisAgentService aiService;

    @PostMapping("/api/agent/analysis")
    public ResponseEntity<String> analyzeJourneys(@RequestBody @NotEmpty List<@Valid JourneyOptionDto> journeys) {
        return ResponseEntity.ok(aiService.analyzeJourneys(journeys));
    }

}

package com.example.backend.controllers;

import com.example.backend.model.dtos.JourneyOptionDto;
import com.example.backend.model.dtos.JourneyResponse;
import com.example.backend.services.JourneyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JourneyController {

    private final JourneyService journeyService;

    @GetMapping("/api/journey/search")
    public ResponseEntity<JourneyResponse> searchJourney() {
        return null;
    }

    @GetMapping("/api/journey/{id}")
    public ResponseEntity<JourneyOptionDto> getJourneyDetails(@PathVariable Long id) {
        return null;
    }

}

package com.example.backend.controllers;

import com.example.backend.model.dtos.journey.JourneyOptionDto;
import com.example.backend.model.dtos.journey.JourneySearchRequest;
import com.example.backend.services.JourneyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class JourneyController {

    private final JourneyService journeyService;

    @GetMapping("/api/journey/search")
    public ResponseEntity<List<JourneyOptionDto>> searchJourney(@Valid @RequestBody JourneySearchRequest request) {
        return ResponseEntity.ok(journeyService.searchJourneys(request));
    }

    @GetMapping("/api/journey/{id}")
    public ResponseEntity<JourneyOptionDto> getJourneyDetails(@PathVariable Long id) {
        return null;
    }

}

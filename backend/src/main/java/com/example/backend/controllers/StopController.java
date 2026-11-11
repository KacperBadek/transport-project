package com.example.backend.controllers;

import com.example.backend.model.dtos.StopDto;
import com.example.backend.services.StopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StopController {

    private final StopService stopService;

    @GetMapping("/api/stop/search")
    public ResponseEntity<List<StopDto>> searchStop(@RequestParam String query) {

        return ResponseEntity.ok(stopService.searchStops(query));
    }

}

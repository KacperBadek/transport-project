package com.example.backend.model.dtos.journey;

import lombok.Builder;

import java.util.List;

@Builder
public record JourneyOptionDto(
        String departureTime,
        String arrivalTime,
        int durationSeconds,
        int numberOfTransfers,
        List<JourneyLegDto> legs
) {
}

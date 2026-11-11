package com.example.backend.model.dtos.journey;

import lombok.Builder;

@Builder
public record JourneyLegDto(
        String mode,
        String departureTime,
        String arrivalTime,
        String from,
        String to,
        String routeShortName,
        String routeLongName
) {
}

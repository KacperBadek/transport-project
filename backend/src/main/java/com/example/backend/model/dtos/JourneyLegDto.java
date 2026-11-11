package com.example.backend.model.dtos;

import java.time.OffsetDateTime;

public record JourneyLegDto(
        String mode,
        OffsetDateTime departureTime,
        OffsetDateTime arrivalTime,
        StopDto from,
        StopDto to,
        RouteInfoDto route
) {
}

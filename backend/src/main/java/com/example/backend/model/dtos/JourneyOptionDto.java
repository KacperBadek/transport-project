package com.example.backend.model.dtos;

import java.time.OffsetDateTime;
import java.util.List;

public record JourneyOptionDto(
        String id,
        OffsetDateTime departureTime,
        OffsetDateTime arrivalTime,
        int durationSeconds,
        int numberOfTransfers,
        int walkingDurationSeconds,
        List<JourneyLegDto> legs
) {
}

package com.example.backend.model.dtos.journey;

import lombok.Builder;

@Builder
public record JourneyCriterionScoreDto(
        String criterion,
        String measuredValue,
        int score,
        int weight
) {
}

package com.example.backend.model.dtos.journey;

import lombok.Builder;

import java.util.List;

@Builder
public record JourneyScoredOptionDto(
        int journeyIndex,
        double overallScore,
        List<JourneyCriterionScoreDto> criteria
) {
}

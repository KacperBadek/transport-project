package com.example.backend.model.dtos.journey;

import java.util.List;

public record JourneyScoredOptionDto(
        int journeyIndex,
        JourneyOptionDto journey,
        double overallScore,
        List<JourneyCriterionScoreDto> criteria
) {
}

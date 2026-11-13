package com.example.backend.model.dtos.journey;

import lombok.Builder;

@Builder
public record JourneyScoreDto(int journeyIndex,
                              double durationScore,
                              double transfersScore,
                              double transferWaitScore,
                              double comfortScore,
                              double overallScore) {
}

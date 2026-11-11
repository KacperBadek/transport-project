package com.example.backend.model.dtos;

import lombok.Builder;

import java.util.List;

@Builder
public record JourneyResponse(List<JourneyOptionDto> journeys) {
}

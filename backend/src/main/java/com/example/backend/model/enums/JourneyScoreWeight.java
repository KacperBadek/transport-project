package com.example.backend.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JourneyScoreWeight {
    TRAVEL_TIME(35),
    NUMBER_OF_TRANSFERS(30),
    TOTAL_TRANSFER_TIME(20),
    COMFORT(15);

    private final int weight;
}

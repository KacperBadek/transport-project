package com.example.backend.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JourneyScoreWeight {
    TIME(35),
    TRANSFERS(30),
    WAITING(20),
    COMFORT(15);

    private final int weight;
}

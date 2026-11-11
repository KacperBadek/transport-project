package com.example.backend.model.dtos.journey;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record JourneySearchRequest(
        @NotBlank
        String originName,

        @NotNull
        @DecimalMin("-90.0")
        @DecimalMax("90.0")
        Double originLatitude,

        @NotNull
        @DecimalMin("-180.0")
        @DecimalMax("180.0")
        Double originLongitude,

        @NotBlank
        String destinationName,

        @NotNull
        @DecimalMin("-90.0")
        @DecimalMax("90.0")
        Double destinationLatitude,

        @NotNull
        @DecimalMin("-180.0")
        @DecimalMax("180.0")
        Double destinationLongitude,

        @NotNull
        OffsetDateTime departureTime
) {
}

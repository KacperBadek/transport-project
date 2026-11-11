package com.example.backend.model.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import jakarta.validation.constraints.NotNull;

@Builder
public record StopDto(@NotBlank String id,
                      @NotBlank String name,
                      @NotNull Double latitude,
                      @NotNull Double longitude) {
}

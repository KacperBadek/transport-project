package com.example.backend.model.dtos;

import lombok.Builder;

@Builder
public record StopDto(String id,
                      String name,
                      Double latitude,
                      Double longitude) {
}

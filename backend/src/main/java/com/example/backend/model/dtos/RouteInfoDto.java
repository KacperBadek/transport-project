package com.example.backend.model.dtos;

import lombok.Builder;

@Builder
public record RouteInfoDto(
        String shortName,
        String longName
) {
}

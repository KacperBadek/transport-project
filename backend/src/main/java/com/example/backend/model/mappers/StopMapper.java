package com.example.backend.model.mappers;

import com.example.backend.model.dtos.StopDto;
import jakarta.validation.constraints.NotNull;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

public class StopMapper {

    private StopMapper() {
    }

    public static StopDto toDto(@NotNull JsonNode stop) {
        return StopDto.builder()
                .id(stop.path("gtfsId").asText())
                .name(stop.path("name").asText())
                .latitude(stop.path("lat").asDouble())
                .longitude(stop.path("lon").asDouble())
                .build();
    }

    public static List<StopDto> toDtos(@NotNull JsonNode response) {
        List<StopDto> result = new ArrayList<>();

        for (JsonNode stop : response) {
            result.add(toDto(stop));
        }

        return result;
    }

}

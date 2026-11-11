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
        return new StopDto(
                stop.path("gtfsId").asText(),
                stop.path("name").asText(),
                stop.path("lat").asDouble(),
                stop.path("lon").asDouble()
        );
    }

    public static List<StopDto> toDtos(@NotNull JsonNode response) {
        List<StopDto> result = new ArrayList<>();

        for (JsonNode stop : response) {
            result.add(toDto(stop));
        }

        return result;
    }

}

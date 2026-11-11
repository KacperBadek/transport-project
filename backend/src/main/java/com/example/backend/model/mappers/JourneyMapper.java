package com.example.backend.model.mappers;

import com.example.backend.model.dtos.journey.JourneyLegDto;
import com.example.backend.model.dtos.journey.JourneyOptionDto;
import jakarta.validation.constraints.NotNull;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

public class JourneyMapper {

    private JourneyMapper() {}

    public static List<JourneyOptionDto> toDtos(@NotNull JsonNode response) {
        List<JourneyOptionDto> journeys = new ArrayList<>();

        JsonNode edges = response
                .path("data")
                .path("planConnection")
                .path("edges");

        for (JsonNode edge : edges) {
            JsonNode node = edge.path("node");

            List<JourneyLegDto> legs = new ArrayList<>();

            for (JsonNode leg : node.path("legs")) {
                JsonNode route = leg.path("route");

                legs.add(new JourneyLegDto(
                        leg.path("mode").asText(),
                        leg.path("start").path("scheduledTime").asText(null),
                        leg.path("end").path("scheduledTime").asText(null),
                        leg.path("from").path("name").asText(null),
                        leg.path("to").path("name").asText(null),
                        route.path("shortName").asText(null),
                        route.path("longName").asText(null)
                ));
            }

            journeys.add(new JourneyOptionDto(
                    node.path("start").asText(),
                    node.path("end").asText(),
                    node.path("duration").asInt(),
                    node.path("numberOfTransfers").asInt(),
                    legs
            ));
        }

        return journeys;
    }

}

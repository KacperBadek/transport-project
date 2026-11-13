package com.example.backend.model.mappers;

import com.example.backend.model.dtos.journey.JourneyLegDto;
import com.example.backend.model.dtos.journey.JourneyOptionDto;
import jakarta.validation.constraints.NotNull;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

public class JourneyMapper {

    private JourneyMapper() {
    }

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

                legs.add(JourneyLegDto.builder()
                        .mode(leg.path("mode").asText())
                        .departureTime(leg.path("start").path("scheduledTime").asText(null))
                        .arrivalTime(leg.path("end").path("scheduledTime").asText(null))
                        .from(leg.path("from").path("name").asText(null))
                        .to(leg.path("to").path("name").asText(null))
                        .routeShortName(route.path("shortName").asText(null))
                        .routeLongName(route.path("longName").asText(null))
                        .build()
                );
            }

            journeys.add(JourneyOptionDto.builder()
                    .departureTime(node.path("start").asText())
                    .arrivalTime(node.path("end").asText())
                    .durationSeconds(node.path("duration").asInt())
                    .numberOfTransfers(node.path("numberOfTransfers").asInt())
                    .legs(legs)
                    .build()
            );
        }

        return journeys;
    }

}

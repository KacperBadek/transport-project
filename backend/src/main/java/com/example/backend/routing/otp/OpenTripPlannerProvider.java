package com.example.backend.routing.otp;

import com.example.backend.model.dtos.StopDto;
import com.example.backend.model.dtos.journey.JourneyOptionDto;
import com.example.backend.model.dtos.journey.JourneySearchRequest;
import com.example.backend.model.mappers.JourneyMapper;
import com.example.backend.model.mappers.StopMapper;
import com.example.backend.routing.RoutingProvider;
import com.example.backend.routing.StopProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OpenTripPlannerProvider implements RoutingProvider, StopProvider {

    private final OtpGraphQlClient otpClient;

    @Override
    public List<JourneyOptionDto> searchJourneys(JourneySearchRequest request) {
        var response = otpClient.searchJourneys(request);
        validateGraphQLResponse(response);

        var planConnection = response
                .path("data")
                .path("planConnection");


        validatePlanConnection(planConnection);
        validateRoutingErrors(planConnection.path("routingErrors"));

        return JourneyMapper.toDtos(response);
    }

    @Override
    public List<StopDto> searchStops(String query) {
        validateQuery(query);
        var response = otpClient.searchStops(query);
        validateGraphQLResponse(response);

        var stops = response.path("data")
                .path("viewer")
                .path("stops");

        validateStops(stops);
        return StopMapper.toDtos(stops);
    }

    private void validateQuery(String query) {
        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException(
                    "Search query can't be null or blank."
            );
        }
    }

    private void validateGraphQLResponse(JsonNode response) {
        if (response.has("errors")) {
            throw new RuntimeException(
                    "OTP returned error: " + response.get("errors")
            );
        }
    }

    private void validateStops(JsonNode stops) {
        if (!stops.isArray()) {
            throw new RuntimeException(
                    "OTP returned an invalid stops response."
            );
        }
    }

    private void validatePlanConnection(JsonNode planConnection) {
        if (planConnection.isMissingNode() || planConnection.isNull()) {
            throw new RuntimeException(
                    "OTP did not return journey data."
            );
        }
    }

    private void validateRoutingErrors(JsonNode routingErrors) {
        if (!routingErrors.isArray()) {
            throw new RuntimeException(
                    "OTP returned an invalid routing errors response."
            );
        }

        if (!routingErrors.isEmpty()) {
            throw new RuntimeException(
                    "OTP returned routing errors: " + routingErrors
            );
        }
    }

}

package com.example.backend.routing.otp;

import com.example.backend.model.dtos.journey.JourneyOptionDto;
import com.example.backend.model.dtos.journey.JourneySearchRequest;
import com.example.backend.model.mappers.JourneyMapper;
import com.example.backend.routing.RoutingProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OpenTripPlannerProvider implements RoutingProvider {

    private final OtpGraphQlClient otpClient;

    @Override
    public List<JourneyOptionDto> searchJourneys(JourneySearchRequest request) {
        var response = otpClient.searchJourneys(request);
        validateGraphQLResponse(response);

        var planConnection = response
                .path("data")
                .path("planConnection");


        validatePlanConnection(planConnection);
        JsonNode routingErrors = planConnection.path("routingErrors");
        validateRoutingErrors(routingErrors);

        return JourneyMapper.toDtos(response);
    }

    private void validateGraphQLResponse(JsonNode response) {
        if (response.has("errors")) {
            throw new RuntimeException(
                    "OTP returned error: " + response.get("errors")
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

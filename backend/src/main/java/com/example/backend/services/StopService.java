package com.example.backend.services;

import com.example.backend.model.dtos.StopDto;
import com.example.backend.model.mappers.StopMapper;
import com.example.backend.routing.otp.OtpGraphQlClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StopService {

    private final OtpGraphQlClient otpClient;

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
        if (response == null) {
            throw new RuntimeException(
                    "OTP returned an empty response."
            );
        }

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

}

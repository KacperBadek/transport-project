package com.example.backend.routing.otp;

import com.example.backend.model.dtos.journey.JourneySearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.JsonNode;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class OtpGraphQlClient {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public JsonNode executeQuery(String query) {

        Map<String, String> request = Map.of(
                "query", query
        );

        return webClient.post()
                .bodyValue(request)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
    }

    public JsonNode searchStops(String name) {

        try {
            String escapedName = objectMapper.writeValueAsString(name);

            String query = """
                    query {
                        viewer {
                            stops(name: %s) {
                                gtfsId
                                name
                                lat
                                lon
                            }
                        }
                    }
                    """.formatted(escapedName);

            return executeQuery(query);

        } catch (Exception exception) {
            throw new RuntimeException(
                    "Failed to find stops in OTP",
                    exception
            );
        }
    }

    public JsonNode searchJourneys(JourneySearchRequest request) {
        try {
            String originName = objectMapper.writeValueAsString(
                    request.originName()
            );

            String destinationName = objectMapper.writeValueAsString(
                    request.destinationName()
            );

            String departureTime = objectMapper.writeValueAsString(
                    request.departureTime().toString()
            );

            String query = """
                    query {
                        planConnection(
                            origin: {
                                label: %s
                                location: {
                                    coordinate: {
                                        latitude: %s
                                        longitude: %s
                                    }
                                }
                            }
                            destination: {
                                label: %s
                                location: {
                                    coordinate: {
                                        latitude: %s
                                        longitude: %s
                                    }
                                }
                            }
                            dateTime: {
                                earliestDeparture: %s
                            }
                            first: 5
                        ) {
                            edges {
                                node {
                                    start
                                    end
                                    duration
                                    numberOfTransfers
                                    legs {
                                        mode
                                        start {
                                            scheduledTime
                                        }
                                        end {
                                            scheduledTime
                                        }
                                        from {
                                            name
                                        }
                                        to {
                                            name
                                        }
                                        route {
                                            shortName
                                            longName
                                        }
                                    }
                                }
                            }
                            routingErrors {
                                code
                                description
                                inputField
                            }
                        }
                    }
                    """.formatted(
                    originName,
                    request.originLatitude(),
                    request.originLongitude(),
                    destinationName,
                    request.destinationLatitude(),
                    request.destinationLongitude(),
                    departureTime
            );

            return executeQuery(query);

        } catch (Exception exception) {
            throw new RuntimeException(
                    "Failed to find journeys in OTP",
                    exception
            );
        }
    }
}

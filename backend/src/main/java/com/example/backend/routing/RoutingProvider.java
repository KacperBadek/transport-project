package com.example.backend.routing;

import com.example.backend.model.dtos.journey.JourneyOptionDto;
import com.example.backend.model.dtos.journey.JourneySearchRequest;

import java.util.List;

public interface RoutingProvider {

    List<JourneyOptionDto> searchJourneys(
            JourneySearchRequest request
    );

}

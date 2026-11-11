package com.example.backend.services;

import com.example.backend.model.dtos.journey.JourneyOptionDto;
import com.example.backend.model.dtos.journey.JourneySearchRequest;
import com.example.backend.routing.RoutingProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JourneyService {

    private final RoutingProvider routingProvider;

    public List<JourneyOptionDto> searchJourneys(JourneySearchRequest request) {
        return routingProvider.searchJourneys(request);
    }

}

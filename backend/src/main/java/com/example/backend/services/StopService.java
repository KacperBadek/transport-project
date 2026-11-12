package com.example.backend.services;

import com.example.backend.model.dtos.StopDto;
import com.example.backend.routing.StopProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StopService {

    private final StopProvider stopProvider;

    public List<StopDto> searchStops(String query) {
        return stopProvider.searchStops(query);
    }

}

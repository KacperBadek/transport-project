package com.example.backend.routing;

import com.example.backend.model.dtos.StopDto;

import java.util.List;

public interface StopProvider {

    List<StopDto> searchStops(String query);

}

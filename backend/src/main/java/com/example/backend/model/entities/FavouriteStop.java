package com.example.backend.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "favourite_stops")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavouriteStop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stopId;

    private String stopName;

    @Builder.Default
    private Double latitude = 0.0;

    @Builder.Default
    private Double longitude = 0.0;
}

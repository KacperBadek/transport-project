package com.example.backend.services;

import com.example.backend.model.dtos.journey.JourneyCriterionScoreDto;
import com.example.backend.model.dtos.journey.JourneyLegDto;
import com.example.backend.model.dtos.journey.JourneyOptionDto;
import com.example.backend.model.dtos.journey.JourneyScoredOptionDto;
import com.example.backend.model.enums.JourneyScoreWeight;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JourneyScoringService {

    public List<JourneyScoredOptionDto> scoreJourneys(
            List<JourneyOptionDto> journeys
    ) {
        if (journeys == null || journeys.isEmpty()) {
            throw new IllegalArgumentException(
                    "Journey list cannot be empty."
            );
        }

        int fastestDuration = journeys.stream()
                .mapToInt(JourneyOptionDto::durationSeconds)
                .min()
                .orElseThrow();

        List<JourneyScoredOptionDto> results = new ArrayList<>();

        for (int i = 0; i < journeys.size(); i++) {
            JourneyOptionDto journey = journeys.get(i);

            int waitingMinutes = calculateWaitingMinutes(journey);
            int shortestTransfer = calculateShortestTransfer(journey);
            int walkingMinutes = calculateWalkingMinutes(journey);

            int timeScore = calculateTimeScore(
                    journey.durationSeconds(),
                    fastestDuration
            );

            int transfersScore = calculateTransfersScore(
                    journey.numberOfTransfers()
            );

            int waitingScore = calculateWaitingScore(
                    journey.numberOfTransfers(),
                    waitingMinutes,
                    shortestTransfer
            );

            int walkingScore = calculateWalkingScore(walkingMinutes);

            double overallScore =
                    timeScore * JourneyScoreWeight.TIME.getWeight() / 100.0
                            + transfersScore * JourneyScoreWeight.TRANSFERS.getWeight() / 100.0
                            + waitingScore * JourneyScoreWeight.WAITING.getWeight() / 100.0
                            + walkingScore * JourneyScoreWeight.COMFORT.getWeight() / 100.0;

            List<JourneyCriterionScoreDto> criteria = List.of(
                    new JourneyCriterionScoreDto(
                            "TRAVEL_TIME",
                            journey.durationSeconds() / 60 + " min",
                            timeScore,
                            JourneyScoreWeight.TIME.getWeight()
                    ),
                    new JourneyCriterionScoreDto(
                            "TRANSFERS",
                            String.valueOf(journey.numberOfTransfers()),
                            transfersScore,
                            JourneyScoreWeight.TRANSFERS.getWeight()
                    ),
                    new JourneyCriterionScoreDto(
                            "TRANSFER_WAITING_TIME",
                            waitingMinutes + " min",
                            waitingScore,
                            JourneyScoreWeight.WAITING.getWeight()
                    ),
                    new JourneyCriterionScoreDto(
                            "WALKING_COMFORT",
                            walkingMinutes + " min",
                            walkingScore,
                            JourneyScoreWeight.COMFORT.getWeight()
                    )
            );

            results.add(new JourneyScoredOptionDto(
                    i,
                    journey,
                    Math.round(overallScore * 100.0) / 100.0,
                    criteria
            ));
        }

        return results;
    }

    private int calculateTimeScore(
            int durationSeconds,
            int fastestDuration
    ) {
        if (fastestDuration <= 0) {
            return 1;
        }

        double ratio = (double) durationSeconds / fastestDuration;

        if (ratio <= 1.10) return 5;
        if (ratio <= 1.25) return 4;
        if (ratio <= 1.50) return 3;
        if (ratio <= 1.75) return 2;

        return 1;
    }

    private int calculateTransfersScore(int transfers) {
        if (transfers <= 0) return 5;
        if (transfers == 1) return 4;
        if (transfers == 2) return 3;
        if (transfers == 3) return 2;

        return 1;
    }

    private int calculateWaitingScore(
            int transfers,
            int totalWaitingMinutes,
            int shortestTransfer
    ) {
        if (transfers <= 0) return 5;

        if (shortestTransfer < 3) return 1;
        if (shortestTransfer <= 5) return 2;

        if (totalWaitingMinutes <= 10) return 5;
        if (totalWaitingMinutes <= 15) return 4;
        if (totalWaitingMinutes <= 25) return 3;

        return 2;
    }

    private int calculateWalkingScore(int walkingMinutes) {
        if (walkingMinutes <= 5) return 5;
        if (walkingMinutes <= 10) return 4;
        if (walkingMinutes <= 20) return 3;
        if (walkingMinutes <= 30) return 2;

        return 1;
    }

    private int calculateWalkingMinutes(JourneyOptionDto journey) {
        int totalWalkingSeconds = 0;

        for (JourneyLegDto leg : journey.legs()) {
            if (!"WALK".equalsIgnoreCase(leg.mode())) {
                continue;
            }

            OffsetDateTime departure =
                    OffsetDateTime.parse(leg.departureTime());

            OffsetDateTime arrival =
                    OffsetDateTime.parse(leg.arrivalTime());

            long seconds = Duration.between(
                    departure,
                    arrival
            ).toSeconds();

            if (seconds < 0) {
                throw new IllegalArgumentException(
                        "Invalid walking leg times."
                );
            }

            totalWalkingSeconds += (int) seconds;
        }

        return (int) Math.ceil(totalWalkingSeconds / 60.0);
    }

    private List<JourneyLegDto> getTransitLegs(JourneyOptionDto journey) {
        return journey.legs().stream()
                .filter(leg -> !"WALK".equalsIgnoreCase(leg.mode()))
                .toList();
    }

    private List<Integer> calculateTransferTimes(
            JourneyOptionDto journey
    ) {
        List<JourneyLegDto> transitLegs = getTransitLegs(journey);
        List<Integer> transferTimes = new ArrayList<>();

        for (int i = 0; i < journey.numberOfTransfers(); i++) {
            OffsetDateTime arrival = OffsetDateTime.parse(
                    transitLegs.get(i).arrivalTime()
            );

            OffsetDateTime departure = OffsetDateTime.parse(
                    transitLegs.get(i + 1).departureTime()
            );

            long minutes = Duration.between(
                    arrival,
                    departure
            ).toMinutes();

            if (minutes < 0) {
                throw new IllegalArgumentException(
                        "Invalid journey transfer times."
                );
            }

            transferTimes.add((int) minutes);
        }

        return transferTimes;
    }

    private int calculateWaitingMinutes(JourneyOptionDto journey) {
        return calculateTransferTimes(journey).stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    private int calculateShortestTransfer(JourneyOptionDto journey) {
        return calculateTransferTimes(journey).stream()
                .mapToInt(Integer::intValue)
                .min()
                .orElse(0);
    }

}

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
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JourneyScoringService {

    public List<JourneyScoredOptionDto> scoreJourneys(List<JourneyOptionDto> journeys) {
        validateJourneys(journeys);

        int fastestDuration = journeys.stream()
                .mapToInt(JourneyOptionDto::durationSeconds)
                .min()
                .orElseThrow();

        List<JourneyScoredOptionDto> results = new ArrayList<>();

        for (int i = 0; i < journeys.size(); i++) {
            var journey = journeys.get(i);

            int travelTimeScore = calculateTimeScore(
                    journey.durationSeconds(),
                    fastestDuration
            );

            int transfersScore = calculateTransfersScore(
                    journey.numberOfTransfers()
            );

            long totalTransferSeconds = calculateTotalTransferSeconds(journey);
            long shortestTransferSeconds = calculateShortestTransferSeconds(journey);

            int transferTimeScore = calculateTransferTimeScore(
                    journey.numberOfTransfers(),
                    totalTransferSeconds,
                    shortestTransferSeconds
            );

            long walkingSeconds = calculateWalkingSeconds(journey);
            int walkingScore = calculateWalkingScore(walkingSeconds);

            double overallScore = calculateOverallScore(
                    travelTimeScore,
                    transfersScore,
                    transferTimeScore,
                    walkingScore
            );

            List<JourneyCriterionScoreDto> criteria = List.of(
                    JourneyCriterionScoreDto.builder()
                            .criterion("TOTAL_TRAVEL_TIME")
                            .measuredValue(formatDuration(journey.durationSeconds()))
                            .score(travelTimeScore)
                            .weight(JourneyScoreWeight.TRAVEL_TIME.getWeight())
                            .build(),

                    JourneyCriterionScoreDto.builder()
                            .criterion("NUMBER_OF_TRANSFERS")
                            .measuredValue(String.valueOf(journey.numberOfTransfers()))
                            .score(transfersScore)
                            .weight(JourneyScoreWeight.NUMBER_OF_TRANSFERS.getWeight())
                            .build(),

                    JourneyCriterionScoreDto.builder()
                            .criterion("TOTAL_TRANSFER_TIME")
                            .measuredValue(formatDuration(totalTransferSeconds))
                            .score(transferTimeScore)
                            .weight(JourneyScoreWeight.TOTAL_TRANSFER_TIME.getWeight())
                            .build(),

                    JourneyCriterionScoreDto.builder()
                            .criterion("TIME_SPENT_WALKING")
                            .measuredValue(formatDuration(walkingSeconds))
                            .score(walkingScore)
                            .weight(JourneyScoreWeight.COMFORT.getWeight())
                            .build()
            );

            results.add(JourneyScoredOptionDto.builder()
                    .journeyIndex(i + 1)
                    .overallScore(overallScore)
                    .criteria(criteria)
                    .build());
        }

        return results;
    }

    private int calculateTimeScore(int durationSeconds, int fastestDuration) {
        double ratio = (double) durationSeconds / fastestDuration;

        if (ratio <= 1.10) return 5;
        if (ratio <= 1.25) return 4;
        if (ratio <= 1.50) return 3;
        if (ratio <= 1.75) return 2;

        return 1;
    }

    private int calculateTransfersScore(int transfers) {
        if (transfers == 0) return 5;
        if (transfers == 1) return 4;
        if (transfers == 2) return 3;
        if (transfers == 3) return 2;

        return 1;
    }

    private int calculateTransferTimeScore(
            int transfers,
            long totalTransferSeconds,
            long shortestTransferSeconds
    ) {
        if (transfers == 0) return 5;

        if (shortestTransferSeconds < 180) return 1;
        if (shortestTransferSeconds <= 300) return 2;

        if (totalTransferSeconds <= 600) return 5;
        if (totalTransferSeconds <= 900) return 4;
        if (totalTransferSeconds <= 1500) return 3;

        return 2;
    }

    private int calculateWalkingScore(long walkingSeconds) {
        if (walkingSeconds <= 5 * 60) return 5;
        if (walkingSeconds <= 10 * 60) return 4;
        if (walkingSeconds <= 20 * 60) return 3;
        if (walkingSeconds <= 30 * 60) return 2;

        return 1;
    }

    private double calculateOverallScore(
            int travelTimeScore,
            int transfersScore,
            int transferTimeScore,
            int walkingScore
    ) {
        double score =
                travelTimeScore * JourneyScoreWeight.TRAVEL_TIME.getWeight() / 100.0
                        + transfersScore * JourneyScoreWeight.NUMBER_OF_TRANSFERS.getWeight() / 100.0
                        + transferTimeScore * JourneyScoreWeight.TOTAL_TRANSFER_TIME.getWeight() / 100.0
                        + walkingScore * JourneyScoreWeight.COMFORT.getWeight() / 100.0;

        return Math.round(score * 100.0) / 100.0;
    }

    private long calculateWalkingSeconds(JourneyOptionDto journey) {
        long totalSeconds = 0;

        for (JourneyLegDto leg : journey.legs()) {
            if (!isWalking(leg)) {
                continue;
            }

            OffsetDateTime departure = parseTime(leg.departureTime());
            OffsetDateTime arrival = parseTime(leg.arrivalTime());

            long seconds = Duration.between(departure, arrival).toSeconds();

            if (seconds < 0) {
                throw new IllegalArgumentException(
                        "Walking leg has an invalid time interval."
                );
            }

            totalSeconds += seconds;
        }

        return totalSeconds;
    }

    private long calculateTotalTransferSeconds(JourneyOptionDto journey) {
        return calculateTransferTimes(journey).stream()
                .mapToLong(Long::longValue)
                .sum();
    }

    private long calculateShortestTransferSeconds(JourneyOptionDto journey) {
        return calculateTransferTimes(journey).stream()
                .mapToLong(Long::longValue)
                .min()
                .orElse(0);
    }

    private List<Long> calculateTransferTimes(JourneyOptionDto journey) {
        List<JourneyLegDto> transitLegs = journey.legs().stream()
                .filter(leg -> !isWalking(leg))
                .toList();

        List<Long> transferTimes = new ArrayList<>();

        for (int i = 0; i < transitLegs.size() - 1; i++) {
            OffsetDateTime arrival = parseTime(
                    transitLegs.get(i).arrivalTime()
            );

            OffsetDateTime departure = parseTime(
                    transitLegs.get(i + 1).departureTime()
            );

            long seconds = Duration.between(arrival, departure).toSeconds();

            if (seconds < 0) {
                throw new IllegalArgumentException(
                        "Invalid transfer times between transit legs."
                );
            }

            transferTimes.add(seconds);
        }

        return transferTimes;
    }

    private boolean isWalking(JourneyLegDto leg) {
        return "WALK".equalsIgnoreCase(leg.mode());
    }

    private OffsetDateTime parseTime(String value) {
        try {
            return OffsetDateTime.parse(value);
        } catch (DateTimeParseException | NullPointerException exception) {
            throw new IllegalArgumentException(
                    "Invalid journey leg date/time: " + value,
                    exception
            );
        }
    }

    private String formatDuration(long seconds) {
        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;

        if (minutes == 0) {
            return remainingSeconds + " s";
        }

        if (remainingSeconds == 0) {
            return minutes + " min";
        }

        return minutes + " min " + remainingSeconds + " s";
    }

    private void validateJourneys(List<JourneyOptionDto> journeys) {
        if (journeys == null || journeys.isEmpty()) {
            throw new IllegalArgumentException(
                    "Journey list cannot be null or empty."
            );
        }

        for (JourneyOptionDto journey : journeys) {
            if (journey == null) {
                throw new IllegalArgumentException(
                        "Journey cannot be null."
                );
            }

            if (journey.durationSeconds() <= 0) {
                throw new IllegalArgumentException(
                        "Journey duration must be greater than zero."
                );
            }

            if (journey.numberOfTransfers() < 0) {
                throw new IllegalArgumentException(
                        "Number of transfers cannot be negative."
                );
            }

            if (journey.legs() == null || journey.legs().isEmpty()) {
                throw new IllegalArgumentException(
                        "Journey must contain at least one leg."
                );
            }

            for (JourneyLegDto leg : journey.legs()) {
                if (leg == null || leg.mode() == null || leg.mode().isBlank()) {
                    throw new IllegalArgumentException(
                            "Journey leg and its mode cannot be null or empty."
                    );
                }

                OffsetDateTime departure = parseTime(leg.departureTime());
                OffsetDateTime arrival = parseTime(leg.arrivalTime());

                if (arrival.isBefore(departure)) {
                    throw new IllegalArgumentException(
                            "Journey leg arrival cannot be before departure."
                    );
                }
            }
        }
    }

}

import type JourneyLegDto from "./journeyLegDto.ts";

export default interface JourneyOptionDto {
    departureTime: string;
    arrivalTime: string;
    durationSeconds: number;
    numberOfTransfers: number;
    legs: JourneyLegDto[];
}
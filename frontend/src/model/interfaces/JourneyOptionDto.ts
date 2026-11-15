import type JourneyLegDto from "./JourneyLegDto";

export default interface JourneyOptionDto {
    departureTime: string;
    arrivalTime: string;
    durationSeconds: number;
    numberOfTransfers: number;
    legs: JourneyLegDto[];
}
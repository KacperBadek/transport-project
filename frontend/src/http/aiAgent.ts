import api from "./axios";
import type JourneyOptionDto from "../model/interfaces/journeyOptionDto.ts";

export async function analyzeJourneys(
    journeys: JourneyOptionDto[]
): Promise<string> {
    return (
        await api.post<string>(
            "/api/agent/analysis",
            journeys
        )
    ).data;
}
import api from "./axios";
import type StopDto from "../model/interfaces/stopDto.ts";

export async function searchStops(
    query: string
): Promise<StopDto[]> {
    return (
        await api.get<StopDto[]>("/api/stop/search", {
            params: {query},
        })
    ).data;
}

import api from "./axios";
import type JourneyOptionDto from "../model/interfaces/journeyOptionDto.ts";
import type JourneySearchRequest from "../model/interfaces/journeySearchRequest.ts";

export async function searchJourneys(
    request: JourneySearchRequest
): Promise<JourneyOptionDto[]> {
    return (
        await api.post<JourneyOptionDto[]>(
            "/api/journey/search",
            request
        )
    ).data
}


// export const searchJourneys = async (
//     request: JourneySearchRequest
// ): Promise<JourneyOptionDto[]> => {
//     const response = await api.post<JourneyOptionDto[]>(
//         "/api/journey/search",
//         request
//     );
//
//     return response.data;
// };
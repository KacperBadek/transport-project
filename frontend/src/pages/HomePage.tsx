import {useMutation} from "@tanstack/react-query";

import Header from "../components/Header.tsx";
import MainContent from "../components/MainContent";

import Error from "../components/Error.tsx";
import JourneyResults from "../components/FoundRoutesPanel/JourneyResults.tsx";

import {searchJourneys} from "../http/journey.ts";

import type JourneySearchRequest from "../model/interfaces/journeySearchRequest.ts";

export default function HomePage() {

    const journeyMutation = useMutation({
        mutationFn: (request: JourneySearchRequest) =>
            searchJourneys(request),
    });

    const handleSearch = (request: JourneySearchRequest) => {
        journeyMutation.mutate(request);
    };

    return (
        <div className="min-h-screen bg-slate-950">

            <Header
                logoLabel={"TransitFinder"}
                secondMessageLabel={"We love finding routes"}
            />

            <MainContent
                onSearch={handleSearch}
                isLoading={journeyMutation.isPending}
            />

            <div className="mx-auto max-w-3xl px-6">
                {journeyMutation.isError && (
                    <Error
                        message={"An error occurred while searching for connections. Please try again."}/>

                )}

                {journeyMutation.isSuccess && (
                    <JourneyResults
                        journeys={journeyMutation.data}
                    />
                )}
            </div>

        </div>
    );
}
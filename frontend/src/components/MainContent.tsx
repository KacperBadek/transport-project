import JourneySearchForm from "./JourneySearchForm/JounreySearchForm.tsx";
import type JourneySearchRequest from "../model/interfaces/journeySearchRequest.ts";

interface MainContentProps {
    onSearch: (request: JourneySearchRequest) => void;
    isLoading: boolean;
}

export default function MainContent({onSearch, isLoading}: MainContentProps) {
    return (
        <div className="mx-auto max-w-3xl px-6 py-12">

            <div className="mb-10 text-center">
                <h1 className="text-4xl font-bold tracking-tight text-white md:text-5xl">
                    I fucking love
                    <span className="text-blue-500"> public transport</span>
                </h1>

                <p className="mx-auto mt-4 max-w-xl text-slate-400">
                    Find the fastest public transportation route.
                    Select your starting and destination stops, and we'll
                    find the best route.
                </p>
            </div>

            <JourneySearchForm
                onSearch={onSearch}
                isLoading={isLoading}
            />

        </div>
    );
}
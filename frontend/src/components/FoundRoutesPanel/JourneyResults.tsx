import type JourneyOptionDto from "../../model/interfaces/JourneyOptionDto.ts";
import JourneyCard from "./JourneyCard.tsx";

interface JourneyResultsProps {
    journeys: JourneyOptionDto[];
}

export default function JourneyResults({journeys}: JourneyResultsProps) {
    if (journeys.length === 0) {
        return (
            <div className="rounded-2xl border border-slate-800 bg-slate-900/50 p-8 text-center">
                <p className="text-lg font-semibold text-white">
                    No routes found
                </p>

                <p className="mt-2 text-sm text-slate-400">
                    Try changing your stops or departure time.
                </p>
            </div>
        );
    }

    return (
        <section className="mt-10">
            <div className="mb-5">
                <h2 className="text-2xl font-bold text-white">
                    Available routes
                </h2>

                <p className="mt-1 text-sm text-slate-400">
                    {journeys.length} routes found
                </p>
            </div>

            <div className="space-y-4">
                {journeys.map((journey, index) => (
                    <JourneyCard
                        key={`${journey.departureTime}-${index}`}
                        journey={journey}
                        index={index}
                    />
                ))}
            </div>
        </section>
    );
}
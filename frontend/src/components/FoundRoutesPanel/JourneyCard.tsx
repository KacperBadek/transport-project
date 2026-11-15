import type JourneyOptionDto from "../../model/interfaces/JourneyOptionDto.ts";
import JourneySummary from "./JourneySummary.tsx";
import JourneyLeg from "./JourneyLeg.tsx";

interface JourneyCardProps {
    journey: JourneyOptionDto;
    index: number;
}

export default function JourneyCard({journey, index}: JourneyCardProps) {
    return (
        <div className="rounded-2xl border border-slate-800 bg-slate-900/70 p-5 transition hover:border-slate-700">

            <JourneySummary
                departureTime={journey.departureTime}
                arrivalTime={journey.arrivalTime}
                durationSeconds={journey.durationSeconds}
                numberOfTransfers={journey.numberOfTransfers}
                index={index}
            />

            <div className="mt-6 border-t border-slate-800 pt-5">
                <p className="mb-4 text-sm font-semibold text-slate-300">
                    Route details
                </p>

                <div className="space-y-4">
                    {journey.legs.map((leg, legIndex) => (
                        <JourneyLeg
                            key={`${legIndex}-${leg.departureTime}`}
                            leg={leg}
                            isLast={legIndex === journey.legs.length - 1}
                        />
                    ))}
                </div>
            </div>
        </div>
    );
}
import type JourneyOptionDto from "../../model/interfaces/journeyOptionDto.ts";
import getTransportIcon from "../../utils/getTransportIcon.tsx";
import getTransportName from "../../utils/getTransportName.ts";
import formatTime from "../../utils/formatTime.ts";

interface JourneyLegProps {
    leg: JourneyOptionDto["legs"][number];
    isLast: boolean;
}

export default function JourneyLeg({leg, isLast}: JourneyLegProps) {
    return (
        <div className="relative flex gap-4">
            <div className="flex flex-col items-center">
                <div
                    className="flex h-9 w-9 shrink-0 items-center justify-center rounded-full bg-blue-500/10 text-sm text-blue-400">
                    {getTransportIcon(leg.mode)}
                </div>

                {!isLast && (
                    <div className="my-1 min-h-8 w-px flex-1 bg-slate-700"/>
                )}
            </div>

            <div className="min-w-0 flex-1 pb-2">
                <div className="flex flex-wrap items-center justify-between gap-2">
                    <p className="font-semibold text-white">
                        {leg.routeShortName
                            ? `${getTransportName(leg.mode)} ${leg.routeShortName}`
                            : getTransportName(leg.mode)}
                    </p>

                    <p className="text-sm text-slate-400">
                        {formatTime(leg.departureTime)} – {formatTime(leg.arrivalTime)}
                    </p>
                </div>

                {leg.routeLongName && (
                    <p className="mt-1 text-xs text-slate-500">
                        {leg.routeLongName}
                    </p>
                )}

                <div className="mt-2 text-sm">
                    <p className="text-slate-300">
                        <span className="text-slate-500">From: </span>
                        {leg.from}
                    </p>

                    <p className="mt-1 text-slate-300">
                        <span className="text-slate-500">To: </span>
                        {leg.to}
                    </p>
                </div>
            </div>
        </div>
    );
}
import formatTime from "../../utils/formatTime.ts";

interface JourneySummaryProps {
    departureTime: string;
    arrivalTime: string;
    durationSeconds: number;
    numberOfTransfers: number;
    index: number;
}

function formatDuration(seconds: number) {
    const hours = Math.floor(seconds / 3600);
    const minutes = Math.floor((seconds % 3600) / 60);

    if (hours === 0) return `${minutes} min`;

    return `${hours} h ${minutes} min`;
}

export default function JourneySummary({
                                           departureTime,
                                           arrivalTime,
                                           durationSeconds,
                                           numberOfTransfers,
                                           index,
                                       }: JourneySummaryProps) {
    return (
        <div className="flex flex-wrap items-center justify-between gap-4">
            <div className="flex items-center gap-4">
                <div>
                    <p className="text-2xl font-bold text-white">
                        {formatTime(departureTime)}
                    </p>

                    <p className="text-sm text-slate-400">
                        Departure
                    </p>
                </div>

                <div className="flex flex-col items-center px-2">
                    <span className="text-xs text-slate-400">
                        {formatDuration(durationSeconds)}
                    </span>

                    <div className="my-2 h-px w-16 bg-blue-500"/>
                </div>

                <div>
                    <p className="text-2xl font-bold text-white">
                        {formatTime(arrivalTime)}
                    </p>

                    <p className="text-sm text-slate-400">
                        Arrival
                    </p>
                </div>
            </div>

            <div className="text-right">
                <span className="rounded-lg bg-blue-500/10 px-3 py-1 text-sm font-medium text-blue-400">
                    {numberOfTransfers === 0
                        ? "Direct"
                        : `${numberOfTransfers} transfers`}
                </span>

                <p className="mt-2 text-xs text-slate-500">
                    Journey #{index + 1}
                </p>
            </div>
        </div>
    );
}
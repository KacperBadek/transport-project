import {useState} from "react";
import StopAutocomplete from "./StopAutoComplete.tsx";
import type StopDto from "../../model/interfaces/StopDto.ts";
import type JourneySearchRequest from "../../model/interfaces/JourneySearchRequest.ts";
import DateTimePicker from "./DateTimePricker.tsx";

interface JourneySearchFormProps {
    onSearch: (request: JourneySearchRequest) => void;
    isLoading: boolean;
}

function getDefaultDateTime() {
    const now = new Date();

    now.setMinutes(now.getMinutes() - now.getTimezoneOffset());

    return now.toISOString().slice(0, 16);
}

export default function JourneySearchForm({
                                              onSearch,
                                              isLoading,
                                          }: JourneySearchFormProps) {
    const [origin, setOrigin] = useState<StopDto | null>(null);
    const [destination, setDestination] = useState<StopDto | null>(null);
    const [departureTime, setDepartureTime] = useState(getDefaultDateTime());

    const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        if (!origin || !destination || !departureTime) {
            return;
        }

        const request: JourneySearchRequest = {
            originName: origin.name,
            originLatitude: origin.latitude,
            originLongitude: origin.longitude,

            destinationName: destination.name,
            destinationLatitude: destination.latitude,
            destinationLongitude: destination.longitude,

            departureTime: new Date(departureTime).toISOString(),
        };

        onSearch(request);
    };

    return (
        <form
            onSubmit={handleSubmit}
            className="rounded-2xl border border-slate-800 bg-slate-900/70 p-6"
        >
            <div className="grid gap-5 md:grid-cols-2">
                <StopAutocomplete
                    label="Where are you coming from?"
                    placeholder="Enter your starting stop..."
                    onSelect={setOrigin}
                />

                <StopAutocomplete
                    label="Where are you going?"
                    placeholder="Enter your destination stop..."
                    onSelect={setDestination}
                />
            </div>

            <DateTimePicker label={"Date and time of departure"} value={departureTime} onChange={setDepartureTime}/>

            <button
                type="submit"
                disabled={!origin || !destination || !departureTime || isLoading}
                className="cursor-pointer mt-6 flex w-full items-center justify-center gap-2 rounded-xl bg-blue-600 px-6 py-4 font-semibold text-white transition hover:bg-blue-500 disabled:cursor-not-allowed disabled:opacity-50"
            >
                {isLoading ? "Searching..." : "Find routes"}
            </button>
        </form>
    );
}
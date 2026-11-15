import {useEffect, useState} from "react";
import {useQuery} from "@tanstack/react-query";
import {searchStops} from "../../http/stop.ts";
import type StopDto from "../../model/interfaces/StopDto.ts";
import AutocompleteOption from "./AutoCompleteOption.tsx";
import AutoCompleteInfo from "./AutoCompleteInfo.tsx";

interface StopAutocompleteProps {
    label: string;
    placeholder: string;
    onSelect: (stop: StopDto | null) => void;
}

export default function StopAutocomplete({
                                             label,
                                             placeholder,
                                             onSelect,
                                         }: StopAutocompleteProps) {
    const [input, setInput] = useState("");
    const [debouncedInput, setDebouncedInput] = useState("");
    const [isOpen, setIsOpen] = useState(false);

    useEffect(() => {
        const timeout = setTimeout(() => {
            setDebouncedInput(input.trim());
        }, 300);

        return () => clearTimeout(timeout);
    }, [input]);

    const {data: stops = [], isFetching} = useQuery({
        queryKey: ["stops", debouncedInput],
        queryFn: () => searchStops(debouncedInput),
        enabled: debouncedInput.length >= 2 && isOpen,
        staleTime: 30_000,
    });

    const handleChange = (value: string) => {
        setInput(value);
        setIsOpen(true);
        onSelect(null);
    };

    const handleSelect = (stop: StopDto) => {
        setInput(stop.name);
        setIsOpen(false);
        onSelect(stop);
    };

    return (
        <div className="relative w-full">
            <label className="mb-2 block text-sm font-medium text-slate-300">
                {label}
            </label>

            <input
                type="text"
                value={input}
                placeholder={placeholder}
                onChange={(e) => handleChange(e.target.value)}
                onFocus={() => setIsOpen(true)}
                onBlur={() => {
                    setIsOpen(false)
                }}
                className="w-full rounded-xl border border-slate-700 bg-slate-900 px-4 py-3 text-white outline-none transition placeholder:text-slate-500 focus:border-blue-500 focus:ring-2 focus:ring-blue-500/20"
            />

            {isOpen && input.trim().length >= 2 && (
                <div
                    className="absolute left-0 right-0 top-full z-20 mt-2 max-h-64 overflow-y-auto rounded-xl border border-slate-700 bg-slate-900">

                    {isFetching && (
                        <AutoCompleteInfo info={"Searching for stops..."}/>
                    )}

                    {!isFetching && stops.length === 0 && (
                        <AutoCompleteInfo info={"No stops were found."}/>
                    )}

                    {!isFetching && stops.map((stop) => (
                        <AutocompleteOption
                            key={stop.id}
                            label={stop.name}
                            onSelect={() => handleSelect(stop)}
                        />
                    ))}
                </div>
            )}
        </div>
    );
}
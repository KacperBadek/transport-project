import {TransportMode} from "../model/enums/TransportMode.ts";

const transportNames: Record<TransportMode, string> = {
    [TransportMode.BUS]: "Bus",
    [TransportMode.TRAM]: "Tram",
    [TransportMode.RAIL]: "Rail",
    [TransportMode.WALK]: "Walk",
};

export default function getTransportName(mode: string): string {
    return transportNames[mode.toUpperCase() as TransportMode] ?? mode;
}
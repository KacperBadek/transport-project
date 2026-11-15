import {TransportMode} from "../model/enums/TransportMode";
import {MdTram} from "react-icons/md";
import {FaBus, FaTrainSubway} from "react-icons/fa6";
import {FaWalking} from "react-icons/fa";

const transportIcons: Record<TransportMode, React.ReactNode> = {
    [TransportMode.BUS]: <FaBus/>,
    [TransportMode.TRAM]: <MdTram/>,
    [TransportMode.RAIL]: <FaTrainSubway/>,
    [TransportMode.WALK]: <FaWalking/>,
};

export default function getTransportIcon(mode: string): React.ReactNode {
    return transportIcons[mode.toUpperCase() as TransportMode] ?? <FaBus/>;
}
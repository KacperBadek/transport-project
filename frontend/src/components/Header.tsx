import { FaBusAlt } from "react-icons/fa";
import { FaHeart } from "react-icons/fa";

interface HeaderProps {
    logoLabel: string;
    secondMessageLabel: string;
}

export default function Header({logoLabel, secondMessageLabel}: HeaderProps) {
    return (
        <header className="border-b border-slate-800">
            <div className="mx-auto flex max-w-5xl items-center justify-between px-6 py-5 cursor-default">
                <div className="flex items-center gap-3">
                    <div className="flex h-10 w-10 items-center justify-center rounded-xl bg-blue-600 text-xl">
                        <FaBusAlt />
                    </div>

                    <span className="text-xl font-bold text-white">
                        {logoLabel}
                    </span>
                </div>

                <div className="flex items-center justify-between gap-1 group">
                    <span className="text-sm text-slate-400 group-hover:hidden">
                        {secondMessageLabel}
                    </span>
                    <span className="hidden group-hover:inline text-slate-400">
                        We love getting employed by Xelerit
                    </span>
                    <span className="text-red-500">
                        <FaHeart/>
                    </span>
                </div>
            </div>
        </header>
    );
}
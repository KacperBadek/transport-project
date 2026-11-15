interface AutoCompleteInfoProps {
    info: string
}

export default function AutoCompleteInfo({info}: AutoCompleteInfoProps) {
    return (
        <p className="p-4 text-sm text-slate-400">
            {info}
        </p>
    )
}
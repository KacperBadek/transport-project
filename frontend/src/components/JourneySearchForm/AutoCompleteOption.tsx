interface AutocompleteOptionProps {
    label: string;
    onSelect: () => void;
}

export default function AutocompleteOption({
                                               label,
                                               onSelect
                                           }: AutocompleteOptionProps) {
    return (
        <div
            onMouseDown={onSelect}
            className="flex w-full cursor-pointer flex-col border-b border-slate-800 px-4 py-3 text-left transition last:border-0 hover:bg-slate-800"
        >
            <span className="font-medium text-white">
                {label}
            </span>
        </div>
    );
}
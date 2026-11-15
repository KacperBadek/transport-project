interface DateTimePickerProps {
    label: string;
    value: string;
    onChange: (value: string) => void;
    required?: boolean;
}

export default function DateTimePicker({
                                           label,
                                           value,
                                           onChange,
                                           required = false
                                       }: DateTimePickerProps) {
    return (
        <div className="mt-5">
            <label className="mb-2 block text-sm font-medium text-slate-300">
                {label}
            </label>

            <input
                type="datetime-local"
                value={value}
                onChange={(e) => onChange(e.target.value)}
                required={required}
                className="w-full rounded-xl border border-slate-700 bg-slate-900 px-4 py-3 text-white outline-none focus:border-blue-500 focus:ring-2 focus:ring-blue-500/20"
            />
        </div>
    );
}
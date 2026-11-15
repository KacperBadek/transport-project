interface ErrorProps {
    message?: string;
}

export default function Error({message}: ErrorProps) {
    return (
        <div className="mt-6 rounded-xl border border-red-500/30 bg-red-500/10 p-4 text-sm text-red-400">
            {message}
        </div>
    );
}
import Error from "../Error.tsx";

interface JourneyAnalysisProps {
    onAnalyze: () => void;
    isLoading: boolean;
    analysis: string | undefined;
    isError: boolean;
}

export default function JourneyAnalysis({
                                            onAnalyze,
                                            isLoading,
                                            analysis,
                                            isError,
                                        }: JourneyAnalysisProps) {
    return (
        <section className="mt-6">
            <button
                type="button"
                onClick={onAnalyze}
                disabled={isLoading || analysis !== undefined}
                className="flex w-full items-center justify-center gap-3 rounded-xl border border-purple-500/30 bg-purple-600/10 px-6 py-4 font-semibold text-purple-300 transition hover:border-purple-400/50 hover:bg-purple-600/20 disabled:cursor-not-allowed disabled:opacity-50"
            >

                {isLoading
                    ? "AI is analyzing routes... (may take a while)"
                    : "Analyze routes with AI"}
            </button>

            {isLoading && (
                <div className="mt-4 rounded-xl border border-purple-500/20 bg-purple-500/5 p-5">
                    <div className="flex items-center gap-3">
                        <div
                            className="h-5 w-5 animate-spin rounded-full border-2 border-purple-400 border-t-transparent"/>

                        <p className="text-sm text-purple-300">
                            AI is comparing available connections...
                        </p>
                    </div>
                </div>
            )}

            {isError && (
                <Error message={"AI analysis failed. Please try again."}/>
            )}

            {analysis && !isLoading && (
                <div
                    className="mt-4 overflow-hidden rounded-2xl border border-purple-500/30 bg-slate-900 shadow-lg shadow-purple-950/10">
                    <div className="flex items-center gap-3 border-b border-purple-500/20 bg-purple-500/5 px-5 py-4">
                        <div>
                            <h3 className="font-semibold text-white">
                                AI Journey Recommendation
                            </h3>

                            <p className="text-xs text-purple-300">
                                Personalized route analysis
                            </p>
                        </div>
                    </div>

                    <div className="p-5">
                        <p className="whitespace-pre-line text-sm leading-7 text-slate-300">
                            {analysis}
                        </p>
                    </div>
                </div>
            )}
        </section>
    );
}
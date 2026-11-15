package com.example.backend.services;

import com.example.backend.model.dtos.journey.JourneyOptionDto;
import com.example.backend.model.dtos.journey.JourneyScoredOptionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JourneyAnalysisAgentService {

    private final ChatClient chatClient;
    private final JourneyScoringService journeyScoringService;
    private final ObjectMapper objectMapper;

    public String analyzeJourneys(
            List<JourneyOptionDto> journeys
    ) {
        List<JourneyScoredOptionDto> scoredJourneys =
                journeyScoringService.scoreJourneys(journeys);

        String journeysJson = convertToJson(scoredJourneys);

        return generateAiAnalysis(journeysJson);
    }

    private String generateAiAnalysis(String journeysJson) {
        String systemPrompt = """
                You are an assistant helping users choose the best
                public transportation journey options.
                                
                You receive a list of journey alternatives along with
                scores calculated by an external algorithm.
                                
                Your tasks:
                1. Compare the available journey alternatives.
                2. Identify the best alternative based on overallScore.
                3. Explain why this alternative is the best choice.
                4. Highlight the key advantages and disadvantages of the remaining alternatives.
                5. Take into account travel time, number of transfers, transfer durations, and time spent having to walk.
                                
                Rules:
                - Do not modify the scores or criterion weights.
                - Do not recalculate overallScore.
                - Do not make up information that is not present in the data.
                - If multiple alternatives have the same score, explain the differences between them.
                - Keep your response concise and easy for passengers to understand.
                - Always prioritize concrete, measurable differences between journey alternatives over generic statements about overall scores or balance.
                - Explicitly state which journey has the shortest total travel time, the fewest transfers, 
                  the shortest total transfer time, and the least walking time, whenever these differences are relevant.
                - Support every claimed advantage or disadvantage with actual values from the input data.
                - Quantify time differences between alternatives whenever possible (e.g. "13 minutes faster than Journey 1").
                - Never use vague phrases such as: "best overall balance", "optimal balance", "most efficient and comfortable option", "highest overall performance"
                  unless they are immediately followed by specific evidence from the data.
                - Do not treat a higher overallScore as proof that a journey is better in every individual criterion.  
                - Don't use emojis.
                - Don't ask if user needs additional help or details.
                - End your response with words: "Safe travels!".
                                
                Return your response using the following structure:
                                
                Recommended journey:
                [alternative number and justification]
                                
                Why choose this journey:
                [brief explanation]
                """;

        return chatClient.prompt()
                .system(systemPrompt)
                .user("""
                        Compare the following journey alternatives.
                        JSON data:

                        %s
                        """.formatted(journeysJson))
                .call()
                .content();
    }

    private String convertToJson(
            List<JourneyScoredOptionDto> journeys
    ) {
        try {
            return objectMapper.writeValueAsString(journeys);
        } catch (JacksonException exception) {
            throw new IllegalStateException(
                    "Could not serialize scored journeys.",
                    exception
            );
        }
    }

}

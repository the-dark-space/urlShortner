package com.project.urlShortner.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class UrlSafetyAnalyzerService {

    private final WebClient webClient;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public boolean isSafeUrl(String url) {

        try {

            String prompt = """
                Analyze this URL for phishing,
                scams, or malicious intent.

                Return ONLY:
                SAFE
                or
                MALICIOUS

                URL:
                """ + url;

            String requestBody = """
                {
                  "contents": [{
                    "parts": [{
                      "text": "%s"
                    }]
                  }]
                }
                """.formatted(prompt);

            String response = webClient.post()
                    .uri(
                            "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key="
                                    + geminiApiKey
                    )
                    .header(
                            "Content-Type",
                            "application/json"
                    )
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return response != null
                    &&
                    response.contains("SAFE");

        } catch (Exception ex) {

            System.out.println(
                    "AI service unavailable: "
                            + ex.getMessage()
            );

            return true;
        }
    }
}

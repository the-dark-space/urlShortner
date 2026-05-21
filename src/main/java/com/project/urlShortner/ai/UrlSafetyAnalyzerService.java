package com.project.urlShortner.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class UrlSafetyAnalyzerService {

    private final WebClient webClient;

    @Value("${openrouter.api.key}")
    private String openRouterApiKey;

    public boolean isSafeUrl(String url) {

        try {

            String prompt = """
                    Analyze this URL for phishing,
                    scams, malicious intent,
                    fake login pages,
                    banking fraud,
                    crypto scams,
                    or suspicious behavior.

                    Return ONLY:
                    SAFE
                    or
                    MALICIOUS

                    URL:
                    """ + url;

            String requestBody = """
                    {
                      "model": "openrouter/free",
                      "messages": [
                        {
                          "role": "user",
                          "content": "%s"
                        }
                      ]
                    }
                    """.formatted(
                    prompt.replace("\"", "\\\"")
            );
            String response = webClient.post()
                    .uri(
                            "https://openrouter.ai/api/v1/chat/completions"
                    )
                    .header(
                            "Authorization",
                            "Bearer " + openRouterApiKey
                    )
                    .header(
                            "Content-Type",
                            "application/json"
                    )
                    .header(
                            "HTTP-Referer",
                            "http://localhost:8080"
                    )
                    .header(
                            "X-Title",
                            "Distributed URL Shortener"
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

            String lowerUrl = url.toLowerCase();

            return !(
                    lowerUrl.contains("login")
                            ||
                            lowerUrl.contains("verify")
                            ||
                            lowerUrl.contains("bank")
                            ||
                            lowerUrl.contains("kyc")
                            ||
                            lowerUrl.contains("reward")
                            ||
                            lowerUrl.contains("gift")
                            ||
                            lowerUrl.contains("crypto")
                            ||
                            lowerUrl.contains("wallet")
                            ||
                            lowerUrl.contains("upi")
            );
        }
    }
}
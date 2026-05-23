package com.project.urlShortner.kafka;

import com.project.urlShortner.repository
        .ShortUrlRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.kafka.annotation
        .KafkaListener;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyticsConsumer {

    private final ShortUrlRepository
            shortUrlRepository;

    @KafkaListener(
            topics = "url-analytics",
            groupId = "analytics-group"
    )
    public void consume(
            AnalyticsEvent event
    ) {

        shortUrlRepository
                .incrementClickCount(
                        event.getShortCode()
                );

        System.out.println(
                "Analytics updated for: "
                        + event.getShortCode()
        );
    }
}
package com.project.urlShortner.kafka;

import lombok.RequiredArgsConstructor;

import org.springframework.kafka.core.KafkaTemplate;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyticsProducer {

    private final KafkaTemplate<String, Object>
            kafkaTemplate;

    private static final String TOPIC =
            "url-analytics";

    public void publishEvent(
            String shortCode
    ) {

        AnalyticsEvent event =
                AnalyticsEvent.builder()
                        .shortCode(shortCode)
                        .build();

        kafkaTemplate.send(
                TOPIC,
                event
        );
    }
}
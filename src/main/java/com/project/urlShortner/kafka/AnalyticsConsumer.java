package com.project.urlShortner.kafka;

import com.project.urlShortner.entity.UrlDailyAnalytics;
import com.project.urlShortner.repository
        .ShortUrlRepository;

import com.project.urlShortner.repository.UrlDailyAnalyticsRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.kafka.annotation
        .KafkaListener;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AnalyticsConsumer {

    private final ShortUrlRepository
            shortUrlRepository;
    private final UrlDailyAnalyticsRepository
            analyticsRepository;

    @KafkaListener(
            topics = "url-analytics",
            groupId = "analytics-group"
    )
    public void consume(
            AnalyticsEvent event
    ) {

        int updatedRows =
                shortUrlRepository
                        .incrementClickCount(
                                event.getShortCode()
                        );

        if (updatedRows == 0) {

            System.out.println(
                    "Short URL not found: "
                            + event.getShortCode()
            );

            return;
        }

        System.out.println(
                "Analytics updated for: "
                        + event.getShortCode()
        );
        LocalDate today =
                LocalDate.now();

        UrlDailyAnalytics analytics =

                analyticsRepository
                        .findByShortCodeAndDate(
                                event.getShortCode(),
                                today
                        )
                        .orElse(

                                UrlDailyAnalytics.builder()
                                        .shortCode(
                                                event.getShortCode()
                                        )
                                        .date(today)
                                        .clicks(0L)
                                        .build()
                        );

        analytics.setClicks(
                analytics.getClicks() + 1
        );

        analyticsRepository.save(analytics);
    }
}
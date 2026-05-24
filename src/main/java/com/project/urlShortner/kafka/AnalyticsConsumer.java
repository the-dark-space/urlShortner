package com.project.urlShortner.kafka;

import com.project.urlShortner.entity.UrlDailyAnalytics;
import com.project.urlShortner.repository
        .ShortUrlRepository;

import com.project.urlShortner.repository.UrlDailyAnalyticsRepository;
import com.project.urlShortner.service.UrlShortenerService;
import lombok.RequiredArgsConstructor;

import org.springframework.kafka.annotation
        .KafkaListener;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class AnalyticsConsumer {

    private final ShortUrlRepository
            shortUrlRepository;
    private final UrlShortenerService urlShortenerService;
    private final UrlDailyAnalyticsRepository
            analyticsRepository;
    private final ConcurrentHashMap<
                String,
                Long
                > clickBuffer =
            new ConcurrentHashMap<>();
    @KafkaListener(
            topics = "url-analytics",
            groupId = "analytics-group"
    )
    public void consume(
            AnalyticsEvent event
    ) {
        clickBuffer.merge(
                event.getShortCode(),
                1L,
                Long::sum
        );
    }
    @Scheduled(
            fixedRate = 5000
    )
    public void flushAnalytics() {

        if(clickBuffer.isEmpty()) {
            return;
        }

        System.out.println(
                "Flushing analytics batch..."
        );

        Map<String, Long> snapshot =
                new HashMap<>(clickBuffer);

        clickBuffer.clear();

        snapshot.forEach(
                (shortCode, clicks) -> {

                    shortUrlRepository
                            .incrementClickCountBy(
                                    shortCode,
                                    clicks
                            );

                    updateDailyAnalytics(
                            shortCode,
                            clicks
                    );
                }
        );
    }
    private void updateDailyAnalytics(
            String shortCode,
            Long clicks
    ) {

        LocalDate today =
                LocalDate.now();

        UrlDailyAnalytics analytics =

                analyticsRepository
                        .findByShortCodeAndDate(
                                shortCode,
                                today
                        )

                        .orElse(

                                UrlDailyAnalytics
                                        .builder()

                                        .shortCode(shortCode)

                                        .date(today)

                                        .clicks(0L)

                                        .build()
                        );

        analytics.setClicks(
                analytics.getClicks() + clicks
        );

        analyticsRepository.save(analytics);
    }
}
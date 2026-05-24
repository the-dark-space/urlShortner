package com.project.urlShortner.repository;

import com.project.urlShortner.entity
        .UrlDailyAnalytics;

import org.springframework.data.jpa.repository
        .JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UrlDailyAnalyticsRepository
        extends JpaRepository<
        UrlDailyAnalytics,
        Long
        > {

    Optional<UrlDailyAnalytics>
    findByShortCodeAndDate(
            String shortCode,
            LocalDate date
    );
    List<UrlDailyAnalytics>
    findByShortCodeOrderByDateAsc(
            String shortCode
    );
}
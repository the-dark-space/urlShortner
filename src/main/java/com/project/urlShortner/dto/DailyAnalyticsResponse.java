package com.project.urlShortner.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class DailyAnalyticsResponse {

    private LocalDate date;

    private Long clicks;
}
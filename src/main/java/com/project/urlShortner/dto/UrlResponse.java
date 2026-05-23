package com.project.urlShortner.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UrlResponse {

    private String shortCode;

    private String originalUrl;

    private Long clickCount;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;
}
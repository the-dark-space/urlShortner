package com.project.urlShortner.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ShortenUrlResponse {

    private String shortUrl;
}

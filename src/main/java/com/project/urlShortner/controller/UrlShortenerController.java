package com.project.urlShortner.controller;

import com.project.urlShortner.dto.ShortenUrlRequest;
import com.project.urlShortner.dto.ShortenUrlResponse;
import com.project.urlShortner.service.UrlShortenerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/urls")
public class UrlShortenerController {

    private final UrlShortenerService urlShortenerService;

    @PostMapping
    public ShortenUrlResponse createShortUrl(
            @Valid @RequestBody ShortenUrlRequest request
    ) {

        return urlShortenerService.createShortUrl(request);
    }
}

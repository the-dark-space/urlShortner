package com.project.urlShortner.controller;

import com.project.urlShortner.cache.RedirectCacheData;
import com.project.urlShortner.exception.ShortUrlExpiredException;
import com.project.urlShortner.model.ShortUrl;
import com.project.urlShortner.service.UrlShortenerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class RedirectController {

    private final UrlShortenerService urlShortenerService;

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirectToOriginalUrl(
            @PathVariable String shortCode
    ) {

        RedirectCacheData redirectData =
                urlShortenerService
                        .getRedirectData(shortCode);

        if (
                redirectData.getExpiresAt() != null
                        &&
                        redirectData.getExpiresAt()
                                .isBefore(LocalDateTime.now())
        ) {

            throw new ShortUrlExpiredException(
                    "Short URL has expired"
            );
        }

        urlShortenerService.incrementClickCount(shortCode);

        HttpHeaders headers = new HttpHeaders();

        headers.add(
                "Location",
                redirectData.getOriginalUrl()
        );

        headers.add(
                "Cache-Control",
                "no-store, no-cache, must-revalidate"
        );

        headers.add("Pragma", "no-cache");

        headers.add("Expires", "0");

        return new ResponseEntity<>(
                headers,
                HttpStatus.TEMPORARY_REDIRECT
        );
    }
}

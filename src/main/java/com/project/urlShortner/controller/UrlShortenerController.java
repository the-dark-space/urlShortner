package com.project.urlShortner.controller;

import com.project.urlShortner.dto.ShortenUrlRequest;
import com.project.urlShortner.dto.ShortenUrlResponse;
import com.project.urlShortner.dto.UpdateExpiryRequest;
import com.project.urlShortner.dto.UrlResponse;
import com.project.urlShortner.model.ShortUrl;
import com.project.urlShortner.service.UrlShortenerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/my-urls")
    public ResponseEntity<List<UrlResponse>>
    getMyUrls() {

        return ResponseEntity.ok(
                urlShortenerService.getMyUrls()
        );
    }

    @DeleteMapping("/{shortCode}")
    public ResponseEntity<String> deleteUrl(
            @PathVariable String shortCode
    ) {

        urlShortenerService.deleteUrl(shortCode);

        return ResponseEntity.ok(
                "URL deleted successfully"
        );
    }

    @PutMapping("/{shortCode}/expiry")
    public ResponseEntity<String> updateExpiry(
            @PathVariable String shortCode,

            @RequestBody
            UpdateExpiryRequest request
    ) {

        urlShortenerService.updateExpiry(
                shortCode,
                request.getExpiryDays()
        );

        return ResponseEntity.ok(
                "Expiry updated successfully"
        );
    }
}

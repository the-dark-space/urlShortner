package com.project.urlShortner.service;

import com.project.urlShortner.dto.ShortenUrlRequest;
import com.project.urlShortner.dto.ShortenUrlResponse;
import com.project.urlShortner.model.ShortUrl;
import com.project.urlShortner.repository.ShortUrlRepository;
import com.project.urlShortner.util.ShortCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UrlShortenerService {

    private final ShortUrlRepository shortUrlRepository;

    public ShortenUrlResponse createShortUrl(ShortenUrlRequest request) {

        String shortCode = ShortCodeGenerator.generateShortCode();

        ShortUrl shortUrl = ShortUrl.builder()
                .originalUrl(request.getUrl())
                .shortCode(shortCode)
                .createdAt(LocalDateTime.now())
                .clickCount(0L)
                .build();

        shortUrlRepository.save(shortUrl);

        return ShortenUrlResponse.builder()
                .shortUrl("http://localhost:8080/" + shortCode)
                .build();
    }
}

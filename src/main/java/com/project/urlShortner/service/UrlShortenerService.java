package com.project.urlShortner.service;

import com.project.urlShortner.cache.RedirectCacheData;
import org.springframework.cache.annotation.Cacheable;
import com.project.urlShortner.dto.ShortenUrlRequest;
import com.project.urlShortner.dto.ShortenUrlResponse;
import com.project.urlShortner.exception.CustomAliasAlreadyExistsException;
import com.project.urlShortner.exception.ShortUrlExpiredException;
import com.project.urlShortner.exception.ShortUrlNotFoundException;
import com.project.urlShortner.model.ShortUrl;
import com.project.urlShortner.repository.ShortUrlRepository;
import com.project.urlShortner.util.ShortCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UrlShortenerService {

    private final ShortUrlRepository shortUrlRepository;

    public ShortenUrlResponse createShortUrl(
            ShortenUrlRequest request
    ) {

        String shortCode;

        if (
                request.getCustomAlias() != null
                        &&
                        !request.getCustomAlias().isBlank()
        ) {

            boolean aliasExists =
                    shortUrlRepository
                            .findByShortCode(
                                    request.getCustomAlias()
                            )
                            .isPresent();

            if (aliasExists) {

                throw new CustomAliasAlreadyExistsException(
                        "Custom alias already exists"
                );
            }

            shortCode = request.getCustomAlias();

        } else {

            do {

                shortCode =
                        ShortCodeGenerator.generateShortCode();

            } while (
                    shortUrlRepository
                            .findByShortCode(shortCode)
                            .isPresent()
            );
        }

        ShortUrl shortUrl = ShortUrl.builder()
                .originalUrl(request.getUrl())
                .shortCode(shortCode)
                .createdAt(LocalDateTime.now())
                .expiresAt(
                        request.getExpiryDays() != null
                                ? LocalDateTime.now()
                                .plusDays(request.getExpiryDays())
                                : null
                )
                .clickCount(0L)
                .build();

        shortUrlRepository.save(shortUrl);

        return ShortenUrlResponse.builder()
                .shortUrl(
                        "http://localhost:8080/" + shortCode
                )
                .build();
    }


    @Cacheable(value = "originalUrls", key = "#shortCode")
    public RedirectCacheData getRedirectData(
            String shortCode
    ) {

        ShortUrl shortUrl =
                shortUrlRepository.findByShortCode(shortCode)
                        .orElseThrow(() ->
                                new ShortUrlNotFoundException(
                                        "Short URL not found"
                                )
                        );

        return RedirectCacheData.builder()
                .originalUrl(shortUrl.getOriginalUrl())
                .expiresAt(shortUrl.getExpiresAt())
                .build();
    }

    public void incrementClickCount(String shortCode) {
        shortUrlRepository.incrementClickCount(shortCode);
    }
}

package com.project.urlShortner.service;

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

        Optional<ShortUrl> existingShortUrl =
                shortUrlRepository.findByOriginalUrl(
                        request.getUrl()
                );

        if (existingShortUrl.isPresent()) {

            return ShortenUrlResponse.builder()
                    .shortUrl(
                            "http://localhost:8080/"
                                    + existingShortUrl.get().getShortCode()
                    )
                    .build();
        }

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

            shortCode =
                    ShortCodeGenerator.generateShortCode();
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

    public String getOriginalUrl(String shortCode) {

        ShortUrl shortUrl = shortUrlRepository.findByShortCode(shortCode)
                .orElseThrow(() ->
                        new ShortUrlNotFoundException("Short URL not found"));

        if (
                shortUrl.getExpiresAt() != null
                        &&
                        shortUrl.getExpiresAt().isBefore(LocalDateTime.now())
        ) {
            throw new ShortUrlExpiredException(
                    "Short URL has expired"
            );
        }

        shortUrl.setClickCount(shortUrl.getClickCount() + 1);

        shortUrlRepository.save(shortUrl);

        return shortUrl.getOriginalUrl();
    }
}

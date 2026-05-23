package com.project.urlShortner.service;

import com.project.urlShortner.ai.UrlSafetyAnalyzerService;
import com.project.urlShortner.cache.RedirectCacheData;
import com.project.urlShortner.dto.UrlResponse;
import com.project.urlShortner.exception.*;
import com.project.urlShortner.model.User;
import com.project.urlShortner.repository.UserRepository;
import com.project.urlShortner.util.AuthUtil;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import com.project.urlShortner.dto.ShortenUrlRequest;
import com.project.urlShortner.dto.ShortenUrlResponse;
import com.project.urlShortner.model.ShortUrl;
import com.project.urlShortner.repository.ShortUrlRepository;
import com.project.urlShortner.util.ShortCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UrlShortenerService {

    private final ShortUrlRepository shortUrlRepository;
    private final UrlSafetyAnalyzerService
            urlSafetyAnalyzerService;
    private final UserRepository userRepository;

    public ShortenUrlResponse createShortUrl(
            ShortenUrlRequest request
    ) {
        boolean isSafe =
                urlSafetyAnalyzerService
                        .isSafeUrl(request.getUrl());

        if (!isSafe) {
            throw new MaliciousUrlException(
                    "Potential malicious URL detected"
            );
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

        String email =
                AuthUtil.getCurrentUserEmail();

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow();
        shortUrl.setUser(user);
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

    public void incrementClickCount(
            String shortCode
    ) {

        int updatedRows =
                shortUrlRepository
                        .incrementClickCount(shortCode);

        if (updatedRows == 0) {

            throw new ShortUrlNotFoundException(
                    "Short URL not found"
            );
        }
    }

    public List<UrlResponse> getMyUrls() {

        String email =
                AuthUtil.getCurrentUserEmail();

        List<ShortUrl> urls =
                shortUrlRepository
                        .findByUserEmail(email);

        return urls.stream()
                .map(shortUrl ->

                        UrlResponse.builder()
                                .shortCode(
                                        shortUrl.getShortCode()
                                )
                                .originalUrl(
                                        shortUrl.getOriginalUrl()
                                )
                                .clickCount(
                                        shortUrl.getClickCount()
                                )
                                .createdAt(
                                        shortUrl.getCreatedAt()
                                )
                                .expiresAt(
                                        shortUrl.getExpiresAt()
                                )
                                .build()
                )
                .toList();
    }

    private ShortUrl validateOwnership(
            String shortCode
    ) {

        ShortUrl shortUrl =
                shortUrlRepository
                        .findByShortCode(shortCode)
                        .orElseThrow(() ->
                                new ShortUrlNotFoundException(
                                        "Short URL not found"
                                )
                        );

        String currentUserEmail =
                AuthUtil.getCurrentUserEmail();

        if (
                shortUrl.getUser() == null
                        ||
                        !shortUrl.getUser()
                                .getEmail()
                                .equals(currentUserEmail)
        ) {

            throw new UnauthorizedUrlAccessException(
                    "You are not authorized to access this URL"
            );
        }

        return shortUrl;
    }
    @CacheEvict(
            value = "redirectData",
            key = "#shortCode"
    )
    public void deleteUrl(
            String shortCode
    ) {

        ShortUrl shortUrl =
                validateOwnership(shortCode);

        shortUrlRepository.delete(shortUrl);
    }
    @CacheEvict(
            value = "redirectData",
            key = "#shortCode"
    )
    public void updateExpiry(
            String shortCode,
            Integer expiryDays
    ) {

        ShortUrl shortUrl =
                validateOwnership(shortCode);

        shortUrl.setExpiresAt(
                LocalDateTime.now()
                        .plusDays(expiryDays)
        );

        shortUrlRepository.save(shortUrl);
    }

}

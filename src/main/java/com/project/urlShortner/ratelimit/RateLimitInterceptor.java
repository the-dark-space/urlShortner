package com.project.urlShortner.ratelimit;

import com.project.urlShortner.exception
        .RateLimitExceededException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class RateLimitInterceptor
        implements HandlerInterceptor {

    private final RateLimitService
            rateLimitService;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {

        String ip =
                request.getRemoteAddr();

        boolean allowed =
                rateLimitService
                        .isAllowed(ip);

        if (!allowed) {

            throw new RateLimitExceededException(
                    "Too many requests"
            );
        }

        return true;
    }
}

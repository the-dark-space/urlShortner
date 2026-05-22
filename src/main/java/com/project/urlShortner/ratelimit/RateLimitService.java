package com.project.urlShortner.ratelimit;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RateLimitService {

    private final StringRedisTemplate
            redisTemplate;

    private static final long MAX_REQUESTS = 10;

    public boolean isAllowed(String ip) {

        String key =
                "rate_limit:" + ip;

        Long currentCount =
                redisTemplate.opsForValue()
                        .increment(key);

        if (currentCount != null
                &&
                currentCount == 1) {

            redisTemplate.expire(
                    key,
                    Duration.ofMinutes(2)
            );
        }

        return currentCount != null
                &&
                currentCount <= MAX_REQUESTS;
    }
}

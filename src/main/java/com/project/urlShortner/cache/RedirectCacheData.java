package com.project.urlShortner.cache;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RedirectCacheData
        implements Serializable {

    private String originalUrl;

    private LocalDateTime expiresAt;
}

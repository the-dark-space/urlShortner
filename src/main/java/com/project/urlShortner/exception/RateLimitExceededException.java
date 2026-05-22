package com.project.urlShortner.exception;

public class RateLimitExceededException
        extends RuntimeException {

    public RateLimitExceededException(
            String message
    ) {
        super(message);
    }
}

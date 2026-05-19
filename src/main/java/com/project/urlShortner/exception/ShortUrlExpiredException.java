package com.project.urlShortner.exception;

public class ShortUrlExpiredException
        extends RuntimeException {

    public ShortUrlExpiredException(String message) {
        super(message);
    }
}

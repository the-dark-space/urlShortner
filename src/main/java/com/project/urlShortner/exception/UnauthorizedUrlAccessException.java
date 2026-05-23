package com.project.urlShortner.exception;

public class UnauthorizedUrlAccessException
        extends RuntimeException {

    public UnauthorizedUrlAccessException(
            String message
    ) {
        super(message);
    }
}
package com.project.urlShortner.exception;

public class MaliciousUrlException
        extends RuntimeException {

    public MaliciousUrlException(String message) {
        super(message);
    }
}

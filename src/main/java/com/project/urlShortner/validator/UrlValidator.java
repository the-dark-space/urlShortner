package com.project.urlShortner.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.net.URI;

public class UrlValidator
        implements ConstraintValidator<ValidUrl, String> {

    @Override
    public boolean isValid(
            String url,
            ConstraintValidatorContext context
    ) {

        if (url == null || url.isBlank()) {
            return false;
        }

        try {

            URI uri = new URI(url);

            return uri.getScheme() != null
                    && (
                    uri.getScheme().equals("http")
                            || uri.getScheme().equals("https")
            );

        } catch (Exception ex) {

            return false;
        }
    }
}

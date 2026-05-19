package com.project.urlShortner.dto;

import com.project.urlShortner.validator.ValidUrl;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShortenUrlRequest {

    @ValidUrl
    private String url;

    private Integer expiryDays;
}

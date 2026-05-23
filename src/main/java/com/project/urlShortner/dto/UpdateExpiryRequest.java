package com.project.urlShortner.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateExpiryRequest {

    private Integer expiryDays;
}
package com.muammer.adybis.help.request.dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HelpRequestRequest {
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String description;
}

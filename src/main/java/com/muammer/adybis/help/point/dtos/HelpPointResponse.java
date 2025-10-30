package com.muammer.adybis.help.point.dtos;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HelpPointResponse {
    private UUID id;
    private String name;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String description;
    private String type;
    private boolean actived;
}

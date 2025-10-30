package com.muammer.adybis.help.request.dtos;

import java.math.BigDecimal;
import java.util.UUID;

import com.muammer.adybis.common.enums.StatusType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HelpRequestDetailRequest {
    private UUID victimId;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String status = StatusType.PND.label;
    private String description;
}

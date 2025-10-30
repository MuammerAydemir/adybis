package com.muammer.adybis.help.point.dtos;

import java.math.BigDecimal;

import com.muammer.adybis.help.enums.HelpPointType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HelpPointRequest {
    private String name;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String description;
    private String type = HelpPointType.TN.label;
    private boolean actived = true;
}

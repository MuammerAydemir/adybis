package com.muammer.adybis.rescue.team.dtos;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RescueTeamLocationRequest {
    private UUID rescueTeamId;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String status;
}

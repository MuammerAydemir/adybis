package com.muammer.adybis.rescue.team.dtos;

import java.math.BigDecimal;

import com.muammer.adybis.user.dtos.UserResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RescueTeamLocationResponse {
    private UserResponse rescueTeam;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String status;
}

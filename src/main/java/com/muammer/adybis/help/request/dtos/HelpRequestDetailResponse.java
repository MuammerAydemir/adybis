package com.muammer.adybis.help.request.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.muammer.adybis.help.point.dtos.HelpPointResponse;
import com.muammer.adybis.rescue.team.dtos.RescueTeamLocationResponse;
import com.muammer.adybis.user.dtos.UserResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HelpRequestDetailResponse {
    private UUID id;
    private UserResponse victim;
    private HelpPointResponse nearestHelpPoint;
    private RescueTeamLocationResponse nearestRescueTeam;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String status;
    private String description;
    private LocalDateTime createdAt;

    public void setNearestHelpPoint(HelpPointResponse nearestHelpPoint) {
        this.nearestHelpPoint = nearestHelpPoint;
    }

    public void setNearestRescueTeam(RescueTeamLocationResponse nearestRescueTeam) {
        this.nearestRescueTeam = nearestRescueTeam;
    }
}

package com.muammer.adybis.help.request.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.muammer.adybis.help.point.dtos.HelpPointResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VictimHelpRequestResponse {

    private UUID id;
    private HelpPointResponse nearestHelpPoint;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String status;
    private String description;
    private LocalDateTime createdAt;

    public void setNearestHelpPoint(HelpPointResponse nearestHelpPoint) {
        this.nearestHelpPoint = nearestHelpPoint;
    }

}

package com.muammer.adybis.rescue.assignment.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

import com.muammer.adybis.common.enums.StatusType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RescueAssignmentDetailRequest {
    private UUID helpRequestId;
    private UUID rescueTeamId;
    private UUID createdBy;
    private LocalDateTime assignedAt = LocalDateTime.now();
    private String status = StatusType.AS.label;
    private LocalDateTime complatedAt;
}

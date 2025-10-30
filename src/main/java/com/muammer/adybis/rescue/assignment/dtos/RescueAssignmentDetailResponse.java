package com.muammer.adybis.rescue.assignment.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

import com.muammer.adybis.help.request.dtos.HelpRequestResponse;
import com.muammer.adybis.user.dtos.UserResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RescueAssignmentDetailResponse {
    private UUID id;
    private HelpRequestResponse helpRequest;
    private UserResponse rescueTeam;
    private UserResponse createdBy;
    private LocalDateTime assignedAt;
    private String status;
    private LocalDateTime complatedAt;
}
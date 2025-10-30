package com.muammer.adybis.rescue.assignment.dtos;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RescueAssignmentRequest {
    private UUID helpRequestId;
    private UUID rescueTeamId;
}

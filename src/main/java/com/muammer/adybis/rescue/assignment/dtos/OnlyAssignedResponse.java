package com.muammer.adybis.rescue.assignment.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

import com.muammer.adybis.help.request.dtos.HelpRequestResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OnlyAssignedResponse {
    private UUID id;
    private HelpRequestResponse helpRequest;
    private LocalDateTime assignedAt;
    private String status;
    private LocalDateTime complatedAt;
}

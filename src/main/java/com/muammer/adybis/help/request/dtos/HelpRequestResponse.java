package com.muammer.adybis.help.request.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.muammer.adybis.user.dtos.UserResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HelpRequestResponse {
    private UUID id;
    private UserResponse victim;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String status;
    private String description;
    private LocalDateTime createdAt;

}

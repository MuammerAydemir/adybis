package com.muammer.adybis.help.point.dtos;

import java.math.BigDecimal;
import java.util.UUID;

import com.muammer.adybis.user.dtos.UserResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HelpPointAdminResponse {
    private UUID id;
    private UserResponse createdBy;
    private String name;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String description;
    private String type;
    private boolean actived;
}

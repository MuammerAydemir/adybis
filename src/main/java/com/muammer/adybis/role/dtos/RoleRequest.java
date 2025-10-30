package com.muammer.adybis.role.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoleRequest {
    private String name;
    private String description;
    private LocalDateTime createdAt;
}

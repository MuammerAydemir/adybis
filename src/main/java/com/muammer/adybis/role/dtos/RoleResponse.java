package com.muammer.adybis.role.dtos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.muammer.adybis.user.dtos.UserResponse;

public record RoleResponse(UUID id, String name, String description, LocalDateTime createdAt,
        List<UserResponse> users) {
}

package com.muammer.adybis.role.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record RoleBasicResponse(UUID id, String name, String description, LocalDateTime createdAt) {

}

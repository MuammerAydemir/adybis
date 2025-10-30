package com.muammer.adybis.user.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.muammer.adybis.role.dtos.RoleBasicResponse;

public record UserDetailResponse(UUID id, String username, String email, String phone,
                LocalDate birthdayDate, String bloodType, boolean chronicIllnesses,
                boolean peopleWithDisabilities, String specialCaseDescription,
                LocalDateTime updatedAt, LocalDateTime createdAt, boolean enabled, LocalDate passwordExpirationDate,
                boolean twoFAEnabled, boolean verified, List<RoleBasicResponse> roles) {

}

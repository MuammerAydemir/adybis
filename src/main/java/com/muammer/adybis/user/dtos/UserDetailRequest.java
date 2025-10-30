package com.muammer.adybis.user.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDetailRequest {
    private String username;
    private List<UUID> roleIds;
    private String email;

    @Pattern(regexp = "^([+]?\\d{1,2}[-\\s]?|)\\d{3}[-\\s]?\\d{3}[-\\s]?\\d{4}$", message = "Phone number is invalid!")
    private String phone;
    private String password;
    private LocalDate birthdayDate;
    private String bloodType;
    private boolean chronicIllnesses = false;
    private boolean peopleWithDisabilities = false;
    private String specialCaseDescription;
    private LocalDateTime updatedAt = LocalDateTime.now();
    private LocalDateTime createdAt = LocalDateTime.now();
    private boolean enabled = true;
    private LocalDate passwordExpirationDate = LocalDate.now().plusMonths(3);
    private boolean twoFAEnabled = false;
    private boolean verified = false;

    public void setRoleIds(List<UUID> roleIds) {
        this.roleIds = roleIds;
    }
}

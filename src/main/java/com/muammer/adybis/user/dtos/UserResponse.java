package com.muammer.adybis.user.dtos;

import java.time.LocalDate;
import java.util.UUID;

public record UserResponse(UUID id, String username, String email, String phone, LocalDate birthdayDate,
        String bloodType,
        boolean chronicIllnesses, boolean peopleWithDisabilities, String specialCaseDescription) {

}

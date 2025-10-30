package com.muammer.adybis.user.dtos;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserRequest {
    private LocalDate birthdayDate;
    private String bloodType;
    private boolean chronicIllnesses;
    private boolean peopleWithDisabilities;
    private String specialCaseDescription;
}

package com.muammer.adybis.base.auth.dtos;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterRequest {
    private String username;
    private String email;
    private String phone;
    private String password;
    private LocalDate birthdayDate;
    private String bloodType;
}

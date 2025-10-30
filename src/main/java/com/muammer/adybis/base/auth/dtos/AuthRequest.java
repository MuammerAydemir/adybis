package com.muammer.adybis.base.auth.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthRequest {
    private String username;
    private String password;
}

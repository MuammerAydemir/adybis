package com.muammer.adybis.base.auth.dtos;

import java.util.UUID;

import lombok.Data;

@Data
public class TwoFactorRequest {
    private UUID userId;
    private String code;
}

package com.muammer.adybis.rescue.enums;

public enum RescueTeamStatus {
    AC("active"), DA("deactive");

    public final String label;

    RescueTeamStatus(String label) {
        this.label = label;
    }
}

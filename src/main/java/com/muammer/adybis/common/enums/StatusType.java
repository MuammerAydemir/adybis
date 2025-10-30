package com.muammer.adybis.common.enums;

public enum StatusType {
    PND("pending"), AS("assigned"), IP("in_progress"), CP("completed");

    public final String label;

    StatusType(String label) {
        this.label = label.toLowerCase();
    }
}

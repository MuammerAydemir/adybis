package com.muammer.adybis.user.enums;

public enum BloodTypesEnums {
    A_POZITIVE("a_positive"),
    B_POZITIVE("b_positive"),
    AB_POZITIVE("ab_positive"),
    Zero_POZITIVE("zero_positive"),
    A_NEGATIVE("a_negative"),
    B_NEGATIVE("b_negative"),
    AB_NEGATIVE("ab_negative"),
    ZERO_NEGATIVE("zero_negative");

    public final String label;

    BloodTypesEnums(String label) {
        this.label = label.toLowerCase();
    }

    public String getLabel() {
        return label;
    }
}

package com.muammer.adybis.help.enums;

public enum HelpPointType {
    TN("tent"),
    GA("gathering_area"),
    MBH("mobile_health");

    public final String label;

    HelpPointType(String label) {
        this.label = label.toLowerCase();
    }
}

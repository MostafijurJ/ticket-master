package com.learn.ms.event.core.domain.enums;

public enum FeatureCode {

    EVENT_CREATION("1101", "Create event"),
    EVENT_UPDATE("1102", "Update event"),
    EVENT_DELETE("1103", "Delete event"),

    BOOKING_EVENT("1110", "Book event"),
    CANCEL_BOOKING("1111", "Cancel booking"),
    ;

    private final String code;
    private final String name;

    FeatureCode(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}

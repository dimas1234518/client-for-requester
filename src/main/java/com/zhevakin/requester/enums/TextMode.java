package com.zhevakin.requester.enums;

public enum TextMode {

    NONE("NONE", 1),
    JSON("JSON", 2),
    XML("XML", 3),
    TEXT("TEXT", 4);

    private final String name;
    private final int value;

    TextMode(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return name;
    }

}

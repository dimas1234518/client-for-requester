package com.zhevakin.requester.enums;

public enum TypeAuthorization {

    NO_AUTH("No Auth", 1),
    FROM_PARENT("From parent", 2),
    BEARER_TOKEN("Bearer ", 3),
    API_KEY("API Key", 4),
    BASIC_AUTH("Basic ", 5);

    final private String name;
    final private int value;

    TypeAuthorization(String name, int value) {
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

package com.zhevakin.requester.enums;

public enum TypeRequest {

    COLLECTIONS("COLLECTIONS", 1),
    FOLDER("FOLDER", 2),
    REQUEST("REQUEST", 3);

    final private String name;
    final private int value;

    TypeRequest(String name, int value) {
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

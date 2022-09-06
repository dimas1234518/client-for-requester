package com.zhevakin.requester.enums;

public enum TypeHeaders {

    CONTENT_TYPE("Content-type", 1),
    CONTENT_LENGTH("Content-length", 2),
    HOST("Host", 3),
    CONNECTION("Connection", 4),
    ACCEPT("Accept", 5);

    final private String name;
    final private int value;

    TypeHeaders(String name, int value) {
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

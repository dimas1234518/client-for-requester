package com.zhevakin.requester.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Variable {

    private String name;
    private String value;
    private String id;
    private int rowId;

    public Variable(String name, String value, int rowId) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.value = value;
        this.rowId = rowId;
    }

    public Variable(String id, String name, String value, int rowId) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.rowId = rowId;
    }

    public Variable(String id) {
        this.id = id;
    }

    @Override
    public String toString() { return name; }
}

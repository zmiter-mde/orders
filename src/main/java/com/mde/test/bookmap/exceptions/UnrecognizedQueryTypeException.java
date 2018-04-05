package com.mde.test.bookmap.exceptions;

public class UnrecognizedQueryTypeException extends Exception {

    private String type;

    public UnrecognizedQueryTypeException(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}

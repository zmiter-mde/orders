package com.mde.test.bookmap.exceptions;

public class UnrecognizedOrderTypeException extends Exception {

    private String type;

    public UnrecognizedOrderTypeException(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}

package com.mde.test.bookmap.exceptions;

public class UnrecognizedOrderSideException extends Exception {

    private char side;

    public UnrecognizedOrderSideException(char side) {
        this.side = side;
    }

    public char getSide() {
        return side;
    }
}

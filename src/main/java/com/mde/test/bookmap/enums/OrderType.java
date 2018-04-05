package com.mde.test.bookmap.enums;

public enum OrderType {
    ORDER("o"), CANCELLATION("c"), QUERY("q");

    private String orderTypeString;

    private OrderType(String orderTypeString) {
        this.orderTypeString = orderTypeString;
    }

    public String getOrderTypeString() {
        return orderTypeString;
    }

    public static OrderType fromString(String str) {
        switch (str) {
            case "o":
                return ORDER;
            case "c":
                return CANCELLATION;
            case "q":
                return QUERY;
            default:
                return null;
        }
    }
}

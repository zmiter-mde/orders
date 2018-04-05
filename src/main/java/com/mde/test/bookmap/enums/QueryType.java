package com.mde.test.bookmap.enums;

public enum QueryType {
    BUYERS("buyers"), SELLERS("sellers"), SIZE("size");

    private String queryTypeString;

    private QueryType(String queryTypeString) {
        this.queryTypeString = queryTypeString;
    }

    public String getQueryTypeString() {
        return queryTypeString;
    }

    public static QueryType fromString(String str) {
        switch (str) {
            case "buyers":
                return BUYERS;
            case "sellers":
                return SELLERS;
            case "size":
                return SIZE;
            default:
                return null;
        }
    }
}

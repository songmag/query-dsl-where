package com.mark.filter;


public enum QueryFilterOperation {
    EQ("eq"),
    LT("lt"),
    LE("loe"),
    GT("gt"),
    GE("goe"),
    LIKE("contains"),
    BETWEEN("NONE"),
    IN("in"),
    NIN("notIn");

    private final String operation;

    QueryFilterOperation(String operation) {
        this.operation = operation;
    }

    public String getOperation() {
        return operation;
    }
}

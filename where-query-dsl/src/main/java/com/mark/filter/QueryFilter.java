package com.mark.filter;

public class QueryFilter {
    private String fieldName;
    private QueryFilterOperation queryFilterOperation;
    private Object value;

    public QueryFilter(String fieldName, QueryFilterOperation queryFilterOperation, Object value) {
        this.fieldName = fieldName;
        this.queryFilterOperation = queryFilterOperation;
        this.value = value;
    }

    public String getFieldName() {
        return fieldName;
    }

    public QueryFilterOperation getQueryFilterOperation() {
        return queryFilterOperation;
    }

    public Object getValue() {
        return value;
    }
}

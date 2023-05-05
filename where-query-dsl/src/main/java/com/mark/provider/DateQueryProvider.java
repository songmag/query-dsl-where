package com.mark.provider;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.mark.filter.DateTimeFilter;
import com.mark.filter.QueryFilterOperation;

public class DateQueryProvider extends QueryProvider {

    @JsonCreator
    public DateQueryProvider(QueryFilterOperation operation, DateTimeFilter value) {
        super(operation, value);
    }

    public DateQueryProvider(QueryFilterOperation operation, DateTimeFilter value, String field) {
        super(operation, value, field);
    }
}

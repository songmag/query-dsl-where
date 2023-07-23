package com.mark.provider;

import com.mark.exception.NotValidInstanceTypeException;
import com.mark.filter.DateFilter;
import com.mark.filter.DateTimeFilter;
import com.mark.filter.QueryFilter;
import com.mark.filter.QueryFilterOperation;

import java.util.Collection;
import java.util.List;

public class QueryProvider {
    private QueryFilterOperation operation;
    protected Object value;
    protected String fieldName;

    public QueryProvider(QueryFilterOperation operation, Object value) {
        this.operation = operation;
        this.value = value;
    }

    public QueryProvider(QueryFilterOperation operation, Object value, String fieldName) {
        this.operation = operation;
        this.value = value;
        this.fieldName = fieldName;
    }

    String getFieldName() {
        return this.fieldName;
    }

    boolean isContainFieldName() {
        return this.fieldName != null;
    }

    List<QueryFilter> getQuery(String fieldName) {
        if (value == null || operation == null) {
            return List.of();
        }

        if ((operation != QueryFilterOperation.IN && operation != QueryFilterOperation.NIN) && value instanceof Collection) {
            throw new NotValidInstanceTypeException("If the case is In or Nin, a Collection Type must be entered");
        }

        if (operation == QueryFilterOperation.BETWEEN) {
            return this.getBetweenFilter(fieldName, value);
        }

        if (value instanceof DateTimeFilter) {
            DateTimeFilter filter = (DateTimeFilter) value;
            return List.of(
                    new QueryFilter(fieldName, operation, filter.getFrom()),
                    new QueryFilter(fieldName, operation, filter.getEnd())
            );
        }

        if (value instanceof DateFilter) {
            DateFilter filter = (DateFilter) value;
            return List.of(
                    new QueryFilter(fieldName, operation, filter.getFrom()),
                    new QueryFilter(fieldName, operation, filter.getEnd())
            );
        }

        return List.of(new QueryFilter(fieldName, operation, value));
    }

    private List<QueryFilter> getBetweenFilter(String fieldName, Object value) {
        if (value instanceof DateTimeFilter) {
            DateTimeFilter filter = (DateTimeFilter) value;
            return List.of(
                    new QueryFilter(fieldName, QueryFilterOperation.GE, filter.getFrom()),
                    new QueryFilter(fieldName, QueryFilterOperation.LT, filter.getEnd())
            );
        }

        if (value instanceof DateFilter) {
            DateFilter filter = (DateFilter) value;
            return List.of(
                    new QueryFilter(fieldName, QueryFilterOperation.GE, filter.getFrom()),
                    new QueryFilter(fieldName, QueryFilterOperation.LT, filter.getEnd())
            );
        }
        throw new NotValidInstanceTypeException("For Between cases, DateTimeFilter must be used.");
    }

    protected void changeValue(Object value) {
        this.value = value;
    }
}

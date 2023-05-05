package com.mark.provider;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.mark.filter.DateFilter;
import com.mark.filter.DateTimeFilter;
import com.mark.filter.QueryFilter;
import com.mark.filter.QueryFilterOperation;
import com.mark.exception.NotValidInstanceTypeException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class QueryProvider {
    private QueryFilterOperation operation;
    private Object value;
    private String fieldName;

    @JsonCreator
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

    List<Optional<QueryFilter>> getQuery(String fieldName) {
        if (value == null || operation == null) {
            return List.of(Optional.empty());
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
                    Optional.of(new QueryFilter(fieldName, operation, filter.getFrom())),
                    Optional.of(new QueryFilter(fieldName, operation, filter.getEnd()))
            );
        }

        if (value instanceof DateFilter) {
            DateFilter filter = (DateFilter) value;
            return List.of(
                    Optional.of(new QueryFilter(fieldName, operation, filter.getFrom())),
                    Optional.of(new QueryFilter(fieldName, operation, filter.getEnd()))
            );
        }

        return List.of(Optional.of(new QueryFilter(fieldName, operation, value)));
    }

    private List<Optional<QueryFilter>> getBetweenFilter(String fieldName, Object value) {
        if (value instanceof DateTimeFilter) {
            DateTimeFilter filter = (DateTimeFilter) value;
            return List.of(
                    Optional.of(new QueryFilter(fieldName, QueryFilterOperation.GE, filter.getFrom())),
                    Optional.of(new QueryFilter(fieldName, QueryFilterOperation.LT, filter.getEnd()))
            );
        }

        if (value instanceof DateFilter) {
            DateFilter filter = (DateFilter) value;
            return List.of(
                    Optional.of(new QueryFilter(fieldName, QueryFilterOperation.GE, filter.getFrom())),
                    Optional.of(new QueryFilter(fieldName, QueryFilterOperation.LT, filter.getEnd()))
            );
        }
        throw new NotValidInstanceTypeException("For Between cases, DateTimeFilter must be used.");
    }

    protected void changeValue(Object value) {
        this.value = value;
    }
}

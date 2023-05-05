package com.mark.provider;

import com.mark.filter.QueryFilter;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class QueryFilters {
    public List<Optional<QueryFilter>> getQuery() {
        Field[] fields = this.getClass().getDeclaredFields();
        return Arrays.stream(fields)
                .map(i -> {
                    i.setAccessible(true);
                    return Map.entry(i.getName(), i);
                })
                .map(i -> {
                    try {
                        var result = i.getValue().get(this);
                        if (result == null) {
                            return null;
                        }
                        return Map.entry(i.getKey(), i.getValue().get(this));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .filter(Objects::nonNull)
                .filter(i -> i.getValue() instanceof QueryProvider)
                .flatMap(i -> {
                    QueryProvider queryProvider = (QueryProvider) i.getValue();
                    if (queryProvider.isContainFieldName()) {
                        return queryProvider.getQuery(queryProvider.getFieldName()).stream();
                    }
                    return queryProvider.getQuery(i.getKey()).stream();
                })
                .collect(Collectors.toList());
    }
}

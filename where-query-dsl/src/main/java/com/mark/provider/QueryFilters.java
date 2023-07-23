package com.mark.provider;

import com.mark.filter.QueryFilter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class QueryFilters {
    public List<QueryFilter> getQuery() {
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
                    //If Field Resolving using reflection field -> can not convert enum field
                    if (queryProvider instanceof EnumQueryProvider) {
                        ((EnumQueryProvider<?>) queryProvider).init();
                    }
                    if (queryProvider.isContainFieldName()) {
                        return queryProvider.getQuery(queryProvider.getFieldName()).stream();
                    }
                    return queryProvider.getQuery(i.getKey()).stream();
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}

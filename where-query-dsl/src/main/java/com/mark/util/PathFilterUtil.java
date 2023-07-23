package com.mark.util;

import com.mark.filter.QueryFilter;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparableExpression;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringExpression;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathFilterUtil<T> {

    private static final Map<String, Method> COMPARE_METHOD;

    static {
        List<Method> COMPARE_EXPRESSION_METHOD = Stream.of(
                        Arrays.stream(ComparableExpression.class.getDeclaredMethods()),
                        Arrays.stream(StringExpression.class.getDeclaredMethods())
                )
                .flatMap(i -> i)
                .filter(i -> i.getReturnType() == BooleanExpression.class)
                .filter(i ->
                        Arrays.stream(i.getParameterTypes())
                                .noneMatch(j -> j.getPackageName().contains("com.querydsl"))
                )
                .filter(i -> i.getParameterTypes().length == 1)
                .collect(Collectors.toList());

        List<Method> SIMPLE_EXPRESSION_METHOD = Arrays.stream(SimpleExpression.class.getDeclaredMethods())
                .filter(i -> i.getReturnType() == BooleanExpression.class)
                .filter(i -> Arrays.stream(i.getParameterTypes()).noneMatch(j -> j.getPackageName().contains("com.querydsl")))
                .filter(i -> {
                    if (i.getName().equals("eq")) {
                        return true;
                    }
                    return Arrays.stream(i.getParameterTypes()).anyMatch(j -> j == Collection.class);
                })
                .collect(Collectors.toList());

        COMPARE_METHOD = Stream.of(COMPARE_EXPRESSION_METHOD.stream(), SIMPLE_EXPRESSION_METHOD.stream())
                .flatMap(i -> i)
                .collect(Collectors.toMap(Method::getName, i -> i));
    }

    private Map<String, Expression<?>> fields;
    private EntityPathBase<T> rootPath;

    public PathFilterUtil(EntityPathBase<T> rootPath) {
        this.rootPath = rootPath;
        this.fields = new HashMap<>();
        makeFields("", rootPath);
    }

    public List<Predicate> invoke(List<QueryFilter> filters) {
        return filters.stream()
                .map(i -> {
                            try {
                                return invoke(i);
                            } catch (NoSuchFieldException | NoSuchMethodException | InvocationTargetException |
                                     IllegalAccessException e) {
                                throw new RuntimeException(e);
                            }
                        }
                )
                .collect(Collectors.toList());
    }


    private void makeFields(String prefix, Expression<?> path) {
        var pathResult = Arrays.stream(path.getClass().getFields())
                .map(i -> {
                    try {
                        String fieldName = prefix.isBlank() ? i.getName() : prefix + i.getName();
                        return new HashMap.SimpleEntry<>(fieldName, i.get(path));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .filter(value -> !fields.containsKey(value.getKey()))
                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, i -> (Expression<?>) i.getValue()));

        fields.putAll(pathResult);
    }

    private BooleanExpression invoke(QueryFilter queryFilter) throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Queue<String> names = new LinkedBlockingQueue<>();
        if (queryFilter.getValue() == null) {
            return null;
        }
        if (queryFilter.getFieldName() == null) {
            return null;
        }

        for (var name : queryFilter.getFieldName().split("\\.")) {
            names.offer(name);
        }

        Expression<?> fieldPath = null;
        StringBuilder prefix = new StringBuilder();
        while (!names.isEmpty()) {
            var fieldName = prefix + names.poll();
            var nextFieldPath = fields.get(fieldName);
            if (nextFieldPath == null && fieldPath != null) {
                makeFields(prefix.toString(), fieldPath);
                nextFieldPath = fields.get(fieldName);
            }
            fieldPath = nextFieldPath;
            prefix.append(fieldName)
                    .append(".");
        }

        if (fieldPath == null) {
            throw new NoSuchFieldException(queryFilter.getFieldName());
        }

        Method method = COMPARE_METHOD.get(queryFilter.getQueryFilterOperation().getOperation());
        if (method == null) {
            throw new NoSuchMethodException(queryFilter.getQueryFilterOperation().getOperation());
        }
        return (BooleanExpression) method.invoke(fieldPath, queryFilter.getValue());
    }
}

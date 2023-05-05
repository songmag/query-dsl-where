package com.mark.provider;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.mark.filter.QueryFilterOperation;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public abstract class EnumQueryProvider<T extends Enum<T>> extends QueryProvider {
    @JsonCreator
    public EnumQueryProvider(QueryFilterOperation operation, Object value) {
        super(operation, value);
        this.stringToEnum(value);
    }

    public EnumQueryProvider(QueryFilterOperation operation, Object value, String field) {
        super(operation, value, field);
        this.stringToEnum(value);
    }

    void stringToEnum(Object value) {
        if (value instanceof Enum<?>) {
            return;
        }
        Class<T> clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        if (value instanceof String) {
            T arg = T.valueOf(clazz, (String) value);
            super.changeValue(arg);
        }
        if (value instanceof Collection) {
            List<T> args = new ArrayList<>();
            Collection<String> values = ((Collection<String>) value);
            Iterator<String> iterator = values.iterator();
            while (iterator.hasNext()) {
                String i = iterator.next();
                args.add(T.valueOf(clazz, i));
            }
            super.changeValue(args);
        }
    }
}

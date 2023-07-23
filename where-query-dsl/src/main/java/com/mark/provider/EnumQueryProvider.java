package com.mark.provider;


import com.mark.filter.QueryFilterOperation;
import com.mark.util.GenericTypeUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public abstract class EnumQueryProvider<T extends Enum<T>> extends QueryProvider {
    public EnumQueryProvider(QueryFilterOperation operation, Object value) {
        super(operation, value);
        this.stringToEnum(value);
    }

    public EnumQueryProvider(QueryFilterOperation operation, Object value, String field) {
        super(operation, value, field);
        this.stringToEnum(value);
    }

    void init() {
        this.stringToEnum(this.value);
    }

    void stringToEnum(Object value) {
        if (value instanceof Enum<?>) {
            return;
        }
        Class<T> clazz = GenericTypeUtil.getGenericTypeClass(getClass());
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

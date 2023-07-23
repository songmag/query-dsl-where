package com.mark.util;

import java.lang.reflect.ParameterizedType;

public class GenericTypeUtil {

    public static <T> Class<T> getGenericTypeClass(Class<?> clazz) {
        return (Class<T>) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0];
    }
}

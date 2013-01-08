package com.facetime.core.creating;

import java.lang.reflect.Method;

public class EmptyArgsMethodCreating<T> implements Creating<T> {

    private Method method;

    public EmptyArgsMethodCreating(Method method) {
        this.method = method;
    }

    @SuppressWarnings("unchecked")
    public T create(Object[] args) {
        try {
            return (T) method.invoke(null);
        } catch (Exception e) {
            throw new CreatingException(e, method.getDeclaringClass(), args);
        }
    }

}

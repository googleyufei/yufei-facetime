package com.facetime.core.creating;

import java.lang.reflect.Constructor;

public class EmptyArgsConstructorCreating<T> implements Creating<T> {

    private Constructor<T> c;

    public EmptyArgsConstructorCreating(Constructor<T> c) {
        this.c = c;
    }

    public T create(Object[] args) {
        try {
            return c.newInstance();
        } catch (Exception e) {
            throw new CreatingException(e, c.getDeclaringClass(), null);
        }
    }

}

package com.facetime.core.creating;

import java.lang.reflect.Constructor;

/**
 * 使用构造函数构建
 *
 * @param <T>
 */
public class ConstructorCreating<T> implements Creating<T> {

    private Constructor<T> c;

    public ConstructorCreating(Constructor<T> c) {
        this.c = c;
    }

    public T create(Object[] args) {
        try {
            return c.newInstance(args);
        } catch (Exception e) {
            throw new CreatingException(e, c.getDeclaringClass(), args);
        }
    }

}

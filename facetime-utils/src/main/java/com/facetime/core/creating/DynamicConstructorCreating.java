package com.facetime.core.creating;

import java.lang.reflect.Constructor;

import com.facetime.core.utils.LE;

/**
 * 使用CE转换参数的构造函构建
 *
 * @param <T>
 */
public class DynamicConstructorCreating<T> implements Creating<T> {

    private Constructor<T> c;

    public DynamicConstructorCreating(Constructor<T> c) {
        this.c = c;
    }

    public T create(Object[] args) {
        try {
            return c.newInstance(LE.evalArgToRealArray(args));
        } catch (Exception e) {
            throw new CreatingException(e, c.getDeclaringClass(), args);
        }
    }

}

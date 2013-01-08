package com.facetime.core.creating;

import java.lang.reflect.Constructor;

import com.facetime.core.utils.LE;

/**
 * 构造函数转换
 *
 * @param <T>
 */
public class ConstructorCastingCreating<T> implements Creating<T> {

    private Constructor<T> c;
    private Class<?>[] pts;

    public ConstructorCastingCreating(Constructor<T> c) {
        this.c = c;
        this.pts = c.getParameterTypes();
    }

    public T create(Object[] args) {
        try {
            args = LE.array2ObjectArray(args, pts);
            return c.newInstance(args);
        } catch (Exception e) {
            throw new CreatingException(e, c.getDeclaringClass(), args);
        }
    }

}

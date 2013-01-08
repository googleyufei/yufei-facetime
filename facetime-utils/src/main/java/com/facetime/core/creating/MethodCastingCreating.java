package com.facetime.core.creating;

import java.lang.reflect.Method;

import com.facetime.core.utils.LE;

public class MethodCastingCreating<T> implements Creating<T> {

    private Method method;
    private Class<?>[] pts;

    public MethodCastingCreating(Method method) {
        this.method = method;
        this.pts = method.getParameterTypes();
    }

    @SuppressWarnings("unchecked")
    public T create(Object[] args) {
        try {
            args = LE.array2ObjectArray(args, pts);
            return (T) method.invoke(null, args);
        } catch (Exception e) {
            throw new CreatingException(e, method.getDeclaringClass(), args);
        }
    }
}

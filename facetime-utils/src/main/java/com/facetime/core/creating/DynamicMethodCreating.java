package com.facetime.core.creating;

import java.lang.reflect.Method;

import com.facetime.core.utils.LE;

/**
 * 使用CE做过参数专款的Method构造
 *
 * @param <T>
 */
public class DynamicMethodCreating<T> implements Creating<T> {

    private Method method;

    public DynamicMethodCreating(Method method) {
        this.method = method;
    }

    @SuppressWarnings("unchecked")
    public T create(Object[] args) {
        try {
            return (T) method.invoke(null, LE.evalArgToRealArray(args));
        } catch (Exception e) {
            throw new CreatingException(e, method.getDeclaringClass(), args);
        }
    }

}

package com.facetime.core.coercion;

/**
 * 类型转换器接口
 *
 * @param <FROM> the source type (input)
 * @param <TO>   the target type (output)
 */
public interface CoercionTuple<FROM, TO> {
    /**
     * 来源类型
     * @return
     */
    public Class<?> getFromType();
    /**
     * 转换的目标类型
     * @return
     */
    public Class<?> getTargetType();
    /**
     * 转换实现接口
     * @param src
     * @return
     * @throws FailToCoerceException
     */
    public TO coerce(FROM src) throws FailToCoerceException;

}

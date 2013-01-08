package com.facetime.core.creating;

/**
 * 对象抽象创建方式
 *
 * @param <T>
 */
public interface Creating<T> {

    T create(Object[] args);

}

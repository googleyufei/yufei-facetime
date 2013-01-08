package com.facetime.core.coercion;

import com.facetime.core.utils.ClassUtils;
import com.facetime.core.utils.LE;

/**
 * 抽象转换器<BR></>
 * 自动获取FROM和TO的具体参数类型和ToString的实现<br></>
 * 方便一般转换器的继承，省点力气
 *
 * @param <FROM> 从什么类型
 * @param <TO>   转到什么类型
 */
public abstract class AbstractCoercion<FROM, TO> implements CoercionTuple<FROM, TO> {

	protected AbstractCoercion() {
		fromType = (Class<?>) LE.extractTypeParams(getClass())[0];
		targetType = (Class<?>) LE.extractTypeParams(getClass())[1];
	}

	protected Class<?> fromType;
	protected Class<?> targetType;

	public Class<?> getFromType() {
		return fromType;
	}

	public Class<?> getTargetType() {
		return targetType;
	}

	@Override
	public String toString() {
		return String.format("%s -> %s", ClassUtils.getClassNameForJava(getFromType()),
				ClassUtils.getClassNameForJava(getTargetType()));
	}
}

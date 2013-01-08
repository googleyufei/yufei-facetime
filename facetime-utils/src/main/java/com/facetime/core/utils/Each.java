package com.facetime.core.utils;

/**
 *
 * @param <T>
 */
public interface Each<T> {
	/**
	 * 遍历回调方法
	 * @param loopIndex
	 * @param elem
	 * @param length
	 * @throws ExitLoop
	 * @throws LoopException
	 */
	void loop(int loopIndex, T elem, int length) throws ExitLoop, LoopException;

}

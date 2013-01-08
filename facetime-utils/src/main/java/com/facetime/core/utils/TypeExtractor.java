package com.facetime.core.utils;

/**
 * 类型提炼器。针对一个CE类型，提炼出一组最能反应其特征的Class类型
 * User: dzb
 * Date: 11-8-1
 * Time: 下午9:36
 *
 */
public interface TypeExtractor {

	Class<?>[] extract(CE<?> ce);

}

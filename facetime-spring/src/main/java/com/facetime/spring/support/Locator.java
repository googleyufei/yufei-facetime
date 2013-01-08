package com.facetime.spring.support;

/**
 * Spring的bean查询类
 */
public interface Locator {

	/**
	 * 加载Spring容器中的指定类型BEAN
	 * 
	 * @param clazz
	 *            bean的类型
	 * @return
	 */
	public <T> T locate(Class<T> clazz);

	/**
	 * 加载Spring容器中指定类型和名称的BEAN. 该方法用于该执行类型的BEAN有多个时.
	 * 
	 * @param clazz
	 *            bean的类型
	 * @param beanName
	 *            bean的名称
	 * @return
	 */
	public <T> T locate(Class<T> clazz, String beanName);
}

package com.facetime.spring.support;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component("beanLocator")
public class BeanLocator implements Locator, Serializable {

	private static final long serialVersionUID = 4876343772245527648L;

	@Autowired
	private ApplicationContext applicationContext;

	private Map<Class<?>, Object> beanCache = new HashMap<Class<?>, Object>();

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@Override
	public <T> T locate(Class<T> clazz) {
		return this.locate(clazz, null);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T locate(Class<T> clazz, String beanName) {
		if (beanName == null && beanCache.containsKey(clazz)) {
			return (T) beanCache.get(clazz);
		}
		Object foundBean = findBeanByTypeAndName(clazz, beanName);
		if (foundBean == null) {
			throw new AssertionError("bean of " + clazz.getName() + " is not found in IOC.");
		}
		beanCache.put(clazz, foundBean);
		return (T) foundBean;
	}

	protected <T> Map<String, T> findBeanImpl(Class<T> clazz) {
		return BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, clazz);
	}

	private Object findBeanByTypeAndName(Class<?> clazz, String beanName) {
		if (!clazz.isInterface()) {
			throw new IllegalArgumentException(clazz.getName() + "is not an interface.");
		}
		Map<?, ?> foundBeans = findBeanImpl(clazz);
		if (foundBeans == null || foundBeans.isEmpty()) {
			throw new AssertionError("there's no bean that impl.  " + clazz.getName());
		}
		if (beanName == null) {
			if (foundBeans.size() > 1) {
				throw new AssertionError(" bean that implement " + clazz.getName()
						+ " more than one, please use locator(clazz, beanName) method.");
			} else {
				for (Object foundBean : foundBeans.values()) {
					return foundBean;
				}
			}
		}
		if (beanName != null && foundBeans.get(beanName) == null) {
			throw new AssertionError(" there's no bean that type is  " + clazz.getName() + " and beanName is "
					+ beanName);
		}
		return foundBeans.get(beanName);
	}
}

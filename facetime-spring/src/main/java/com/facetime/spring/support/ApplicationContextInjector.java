package com.facetime.spring.support;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 
 *
 *ApplicationContext注入器
 *
 * @author yufei
 * @Create_by 2012-11-29
 * @Design_by eclipse  
 */
@Component
public class ApplicationContextInjector implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	private Map<Class<?>, Object> beanCache = new HashMap<Class<?>, Object>();

	public ApplicationContext getApplicationContext() {
		return this.applicationContext;
	}

	public final <T> T locate(Class<T> beanClass) {
		return locate(beanClass, null);
	}

	@SuppressWarnings("unchecked")
	public final <T> T locate(Class<T> beanClass, String beanName) {
		if (beanName == null && beanCache.containsKey(beanClass)) {
			return (T) beanCache.get(beanClass);
		}
		T foundBean = findBeanByTypeAndName(beanClass, beanName);
		if (foundBean == null) {
			throw new AssertionError("bean of " + beanClass.getName() + " is not found in IOC.");
		}
		beanCache.put(beanClass, foundBean);
		return foundBean;
	}

	public String message(String key) {
		return message(key, null);
	}

	public String message(String key, Object[] params) {
		return applicationContext.getMessage(key, params, Locale.CHINA);
	}

	public String message(String key, Object[] params, Locale locale) {
		return applicationContext.getMessage(key, params, locale);
	}

	public String message(String key, Object[] params, String defaultMsg) {
		return applicationContext.getMessage(key, params, defaultMsg, Locale.CHINA);
	}

	public String message(String key, Object[] params, String defaultMsg, Locale locale) {
		return applicationContext.getMessage(key, params, defaultMsg, locale);
	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.applicationContext = context;
		SpringContextUtils.initSpringContextUtils(this);
	}

	private final <T> T findBeanByTypeAndName(Class<T> clazz, String beanName) {
		if (!clazz.isInterface()) {
			throw new IllegalArgumentException(clazz.getName() + "is not an interface.");
		}
		Map<String, T> foundBeans = findBeanImpl(clazz);
		if (foundBeans == null || foundBeans.isEmpty()) {
			throw new AssertionError("there's no bean that impl.  " + clazz.getName());
		}
		if (beanName == null) {
			if (foundBeans.size() > 1) {
				throw new AssertionError(" bean that implement " + clazz.getName()
						+ " more than one, please use getBean(clazz, beanName) method.");
			} else {
				for (T foundBean : foundBeans.values()) {
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

	private final <T> Map<String, T> findBeanImpl(Class<T> clazz) {
		return BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, clazz);
	}

}

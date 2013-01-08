package com.facetime.spring.support;

import java.util.Locale;

import org.springframework.context.ApplicationContext;

/**
 * 通过文件系统方式初始花上下文，并提供静态方法给其他类调用，用于获取Spring Bean.
 */
public class SpringContextUtils {

	private static ApplicationContextInjector injector;

	public final static ApplicationContext getAppContext() {
		if (injector == null) {
			throw new AssertionError("Spring application context is null.");
		}
		return injector.getApplicationContext();
	}

	public static final void initSpringContextUtils(ApplicationContextInjector injector) {
		SpringContextUtils.injector = injector;
	}

	public static final <T> T locate(Class<T> beanClass) {
		return locate(beanClass, null);
	}

	public static final <T> T locate(Class<T> beanClass, String beanName) {
		return injector.locate(beanClass, beanName);
	}

	public static String message(String key) {
		return message(key, null);
	}

	public static String message(String key, Object[] params) {
		return injector.getApplicationContext().getMessage(key, params, Locale.CHINA);
	}

	public static String message(String key, Object[] params, Locale locale) {
		return injector.getApplicationContext().getMessage(key, params, locale);
	}

	public static String message(String key, Object[] params, String defaultMsg) {
		return injector.getApplicationContext().getMessage(key, params, defaultMsg, Locale.CHINA);
	}

	public static String message(String key, Object[] params, String defaultMsg, Locale locale) {
		return injector.getApplicationContext().getMessage(key, params, defaultMsg, locale);
	}

}

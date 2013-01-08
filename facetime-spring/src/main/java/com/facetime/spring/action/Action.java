package com.facetime.spring.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.facetime.core.conf.SysLogger;
import com.facetime.spring.dao.Dao;
import com.facetime.spring.logic.Logic;
import com.facetime.spring.support.Locator;

/**
 * SpringMVC 框架的控制器层的ROOT抽象类, 用于非Json类型的返回
 * @author YUFEI
 */
public class Action implements ApplicationContextAware {

	@Autowired
	@Qualifier("beanLocator")
	protected Locator beanLocator;

	@Autowired
	@Qualifier("defaultLogic")
	protected Logic defaultLogic;

	protected ApplicationContext context;

	public Logic getDefaultLogic() {
		return defaultLogic;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}

	public <T extends Dao> T locate(Class<T> beanClass) {
		T logic = beanLocator.locate(beanClass);
		if (Dao.class.isAssignableFrom(beanClass))
			return logic;
		throw new AssertionError("you can only locate logic class.");
	}

	public <T extends Dao> T locate(Class<T> beanClass, String beanName) {
		T logic = beanLocator.locate(beanClass, beanName);
		if (Dao.class.isAssignableFrom(beanClass))
			return logic;
		throw new AssertionError("you can only locate logic class.");
	}

	public String message(String key, Object[] params, Locale locale) {
		return context.getMessage(key, params, locale);
	}

	public String message(String key) {
		return message(key, new Object[] {});
	}

	public String message(String key, Object[] params, String defaultMsg, Locale locale) {
		return context.getMessage(key, params, defaultMsg, locale);
	}

	public String message(String key, Object... params) {
		return context.getMessage(key, params, Locale.CHINA);
	}

	public String message(String key, Object[] params, String defaultMsg) {
		return context.getMessage(key, params, defaultMsg, Locale.CHINA);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationcontext) throws BeansException {
		context = applicationcontext;
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public String handle400Exception(Exception ex, HttpServletRequest request) {
		SysLogger.facetime().error(request.getRequestURI() + " request fail! [excetption]:  ", ex);
		return "error/400";
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public String handle500Exception(Exception ex, HttpServletRequest request) {
		SysLogger.facetime().error(request.getRequestURI() + " request fail! [excetption]:  ", ex);
		return "error/500";
	}
}

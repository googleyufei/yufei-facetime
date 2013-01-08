package com.facetime.spring.action;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.facetime.core.conf.SysLogger;
import com.facetime.core.http.ErrorType;
import com.facetime.spring.dao.Dao;
import com.facetime.spring.logic.Logic;
import com.facetime.spring.logic.RemoteLogic;
import com.facetime.spring.support.Locator;
import com.facetime.spring.support.LogicException;

/**
 * 返回ResponseBody类型的Action
 *
 * @author yufei
 * @Create_by 2012-12-7
 * @Design_by eclipse  
 */
public class ServiceAction {

	@Autowired
	@Qualifier("beanLocator")
	protected Locator beanLocator;

	@Autowired
	@Qualifier("remoteLogic")
	protected RemoteLogic remoteLogic;

	@Autowired
	@Qualifier("defaultLogic")
	protected Logic defaultLogic;

	public <T extends Dao> T locate(Class<T> beanClass) {
		return beanLocator.locate(beanClass);
	}

	public <T extends Dao> T locate(Class<T> beanClass, String beanName) {
		return beanLocator.locate(beanClass, beanName);
	}

	/**
	* 异常控制，这便是异常细节可控，将来可用于支持国际化（异常信息国际化）
	* */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public String handleException(Exception ex, HttpServletRequest request) {
		SysLogger.facetime().error(request.getRequestURI() + " request fail! [excetption]:  ", ex);
		return ErrorType.ERR_SYS_ERR;
	}

	@ExceptionHandler(LogicException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public String handleLogicException(LogicException ex, HttpServletRequest request) {
		SysLogger.facetime().info(request.getRequestURI() + " request fail! error type is: " + ex.getErrorType());
		return ex.getErrorType() != null ? ex.getErrorType() : ErrorType.ERR_SYS_ERR;
	}
}

package com.facetime.core.conf;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * 封装log4j 提供静态方法，记录系统的调试信息，一般信息和错误信息并保存到指定文件
 */
public class SysLogger {

	public static final String DEFAULT_LOGFILE = "log4j.properties";
	public static Logger facetimeLogger = Logger.getLogger("facetime");
	public static Logger shopLogger = Logger.getLogger("shop");
	public static Logger cloudServerLogger = Logger.getLogger("cloud.server");
	public static Logger cloudAppLogger = Logger.getLogger("cloud.app");
	public static Logger cloudAdminLogger = Logger.getLogger("cloud.admin");

	/**
	 * SysLog init method
	 */
	static {
		PropertyConfigurator.configure(ConfigUtils.confProperties);
		PropertyConfigurator.configure(SysLogger.class.getClassLoader().getResource(DEFAULT_LOGFILE));
	}

	public static Logger cloudApp() {
		return cloudAppLogger;
	}

	public static Logger cloudServer() {
		return cloudServerLogger;
	}

	public static Logger facetime() {
		return facetimeLogger;
	}

	public static void main(String[] args) {
		Logger[] loggers = new Logger[] { facetimeLogger, shopLogger, cloudServerLogger, cloudAppLogger,
				cloudAdminLogger };
		for (Logger logger : loggers) {
			logger.debug("debug");
			logger.info("info");
			logger.error("error");
			logger.error(new Exception("test"));
		}

	}

	public static Logger shop() {
		return shopLogger;
	}

	public Logger cloudAdmin() {
		return cloudAdminLogger;
	}
}

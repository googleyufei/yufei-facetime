package com.facetime.core.conf;

import java.io.InputStream;
import java.util.Properties;

/**
 * 配置文件是相对于类路径根目录放置的，不是物理路径 例如放在WEB-INF/class/里面
 * 缺省配置文件为viewConfig.properties,放在当前类路径
 */
public class ConfigUtils {

	/** 系统根路径 */
	public static String SYSTEM_ROOT_DIR = null;
	/** 日志根路径 */
	public static String LOG_ROOT_DIR = null;
	/** 保存缺省配置文件的配置信息 */
	public static Properties confProperties = new Properties();

	public static boolean IS_DEV_MODE = false;

	/**
	 * 根据key值取得缺省配置文件中相应的内容
	 */
	public static String getProperty(String key) {
		return getProperty(key, null);
	}

	/**
	 * 根据key值取得缺省配置文件中相应的内容
	 */
	public static String getProperty(String key, Object defaultValue) {
		String str = confProperties.getProperty(key);
		if (str != null)
			return str.trim();
		if (defaultValue != null)
			return defaultValue.toString();
		return null;
	}

	static {
		try {
			InputStream resourceAsStream = ConfigUtils.class.getClassLoader().getResourceAsStream(
					ConfigConstants.CONF_FILE_NAME);
			confProperties.load(resourceAsStream);
			if (confProperties == null)
				throw new RuntimeException("Can't Load conf.properties in classpath.");
			SYSTEM_ROOT_DIR = confProperties.getProperty(ConfigConstants.KEY_SYSTEM_ROOT_DIR);
			LOG_ROOT_DIR = confProperties.getProperty(ConfigConstants.KEY_LOG_ROOT_DIR);
			if (ConfigConstants.PROJECT_MODE_DEV.equals(confProperties.getProperty(ConfigConstants.KEY_PROJECT_MODE))) {
				IS_DEV_MODE = true;
			}

			System.setProperty(ConfigConstants.KEY_SYSTEM_ROOT_DIR, SYSTEM_ROOT_DIR);
			System.setProperty(ConfigConstants.KEY_LOG_ROOT_DIR, LOG_ROOT_DIR);
			if (IS_DEV_MODE)
				System.setProperty(ConfigConstants.LOGGER_LEVEL, "DEBUG");
			else
				System.setProperty(ConfigConstants.LOGGER_LEVEL, "INFO");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
	}
}

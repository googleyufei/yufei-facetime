package com.facetime.core.logging;

import java.util.List;

import com.facetime.core.plugin.NoValidPluginException;
import com.facetime.core.plugin.Plugin;
import com.facetime.core.plugin.PluginManager;
import com.facetime.core.plugin.PluginManagerImpl;
import com.facetime.core.utils.DefaultSettings;


/**
 * 便利于程序重构替换的Log工厂类
 * @author dzb
 *
 */
public class LogFactory {

	static LogAdapter adapter = null;
	static Log log = null;

	static {

		PluginManager manager = new PluginManagerImpl();
		List<Plugin> plugins = manager.loadPackage("net.street.common.logging.impl");

		if (plugins.size() == 0) {
			throw new NoValidPluginException("There are no valid Log-plugins.");
		}

        for (Plugin log : plugins) {
			manager.attach(log);
		}

        adapter = (LogAdapter)(manager.get(DefaultSettings.LOG_ID).get(0));
        if (adapter == null) {
            //如果要求的没有 就取载入的第一个
            adapter = (LogAdapter)plugins.get(0);
        }

        log = getLog("STREET-FRAMEWORK");
        log.info("Using %s as default logger.", ((Plugin)adapter).getDef().getName());
	}
	
	
	public static Log getLog(String name) {
		Log log = adapter.getLog(name);
        log.setLevel(DefaultSettings.LOG_LEVEL);
        return log;
	}
	
	public static Log getLog(Class<?> clazz) {
		Log log = adapter.getLog(clazz.getName());
        log.setLevel(DefaultSettings.LOG_LEVEL);
        return log;
	}

	public static Log getLog() {
		Log log = adapter.getLog(new Throwable().getStackTrace()[1].getClassName());
        log.setLevel(DefaultSettings.LOG_LEVEL);
        return log;
	}

    public static Log get() {
        return log;
    }
}

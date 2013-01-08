package com.facetime.core.logging.impl;

import java.util.Map;
import java.util.logging.Level;

import com.facetime.core.logging.Log;
import com.facetime.core.logging.LogAdapter;
import com.facetime.core.plugin.AbstractPlugin;
import com.facetime.core.plugin.Plugin;
import com.facetime.core.plugin.PluginDef;
import com.facetime.core.utils.ResultPack;
import com.facetime.core.utils.Version;

/**
 * JDK Logger 实现，如果没有其他的更优实现附加，框架就会自动使用这个
 * @author thinkpad
 */
public class JDKLogAdapter extends AbstractPlugin implements LogAdapter, Plugin, PluginDef {

	public JDKLogAdapter() {
		this.setDef(this);
	}

	public String getId() {
		return "jdk";
	}

	public String getName() {
		return "logger-JDK";
	}

	public String getOrder() {
		return "after:*";
	}

	public Version getVersion() {
		return Version.of("1.5.22");
	}

	/** JDK 自带,所以必然Loaded, 否则就见鬼了 */
	public ResultPack validate(Map<String, Object> ctx) {
		return ResultPack.succeed();//直接返回
	}

	public Log getLog(String label) {
		return new Log4Jdk(label);
	}

	public String toString() {
		return "STREET-FRAMEWORK is using JDK's java.util.logging.Logger";
	}

	@SuppressWarnings("serial")
	private static class Log4Jdk implements Log {

		private java.util.logging.Logger _log = null;

		public Log4Jdk(String name) {
			this._log = java.util.logging.Logger.getLogger(name);
		};

		public void setLevel(int level) {
			switch (level) {
			case Log.DEBUG_LEVEL:
				_log.setLevel(Level.FINE);
				break;
			case Log.TRACE_LEVEL:
				_log.setLevel(Level.FINER);
				break;
			case Log.INFO_LEVEL:
				_log.setLevel(Level.INFO);
				break;
			case Log.WARN_LEVEL:
				_log.setLevel(Level.WARNING);
				break;
			case Log.ERROR_LEVEL:
				_log.setLevel(Level.SEVERE);
				break;
			default:
				_log.setLevel(Level.INFO);
			}
		}

		public int getLevel() {
			Level l = _log.getLevel();
			if (l.equals(Level.INFO)) {
				return Log.INFO_LEVEL;
			} else if (l.equals(Level.FINE)) {
				return Log.DEBUG_LEVEL;
			} else if (l.equals(Level.FINER)) {
				return Log.TRACE_LEVEL;
			} else if (l.equals(Level.WARNING)) {
				return Log.WARN_LEVEL;
			} else if (l.equals(Level.SEVERE)) {
				return Log.ERROR_LEVEL;
			} else {
				return Log.INFO_LEVEL;
			}
		}

		public void debug(Object message, Object... args) {
			String msg = String.valueOf(message);
			if (args != null && args.length > 0)
				msg = String.format(msg, args);
			_log.log(Level.FINE, msg);
		}

		public void debug(Throwable e, Object message, Object... args) {
			String msg = String.valueOf(message);
			if (args != null && args.length > 0)
				msg = String.format(msg, args);
			_log.log(Level.FINE, msg, e);
		}

		public void trace(Object message, Object... args) {
			String msg = String.valueOf(message);
			if (args != null && args.length > 0)
				msg = String.format(msg, args);
			_log.log(Level.FINER, msg);

		}

		public void trace(Throwable e, Object message, Object... args) {
			String msg = String.valueOf(message);
			if (args != null && args.length > 0)
				msg = String.format(msg, args);
			_log.log(Level.FINER, msg, e);
		}

		public void info(Object message, Object... args) {
			String msg = String.valueOf(message);
			if (args != null && args.length > 0)
				msg = String.format(msg, args);
			_log.log(Level.INFO, msg);
		}

		public void info(Throwable e, Object message, Object... args) {
			String msg = String.valueOf(message);
			if (args != null && args.length > 0)
				msg = String.format(msg, args);
			_log.log(Level.INFO, msg, e);
		}

		public void warn(Object message, Object... args) {
			String msg = String.valueOf(message);
			if (args != null && args.length > 0)
				msg = String.format(msg, args);
			_log.log(Level.WARNING, msg);
		}

		public void warn(Throwable e, Object message, Object... args) {
			String msg = String.valueOf(message);
			if (args != null && args.length > 0)
				msg = String.format(msg, args);
			_log.log(Level.WARNING, msg, e);
		}

		public void error(Object message, Object... args) {
			String msg = String.valueOf(message);
			if (args != null && args.length > 0)
				msg = String.format(msg, args);
			_log.log(Level.SEVERE, msg);
		}

		public void error(Throwable e, Object message, Object... args) {
			String msg = String.valueOf(message);
			if (args != null && args.length > 0)
				msg = String.format(msg, args);
			_log.log(Level.SEVERE, msg, e);
		}

		public boolean isDebugEnabled() {
			return _log.isLoggable(Level.CONFIG);
		}

		public boolean isTraceEnabled() {
			return _log.isLoggable(Level.FINE);
		}

		public boolean isInfoEnabled() {
			return _log.isLoggable(Level.INFO);
		}

		public boolean isWarnEnabled() {
			return _log.isLoggable(Level.WARNING);
		}

		public boolean isErrorEnabled() {
			return _log.isLoggable(Level.SEVERE);
		}
	}

	@Override
	public PluginDef getDef() {
		// 
		return null;
	}

	@Override
	public void start(Map<String, Object> ctx) throws Exception {
		// 

	}

	@Override
	public void stop(Map<String, Object> ctx) throws Exception {
		// 

	}
}

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.facetime.core.logging.impl;

import java.util.Map;

import com.facetime.core.logging.Log;
import com.facetime.core.logging.LogAdapter;
import com.facetime.core.plugin.AbstractPlugin;
import com.facetime.core.plugin.Plugin;
import com.facetime.core.plugin.PluginDef;
import com.facetime.core.utils.ResultPack;
import com.facetime.core.utils.Version;

/**
 * 使用 <tt>System.out</tt>的控制台log实现
 * <p/>
 * ConsoleLogAdapter STREET-Framework的缺省{@link Log}输出
 * <p/>
 */
public class ConsoleLogAdapter extends AbstractPlugin implements LogAdapter, Plugin, PluginDef {

	/** The level names. */
	public static final String[] LEVELS = { "[trace] ", "[debug] ", "[info ] ", "[warn ] ", "[error] " };

	public ConsoleLogAdapter() {
		setDef(this);
	}

	public Log getLog(String name) {
		return new Log4Console(name);
	}

	public ResultPack validate(Map<String, Object> ctx) {
		return ResultPack.succeed();
	}

	public String getId() {
		return "console";
	}

	public String getName() {
		return "Console-logger";
	}

	public Version getVersion() {
		return new Version("3.1.0");
	}

	public String getOrder() {
		return "before:*";
	}

	public static class Log4Console implements Log {

		protected boolean flag = false;
		/** The logging level. */
		protected int logLevel = INFO_LEVEL;
		/** The log getName. */
		protected String name = "STREET";

		Log4Console(String name) {
			this.name = name;
		}

		public int getLevel() {
			return logLevel;
		}

		/**
		 * Set the logging level
		 * <tt>[ TRACE_LEVEL | DEBUG_LEVEL | INFO_LEVEL | WARN_LEVEL | ERROR_LEVEL ]</tt>.
		 *
		 * @param level the logging level
		 */
		public void setLevel(int level) {
			logLevel = level;
		}

		public void debug(Object message, Object... args) {
			log(DEBUG_LEVEL, (Throwable) null, message, args);
		}

		public void debug(Throwable error, Object message, Object... args) {
			log(DEBUG_LEVEL, error, message, args);
		}

		public void error(Object message, Object... args) {
			log(ERROR_LEVEL, (Throwable) null, message, args);
		}

		public void error(Throwable error, Object message, Object... args) {
			log(ERROR_LEVEL, error, message, args);
		}

		public void info(Object message, Object... args) {
			log(INFO_LEVEL, (Throwable) null, message, args);
		}

		public void info(Throwable error, Object message, Object... args) {
			log(INFO_LEVEL, error, message, args);
		}

		public void trace(Object message, Object... args) {
			log(TRACE_LEVEL, (Throwable) null, message, args);
		}

		public void trace(Throwable error, Object message, Object... args) {
			log(TRACE_LEVEL, error, message, args);
		}

		public void warn(Object message, Object... args) {
			log(WARN_LEVEL, (Throwable) null, message, args);
		}

		public void warn(Throwable error, Object message, Object... args) {
			log(WARN_LEVEL, error, message, args);
		}

		public boolean isDebugEnabled() {
			return logLevel <= DEBUG_LEVEL;
		}

		public boolean isInfoEnabled() {
			return logLevel <= INFO_LEVEL;
		}

		public boolean isErrorEnabled() {
			return logLevel <= ERROR_LEVEL;
		}

		public boolean isTraceEnabled() {
			return logLevel <= TRACE_LEVEL;
		}

		public boolean isWarnEnabled() {
			return logLevel <= WARN_LEVEL;
		}

		protected void log(int level, Throwable error, Object message, Object[] args) {
			if (level < logLevel) {
				return;
			}

			StringBuilder buffer = new StringBuilder();

			buffer.append(LEVELS[level + 1]).append("- ").append(this.name).append("\n");
			buffer.append(String.valueOf(message));

			String msg = buffer.toString();
			if (args != null && args.length > 0)
				msg = String.format(msg, args);

			if (error != null) {
				System.out.print(msg);
				error.printStackTrace(System.out);
			} else {
				System.out.println(msg);
			}
		}
	}
}

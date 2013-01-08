/*
 * Copyright (C) 2010 SUNRico Inc.
 * ------------------------------------------------------------------------------
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Visit  http://www.streeter.net for more infomation.
 *
 * ----------------------------------------------------------------------------------
 */

package com.facetime.core.utils;

import com.facetime.core.logging.Log;

/**
 * Common 缺省设置
 */
public class DefaultSettings {

	//TODO: 可放到外部配置中去读取 同时level可以放到log的初始化context中。

	/**
	 * 缺省日志类
	 */
	public static String LOG_ID = "slf4j"; //"console";
	/**
	 * 缺省日志名称
	 */
	public static String LOG_NAME = "framework";
	/**
	 * 缺省日志级别
	 */
	public static int LOG_LEVEL = Log.INFO_LEVEL;

	/**
	 * Default file Encoding (UTF8).
	 */
	public static String Encoding = StringPool.UTF_8;
	/**
	 * Default IO buffer size.
	 */
	public static int IoBufferSize = 1024 * 4;
	/**
	 * 缺省日期格式
	 */
	public static final String DatePattern = "yyyy-MM-dd";
	/**
	 * 缺省时间格式
	 */
	public static final String TimePattern = "HH:mm:ss";
	/**
	 * 缺省日期时间格式
	 */
	public static final String DateTimePattern = "yyyy-MM-dd HH:mm:ss";
}
/*
 * Copyright (C) 2010 SUNRico Inc.
 *  ------------------------------------------------------------------------------
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 *       http://www.streets.cn
 *
 *  ----------------------------------------------------------------------------------
 */

package com.facetime.core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Resolve pattern string in a specific format string
 *
 * @author dingzhenbo
 *
 */
public final class ResolveUtils {
	/**
	 * 从text中解析出中文日期字符串
	 *
	 * @param text
	 * @return
	 */
	public static String resolveCNDateStr(String text) {
		String ret = null;
		Pattern p = Pattern
				.compile("((\\d{2}|\\d{4})[年\\\\/-])*\\s{0,1}\\d{1,2}[月\\\\/-]\\s{0,1}\\d{1,2}(日|\\\\*|-*|/*)");
		Matcher m = p.matcher(text);
		if (m.find())
			ret = m.group();
		return ret;
	}

	/**
	 * 从url中解析出host
	 * @param url
	 * @return
	 */
	public static String resolveUrlHost(String url) {
		if (url == null || url.length() == 0)
			return "";
		String site = "";
		Pattern pattern = Pattern.compile("(http://)(\\S*?[^/])(/)");
		Matcher matcher = pattern.matcher(url);
		if (matcher.find()) {
			site = matcher.group(2);
			int tmpPos = site.indexOf(':');
			if (tmpPos > 0)
				site = site.substring(0, tmpPos);
		}
		return site;
	}
}

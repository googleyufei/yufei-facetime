package com.facetime.core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式相关的工具类
 */
public class RegexUtils {

	/**
	 * For regular expression validation
	 */
	public static final String REG_FOR_MAIL = "^[\\w._%+-]+@[\\w.-]+\\.[A-Za-z]{2,4}$";
	public static final String REG_FOR_MAIL2 = "[\\w._%+-]+@[\\w.-]+\\.[A-Za-z]{2,4}";
	public static final String REG_FOR_MAIL_ACCOUNT_NAME = "[\\w._%+-]+";
	public static final String REG_FOR_MAIL_ACCOUNT_PASSWORD = "^.{6,20}";
	public static final String REG_FOR_MSN_MAIL = "^[\\w._%+-]+@(msn|hotmail).[A-Za-z]{2,4}$";
	public static final String REG_FOR_YAHOO_MAIL = "^[\\w._%+-]+@yahoo.[A-Za-z]{2,4}$";
	public static final String REG_FOR_GOOGLE_MAIL = "^[\\w._%+-]+@(gmail|googlemail).[A-Za-z]{2,4}$";
	public static final String REG_FOR_NOT_NULL = ".+";
	public static final String REG_FOR_USER_ACCOUNT = "^[a-zA-Z][\\w_.-]{2,19}$";
	public static final String REG_FOR_FILE_NAME = "^[^\\/:*?\\u0022<>|]{1,250}";
	public static final String REG_FOR_VERIFY_CODE = "^[a-zA-Z0-9]{4}$";
	public static final String REG_FOR_MOBILE_NUMBER = "^([0-9]{1,3}[\\s\\-]?)?[0-9]{8,}$";
	public static final Pattern PATTERN_MAIL = Pattern.compile(REG_FOR_MAIL);

	public static final boolean isEmail(String email) {
		if (!StringUtils.isValid(email))
			return false;
		Matcher matcher = PATTERN_MAIL.matcher(email);
		if (matcher.find())
			return true;
		return false;
	}
}

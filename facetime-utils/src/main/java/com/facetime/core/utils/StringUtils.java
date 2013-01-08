package com.facetime.core.utils;

import static com.facetime.core.utils.StringPool.EMPTY;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串功能类
 */
public class StringUtils {

	public static final char LF = '\n';
	public static final char CR = '\r';

	/**
	 * 没有找到索引的确定返回值（indexOf）
	 */
	public static final int INDEX_NOT_FOUND = -1;
	/**
	 * <p>填充常数的最大限制</p>
	 */
	private static final int PAD_LIMIT = 8192;

	/**
	 * 把字符串按填充符从左边补足指定的位数
	 * @param src 要填充的字符串
	 * @param ch 用来填充的字符
	 * @param byteLen 指定填充后的字符串字节数
	 */
	public static String appendLeft(String src, char ch, int byteLen) {
		StringBuffer sb = new StringBuffer();
		byteLen = byteLen - src.getBytes().length;
		for (int i = 0; i < byteLen; i++) {
			sb.append(ch);
		}
		sb.append(src);
		return sb.toString();
	}

	public static String appendRight(String src, char ch, int byteLen) {
		StringBuffer sb = new StringBuffer();
		sb.append(src);
		byteLen = byteLen - src.getBytes().length;
		for (int i = 0; i < byteLen; i++) {
			sb.append(ch);
		}
		return sb.toString();
	}

	/**
	 * Get binary length of string
	 * @param inputString
	 * @return
	 */
	public static int bin2int(String inputString) {
		int index;
		int tempLength;
		int returnLength = 0;
		byte[] tempArray;

		if (inputString == null) {
			return returnLength;
		}

		tempArray = inputString.getBytes();
		tempLength = tempArray.length;
		for (index = 0; index < tempLength; returnLength = returnLength * 2 + tempArray[index] - '0', index++) {
			;
		}

		return returnLength;
	}

	/**
	 * Conver byte to binary
	 * @param inputByte
	 * @return
	 */
	public static String byte2bin(byte inputByte) {
		char lbinDigit[] = { '0', '1' };
		char[] lArray = { lbinDigit[inputByte >> 7 & 0x01],

		lbinDigit[inputByte >> 6 & 0x01], lbinDigit[inputByte >> 5 & 0x01], lbinDigit[inputByte >> 4 & 0x01],
				lbinDigit[inputByte >> 3 & 0x01], lbinDigit[inputByte >> 2 & 0x01], lbinDigit[inputByte >> 1 & 0x01],
				lbinDigit[inputByte & 0x01] };

		return new String(lArray);
	}

	/**
	 * Conver bytes to hex string
	 * @param inputByte
	 * @return
	 */
	public static String byteToHex(byte inputByte) {

		char hexDigitArray[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

		char[] tempArray = { hexDigitArray[inputByte >> 4 & 0x0f], hexDigitArray[inputByte & 0x0f] };

		return new String(tempArray);
	}

	/**
	 * <p>把一个英文字符串的第一个字符变成大写字母</p>
	 * <p>Capitalizes a String changing the first letter to title case as
	 * per {@link Character#toTitleCase(char)}. No other letters are changed.</p>
	 * <p/>
	 * <p>For a word based algorithm, see {@link TextUtils#capitalize(String)}.
	 * A <code>null</code> input String returns <code>null</code>.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.capitalize(null)  = null
	 * StringUtils.capitalize("")    = ""
	 * StringUtils.capitalize("cat") = "Cat"
	 * StringUtils.capitalize("cAt") = "CAt"
	 * </pre>
	 *
	 * @param str the String to capitalize, may be null
	 * @return the capitalized String, <code>null</code> if null String input
	 * @see TextUtils#capitalize(String)
	 * @see #uncapitalize(String)
	 * @since 2.0
	 */
	public static String capitalize(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return str;
		}
		return new StringBuilder(strLen).append(Character.toTitleCase(str.charAt(0))).append(str.substring(1))
				.toString();
	}

	/**
	 * Check if the string is null, if yes return ""
	 * @param rStr
	 * @return
	 */
	public static String checkNull(String rStr) {
		if (rStr == null) {
			return "";
		} else {
			return rStr.trim();
		}
	}

	/**
	 * Check if the string is null, if yes return ""
	 * @param rStr
	 * @return
	 */
	public static String checkNull(String rStr, String defaultValue) {
		if (rStr == null || rStr.equals("")) {
			return defaultValue;
		} else {
			return rStr.trim();
		}
	}

	/**
	 * <p>Checks if String contains a search character, handling <code>null</code>.
	 * This method uses {@link String#indexOf(int)}.</p>
	 * <p/>
	 * <p>A <code>null</code> or empty ("") String will return <code>false</code>.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.contains(null, *)    = false
	 * StringUtils.contains("", *)      = false
	 * StringUtils.contains("abc", 'a') = true
	 * StringUtils.contains("abc", 'z') = false
	 * </pre>
	 *
	 * @param str        the String to validate, may be null
	 * @param searchChar the character to find
	 * @return true if the String contains the search character,
	 *         false if not or <code>null</code> string input
	 * @since 2.0
	 */
	public static boolean contains(String str, char searchChar) {
		if (isEmpty(str)) {
			return false;
		}
		return str.indexOf(searchChar) >= 0;
	}

	/**
	 * <p>Checks if String contains a search String, handling <code>null</code>.
	 * This method uses {@link String#indexOf(String)}.</p>
	 * <p/>
	 * <p>A <code>null</code> String will return <code>false</code>.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.contains(null, *)     = false
	 * StringUtils.contains(*, null)     = false
	 * StringUtils.contains("", "")      = true
	 * StringUtils.contains("abc", "")   = true
	 * StringUtils.contains("abc", "a")  = true
	 * StringUtils.contains("abc", "z")  = false
	 * </pre>
	 *
	 * @param str       the String to validate, may be null
	 * @param searchStr the String to find, may be null
	 * @return true if the String contains the search String,
	 *         false if not or <code>null</code> string input
	 * @since 2.0
	 */
	public static boolean contains(String str, String searchStr) {
		if (str == null || searchStr == null) {
			return false;
		}
		return str.indexOf(searchStr) >= 0;
	}

	/**
	 * <p>Checks if String contains a search String irrespective of case,
	 * handling <code>null</code>. This method uses
	 * {@link #contains(String, String)}.</p>
	 * <p/>
	 * <p>A <code>null</code> String will return <code>false</code>.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.contains(null, *) = false
	 * StringUtils.contains(*, null) = false
	 * StringUtils.contains("", "") = true
	 * StringUtils.contains("abc", "") = true
	 * StringUtils.contains("abc", "a") = true
	 * StringUtils.contains("abc", "z") = false
	 * StringUtils.contains("abc", "A") = true
	 * StringUtils.contains("abc", "Z") = false
	 * </pre>
	 *
	 * @param str       the String to validate, may be null
	 * @param searchStr the String to find, may be null
	 * @return true if the String contains the search String irrespective of
	 *         case or false if not or <code>null</code> string input
	 */
	public static boolean containsIgnoreCase(String str, String searchStr) {
		if (str == null || searchStr == null) {
			return false;
		}
		return contains(str.toUpperCase(), searchStr.toUpperCase());
	}

	/**
	 * Conver the string from one encoding to another
	 * @param inputString
	 * @param oldEncode
	 * @param newEncode
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String converEncode(String inputString, String oldEncode, String newEncode)
			throws UnsupportedEncodingException {

		String returnString = "";

		if (inputString == null) {
			return returnString;
		}
		try {
			returnString = new String(inputString.getBytes(oldEncode), newEncode);
		} catch (UnsupportedEncodingException e) {
			throw e;
		}
		return returnString;
	}

	// Trim
	//-----------------------------------------------------------------------

	/**
	 * @param source
	 * @param c
	 * @return
	 */
	public static int count(String source, char c) {
		return count(source, c, 0);
	}

	/**
	 * @param source
	 * @param c
	 * @param start
	 * @return
	 */
	public static int count(String source, char c, int start) {
		int count = 0;
		int j = start;
		while (true) {
			int i = source.indexOf(c, j);
			if (i == -1) {
				break;
			}
			count++;
			j = i + 1;
		}
		return count;
	}

	/**
	 * Counts substring occurrences in a source string.
	 * <p/>
	 * <pre>
	 * StringUtils.countMatches(null, *)       = 0
	 * StringUtils.countMatches("", *)         = 0
	 * StringUtils.countMatches("abba", null)  = 0
	 * StringUtils.countMatches("abba", "")    = 0
	 * StringUtils.countMatches("abba", "a")   = 2
	 * StringUtils.countMatches("abba", "ab")  = 1
	 * StringUtils.countMatches("abba", "xxx") = 0
	 * </pre>
	 *
	 * @param str the String to validate, may be null
	 * @param sub the substring to count, may be null
	 * @return the number of occurrences, 0 if either String is <code>null</code>
	 */
	public static int count(String str, String sub) {
		return count(str, sub, 0);
	}

	/**
	 * @param source
	 * @param sub
	 * @param start  start index
	 * @return
	 */
	public static int count(String source, String sub, int start) {
		int count = 0;
		int j = start;
		int sublen = sub.length();
		if (sublen == 0) {
			return 0;
		}
		while (true) {
			int i = source.indexOf(sub, j);
			if (i == -1) {
				break;
			}
			count++;
			j = i + sublen;
		}
		return count;
	}

	/**
	 * 分解字符串，返回String[] 在 delim 参数中的字符是分隔标记的分隔符
	 */
	public final static String[] decomposerString(String str, String delim) {
		String sRst[] = null;
		int j = 0;

		StringTokenizer st = new StringTokenizer(str, delim);
		int i = st.countTokens();
		if (i == 0) {
			return null;
		}
		sRst = new String[i];
		while (st.hasMoreTokens()) {
			sRst[j++] = st.nextToken().trim();
		}
		return sRst;
	}

	/**
	 * <p>检查一个字符串指定字符串结尾</p>
	 * <p>Check if a String ends with a specified suffix.</p>
	 * <p/>
	 * <p><code>null</code>s are handled without exceptions. Two <code>null</code>
	 * references are considered to be equal. The comparison is case sensitive.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.endsWith(null, null)      = true
	 * StringUtils.endsWith(null, "abcdef")  = false
	 * StringUtils.endsWith("def", null)     = false
	 * StringUtils.endsWith("def", "abcdef") = true
	 * StringUtils.endsWith("def", "ABCDEF") = false
	 * </pre>
	 *
	 * @param str    the String to validate, may be null
	 * @param suffix the suffix to find, may be null
	 * @return <code>true</code> if the String ends with the suffix, case sensitive, or
	 *         both <code>null</code>
	 * @see java.lang.String#endsWith(String)
	 * @since 2.4
	 */
	public static boolean endsWith(String str, String suffix) {
		return endsWith(str, suffix, false);
	}

	// IndexOf
	//-----------------------------------------------------------------------

	/**
	 * <p>检查一个字符串指定字符串结尾</p>
	 * <p>Check if a String ends with a specified suffix (optionally case insensitive).</p>
	 *
	 * @param str        the String to validate, may be null
	 * @param suffix     the suffix to find, may be null
	 * @param ignoreCase inidicates whether the compare should ignore case
	 *                   (case insensitive) or not.
	 * @return <code>true</code> if the String starts with the prefix or
	 *         both <code>null</code>
	 * @see java.lang.String#endsWith(String)
	 */
	private static boolean endsWith(String str, String suffix, boolean ignoreCase) {
		if (str == null || suffix == null) {
			return str == null && suffix == null;
		}
		if (suffix.length() > str.length()) {
			return false;
		}
		int strOffset = str.length() - suffix.length();
		return str.regionMatches(ignoreCase, strOffset, suffix, 0, suffix.length());
	}

	/**
	 * <p>检查一个字符串指定字符结尾</p>
	 * Returns if string ends with provided character.
	 */
	public static boolean endsWithChar(String s, char c) {
		if (s.length() == 0) {
			return false;
		}
		return s.charAt(s.length() - 1) == c;
	}

	/**
	 * <p>检查一个字符串指定字符串结尾,忽略大小写</p>
	 * <p>Case insensitive validate if a String ends with a specified suffix.</p>
	 * <p/>
	 * <p><code>null</code>s are handled without exceptions. Two <code>null</code>
	 * references are considered to be equal. The comparison is case insensitive.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.endsWithIgnoreCase(null, null)      = true
	 * StringUtils.endsWithIgnoreCase(null, "abcdef")  = false
	 * StringUtils.endsWithIgnoreCase("def", null)     = false
	 * StringUtils.endsWithIgnoreCase("def", "abcdef") = true
	 * StringUtils.endsWithIgnoreCase("def", "ABCDEF") = false
	 * </pre>
	 *
	 * @param str    the String to validate, may be null
	 * @param suffix the suffix to find, may be null
	 * @return <code>true</code> if the String ends with the suffix, case insensitive, or
	 *         both <code>null</code>
	 * @see java.lang.String#endsWith(String)
	 * @since 2.4
	 */
	public static boolean endsWithIgnoreCase(String str, String suffix) {
		return endsWith(str, suffix, true);
	}

	public static boolean equalsAnyOf(Enum<?> src, String[] strArray) {
		return equalsAnyOf(src.name(), strArray);
	}

	public static boolean equalsAnyOf(Enum<?> src, Enum<?>[] strArray) {
		for (Enum<?> str : strArray) {
			if (str.name().equalsIgnoreCase(src.name())) {
				return true;
			}
		}
		return false;
	}

	public static boolean equalsAnyOf(String src, String[] strArray) {
		for (String str : strArray) {
			if (str.equalsIgnoreCase(src)) {
				return true;
			}
		}
		return false;
	}

	// Equals
	//-----------------------------------------------------------------------
	/**
	 * <p>Compares two Strings, returning <code>true</code> if they are equal.</p>
	 * <p/>
	 * <p><code>null</code>s are handled without exceptions. Two <code>null</code>
	 * references are considered to be equal. The comparison is case sensitive.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.equals(null, null)   = true
	 * StringUtils.equals(null, "abc")  = false
	 * StringUtils.equals("abc", null)  = false
	 * StringUtils.equals("abc", "abc") = true
	 * StringUtils.equals("abc", "ABC") = false
	 * </pre>
	 *
	 * @param str1 the first String, may be null
	 * @param str2 the second String, may be null
	 * @return <code>true</code> if the Strings are equal, case sensitive, or
	 *         both <code>null</code>
	 * @see java.lang.String#equals(Object)
	 */
	public static boolean equals(String str1, String str2) {
		return str1 == null ? str2 == null : str1.equals(str2);
	}

	// LastIndexOf
	//-----------------------------------------------------------------------

	/**
	 * <p>Compares two Strings, returning <code>true</code> if they are equal ignoring
	 * the case.</p>
	 * <p/>
	 * <p><code>null</code>s are handled without exceptions. Two <code>null</code>
	 * references are considered equal. Comparison is case insensitive.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.equalsIgnoreCase(null, null)   = true
	 * StringUtils.equalsIgnoreCase(null, "abc")  = false
	 * StringUtils.equalsIgnoreCase("abc", null)  = false
	 * StringUtils.equalsIgnoreCase("abc", "abc") = true
	 * StringUtils.equalsIgnoreCase("abc", "ABC") = true
	 * </pre>
	 *
	 * @param str1 the first String, may be null
	 * @param str2 the second String, may be null
	 * @return <code>true</code> if the Strings are equal, case insensitive, or
	 *         both <code>null</code>
	 * @see java.lang.String#equalsIgnoreCase(String)
	 */
	public static boolean equalsIgnoreCase(String str1, String str2) {
		return str1 == null ? str2 == null : str1.equalsIgnoreCase(str2);
	}

	/**
	 * 去掉字符串里面跟XML标签冲突的字符
	 * @param input 要处理的字符串
	 * @return 替换后的字符串
	 */
	public static String escapeXMLstr(String input) {
		if (input == null) {
			return null;
		}

		StringBuffer output = new StringBuffer();
		int len = input.length();
		for (int i = 0; i < len; i++) {
			char ch = input.charAt(i);
			if (ch == '&') {
				output.append("&amp;");
				continue;
			}
			if (ch == '<') {
				output.append("&lt;");
				continue;
			}
			if (ch == '>') {
				output.append("&gt;");
				continue;
			}
			if (ch == '\'') {
				output.append("&apos;");
				continue;
			}
			if (ch == '"') {
				output.append("&quot;");
			} else {
				output.append(ch);
			}
		}

		return output.toString();
	}

	/**
	 * 限制
	 * @param label
	 * @param maxLen
	 * @return
	 */
	public static String fitLabel(String label, int maxLen) {
		if (label.length() >= maxLen) {
			return label.substring(0, maxLen) + "..";
		}

		return label;
	}

	/**
	 * Replace the special char in html
	 * @param inputString
	 * @return
	 */
	public static String fixHTML(String inputString) {

		int index = 0;
		char tempChar;
		StringBuffer stringBuffer;

		if (inputString == null) {
			return "";
		}
		if (inputString.trim().equals("")) {
			return inputString;
		}

		stringBuffer = new StringBuffer(inputString);

		while (index < stringBuffer.length()) {
			if ((tempChar = stringBuffer.charAt(index)) == '"') {
				stringBuffer.replace(index, index + 1, "&#34;");
				index += 5;
				continue;
			}
			if (tempChar == '\'') { // "'"
				stringBuffer.replace(index, index + 1, "&#39;");
				index += 5;
				continue;
			}
			if (tempChar == '&') {
				try {
					if ((tempChar = stringBuffer.charAt(index + 1)) == '#') {
						index += 2;
						continue;
					}
				} catch (StringIndexOutOfBoundsException ex) {
					// needn't to do anything
				}

				stringBuffer.replace(index, index + 1, "&amp;");
				index += 5;
				continue;
			}
			if (tempChar == '<') {
				stringBuffer.replace(index, index + 1, "&lt;");
				index += 4;
				continue;
			}
			if (tempChar == '>') {
				stringBuffer.replace(index, index + 1, "&gt;");
				index += 4;
				continue;
			}
			index++;
		}
		return stringBuffer.toString();
	}

	/**
	 * Conver "'" to "''"
	 * @param inputString
	 * @return
	 */
	public static String fixSQL(String inputString) {

		int index = 0;
		StringBuffer stringBuffer;

		if (inputString == null) {
			return "";
		}
		if (inputString.trim().equals("")) {
			return inputString;
		}

		stringBuffer = new StringBuffer(inputString);

		while (index < stringBuffer.length()) {
			if (stringBuffer.charAt(index) == '\'') {
				stringBuffer.replace(index, index + 1, "''");
				index += 2;
				continue;
			}
			index++;
		}
		return stringBuffer.toString();
	}

	/**
	 * Replace some special character in the input string
	 * @param inputString
	 * @return
	 */
	public static String fixWML(String inputString) {
		int index = 0;
		char ch;

		if (inputString == null) {
			return "";
		}
		StringBuffer strbuff = new StringBuffer(inputString);

		while (index < strbuff.length()) {
			if ((ch = strbuff.charAt(index)) == '"') {
				strbuff.replace(index, index + 1, "&#34;");
				index += 5;
				continue;
			}

			if (ch == '\'') {
				strbuff.replace(index, index + 1, "&#39;");
				index += 5;
				continue;
			}

			if (ch == '$') {
				strbuff.replace(index, index + 1, "$$");
				index += 2;
				continue;
			}

			if (ch == '&') {
				strbuff.replace(index, index + 1, "&amp;");
				index += 5;
				continue;
			}

			if (ch == '<') {
				strbuff.replace(index, index + 1, "&lt;");
				index += 4;
				continue;
			}

			if (ch == '>') {
				strbuff.replace(index, index + 1, "&gt;");
				index += 4;
				continue;
			}

			index++;
		}

		return strbuff.toString();
	}

	// Character Tests
	//-----------------------------------------------------------------------

	public static final String formatDecimal(double decimal, String format) {
		java.text.DecimalFormat nf = new java.text.DecimalFormat(format);
		return nf.format(decimal);
	}

	/**
	 * @param strDecimal
	 * @param format like ###.### or ###,###.## or $###,###.##
	 * @return
	 */
	public static final String formatDecimal(String strDecimal, String format) {
		java.text.DecimalFormat nf = new java.text.DecimalFormat(format);
		return nf.format(Double.parseDouble(strDecimal));
	}

	/**
	 * <p>比较字符串数组中的所有字符串，并返回初始序列是共同所有的字符串</p>
	 * <p>Compares all Strings in an array and returns the initial sequence of
	 * characters that is common to all of them.</p>
	 * <p/>
	 * <p>For example,
	 * <code>getCommonPrefix(new String[] {"i am a machine", "i am a robot"}) -> "i am a "</code></p>
	 * <p/>
	 * <pre>
	 * StringUtils.getCommonPrefix(null) = ""
	 * StringUtils.getCommonPrefix(new String[] {}) = ""
	 * StringUtils.getCommonPrefix(new String[] {"abc"}) = "abc"
	 * StringUtils.getCommonPrefix(new String[] {null, null}) = ""
	 * StringUtils.getCommonPrefix(new String[] {"", ""}) = ""
	 * StringUtils.getCommonPrefix(new String[] {"", null}) = ""
	 * StringUtils.getCommonPrefix(new String[] {"abc", null, null}) = ""
	 * StringUtils.getCommonPrefix(new String[] {null, null, "abc"}) = ""
	 * StringUtils.getCommonPrefix(new String[] {"", "abc"}) = ""
	 * StringUtils.getCommonPrefix(new String[] {"abc", ""}) = ""
	 * StringUtils.getCommonPrefix(new String[] {"abc", "abc"}) = "abc"
	 * StringUtils.getCommonPrefix(new String[] {"abc", "a"}) = "a"
	 * StringUtils.getCommonPrefix(new String[] {"ab", "abxyz"}) = "ab"
	 * StringUtils.getCommonPrefix(new String[] {"abcde", "abxyz"}) = "ab"
	 * StringUtils.getCommonPrefix(new String[] {"abcde", "xyz"}) = ""
	 * StringUtils.getCommonPrefix(new String[] {"xyz", "abcde"}) = ""
	 * StringUtils.getCommonPrefix(new String[] {"i am a machine", "i am a robot"}) = "i am a "
	 * </pre>
	 *
	 * @param strs array of String objects, entries may be null
	 * @return the initial sequence of characters that are common to all Strings
	 *         in the array; empty String if the array is null, the elements are all null
	 *         or if there is no common prefix.
	 * @since 2.4
	 */
	public static String getCommonPrefix(String[] strs) {
		if (strs == null || strs.length == 0) {
			return EMPTY;
		}
		int smallestIndexOfDiff = indexOfDifference(strs);
		if (smallestIndexOfDiff == -1) {
			// all strings were identical
			if (strs[0] == null) {
				return EMPTY;
			}
			return strs[0];
		} else if (smallestIndexOfDiff == 0) {
			// there were no common initial characters
			return EMPTY;
		} else {
			// we found a common initial character sequence
			return strs[0].substring(0, smallestIndexOfDiff);
		}
	}

	// 字符串以;号换行
	public final static String getNewLine(String string) {
		String br = ";<br>";
		StringBuffer sb = new StringBuffer();
		String[] str = StringUtils.split(string, ";");
		for (String element : str) {
			sb.append(element);
			sb.append(br);
		}
		return sb.toString();
	}

	/**
	 * 如果dest参数非空, 则返回dest, 否则返回defaultVal
	 * @param dest
	 * @param defaultVal
	 * @return
	 */
	public static final String getValidStr(String dest, String defaultVal) {
		if (isValid(dest)) {
			return dest.trim();
		}
		return defaultVal;
	}

	/**
	 * 如果dest参数非空, 则返回dest, 否则返回""
	 * @param dest
	 * @return
	 */
	public static final String getValiStr(String dest) {
		return getValidStr(dest, "");
	}

	/**
	 * Check if the string contains chinese
	 * @param inputString
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static boolean hasChinese(String inputString) throws UnsupportedEncodingException {
		byte[] tempArray;
		String tempCode = new String();
		boolean returnFlag = false;

		try {
			// lUniStr = new String(inputString.getBytes(), "big5");
			tempArray = inputString.getBytes("UnicodeBig");
		} catch (UnsupportedEncodingException e) {
			throw e;
		}
		for (int lk = 0; lk < tempArray.length; lk++) {
			if (tempArray[lk] == 0) {
				lk++;
			} else {
				tempCode = byteToHex(tempArray[lk++]) + byteToHex(tempArray[lk]);

				if (!tempCode.equals("feff")) {
					returnFlag = true;
					break;
				}
			}
		}
		return returnFlag;
	}

	/**
	 * Check if the string contains space
	 * @param inputString
	 * @return
	 */
	public static boolean hasSpace(String inputString) {
		boolean returnFlag = true;

		if (inputString.indexOf(" ") < 0) {
			returnFlag = false;
		}

		return returnFlag;
	}

	/**
	 * <p>Finds the first index within a String, handling <code>null</code>.
	 * This method uses {@link String#indexOf(int)}.</p>
	 * <p/>
	 * <p>A <code>null</code> or empty ("") String will return <code>-1</code>.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.indexOf(null, *)         = -1
	 * StringUtils.indexOf("", *)           = -1
	 * StringUtils.indexOf("aabaabaa", 'a') = 0
	 * StringUtils.indexOf("aabaabaa", 'b') = 2
	 * </pre>
	 *
	 * @param str        the String to validate, may be null
	 * @param searchChar the character to find
	 * @return the first index of the search character,
	 *         -1 if no match or <code>null</code> string input
	 * @since 2.0
	 */
	public static int indexOf(String str, char searchChar) {
		if (isEmpty(str)) {
			return -1;
		}
		return str.indexOf(searchChar);
	}

	/**
	 * KMP index & String index mix indexof
	 *
	 * @param str
	 * @param sub
	 */
	public static int indexOf(String str, String sub) {
		if (sub.length() > 20) {
			return indexOf(str, sub, 0, str.length());
		} else {
			return str.indexOf(sub);
		}
	}

	/**
	 * <p>Finds the first index within a String from a start position,
	 * handling <code>null</code>.
	 * This method uses D.E.Knuth V.R.Pratt & J.H.Morris matching algorithms.</p>
	 * <p/>
	 * <p>A <code>null</code> String will return <code>-1</code>.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.indexOf(null, *)          = -1
	 * StringUtils.indexOf(*, null)          = -1
	 * StringUtils.indexOf("", "")           = 0
	 * StringUtils.indexOf("aabaabaa", "a")  = 0
	 * StringUtils.indexOf("aabaabaa", "b")  = 2
	 * StringUtils.indexOf("aabaabaa", "ab") = 1
	 * StringUtils.indexOf("aabaabaa", "")   = 0
	 * </pre>
	 *
	 * @param str        the String to validate, may be null
	 * @param sub        the String to find, may be null
	 * @param str        Matching String
	 * @param sub        Pattern String
	 * @param startIndex match begin index
	 * @return index of matching pattern
	 * @author dzb2k9
	 * @since 2.0
	 *        D.E.Knuth V.R.Pratt & J.H.Morris matching algorithms(KMP)
	 */
	public static int indexOf(String str, String sub, int startIndex, int endIndex) {

		if (str == null || sub == null) {
			return -1;
		}

		if (sub.equals("")) {
			return 0;
		}
		//---------------------------
		int l_p = sub.length();
		int l_s = str.length();

		if (l_p > l_s) {
			return -1;
		}
		if (endIndex > 0 && l_s > endIndex) {
			l_s = endIndex;
		}
		//---------------------------
		// compute next[]        
		int[] next = new int[l_p];
		int i = 0, j = -1;

		next[0] = -1;
		while (i < l_p - 1) {
			if (j == -1 || sub.charAt(i) == sub.charAt(j)) {
				i++;
				j++;
				if (sub.charAt(i) != sub.charAt(j)) {
					next[i] = j;
				} else {
					next[i] = next[j];
				}
			} else {
				j = next[j];
			}
		}
		//-----------------------------
		// matching         
		i = startIndex;
		j = 0;
		while (i < l_s && j < l_p) {
			if (j == -1 || str.charAt(i) == sub.charAt(j)) {
				i++;
				j++;
			} else {
				j = next[j];
			}
		}

		if (j == l_p) {
			return i - j;
		} else {
			return -1;
		}
	}

	/**
	 * <p>Compares all Strings in an array and returns the index at which the
	 * Strings begin to differ.</p>
	 * <p/>
	 * <p>For example,
	 * <code>indexOfDifference(new String[] {"i am a machine", "i am a robot"}) -> 7</code></p>
	 * <p/>
	 * <pre>
	 * StringUtils.indexOfDifference(null) = -1
	 * StringUtils.indexOfDifference(new String[] {}) = -1
	 * StringUtils.indexOfDifference(new String[] {"abc"}) = -1
	 * StringUtils.indexOfDifference(new String[] {null, null}) = -1
	 * StringUtils.indexOfDifference(new String[] {"", ""}) = -1
	 * StringUtils.indexOfDifference(new String[] {"", null}) = 0
	 * StringUtils.indexOfDifference(new String[] {"abc", null, null}) = 0
	 * StringUtils.indexOfDifference(new String[] {null, null, "abc"}) = 0
	 * StringUtils.indexOfDifference(new String[] {"", "abc"}) = 0
	 * StringUtils.indexOfDifference(new String[] {"abc", ""}) = 0
	 * StringUtils.indexOfDifference(new String[] {"abc", "abc"}) = -1
	 * StringUtils.indexOfDifference(new String[] {"abc", "a"}) = 1
	 * StringUtils.indexOfDifference(new String[] {"ab", "abxyz"}) = 2
	 * StringUtils.indexOfDifference(new String[] {"abcde", "abxyz"}) = 2
	 * StringUtils.indexOfDifference(new String[] {"abcde", "xyz"}) = 0
	 * StringUtils.indexOfDifference(new String[] {"xyz", "abcde"}) = 0
	 * StringUtils.indexOfDifference(new String[] {"i am a machine", "i am a robot"}) = 7
	 * </pre>
	 *
	 * @param strs array of strings, entries may be null
	 * @return the index where the strings begin to differ; -1 if they are all equal
	 * @since 2.4
	 */
	public static int indexOfDifference(String[] strs) {
		if (strs == null || strs.length <= 1) {
			return -1;
		}
		boolean anyStringNull = false;
		boolean allStringsNull = true;
		int arrayLen = strs.length;
		int shortestStrLen = Integer.MAX_VALUE;
		int longestStrLen = 0;

		// find the min and max string lengths; this avoids checking to make
		// sure we are not exceeding the length of the string each time through
		// the bottom loop.
		for (int i = 0; i < arrayLen; i++) {
			if (strs[i] == null) {
				anyStringNull = true;
				shortestStrLen = 0;
			} else {
				allStringsNull = false;
				shortestStrLen = Math.min(strs[i].length(), shortestStrLen);
				longestStrLen = Math.max(strs[i].length(), longestStrLen);
			}
		}

		// handle lists containing all nulls or all empty strings
		if (allStringsNull || longestStrLen == 0 && !anyStringNull) {
			return -1;
		}

		// handle lists containing some nulls or some empty strings
		if (shortestStrLen == 0) {
			return 0;
		}

		// find the position with the first difference across all strings
		int firstDiff = -1;
		for (int stringPos = 0; stringPos < shortestStrLen; stringPos++) {
			char comparisonChar = strs[0].charAt(stringPos);
			for (int arrayPos = 1; arrayPos < arrayLen; arrayPos++) {
				if (strs[arrayPos].charAt(stringPos) != comparisonChar) {
					firstDiff = stringPos;
					break;
				}
			}
			if (firstDiff != -1) {
				break;
			}
		}

		if (firstDiff == -1 && shortestStrLen != longestStrLen) {
			// we compared all of the characters up to the length of the
			// shortest string and didn't find a match, but the string lengths
			// vary, so return the length of the shortest string.
			return shortestStrLen;
		}
		return firstDiff;
	}

	/**
	 * Finds first index of a substring in the given source string and range with
	 * ignored case.
	 *
	 * @param src        source string for examination
	 * @param sub        substring to find
	 * @param startIndex starting index from where search begins
	 * @param endIndex   endint index
	 * @return index of founded substring or -1 if substring is not found
	 */
	public static int indexOfIgnoreCase(String src, String sub, int startIndex, int endIndex) {
		if (startIndex < 0) {
			startIndex = 0;
		}
		int srclen = src.length();
		if (endIndex > srclen) {
			endIndex = srclen;
		}

		int sublen = sub.length();
		if (sublen == 0) {
			return startIndex > srclen ? srclen : startIndex;
		}
		sub = sub.toLowerCase();
		int total = endIndex - sublen + 1;
		char c = sub.charAt(0);
		mainloop: for (int i = startIndex; i < total; i++) {
			if (Character.toLowerCase(src.charAt(i)) != c) {
				continue;
			}
			int j = 1;
			int k = i + 1;
			while (j < sublen) {
				char source = Character.toLowerCase(src.charAt(k));
				if (sub.charAt(j) != source) {
					continue mainloop;
				}
				j++;
				k++;
			}
			return i;
		}
		return -1;
	}

	/**
	 * Check if the charater is alpha
	 * @param inputChar
	 * @return
	 */
	public static boolean isAlpha(char inputChar) {
		if (inputChar >= 'a' && inputChar <= 'z') {
			return true;
		}
		if (inputChar >= 'A' && inputChar <= 'Z') {
			return true;
		}

		return false;
	}

	/**
	 * <p>Checks if the String contains only unicode letters.</p>
	 * <p>检查字符串是否全部有字母组成.</p>
	 * <p/>
	 * <p><code>null</code> will return <code>false</code>.
	 * An empty String ("") will return <code>true</code>.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.isAlpha(null)   = false
	 * StringUtils.isAlpha("")     = true
	 * StringUtils.isAlpha("  ")   = false
	 * StringUtils.isAlpha("abc")  = true
	 * StringUtils.isAlpha("ab2c") = false
	 * StringUtils.isAlpha("ab-c") = false
	 * </pre>
	 *
	 * @param str the String to validate, may be null
	 * @return <code>true</code> if only contains letters, and is non-null
	 */
	public static boolean isAlpha(String str) {
		if (str == null) {
			return false;
		}
		for (int i = str.length(); i > 0; i--) {
			if (Character.isLetter(str.charAt(i - 1)) == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * <p>Checks if a String is whitespace, empty ("") or null.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.isBlank(null)      = true
	 * StringUtils.isBlank("")        = true
	 * StringUtils.isBlank(" ")       = true
	 * StringUtils.isBlank("bob")     = false
	 * StringUtils.isBlank("  bob  ") = false
	 * </pre>
	 *
	 * @param str the String to validate, may be null
	 * @return <code>true</code> if the String is null, empty or whitespace
	 * @since 2.0
	 */
	public static boolean isBlank(CharSequence str) {
		int len;
		if (str == null || (len = str.length()) == 0) {
			return true;
		}
		for (int i = 0; i < len; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static boolean isBlank(String str) {
		int len;
		if (str == null || (len = str.length()) == 0) {
			return true;
		}
		for (int i = 0; i < len; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	// Splitting
	//-----------------------------------------------------------------------

	/**
	 * <p>判断字符否是中文字符 </p>
	 * @param c 
	 * 		要判断的字符
	 * @return boolean 
	 * 				
	 */
	public static boolean isChineseChar(char c) {
		return c >= '\u4E00' && c <= '\u9FA5';
	}

	/**
	 * <p>检查字符串是否有数字[0~9]组成</p>
	 * <p>Checks whether the <code>String</code> contains only
	 * digit characters.</p>
	 * <pre>
	 * StringUtils.isDigits("")     = false;
	 * StringUtils.isDigits(null)     = false;
	 * StringUtils.isDigits("123")     = true;
	 * </pre> 
	 * <p><code>Null</code> and empty String will return
	 * <code>false</code>.</p>
	 *
	 * @param str the <code>String</code> to validate
	 * @return <code>true</code> if str contains only unicode numeric
	 */
	public static boolean isDigits(String str) {
		if (StringUtils.isEmpty(str)) {
			return false;
		}
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * <p>不做Trim动作来，检查一个字符串是否为 ("") 或者为 null.如果需要Trim()动作配合，使用isBlank()</p>
	 * <pre>
	 * StringUtils.isEmpty(null)      = true
	 * StringUtils.isEmpty("")        = true
	 * StringUtils.isEmpty(" ")       = false
	 * StringUtils.isEmpty("bob")     = false
	 * StringUtils.isEmpty("  bob  ") = false
	 * </pre>
	 * @param str 
	 * 			需要检查的字符串
	 * @return 是否为空 		
	 */
	public static boolean isEmpty(CharSequence str) {
		return str == null || str.length() == 0;
	}

	public static final boolean isEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}

	// -----------------------------------------------------------------------

	/**
	 * <p>是否包括中文字符 </p>
	 * @param str 
	 * 		要判断的字符串
	 * @return boolean 		
	 * 		返回的boolean值:空或者不包括中文字符返回false;
	 */
	public static boolean isIncludeChinese(String str) {
		if (StringUtils.isEmpty(str)) {
			return false;
		}
		Pattern p = Pattern.compile("[\u4E00-\u9FA5]+");
		Matcher m = p.matcher(str);
		return m.find();
	}

	/**
	 * <p>Checks if a String is not empty (""), not null and not whitespace only.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.isNotBlank(null)      = false
	 * StringUtils.isNotBlank("")        = false
	 * StringUtils.isNotBlank(" ")       = false
	 * StringUtils.isNotBlank("bob")     = true
	 * StringUtils.isNotBlank("  bob  ") = true
	 * </pre>
	 *
	 * @param str the String to validate, may be null
	 * @return <code>true</code> if the String is
	 *         not empty and not null and not whitespace
	 * @since 2.0
	 */
	public static boolean isNotBlank(CharSequence str) {
		return !StringUtils.isBlank(str);
	}

	/**
	 * <p>Checks if a String is not empty (""), not null and not whitespace only.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.isNotBlank(null)      = false
	 * StringUtils.isNotBlank("")        = false
	 * StringUtils.isNotBlank(" ")       = false
	 * StringUtils.isNotBlank("bob")     = true
	 * StringUtils.isNotBlank("  bob  ") = true
	 * </pre>
	 *
	 * @param str the String to validate, may be null
	 * @return <code>true</code> if the String is
	 *         not empty and not null and not whitespace
	 * @since 2.0
	 */
	public static boolean isNotBlank(String str) {
		return !StringUtils.isBlank(str);
	}

	/**
	 * <p>Checks if a String is not empty ("") and not null.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.isNotEmpty(null)      = false
	 * StringUtils.isNotEmpty("")        = false
	 * StringUtils.isNotEmpty(" ")       = true
	 * StringUtils.isNotEmpty("bob")     = true
	 * StringUtils.isNotEmpty("  bob  ") = true
	 * </pre>
	 *
	 * @param str the String to validate, may be null
	 * @return <code>true</code> if the String is not empty and not null
	 */
	public static boolean isNotEmpty(CharSequence str) {
		return !StringUtils.isEmpty(str);
	}

	/**
	 * <p>检查字符串是否一个合法的数字</p> 
	 * <p>Checks whether the String a valid Java number.</p>
	 * <p>Valid numbers include hexadecimal marked with the <code>0x</code>
	 * qualifier, scientific notation and numbers marked with a type
	 * qualifier (e.g. 123L).</p>
	 * <pre>
	 * StringUtils.isNumeric("")     = false;
	 * StringUtils.isNumeric(null)     = false;
	 * StringUtils.isNumeric("123")     = true;
	 * StringUtils.isNumeric("123f")     = true;
	 * StringUtils.isNumeric("123l")     = true;
	 * </pre> 
	 * <p><code>Null</code> and empty String will return
	 * <code>false</code>.</p>
	 *
	 * @param str the <code>String</code> to validate
	 * @return <code>true</code> if the string is a correctly formatted number
	 */
	public static boolean isNumeric(String str) {
		if (StringUtils.isEmpty(str)) {
			return false;
		}
		char[] chars = str.toCharArray();
		int sz = chars.length;
		boolean hasExp = false;
		boolean hasDecPoint = false;
		boolean allowSigns = false;
		boolean foundDigit = false;
		// deal with any possible sign up front
		int start = chars[0] == '-' ? 1 : 0;
		if (sz > start + 1) {
			if (chars[start] == '0' && chars[start + 1] == 'x') {
				int i = start + 2;
				if (i == sz) {
					return false; // str == "0x"
				}
				// checking hex (it can't be anything else)
				for (; i < chars.length; i++) {
					if ((chars[i] < '0' || chars[i] > '9') && (chars[i] < 'a' || chars[i] > 'f')
							&& (chars[i] < 'A' || chars[i] > 'F')) {
						return false;
					}
				}
				return true;
			}
		}
		sz--; // don't want to loop to the last char, validate it afterwords
		// for type qualifiers
		int i = start;
		// loop to the next to last char or to the last char if we need another digit to
		// make a valid number (e.g. chars[0..5] = "1234E")
		while (i < sz || i < sz + 1 && allowSigns && !foundDigit) {
			if (chars[i] >= '0' && chars[i] <= '9') {
				foundDigit = true;
				allowSigns = false;

			} else if (chars[i] == '.') {
				if (hasDecPoint || hasExp) {
					// two decimal points or dec in exponent   
					return false;
				}
				hasDecPoint = true;
			} else if (chars[i] == 'e' || chars[i] == 'E') {
				// we've already taken care of hex.
				if (hasExp) {
					// two E's
					return false;
				}
				if (!foundDigit) {
					return false;
				}
				hasExp = true;
				allowSigns = true;
			} else if (chars[i] == '+' || chars[i] == '-') {
				if (!allowSigns) {
					return false;
				}
				allowSigns = false;
				foundDigit = false; // we need a digit after the E
			} else {
				return false;
			}
			i++;
		}
		if (i < chars.length) {
			if (chars[i] >= '0' && chars[i] <= '9') {
				// no type qualifier, OK
				return true;
			}
			if (chars[i] == 'e' || chars[i] == 'E') {
				// can't have an E at the last byte
				return false;
			}
			if (chars[i] == '.') {
				if (hasDecPoint || hasExp) {
					// two decimal points or dec in exponent
					return false;
				}
				// single trailing decimal point after non-exponent is ok
				return foundDigit;
			}
			if (!allowSigns && (chars[i] == 'd' || chars[i] == 'D' || chars[i] == 'f' || chars[i] == 'F')) {
				return foundDigit;
			}
			if (chars[i] == 'l' || chars[i] == 'L') {
				// not allowing L with an exponent
				return foundDigit && !hasExp;
			}
			// last character is illegal
			return false;
		}
		// allowSigns is true iff the val ends in 'E'
		// found digit it to make sure weird stuff like '.' and '1E-' doesn't pass
		return !allowSigns && foundDigit;
	}

	public static final boolean isValid(String str) {
		return str != null && str.trim().length() > 0;
	}
	
	public static final boolean hasText(String str) {
		return str != null && str.trim().length() > 0;
	}

	/**
	 * Joins together some number of elements to form a comma separated list.
	 */
	public static String join(List<?> elements) {
		return join(elements, ", ");
	}

	/**
	 * Joins together some number of elements. If a symbol in the list is the empty string, it is replaced with the
	 * string "(blank)".
	 *
	 * @param elements objects to be joined together
	 * @param sep      used between elements when joining
	 */
	public static String join(List<?> elements, String sep) {
		switch (elements.size()) {
		case 0:
			return "";

		case 1:
			return elements.get(0).toString();

		default:

			StringBuilder buffer = new StringBuilder();
			boolean first = true;

			for (Object o : elements) {
				if (!first) {
					buffer.append(sep);
				}
				String str = String.valueOf(o);
				if (str.length() == 0) {
					str = "(blank)";
				}
				buffer.append(str);
				first = false;
			}

			return buffer.toString();
		}
	}

	/**
	 * <p>Joins the elements of the provided array into a single String
	 * containing the provided list of elements.</p>
	 * <p/>
	 * <p>No separator is added to the joined String.
	 * Null objects or empty strings within the array are represented by
	 * empty strings.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.joinBy(null)            = null
	 * StringUtils.joinBy([])              = ""
	 * StringUtils.joinBy([null])          = ""
	 * StringUtils.joinBy(["a", "b", "c"]) = "abc"
	 * StringUtils.joinBy([null, "", "a"]) = "a"
	 * </pre>
	 *
	 * @param array the array of values to joinBy together, may be null
	 * @return the joined String, <code>null</code> if null array input
	 * @since 2.0
	 */
	public static String join(Object[] array) {
		return join(array, null);
	}

	/**
	 * <p>Joins the elements of the provided array into a single String
	 * containing the provided list of elements.</p>
	 * <p/>
	 * <p>No delimiter is added before or after the list.
	 * Null objects or empty strings within the array are represented by
	 * empty strings.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.joinBy(null, *)               = null
	 * StringUtils.joinBy([], *)                 = ""
	 * StringUtils.joinBy([null], *)             = ""
	 * StringUtils.joinBy(["a", "b", "c"], ';')  = "a;b;c"
	 * StringUtils.joinBy(["a", "b", "c"], null) = "abc"
	 * StringUtils.joinBy([null, "", "a"], ';')  = ";;a"
	 * </pre>
	 *
	 * @param array     the array of values to joinBy together, may be null
	 * @param separator the separator character to use
	 * @return the joined String, <code>null</code> if null array input
	 * @since 2.0
	 */
	public static String join(Object[] array, char separator) {
		if (array == null) {
			return null;
		}

		return join(array, separator, 0, array.length);
	}

	/**
	 * <p>Joins the elements of the provided array into a single String
	 * containing the provided list of elements.</p>
	 * <p/>
	 * <p>No delimiter is added before or after the list.
	 * Null objects or empty strings within the array are represented by
	 * empty strings.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.joinBy(null, *)               = null
	 * StringUtils.joinBy([], *)                 = ""
	 * StringUtils.joinBy([null], *)             = ""
	 * StringUtils.joinBy(["a", "b", "c"], ';')  = "a;b;c"
	 * StringUtils.joinBy(["a", "b", "c"], null) = "abc"
	 * StringUtils.joinBy([null, "", "a"], ';')  = ";;a"
	 * </pre>
	 *
	 * @param array      the array of values to joinBy together, may be null
	 * @param separator  the separator character to use
	 * @param startIndex the first index to start joining from.  It is
	 *                   an error to pass in an end index past the end of the array
	 * @param endIndex   the index to stop joining from (exclusive). It is
	 *                   an error to pass in an end index past the end of the array
	 * @return the joined String, <code>null</code> if null array input
	 * @since 2.0
	 */
	public static String join(Object[] array, char separator, int startIndex, int endIndex) {
		if (array == null) {
			return null;
		}
		int bufSize = endIndex - startIndex;
		if (bufSize <= 0) {
			return EMPTY;
		}

		bufSize *= (array[startIndex] == null ? 16 : array[startIndex].toString().length()) + 1;
		StringBuilder buf = new StringBuilder(bufSize);

		for (int i = startIndex; i < endIndex; i++) {
			if (i > startIndex) {
				buf.append(separator);
			}
			if (array[i] != null) {
				buf.append(array[i]);
			}
		}
		return buf.toString();
	}

	/**
	 * <p>Joins the elements of the provided array into a single String
	 * containing the provided list of elements.</p>
	 * <p/>
	 * <p>No delimiter is added before or after the list.
	 * A <code>null</code> separator is the same as an empty String ("").
	 * Null objects or empty strings within the array are represented by
	 * empty strings.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.joinBy(null, *)                = null
	 * StringUtils.joinBy([], *)                  = ""
	 * StringUtils.joinBy([null], *)              = ""
	 * StringUtils.joinBy(["a", "b", "c"], "--")  = "a--b--c"
	 * StringUtils.joinBy(["a", "b", "c"], null)  = "abc"
	 * StringUtils.joinBy(["a", "b", "c"], "")    = "abc"
	 * StringUtils.joinBy([null, "", "a"], ',')   = ",,a"
	 * </pre>
	 *
	 * @param array     the array of values to joinBy together, may be null
	 * @param separator the separator character to use, null treated as ""
	 * @return the joined String, <code>null</code> if null array input
	 */
	public static String join(Object[] array, String separator) {
		if (array == null) {
			return null;
		}
		return join(array, separator, 0, array.length);
	}

	/**
	 * <p>Joins the elements of the provided array into a single String
	 * containing the provided list of elements.</p>
	 * <p/>
	 * <p>No delimiter is added before or after the list.
	 * A <code>null</code> separator is the same as an empty String ("").
	 * Null objects or empty strings within the array are represented by
	 * empty strings.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.joinBy(null, *)                = null
	 * StringUtils.joinBy([], *)                  = ""
	 * StringUtils.joinBy([null], *)              = ""
	 * StringUtils.joinBy(["a", "b", "c"], "--")  = "a--b--c"
	 * StringUtils.joinBy(["a", "b", "c"], null)  = "abc"
	 * StringUtils.joinBy(["a", "b", "c"], "")    = "abc"
	 * StringUtils.joinBy([null, "", "a"], ',')   = ",,a"
	 * </pre>
	 *
	 * @param array      the array of values to joinBy together, may be null
	 * @param separator  the separator character to use, null treated as ""
	 * @param startIndex the first index to start joining from.  It is
	 *                   an error to pass in an end index past the end of the array
	 * @param endIndex   the index to stop joining from (exclusive). It is
	 *                   an error to pass in an end index past the end of the array
	 * @return the joined String, <code>null</code> if null array input
	 */
	public static String join(Object[] array, String separator, int startIndex, int endIndex) {
		if (array == null) {
			return null;
		}
		if (separator == null) {
			separator = EMPTY;
		}

		// endIndex - startIndex > 0:   Len = NofStrings *(len(firstString) + len(separator))
		//           (Assuming that all Strings are roughly equally long)
		int bufSize = endIndex - startIndex;
		if (bufSize <= 0) {
			return EMPTY;
		}

		bufSize *= (array[startIndex] == null ? 16 : array[startIndex].toString().length()) + separator.length();

		StringBuilder buf = new StringBuilder(bufSize);

		for (int i = startIndex; i < endIndex; i++) {
			if (i > startIndex) {
				buf.append(separator);
			}
			if (array[i] != null) {
				buf.append(array[i]);
			}
		}
		return buf.toString();
	}

	/**
	 * Join a Collection of Strings together.
	 *
	 * @param glue   Token to place between Strings.
	 * @param pieces Collection of Strings to joinBy.
	 * @return String presentation of joined Strings.
	 * @see #join(String, java.util.Iterator)
	 */
	public final static String join(String glue, Collection<String> pieces) {
		return join(glue, pieces.iterator());
	}

	/**
	 * Join an Iteration of Strings together.
	 * <p/>
	 * <h5>Example</h5>
	 * <p/>
	 * <pre>
	 *   // getValue Iterator of Strings ("abc","def","123");
	 *   Iterator i = getIterator();
	 *   out.print( TextUtils.joinBy(", ",i) );
	 *   // prints: "abc, def, 123"
	 * </pre>
	 *
	 * @param glue   Token to place between Strings.
	 * @param pieces Iteration of Strings to joinBy.
	 * @return String presentation of joined Strings.
	 */
	public final static String join(String glue, Iterator<String> pieces) {
		StringBuilder sb = new StringBuilder();
		while (pieces.hasNext()) {
			sb.append(pieces.next());
			if (pieces.hasNext()) {
				sb.append(glue);
			}
		}
		return sb.toString();
	}

	/**
	 * Join an array of Strings together.
	 *
	 * @param glue   Token to place between Strings.
	 * @param pieces ArrayEx of Strings to joinBy.
	 * @return String presentation of joined Strings.
	 * @see #join(String, java.util.Iterator)
	 */
	public final static String join(String glue, String[] pieces) {
		return join(glue, Arrays.asList(pieces).iterator());
	}

	/**
	 * <p>Finds the last index within a String, handling <code>null</code>.
	 * This method uses {@link String#lastIndexOf(int)}.</p>
	 * <p/>
	 * <p>A <code>null</code> or empty ("") String will return <code>-1</code>.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.lastIndexOf(null, *)         = -1
	 * StringUtils.lastIndexOf("", *)           = -1
	 * StringUtils.lastIndexOf("aabaabaa", 'a') = 7
	 * StringUtils.lastIndexOf("aabaabaa", 'b') = 5
	 * </pre>
	 *
	 * @param str        the String to validate, may be null
	 * @param searchChar the character to find
	 * @return the last index of the search character,
	 *         -1 if no match or <code>null</code> string input
	 * @since 2.0
	 */
	public static int lastIndexOf(String str, char searchChar) {
		if (isEmpty(str)) {
			return -1;
		}
		return str.lastIndexOf(searchChar);
	}

	/**
	 * <p>Finds the last index within a String from a start position,
	 * handling <code>null</code>.
	 * This method uses {@link String#lastIndexOf(int, int)}.</p>
	 * <p/>
	 * <p>A <code>null</code> or empty ("") String will return <code>-1</code>.
	 * A negative start position returns <code>-1</code>.
	 * A start position greater than the string length searches the whole string.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.lastIndexOf(null, *, *)          = -1
	 * StringUtils.lastIndexOf("", *,  *)           = -1
	 * StringUtils.lastIndexOf("aabaabaa", 'b', 8)  = 5
	 * StringUtils.lastIndexOf("aabaabaa", 'b', 4)  = 2
	 * StringUtils.lastIndexOf("aabaabaa", 'b', 0)  = -1
	 * StringUtils.lastIndexOf("aabaabaa", 'b', 9)  = 5
	 * StringUtils.lastIndexOf("aabaabaa", 'b', -1) = -1
	 * StringUtils.lastIndexOf("aabaabaa", 'a', 0)  = 0
	 * </pre>
	 *
	 * @param str        the String to validate, may be null
	 * @param searchChar the character to find
	 * @param startPos   the start position
	 * @return the last index of the search character,
	 *         -1 if no match or <code>null</code> string input
	 * @since 2.0
	 */
	public static int lastIndexOf(String str, char searchChar, int startPos) {
		if (isEmpty(str)) {
			return -1;
		}
		return str.lastIndexOf(searchChar, startPos);
	}

	/**
	 * <p>Finds the last index within a String, handling <code>null</code>.
	 * This method uses {@link String#lastIndexOf(String)}.</p>
	 * <p/>
	 * <p>A <code>null</code> String will return <code>-1</code>.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.lastIndexOf(null, *)          = -1
	 * StringUtils.lastIndexOf(*, null)          = -1
	 * StringUtils.lastIndexOf("", "")           = 0
	 * StringUtils.lastIndexOf("aabaabaa", "a")  = 0
	 * StringUtils.lastIndexOf("aabaabaa", "b")  = 2
	 * StringUtils.lastIndexOf("aabaabaa", "ab") = 1
	 * StringUtils.lastIndexOf("aabaabaa", "")   = 8
	 * </pre>
	 *
	 * @param str       the String to validate, may be null
	 * @param searchStr the String to find, may be null
	 * @return the last index of the search String,
	 *         -1 if no match or <code>null</code> string input
	 * @since 2.0
	 */
	public static int lastIndexOf(String str, String searchStr) {
		if (str == null || searchStr == null) {
			return -1;
		}
		return str.lastIndexOf(searchStr);
	}

	/**
	 * <p>Finds the first index within a String, handling <code>null</code>.
	 * This method uses {@link String#lastIndexOf(String, int)}.</p>
	 * <p/>
	 * <p>A <code>null</code> String will return <code>-1</code>.
	 * A negative start position returns <code>-1</code>.
	 * An empty ("") search String always matches unless the start position is negative.
	 * A start position greater than the string length searches the whole string.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.lastIndexOf(null, *, *)          = -1
	 * StringUtils.lastIndexOf(*, null, *)          = -1
	 * StringUtils.lastIndexOf("aabaabaa", "a", 8)  = 7
	 * StringUtils.lastIndexOf("aabaabaa", "b", 8)  = 5
	 * StringUtils.lastIndexOf("aabaabaa", "ab", 8) = 4
	 * StringUtils.lastIndexOf("aabaabaa", "b", 9)  = 5
	 * StringUtils.lastIndexOf("aabaabaa", "b", -1) = -1
	 * StringUtils.lastIndexOf("aabaabaa", "a", 0)  = 0
	 * StringUtils.lastIndexOf("aabaabaa", "b", 0)  = -1
	 * </pre>
	 *
	 * @param str       the String to validate, may be null
	 * @param searchStr the String to find, may be null
	 * @param startPos  the start position, negative treated as zero
	 * @return the first index of the search String,
	 *         -1 if no match or <code>null</code> string input
	 * @since 2.0
	 */
	public static int lastIndexOf(String str, String searchStr, int startPos) {
		if (str == null || searchStr == null) {
			return -1;
		}
		return str.lastIndexOf(searchStr, startPos);
	}

	/**
	 * <p>Gets the leftmost <code>len</code> characters of a String.</p>
	 * <p/>
	 * <p>If <code>len</code> characters are not available, or the
	 * String is <code>null</code>, the String will be returned without
	 * an exception. An exception is thrown if len is negative.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.left(null, *)    = null
	 * StringUtils.left(*, -ve)     = ""
	 * StringUtils.left("", *)      = ""
	 * StringUtils.left("abc", 0)   = ""
	 * StringUtils.left("abc", 2)   = "ab"
	 * StringUtils.left("abc", 4)   = "abc"
	 * </pre>
	 *
	 * @param str the String to getValue the leftmost characters from, may be null
	 * @param len the length of the required String, must be zero or positive
	 * @return the leftmost characters, <code>null</code> if null String input
	 */
	public static String left(String str, int len) {
		if (str == null) {
			return null;
		}
		if (len < 0) {
			return EMPTY;
		}
		if (str.length() <= len) {
			return str;
		}
		return str.substring(0, len);
	}

	/**
	 * <p>Left pad a String with spaces (' ').</p>
	 * <p/>
	 * <p>The String is padded to the size of <code>size<code>.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.leftPad(null, *)   = null
	 * StringUtils.leftPad("", 3)     = "   "
	 * StringUtils.leftPad("bat", 3)  = "bat"
	 * StringUtils.leftPad("bat", 5)  = "  bat"
	 * StringUtils.leftPad("bat", 1)  = "bat"
	 * StringUtils.leftPad("bat", -1) = "bat"
	 * </pre>
	 *
	 * @param str  the String to pad out, may be null
	 * @param size the size to pad to
	 * @return left padded String or original String if no padding is necessary,
	 *         <code>null</code> if null String input
	 */
	public static String leftPad(String str, int size) {
		return leftPad(str, size, ' ');
	}

	/**
	 * <p>左边填充指定的字符,总长度不超过指定的长度</p>
	 * <p>Left pad a String with a specified character.</p>
	 * <p/>
	 * <p>Pad to a size of <code>size</code>.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.leftPad(null, *, *)     = null
	 * StringUtils.leftPad("", 3, 'z')     = "zzz"
	 * StringUtils.leftPad("bat", 3, 'z')  = "bat"
	 * StringUtils.leftPad("bat", 5, 'z')  = "zzbat"
	 * StringUtils.leftPad("bat", 1, 'z')  = "bat"
	 * StringUtils.leftPad("bat", -1, 'z') = "bat"
	 * </pre>
	 *
	 * @param str     the String to pad out, may be null
	 * @param size    the size to pad to
	 * @param padChar the character to pad with
	 * @return left padded String or original String if no padding is necessary,
	 *         <code>null</code> if null String input
	 * @since 2.0
	 */
	public static String leftPad(String str, int size, char padChar) {
		if (str == null) {
			return null;
		}
		int pads = size - str.length();
		if (pads <= 0) {
			return str; // returns original String when possible
		}
		if (pads > PAD_LIMIT) {
			return leftPad(str, size, String.valueOf(padChar));
		}
		return padding(pads, padChar).concat(str);
	}

	/**
	 * <p>左边填充指定的字符串,总长度不超过指定的长度</p>
	 * <p>Left pad a String with a specified String.</p>
	 * <p/>
	 * <p>Pad to a size of <code>size</code>.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.leftPad(null, *, *)      = null
	 * StringUtils.leftPad("", 3, "z")      = "zzz"
	 * StringUtils.leftPad("bat", 3, "yz")  = "bat"
	 * StringUtils.leftPad("bat", 5, "yz")  = "yzbat"
	 * StringUtils.leftPad("bat", 8, "yz")  = "yzyzybat"
	 * StringUtils.leftPad("bat", 1, "yz")  = "bat"
	 * StringUtils.leftPad("bat", -1, "yz") = "bat"
	 * StringUtils.leftPad("bat", 5, null)  = "  bat"
	 * StringUtils.leftPad("bat", 5, "")    = "  bat"
	 * </pre>
	 *
	 * @param str    the String to pad out, may be null
	 * @param size   the size to pad to
	 * @param padStr the String to pad with, null or empty treated as single space
	 * @return left padded String or original String if no padding is necessary,
	 *         <code>null</code> if null String input
	 */
	public static String leftPad(String str, int size, String padStr) {
		if (str == null) {
			return null;
		}
		if (isEmpty(padStr)) {
			padStr = " ";
		}
		int padLen = padStr.length();
		int strLen = str.length();
		int pads = size - strLen;
		if (pads <= 0) {
			return str; // returns original String when possible
		}
		if (padLen == 1 && pads <= PAD_LIMIT) {
			return leftPad(str, size, padStr.charAt(0));
		}

		if (pads == padLen) {
			return padStr.concat(str);
		} else if (pads < padLen) {
			return padStr.substring(0, pads).concat(str);
		} else {
			char[] padding = new char[pads];
			char[] padChars = padStr.toCharArray();
			for (int i = 0; i < pads; i++) {
				padding[i] = padChars[i % padLen];
			}
			return new String(padding).concat(str);
		}
	}

	/**
	 * Gets a String's length or <code>0</code> if the String is <code>null</code>.
	 *
	 * @param str a String or <code>null</code>
	 * @return String length or <code>0</code> if the String is <code>null</code>.
	 * @since 2.4
	 */
	public static int length(String str) {
		return str == null ? 0 : str.length();
	}

	public static final String likeOf(String src) {
		return StringUtils.isValid(src) ? "%" + src + "%" : src;
	}

	/**
	 * <p>转换一个String为小写String,如果是null返回空</p> 
	 * <p>Converts a String to lower case as per {@link String#toLowerCase()}.</p>
	 * <p/>
	 * <p>A <code>null</code> input String returns <code>null</code>.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.lowerCase(null)  = null
	 * StringUtils.lowerCase("")    = ""
	 * StringUtils.lowerCase("aBc") = "abc"
	 * </pre>
	 *
	 * @param str the String to lower case, may be null
	 * @return the lower cased String, <code>null</code> if null String input
	 */
	public static String lowerCase(String str) {
		if (str == null) {
			return null;
		}
		return str.toLowerCase();
	}

	/**
	 * convert java method getName to function getId
	 * example: getRemoteCaclTax -> get_remote_calc_tax
	 *
	 * @param str
	 * @return
	 */
	public static String lowerSeparateCase(String str) {

		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return str;
		}
		str = new StringBuilder(strLen).append(Character.toLowerCase(str.charAt(0))).append(str.substring(1))
				.toString();

		StringBuilder attr_name = new StringBuilder();
		int pos = 0;
		for (int i = 0; i < strLen; i++) {
			if (Character.isUpperCase(str.charAt(i))) {
				attr_name.append(StringUtils.uncapitalize(str.substring(pos, i)));
				attr_name.append('_');
				pos = i;
			}
		}
		return attr_name.append(str.substring(pos)).toString().toLowerCase();
	}

	/**
	 * <p>Gets <code>len</code> characters from the middle of a String.</p>
	 * <p/>
	 * <p>If <code>len</code> characters are not available, the remainder
	 * of the String will be returned without an exception. If the
	 * String is <code>null</code>, <code>null</code> will be returned.
	 * An exception is thrown if len is negative.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.mid(null, *, *)    = null
	 * StringUtils.mid(*, *, -ve)     = ""
	 * StringUtils.mid("", 0, *)      = ""
	 * StringUtils.mid("abc", 0, 2)   = "ab"
	 * StringUtils.mid("abc", 0, 4)   = "abc"
	 * StringUtils.mid("abc", 2, 4)   = "c"
	 * StringUtils.mid("abc", 4, 2)   = ""
	 * StringUtils.mid("abc", -2, 2)  = "ab"
	 * </pre>
	 *
	 * @param str the String to getValue the characters from, may be null
	 * @param pos the position to start from, negative treated as zero
	 * @param len the length of the required String, must be zero or positive
	 * @return the middle characters, <code>null</code> if null String input
	 */
	public static String mid(String str, int pos, int len) {
		if (str == null) {
			return null;
		}
		if (len < 0 || pos > str.length()) {
			return EMPTY;
		}
		if (pos < 0) {
			pos = 0;
		}
		if (str.length() <= pos + len) {
			return str.substring(pos);
		}
		return str.substring(pos, pos + len);
	}

	public static final boolean notEmpty(String str) {
		return !StringUtils.isEmpty(str);
	}

	public static final String notNull(String str) {
		return str != null ? str : "";
	}

	/**
	 * <p>Returns padding using the specified delimiter repeated
	 * to a given length.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.padding(0, 'e')  = ""
	 * StringUtils.padding(3, 'e')  = "eee"
	 * StringUtils.padding(-2, 'e') = IndexOutOfBoundsException
	 * </pre>
	 * <p/>
	 * <p>Note: this method doesn't not support padding with
	 * <a href="http://www.unicode.org/glossary/#supplementary_character">Unicode Supplementary Characters</a>
	 * as they require a pair of <code>char</code>s to be represented.
	 * If you are needing to support full I18N of your applications
	 * consider using {@link #repeat(String, int)} instead.
	 * </p>
	 *
	 * @param repeat  number of times to repeat delim
	 * @param padChar character to repeat
	 * @return String with repeated character
	 * @throws IndexOutOfBoundsException if <code>repeat &lt; 0</code>
	 * @see #repeat(String, int)
	 */
	private static String padding(int repeat, char padChar) throws IndexOutOfBoundsException {
		if (repeat < 0) {
			throw new IndexOutOfBoundsException("Cannot pad a negative amount: " + repeat);
		}
		final char[] buf = new char[repeat];
		for (int i = 0; i < buf.length; i++) {
			buf[i] = padChar;
		}
		return new String(buf);
	}

	/**
	 * Pad string in lead
	 * @param padString
	 * @param minLength
	 * @param padChar
	 * @return
	 */
	public static String padLeading(String padString, int minLength, String padChar) {
		String tempPad = "";
		int lCnt = 0;

		String tempString = StringUtils.checkNull(padString);

		if (tempString.length() >= minLength) {
			return tempString;
		} else {
			for (lCnt = 1; lCnt <= minLength - tempString.length(); lCnt++) {
				tempPad = tempPad + padChar;
			}
		}
		return tempPad + tempString;
	}

	/**
	 * Pad string in tail
	 * @param padString
	 * @param minLength
	 * @param padChar
	 * @return
	 */
	public static String padTrailing(String padString, int minLength, String padChar) {
		String tempPad = "";
		int lCnt = 0;

		String tempString = StringUtils.checkNull(padString);

		if (tempString.length() >= minLength) {
			return tempString;
		} else {
			for (lCnt = 1; lCnt <= minLength - tempString.length(); lCnt++) {
				tempPad = padChar + tempPad;
			}
		}
		return tempString + tempPad;
	}

	/**
	 * <p>用单引号引用给定的字符串</p>
	 * Quote the given String with single quotes.
	 *
	 * @param str the input String (e.g. "myString")
	 * @return the quoted String (e.g. "'myString'"),
	 *         or <code>null<code> if the input was <code>null</code>
	 */
	public static String quote(String str) {
		return str != null ? StringPool.SINGLE_QUOTE + str + StringPool.SINGLE_QUOTE : null;
	}

	public static String removeString(String tmp, String start, String end) {
		int startPosi = -1;
		int endPosi = -1;
		startPosi = tmp.indexOf(start);
		if (startPosi >= 0) {
			endPosi = tmp.indexOf(end, startPosi);
			if (endPosi >= startPosi) {
				tmp = tmp.substring(0, startPosi) + tmp.substring(endPosi + 2);
			}
		}
		return tmp;
	}

	// startsWith
	//-----------------------------------------------------------------------

	public static String repeat(char c, int count) {
		StringBuilder result = new StringBuilder(count);
		while (count > 0) {
			result.append(c);
			count--;
		}
		return result.toString();
	}

	/**
	 * <p>Repeat a String <code>repeat</code> times to form a
	 * new String.</p>
	 *
	 * <pre>
	 * StringUtils.repeat(null, 2) = null
	 * StringUtils.repeat("", 0)   = ""
	 * StringUtils.repeat("", 2)   = ""
	 * StringUtils.repeat("a", 3)  = "aaa"
	 * StringUtils.repeat("ab", 2) = "abab"
	 * StringUtils.repeat("a", -2) = ""
	 * </pre>
	 *
	 * @param str  the String to repeat, may be null
	 * @param repeat  number of times to repeat str, negative treated as zero
	 * @return a new String consisting of the original String repeated,
	 *  <code>null</code> if null String input
	 */
	/**
	 * CreatingUtils a new string that contains the provided string a number of times.
	 */
	public static String repeat(String source, int count) {
		StringBuilder result = new StringBuilder(count * source.length());
		while (count > 0) {
			result.append(source);
			count--;
		}
		return result.toString();
	}

	/**
	 * <p>Replaces all occurrences of a String within another String.</p>
	 * <p/>
	 * <p>A <code>null</code> reference passed to this method is a no-op.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.replace(null, *, *)        = null
	 * StringUtils.replace("", *, *)          = ""
	 * StringUtils.replace("any", null, *)    = "any"
	 * StringUtils.replace("any", *, null)    = "any"
	 * StringUtils.replace("any", "", *)      = "any"
	 * StringUtils.replace("aba", "a", null)  = "aba"
	 * StringUtils.replace("aba", "a", "")    = "b"
	 * StringUtils.replace("aba", "a", "z")   = "zbz"
	 * </pre>
	 *
	 * @param text         text to search and replace in, may be null
	 * @param searchString the String to search for, may be null
	 * @param replacement  the String to replace it with, may be null
	 * @return the text with any replacements processed,
	 *         <code>null</code> if null String input
	 * @see #replace(String text, String searchString, String replacement, int max)
	 */
	public static String replace(String text, String searchString, String replacement) {
		return replace(text, searchString, replacement, -1);
	}

	/**
	 * <p>Replaces a String with another String inside a larger String,
	 * for the first <code>max</code> values of the search String.</p>
	 * <p/>
	 * <p>A <code>null</code> reference passed to this method is a no-op.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.replace(null, *, *, *)         = null
	 * StringUtils.replace("", *, *, *)           = ""
	 * StringUtils.replace("any", null, *, *)     = "any"
	 * StringUtils.replace("any", *, null, *)     = "any"
	 * StringUtils.replace("any", "", *, *)       = "any"
	 * StringUtils.replace("any", *, *, 0)        = "any"
	 * StringUtils.replace("abaa", "a", null, -1) = "abaa"
	 * StringUtils.replace("abaa", "a", "", -1)   = "b"
	 * StringUtils.replace("abaa", "a", "z", 0)   = "abaa"
	 * StringUtils.replace("abaa", "a", "z", 1)   = "zbaa"
	 * StringUtils.replace("abaa", "a", "z", 2)   = "zbza"
	 * StringUtils.replace("abaa", "a", "z", -1)  = "zbzz"
	 * </pre>
	 *
	 * @param text         text to search and replace in, may be null
	 * @param searchString the String to search for, may be null
	 * @param replacement  the String to replace it with, may be null
	 * @param max          maximum number of values to replace, or <code>-1</code> if no maximum
	 * @return the text with any replacements processed,
	 *         <code>null</code> if null String input
	 */
	public static String replace(String text, String searchString, String replacement, int max) {
		if (isEmpty(text) || isEmpty(searchString) || replacement == null || max == 0) {
			return text;
		}
		int start = 0;
		int end = text.indexOf(searchString, start);
		if (end == -1) {
			return text;
		}
		int replLength = searchString.length();
		int increase = replacement.length() - replLength;
		increase = increase < 0 ? 0 : increase;
		increase *= max < 0 ? 16 : max > 64 ? 64 : max;
		StringBuilder buf = new StringBuilder(text.length() + increase);
		while (end != -1) {
			buf.append(text.substring(start, end)).append(replacement);
			start = end + replLength;
			if (--max == 0) {
				break;
			}
			end = text.indexOf(searchString, start);
		}
		buf.append(text.substring(start));
		return buf.toString();
	}

	/**
	 * <p>Replaces all occurrences of a character in a String with another.
	 * This is a null-safe getVersion of {@link String#replace(char, char)}.</p>
	 * <p/>
	 * <p>A <code>null</code> string input returns <code>null</code>.
	 * An empty ("") string input returns an empty string.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.replaceChars(null, *, *)        = null
	 * StringUtils.replaceChars("", *, *)          = ""
	 * StringUtils.replaceChars("abcba", 'b', 'y') = "aycya"
	 * StringUtils.replaceChars("abcba", 'z', 'y') = "abcba"
	 * </pre>
	 *
	 * @param str         String to replace characters in, may be null
	 * @param searchChar  the character to search for, may be null
	 * @param replaceChar the character to replace, may be null
	 * @return modified String, <code>null</code> if null string input
	 * @since 2.0
	 */
	public static String replaceChars(String str, char searchChar, char replaceChar) {
		if (str == null) {
			return null;
		}
		return str.replace(searchChar, replaceChar);
	}

	/**
	 * 替换字符串中指定字符串,忽略大小写
	 * @param line 原字符串
	 * @param oldString 待替换字符串
	 * @param newString 更新字符串
	 * @return
	 */
	public static final String replaceIgnoreCase(String line, String oldString, String newString) {
		if (line == null || oldString == null || newString == null) {
			return null;
		}
		String lcLine = line.toLowerCase();
		String lcOldString = oldString.toLowerCase();
		int i = 0;
		if ((i = lcLine.indexOf(lcOldString, i)) >= 0) {
			char[] line2 = line.toCharArray();
			char[] newString2 = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j = i;
			while ((i = lcLine.indexOf(lcOldString, i)) > 0) {
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
				j = i;
			}
			buf.append(line2, j, line2.length - j);
			return buf.toString();
		}
		return line;
	}

	/**
	 * <p>Replaces a String with another String inside a larger String, once.</p>
	 * <p/>
	 * <p>A <code>null</code> reference passed to this method is a no-op.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.replaceOnce(null, *, *)        = null
	 * StringUtils.replaceOnce("", *, *)          = ""
	 * StringUtils.replaceOnce("any", null, *)    = "any"
	 * StringUtils.replaceOnce("any", *, null)    = "any"
	 * StringUtils.replaceOnce("any", "", *)      = "any"
	 * StringUtils.replaceOnce("aba", "a", null)  = "aba"
	 * StringUtils.replaceOnce("aba", "a", "")    = "ba"
	 * StringUtils.replaceOnce("aba", "a", "z")   = "zba"
	 * </pre>
	 *
	 * @param text         text to search and replace in, may be null
	 * @param searchString the String to search for, may be null
	 * @param replacement  the String to replace with, may be null
	 * @return the text with any replacements processed,
	 *         <code>null</code> if null String input
	 * @see #replace(String text, String searchString, String replacement, int max)
	 */
	public static String replaceOnce(String text, String searchString, String replacement) {
		return replace(text, searchString, replacement, 1);
	}

	/**
	 * Replace substring in the input string
	 * @param inputString
	 * @param beReplaced
	 * @param replaceTo
	 * @return
	 */
	public static String replaceString(String inputString, String beReplaced, String replaceTo) {
		int index = 0;
		String returnString = "";

		returnString = inputString;

		do {
			index = inputString.indexOf(beReplaced, index);

			if (index == -1) {
				break;
			}

			returnString = inputString.substring(0, index) + replaceTo
					+ inputString.substring(index + beReplaced.length());
			index += replaceTo.length();
			inputString = returnString;

		} while (true);

		return returnString.substring(0, returnString.length());
	}

	/**
	 * <p>Gets the rightmost <code>len</code> characters of a String.</p>
	 * <p/>
	 * <p>If <code>len</code> characters are not available, or the String
	 * is <code>null</code>, the String will be returned without an
	 * an exception. An exception is thrown if len is negative.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.right(null, *)    = null
	 * StringUtils.right(*, -ve)     = ""
	 * StringUtils.right("", *)      = ""
	 * StringUtils.right("abc", 0)   = ""
	 * StringUtils.right("abc", 2)   = "bc"
	 * StringUtils.right("abc", 4)   = "abc"
	 * </pre>
	 *
	 * @param str the String to getValue the rightmost characters from, may be null
	 * @param len the length of the required String, must be zero or positive
	 * @return the rightmost characters, <code>null</code> if null String input
	 */
	public static String right(String str, int len) {
		if (str == null) {
			return null;
		}
		if (len < 0) {
			return EMPTY;
		}
		if (str.length() <= len) {
			return str;
		}
		return str.substring(str.length() - len);
	}

	/**
	 * <p>Right pad a String with a specified character.</p>
	 * <p/>
	 * <p>The String is padded to the size of <code>size</code>.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.rightPad(null, *, *)     = null
	 * StringUtils.rightPad("", 3, 'z')     = "zzz"
	 * StringUtils.rightPad("bat", 3, 'z')  = "bat"
	 * StringUtils.rightPad("bat", 5, 'z')  = "batzz"
	 * StringUtils.rightPad("bat", 1, 'z')  = "bat"
	 * StringUtils.rightPad("bat", -1, 'z') = "bat"
	 * </pre>
	 *
	 * @param str     the String to pad out, may be null
	 * @param size    the size to pad to
	 * @param padChar the character to pad with
	 * @return right padded String or original String if no padding is necessary,
	 *         <code>null</code> if null String input
	 * @since 2.0
	 */
	public static String rightPad(String str, int size, char padChar) {
		if (str == null) {
			return null;
		}
		int pads = size - str.length();
		if (pads <= 0) {
			return str; // returns original String when possible
		}
		if (pads > PAD_LIMIT) {
			return rightPad(str, size, String.valueOf(padChar));
		}
		return str.concat(padding(pads, padChar));
	}

	/**
	 * <p>Right pad a String with a specified String.</p>
	 * <p/>
	 * <p>The String is padded to the size of <code>size</code>.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.rightPad(null, *, *)      = null
	 * StringUtils.rightPad("", 3, "z")      = "zzz"
	 * StringUtils.rightPad("bat", 3, "yz")  = "bat"
	 * StringUtils.rightPad("bat", 5, "yz")  = "batyz"
	 * StringUtils.rightPad("bat", 8, "yz")  = "batyzyzy"
	 * StringUtils.rightPad("bat", 1, "yz")  = "bat"
	 * StringUtils.rightPad("bat", -1, "yz") = "bat"
	 * StringUtils.rightPad("bat", 5, null)  = "bat  "
	 * StringUtils.rightPad("bat", 5, "")    = "bat  "
	 * </pre>
	 *
	 * @param str    the String to pad out, may be null
	 * @param size   the size to pad to
	 * @param padStr the String to pad with, null or empty treated as single space
	 * @return right padded String or original String if no padding is necessary,
	 *         <code>null</code> if null String input
	 */
	public static String rightPad(String str, int size, String padStr) {
		if (str == null) {
			return null;
		}
		if (isEmpty(padStr)) {
			padStr = " ";
		}
		int padLen = padStr.length();
		int strLen = str.length();
		int pads = size - strLen;
		if (pads <= 0) {
			return str; // returns original String when possible
		}
		if (padLen == 1 && pads <= PAD_LIMIT) {
			return rightPad(str, size, padStr.charAt(0));
		}

		if (pads == padLen) {
			return str.concat(padStr);
		} else if (pads < padLen) {
			return str.concat(padStr.substring(0, pads));
		} else {
			char[] padding = new char[pads];
			char[] padChars = padStr.toCharArray();
			for (int i = 0; i < pads; i++) {
				padding[i] = padChars[i % padLen];
			}
			return str.concat(new String(padding));
		}
	}

	/**
	 * <p>Splits the provided text into an array, using whitespace as the
	 * separator.
	 * Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
	 * <p/>
	 * <p>The separator is not included in the returned String array.
	 * Adjacent separators are treated as one separator.
	 * For more control over the split use the StrTokenizer class.</p>
	 * <p/>
	 * <p>A <code>null</code> input String returns <code>null</code>.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.split(null)       = null
	 * StringUtils.split("")         = []
	 * StringUtils.split("abc def")  = ["abc", "def"]
	 * StringUtils.split("abc  def") = ["abc", "def"]
	 * StringUtils.split(" abc ")    = ["abc"]
	 * </pre>
	 *
	 * @param str the String to parse, may be null
	 * @return an array of parsed Strings, <code>null</code> if null String input
	 */
	public static String[] split(String str) {
		return split(str, null, -1);
	}

	/**
	 * <p>Splits the provided text into an array, separator specified.
	 * This is an alternative to using StringTokenizer.</p>
	 * <p/>
	 * <p>The separator is not included in the returned String array.
	 * Adjacent separators are treated as one separator.
	 * For more control over the split use the StrTokenizer class.</p>
	 * <p/>
	 * <p>A <code>null</code> input String returns <code>null</code>.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.split(null, *)         = null
	 * StringUtils.split("", *)           = []
	 * StringUtils.split("a.b.c", '.')    = ["a", "b", "c"]
	 * StringUtils.split("a..b.c", '.')   = ["a", "b", "c"]
	 * StringUtils.split("a:b:c", '.')    = ["a:b:c"]
	 * StringUtils.split("a b c", ' ')    = ["a", "b", "c"]
	 * </pre>
	 *
	 * @param str       the String to parse, may be null
	 * @param separator the character used as the delimiter
	 * @return an array of parsed Strings, <code>null</code> if null String input
	 * @since 2.0
	 */
	public static String[] split(String str, char separator) {
		return split(str, separator, false);
	}

	/**
	 * Performs the logic for the <code>split</code> and
	 * <code>splitPreserveAllTokens</code> methods that do not return a
	 * maximum array length.
	 *
	 * @param str            the String to parse, may be <code>null</code>
	 * @param separator      the separate character
	 * @param preserveTokens if <code>true</code>, adjacent separators are
	 *                       treated as empty token separators; if <code>false</code>, adjacent
	 *                       separators are treated as one separator.
	 * @return an array of parsed Strings, <code>null</code> if null String input
	 */
	public static String[] split(String str, char separator, boolean preserveTokens) {
		// Performance tuned for 2.0 (JDK1.4)

		if (str == null) {
			return null;
		}
		int len = str.length();
		if (len == 0) {
			return new String[0];
		}
		List<String> list = new ArrayList<String>();
		int i = 0, start = 0;
		boolean match = false;
		boolean lastMatch = false;
		while (i < len) {
			if (str.charAt(i) == separator) {
				if (match || preserveTokens) {
					list.add(str.substring(start, i));
					match = false;
					lastMatch = true;
				}
				start = ++i;
				continue;
			}
			lastMatch = false;
			match = true;
			i++;
		}
		if (match || preserveTokens && lastMatch) {
			list.add(str.substring(start, i));
		}
		return list.toArray(new String[list.size()]);
	}

	/**
	 * Splits a string in several parts (tokens) that are separated by delimiter.
	 * Delimiter is <b>always</b> surrounded by two strings! If there is no
	 * content between two delimiters, empty string will be returned for that
	 * token. Therefore, the length of the returned array will always be:
	 * #delimiters + 1.
	 * <p/>
	 * Method is much, much faster then regexp <code>String.split()</code>,
	 * and a bit faster then <code>StringTokenizer</code>.
	 *
	 * @param src       string to split
	 * @param delimeter split delimiter
	 * @return array of split strings
	 */
	public static String[] split(String src, String delimeter) {
		int maxparts = src.length() / delimeter.length() + 2; // one more for the last
		int[] positions = new int[maxparts];
		int dellen = delimeter.length();

		int i, j = 0;
		int count = 0;
		positions[0] = -dellen;
		while ((i = src.indexOf(delimeter, j)) != -1) {
			count++;
			positions[count] = i;
			j = i + dellen;
		}
		count++;
		positions[count] = src.length();

		String[] result = new String[count];

		for (i = 0; i < count; i++) {
			result[i] = src.substring(positions[i] + dellen, positions[i + 1]);
		}
		return result;
	}

	/**
	 * <p>Splits the provided text into an array with a maximum length,
	 * separators specified.</p>
	 * <p/>
	 * <p>The separator is not included in the returned String array.
	 * Adjacent separators are treated as one separator.</p>
	 * <p/>
	 * <p>A <code>null</code> input String returns <code>null</code>.
	 * A <code>null</code> separatorChars splits on whitespace.</p>
	 * <p/>
	 * <p>If more than <code>max</code> delimited substrings are found, the last
	 * returned string includes all characters after the first <code>max - 1</code>
	 * returned strings (including separator characters).</p>
	 * <p/>
	 * <pre>
	 * StringUtils.split(null, *, *)            = null
	 * StringUtils.split("", *, *)              = []
	 * StringUtils.split("ab de fg", null, 0)   = ["ab", "cd", "ef"]
	 * StringUtils.split("ab   de fg", null, 0) = ["ab", "cd", "ef"]
	 * StringUtils.split("ab:cd:ef", ":", 0)    = ["ab", "cd", "ef"]
	 * StringUtils.split("ab:cd:ef", ":", 2)    = ["ab", "cd:ef"]
	 * </pre>
	 *
	 * @param str       the String to parse, may be null
	 * @param delimeter the characters used as the delimiters,
	 *                  <code>null</code> splits on whitespace
	 * @param max       the maximum number of elements to include in the
	 *                  array. A zero or negative symbol implies no limit
	 * @return an array of parsed Strings, <code>null</code> if null String input
	 */
	public static String[] split(String str, String delimeter, int max) {
		return split(str, delimeter, max, false);
	}

	/**
	 * Performs the logic for the <code>split</code> and
	 * <code>splitPreserveAllTokens</code> methods that return a maximum array
	 * length.
	 *
	 * @param str               the String to parse, may be <code>null</code>
	 * @param delimeter         the separate character
	 * @param max               the maximum number of elements to include in the
	 *                          array. A zero or negative symbol implies no limit.
	 * @param preserveAllTokens if <code>true</code>, adjacent separators are
	 *                          treated as empty token separators; if <code>false</code>, adjacent
	 *                          separators are treated as one separator.
	 * @return an array of parsed Strings, <code>null</code> if null String input
	 */
	public static String[] split(String str, String delimeter, int max, boolean preserveTokens) {
		// Performance tuned for 2.0 (JDK1.4)
		// Direct code is quicker than StringTokenizer.
		// Also, StringTokenizer uses isSpace() not isWhitespace()

		if (str == null || str.length() == 0) {
			return new String[0];
		}

		int len = str.length();

		List<String> list = new ArrayList<String>();
		int sizePlus1 = 1;
		int i = 0, start = 0;
		boolean match = false;
		boolean lastMatch = false;
		if (delimeter == null) {
			// Null separator means use whitespace
			while (i < len) {
				if (Character.isWhitespace(str.charAt(i))) {
					if (match || preserveTokens) {
						lastMatch = true;
						if (sizePlus1++ == max) {
							i = len;
							lastMatch = false;
						}
						list.add(str.substring(start, i));
						match = false;
					}
					start = ++i;
					continue;
				}
				lastMatch = false;
				match = true;
				i++;
			}
		} else if (delimeter.length() == 1) {
			// Optimise 1 character case
			char sep = delimeter.charAt(0);
			while (i < len) {
				if (str.charAt(i) == sep) {
					if (match || preserveTokens) {
						lastMatch = true;
						if (sizePlus1++ == max) {
							i = len;
							lastMatch = false;
						}
						list.add(str.substring(start, i));
						match = false;
					}
					start = ++i;
					continue;
				}
				lastMatch = false;
				match = true;
				i++;
			}
		} else {
			// standard case
			while (i < len) {
				if (delimeter.indexOf(str.charAt(i)) >= 0) {
					if (match || preserveTokens) {
						lastMatch = true;
						if (sizePlus1++ == max) {
							i = len;
							lastMatch = false;
						}
						list.add(str.substring(start, i));
						match = false;
					}
					start = ++i;
					continue;
				}
				lastMatch = false;
				match = true;
				i++;
			}
		}
		if (match || preserveTokens && lastMatch) {
			list.add(str.substring(start, i));
		}
		return list.toArray(new String[list.size()]);
	}

	/**
	 * 名字对切分
	 *
	 * @param input     list of key/symbol pairs separated by a given delimiter. For
	 *                  example, " <code>param1=foo,param2=bar</code>
	 *                  " where delimiter is "<code>,</code>".
	 * @param delimiter delimiter <code>String</code> used to separate key/symbol pairs
	 */

	@SuppressWarnings("unchecked")
	public static KV<String, String>[] split2KV(String input, String delimiter) {
		List<KV<String, String>> kvs = new LinkedList<KV<String, String>>();
		int start = 0;
		int idx_e1 = input.indexOf('=');
		int idx_d = input.indexOf(delimiter, idx_e1);
		if (idx_d == -1) {
			idx_d = input.length();
		}
		while (idx_e1 != -1) {
			if (idx_d < input.length()) {
				int idx_e2 = input.indexOf('=', idx_d + 1);
				if (idx_e2 != -1) {
					idx_d = input.lastIndexOf(delimiter, idx_e2);
				} else {
					idx_d = input.length();
				}
			}
			String key = input.substring(start, idx_e1);
			String value = input.substring(idx_e1 + 1, idx_d);
			kvs.add(KV.of(key.trim(), value.trim()));
			if (idx_d < input.length()) {
				start = idx_d + 1;
				idx_e1 = input.indexOf('=', start);
				if (idx_e1 != -1) {
					idx_d = input.indexOf(delimiter, idx_e1);
					if (idx_d == -1) {
						idx_d = input.length();
					}
				}
			} else {
				idx_e1 = -1;
			}
		}
		return kvs.toArray(new KV[0]);
	}

	/**
	 * Input format is [aaa];[bbb];[xxxcc]
	 *
	 * @param input
	 * @return
	 */
	public static String[] splitBracketStr(String input) {

		if (input == null) {
			return new String[0];
		}

		input = input.trim().replaceAll("\\];[\r\n\t]*", "\\];");
		if (input.startsWith("[") && input.length() > 2) {
			if (input.endsWith("]")) {
				input = input.substring(1, input.length() - 1);
			} else if (input.endsWith("];")) {
				input = input.substring(1, input.length() - 2);
			}
		}
		String[] array = input.split("\\];\\[");
		List<String> list = new ArrayList<String>(array.length);
		for (String s : array) {
			if (s.length() > 0) {
				list.add(s);
			}
		}
		return list.toArray(new String[list.size()]);
	}

	/**
	 * 根据一个正则式，将字符串拆分成数组，空元素将被忽略
	 *
	 * @param s     字符串
	 * @param regex 正则式
	 * @return 字符串数组
	 */
	public static String[] splitIgnoreBlank(String s, String regex) {
		if (null == s) {
			return null;
		}
		String[] ss = s.split(regex);
		List<String> list = new LinkedList<String>();
		for (String st : ss) {
			if (isBlank(st)) {
				continue;
			}
			list.add(trim(st));
		}
		return list.toArray(new String[list.size()]);
	}

	public static String[] splitToArray(String inputString, String delimiter) {
		return split(inputString, delimiter);
	}

	/**
	 * Split the string according to the delimetr to a vector
	 * @param inputString
	 * @param delimiter
	 * @return
	 */
	public static List<String> splitToList(String inputString, String delimiter) {
		List<String> returnVector = new ArrayList<String>();
		int position = -1;

		position = inputString.indexOf(delimiter);
		while (position >= 0) {
			if (position > 0) {
				returnVector.add(inputString.substring(0, position));
			}

			inputString = inputString.substring(position + delimiter.length());
			position = inputString.indexOf(delimiter);
		}
		if (!inputString.equals("")) {
			returnVector.add(inputString);
		}
		return returnVector;
	}

	/**
	 * <p>Splits the provided text into an array, using whitespace as the
	 * separator, preserving all tokens, including empty tokens created by
	 * adjacent separators. This is an alternative to using StringTokenizer.
	 * Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
	 * <p/>
	 * <p>The separator is not included in the returned String array.
	 * Adjacent separators are treated as separators for empty tokens.
	 * For more control over the split use the StrTokenizer class.</p>
	 * <p/>
	 * <p>A <code>null</code> input String returns <code>null</code>.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.splitWithAllTokens(null)       = null
	 * StringUtils.splitWithAllTokens("")         = []
	 * StringUtils.splitWithAllTokens("abc def")  = ["abc", "def"]
	 * StringUtils.splitWithAllTokens("abc  def") = ["abc", "", "def"]
	 * StringUtils.splitWithAllTokens(" abc ")    = ["", "abc", ""]
	 * </pre>
	 *
	 * @param str the String to parse, may be <code>null</code>
	 * @return an array of parsed Strings, <code>null</code> if null String input
	 * @since 2.1
	 */
	public static String[] splitWithAllTokens(String str) {
		return split(str, null, -1, true);
	}

	/**
	 * <p>Splits the provided text into an array, separator specified,
	 * preserving all tokens, including empty tokens created by adjacent
	 * separators. This is an alternative to using StringTokenizer.</p>
	 * <p/>
	 * <p>The separator is not included in the returned String array.
	 * Adjacent separators are treated as separators for empty tokens.
	 * For more control over the split use the StrTokenizer class.</p>
	 * <p/>
	 * <p>A <code>null</code> input String returns <code>null</code>.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.splitPreserveAllTokens(null, *)         = null
	 * StringUtils.splitPreserveAllTokens("", *)           = []
	 * StringUtils.splitPreserveAllTokens("a.b.c", '.')    = ["a", "b", "c"]
	 * StringUtils.splitPreserveAllTokens("a..b.c", '.')   = ["a", "", "b", "c"]
	 * StringUtils.splitPreserveAllTokens("a:b:c", '.')    = ["a:b:c"]
	 * StringUtils.splitPreserveAllTokens("a\tb\nc", null) = ["a", "b", "c"]
	 * StringUtils.splitPreserveAllTokens("a b c", ' ')    = ["a", "b", "c"]
	 * StringUtils.splitPreserveAllTokens("a b c ", ' ')   = ["a", "b", "c", ""]
	 * StringUtils.splitPreserveAllTokens("a b c  ", ' ')   = ["a", "b", "c", "", ""]
	 * StringUtils.splitPreserveAllTokens(" a b c", ' ')   = ["", a", "b", "c"]
	 * StringUtils.splitPreserveAllTokens("  a b c", ' ')  = ["", "", a", "b", "c"]
	 * StringUtils.splitPreserveAllTokens(" a b c ", ' ')  = ["", a", "b", "c", ""]
	 * </pre>
	 *
	 * @param str       the String to parse, may be <code>null</code>
	 * @param separator the character used as the delimiter,
	 *                  <code>null</code> splits on whitespace
	 * @return an array of parsed Strings, <code>null</code> if null String input
	 * @since 2.1
	 */
	public static String[] splitWithAllTokens(String str, char separator) {
		return split(str, separator, true);
	}

	/**
	 * <p>Splits the provided text into an array, separators specified,
	 * preserving all tokens, including empty tokens created by adjacent
	 * separators. This is an alternative to using StringTokenizer.</p>
	 * <p/>
	 * <p>The separator is not included in the returned String array.
	 * Adjacent separators are treated as separators for empty tokens.
	 * For more control over the split use the StrTokenizer class.</p>
	 * <p/>
	 * <p>A <code>null</code> input String returns <code>null</code>.
	 * A <code>null</code> separatorChars splits on whitespace.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.splitWithAllTokens(null, *)           = null
	 * StringUtils.splitWithAllTokens("", *)             = []
	 * StringUtils.splitWithAllTokens("abc def", null)   = ["abc", "def"]
	 * StringUtils.splitWithAllTokens("abc def", " ")    = ["abc", "def"]
	 * StringUtils.splitWithAllTokens("abc  def", " ")   = ["abc", "", def"]
	 * StringUtils.splitWithAllTokens("ab:cd:ef", ":")   = ["ab", "cd", "ef"]
	 * StringUtils.splitWithAllTokens("ab:cd:ef:", ":")  = ["ab", "cd", "ef", ""]
	 * StringUtils.splitWithAllTokens("ab:cd:ef::", ":") = ["ab", "cd", "ef", "", ""]
	 * StringUtils.splitWithAllTokens("ab::cd:ef", ":")  = ["ab", "", cd", "ef"]
	 * StringUtils.splitWithAllTokens(":cd:ef", ":")     = ["", cd", "ef"]
	 * StringUtils.splitWithAllTokens("::cd:ef", ":")    = ["", "", cd", "ef"]
	 * StringUtils.splitWithAllTokens(":cd:ef:", ":")    = ["", cd", "ef", ""]
	 * </pre>
	 *
	 * @param str       the String to parse, may be <code>null</code>
	 * @param separator the characters used as the delimiters,
	 *                  <code>null</code> splits on whitespace
	 * @return an array of parsed Strings, <code>null</code> if null String input
	 * @since 2.1
	 */
	public static String[] splitWithAllTokens(String str, String separator) {
		return split(str, separator, -1, true);
	}

	/**
	 * <p>Splits the provided text into an array with a maximum length,
	 * separators specified, preserving all tokens, including empty tokens
	 * created by adjacent separators.</p>
	 * <p/>
	 * <p>The separator is not included in the returned String array.
	 * Adjacent separators are treated as separators for empty tokens.
	 * Adjacent separators are treated as one separator.</p>
	 * <p/>
	 * <p>A <code>null</code> input String returns <code>null</code>.
	 * A <code>null</code> separatorChars splits on whitespace.</p>
	 * <p/>
	 * <p>If more than <code>max</code> delimited substrings are found, the last
	 * returned string includes all characters after the first <code>max - 1</code>
	 * returned strings (including separator characters).</p>
	 * <p/>
	 * <pre>
	 * StringUtils.splitWithAllTokens(null, *, *)            = null
	 * StringUtils.splitWithAllTokens("", *, *)              = []
	 * StringUtils.splitWithAllTokens("ab de fg", null, 0)   = ["ab", "cd", "ef"]
	 * StringUtils.splitWithAllTokens("ab   de fg", null, 0) = ["ab", "cd", "ef"]
	 * StringUtils.splitWithAllTokens("ab:cd:ef", ":", 0)    = ["ab", "cd", "ef"]
	 * StringUtils.splitWithAllTokens("ab:cd:ef", ":", 2)    = ["ab", "cd:ef"]
	 * StringUtils.splitWithAllTokens("ab   de fg", null, 2) = ["ab", "  de fg"]
	 * StringUtils.splitWithAllTokens("ab   de fg", null, 3) = ["ab", "", " de fg"]
	 * StringUtils.splitWithAllTokens("ab   de fg", null, 4) = ["ab", "", "", "de fg"]
	 * </pre>
	 *
	 * @param str       the String to parse, may be <code>null</code>
	 * @param delimeter the characters used as the delimiters,
	 *                  <code>null</code> splits on whitespace
	 * @param max       the maximum number of elements to include in the
	 *                  array. A zero or negative symbol implies no limit
	 * @return an array of parsed Strings, <code>null</code> if null String input
	 * @since 2.1
	 */
	public static String[] splitWithAllTokens(String str, String delimeter, int max) {
		return split(str, delimeter, max, true);
	}

	/**
	 * 打印出exception的出错StackTrace
	 * @param e 异常对象
	 * @return e.printStackTrace的字符串内容
	 */
	public static String stackToString(Exception e) {
		try {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			return sw.toString();
		} catch (Exception e2) {
			return "(bad stack2string)".concat(e.getMessage());
		}
	}

	/**
	 * <p>检查一个字符串指定前缀开始，不忽略大小写</p>
	 * <p>Check if a String starts with a specified prefix.</p>
	 * <p/>
	 * <p><code>null</code>s are handled without exceptions. Two <code>null</code>
	 * references are considered to be equal. The comparison is case sensitive.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.startsWith(null, null)      = true
	 * StringUtils.startsWith(null, "abcdef")  = false
	 * StringUtils.startsWith("abc", null)     = false
	 * StringUtils.startsWith("abc", "abcdef") = true
	 * StringUtils.startsWith("abc", "ABCDEF") = false
	 * </pre>
	 *
	 * @param str    the String to validate, may be null
	 * @param prefix the prefix to find, may be null
	 * @return <code>true</code> if the String starts with the prefix, case sensitive, or
	 *         both <code>null</code>
	 * @see java.lang.String#startsWith(String)
	 * @since 2.4
	 */
	public static boolean startsWith(String str, String prefix) {
		return startsWith(str, prefix, false);
	}

	/**
	 * <p>检查一个字符串指定前缀开始</p>     
	 * <p>Check if a String starts with a specified prefix (optionally case insensitive).</p>
	 *
	 * @param str        the String to validate, may be null
	 * @param prefix     the prefix to find, may be null
	 * @param ignoreCase inidicates whether the compare should ignore case
	 *                   (case insensitive) or not.
	 * @return <code>true</code> if the String starts with the prefix or
	 *         both <code>null</code>
	 * @see java.lang.String#startsWith(String)
	 */
	private static boolean startsWith(String str, String prefix, boolean ignoreCase) {
		if (str == null || prefix == null) {
			return str == null && prefix == null;
		}
		if (prefix.length() > str.length()) {
			return false;
		}
		return str.regionMatches(ignoreCase, 0, prefix, 0, prefix.length());
	}

	/**
	 *  <p>检查一个字符串指定前缀字符开始</p>
	 * Returns if string starts with given character.
	 */
	public static boolean startsWithChar(String s, char c) {
		if (s.length() == 0) {
			return false;
		}
		return s.charAt(0) == c;
	}

	/**
	 * <p>检查一个字符串指定前缀开始,忽略大小写</p>
	 * <p>Case insensitive validate if a String starts with a specified prefix.</p>
	 * <p/>
	 * <p><code>null</code>s are handled without exceptions. Two <code>null</code>
	 * references are considered to be equal. The comparison is case insensitive.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.startsWithIgnoreCase(null, null)      = true
	 * StringUtils.startsWithIgnoreCase(null, "abcdef")  = false
	 * StringUtils.startsWithIgnoreCase("abc", null)     = false
	 * StringUtils.startsWithIgnoreCase("abc", "abcdef") = true
	 * StringUtils.startsWithIgnoreCase("abc", "ABCDEF") = true
	 * </pre>
	 *
	 * @param str    the String to validate, may be null
	 * @param prefix the prefix to find, may be null
	 * @return <code>true</code> if the String starts with the prefix, case insensitive, or
	 *         both <code>null</code>
	 * @see java.lang.String#startsWith(String)
	 * @since 2.4
	 */
	public static boolean startsWithIgnoreCase(String str, String prefix) {
		return startsWith(str, prefix, true);
	}

	/**
	 * <p>Gets a substring from the specified String avoiding exceptions.</p>
	 * <p/>
	 * <p>A negative start position can be used to start <code>n</code>
	 * characters from the end of the String.</p>
	 * <p/>
	 * <p>A <code>null</code> String will return <code>null</code>.
	 * An empty ("") String will return "".</p>
	 * <p/>
	 * <pre>
	 * StringUtils.substring(null, *)   = null
	 * StringUtils.substring("", *)     = ""
	 * StringUtils.substring("abc", 0)  = "abc"
	 * StringUtils.substring("abc", 2)  = "c"
	 * StringUtils.substring("abc", 4)  = ""
	 * StringUtils.substring("abc", -2) = "bc"
	 * StringUtils.substring("abc", -4) = "abc"
	 * </pre>
	 *
	 * @param str   the String to getValue the substring from, may be null
	 * @param start the position to start from, negative means
	 *              count back from the end of the String by this many characters
	 * @return substring from start position, <code>null</code> if null String input
	 */
	public static String substring(String str, int start) {
		if (str == null) {
			return null;
		}

		// handle negatives, which means last n characters
		if (start < 0) {
			start = str.length() + start; // remember start is negative
		}

		if (start < 0) {
			start = 0;
		}
		if (start > str.length()) {
			return EMPTY;
		}

		return str.substring(start);
	}

	/**
	 * <p>Gets a substring from the specified String avoiding exceptions.</p>
	 * <p/>
	 * <p>A negative start position can be used to start/end <code>n</code>
	 * characters from the end of the String.</p>
	 * <p/>
	 * <p>The returned substring starts with the character in the <code>start</code>
	 * position and ends before the <code>end</code> position. All position counting is
	 * zero-based -- i.e., to start at the beginning of the string use
	 * <code>start = 0</code>. Negative start and end positions can be used to
	 * specify offsets relative to the end of the String.</p>
	 * <p/>
	 * <p>If <code>start</code> is not strictly to the left of <code>end</code>, ""
	 * is returned.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.substring(null, *, *)    = null
	 * StringUtils.substring("", * ,  *)    = "";
	 * StringUtils.substring("abc", 0, 2)   = "ab"
	 * StringUtils.substring("abc", 2, 0)   = ""
	 * StringUtils.substring("abc", 2, 4)   = "c"
	 * StringUtils.substring("abc", 4, 6)   = ""
	 * StringUtils.substring("abc", 2, 2)   = ""
	 * StringUtils.substring("abc", -2, -1) = "b"
	 * StringUtils.substring("abc", -4, 2)  = "ab"
	 * </pre>
	 *
	 * @param str   the String to getValue the substring from, may be null
	 * @param start the position to start from, negative means
	 *              count back from the end of the String by this many characters
	 * @param end   the position to end at (exclusive), negative means
	 *              count back from the end of the String by this many characters
	 * @return substring from start position to end positon,
	 *         <code>null</code> if null String input
	 */
	public static String substring(String str, int start, int end) {
		if (str == null) {
			return null;
		}

		// handle negatives
		if (end < 0) {
			end = str.length() + end; // remember end is negative
		}
		if (start < 0) {
			start = str.length() + start; // remember start is negative
		}

		// validate length next
		if (end > str.length()) {
			end = str.length();
		}

		// if start is greater than end, return ""
		if (start > end) {
			return EMPTY;
		}

		if (start < 0) {
			start = 0;
		}
		if (end < 0) {
			end = 0;
		}

		return str.substring(start, end);
	}

	/**
	 * Cur string according to the length
	 * @param inputString
	 * @param length
	 * @return
	 */
	public static String subString(String inputString, int length) {

		String tempString = StringUtils.checkNull(inputString);

		if (tempString.length() >= length) {
			return tempString.substring(0, length);
		} else {
			return tempString;
		}
	}

	/**
	 * <p>Gets the substring after the first occurrence of a separator.
	 * The separator is not returned.</p>
	 * <p/>
	 * <p>A <code>null</code> string input will return <code>null</code>.
	 * An empty ("") string input will return the empty string.
	 * A <code>null</code> separator will return the empty string if the
	 * input string is not <code>null</code>.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.substringAfter(null, *)      = null
	 * StringUtils.substringAfter("", *)        = ""
	 * StringUtils.substringAfter(*, null)      = ""
	 * StringUtils.substringAfter("abc", "a")   = "bc"
	 * StringUtils.substringAfter("abcba", "b") = "cba"
	 * StringUtils.substringAfter("abc", "c")   = ""
	 * StringUtils.substringAfter("abc", "d")   = ""
	 * StringUtils.substringAfter("abc", "")    = "abc"
	 * </pre>
	 *
	 * @param str       the String to getValue a substring from, may be null
	 * @param separator the String to search for, may be null
	 * @return the substring after the first occurrence of the separator,
	 *         <code>null</code> if null String input
	 * @since 2.0
	 */
	public static String substringAfter(String str, String separator) {
		if (isEmpty(str)) {
			return str;
		}
		if (separator == null) {
			return EMPTY;
		}
		int pos = str.indexOf(separator);
		if (pos == -1) {
			return EMPTY;
		}
		return str.substring(pos + separator.length());
	}

	/**
	 * <p>Gets the substring after the last occurrence of a separator.
	 * The separator is not returned.</p>
	 * <p/>
	 * <p>A <code>null</code> string input will return <code>null</code>.
	 * An empty ("") string input will return the empty string.
	 * An empty or <code>null</code> separator will return the empty string if
	 * the input string is not <code>null</code>.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.substringAfterLast(null, *)      = null
	 * StringUtils.substringAfterLast("", *)        = ""
	 * StringUtils.substringAfterLast(*, "")        = ""
	 * StringUtils.substringAfterLast(*, null)      = ""
	 * StringUtils.substringAfterLast("abc", "a")   = "bc"
	 * StringUtils.substringAfterLast("abcba", "b") = "a"
	 * StringUtils.substringAfterLast("abc", "c")   = ""
	 * StringUtils.substringAfterLast("a", "a")     = ""
	 * StringUtils.substringAfterLast("a", "z")     = ""
	 * </pre>
	 *
	 * @param str       the String to getValue a substring from, may be null
	 * @param separator the String to search for, may be null
	 * @return the substring after the last occurrence of the separator,
	 *         <code>null</code> if null String input
	 * @since 2.0
	 */
	public static String substringAfterLast(String str, String separator) {
		if (isEmpty(str)) {
			return str;
		}
		if (isEmpty(separator)) {
			return EMPTY;
		}
		int pos = str.lastIndexOf(separator);
		if (pos == -1 || pos == str.length() - separator.length()) {
			return EMPTY;
		}
		return str.substring(pos + separator.length());
	}

	/**
	 * <p>Gets the substring before the first occurrence of a separator.
	 * The separator is not returned.</p>
	 * <p/>
	 * <p>A <code>null</code> string input will return <code>null</code>.
	 * An empty ("") string input will return the empty string.
	 * A <code>null</code> separator will return the input string.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.substringBefore(null, *)      = null
	 * StringUtils.substringBefore("", *)        = ""
	 * StringUtils.substringBefore("abc", "a")   = ""
	 * StringUtils.substringBefore("abcba", "b") = "a"
	 * StringUtils.substringBefore("abc", "c")   = "ab"
	 * StringUtils.substringBefore("abc", "d")   = "abc"
	 * StringUtils.substringBefore("abc", "")    = ""
	 * StringUtils.substringBefore("abc", null)  = "abc"
	 * </pre>
	 *
	 * @param str       the String to getValue a substring from, may be null
	 * @param separator the String to search for, may be null
	 * @return the substring before the first occurrence of the separator,
	 *         <code>null</code> if null String input
	 * @since 2.0
	 */
	public static String substringBefore(String str, String separator) {
		if (isEmpty(str) || separator == null) {
			return str;
		}
		if (separator.length() == 0) {
			return EMPTY;
		}
		int pos = str.indexOf(separator);
		if (pos == -1) {
			return str;
		}
		return str.substring(0, pos);
	}

	/**
	 * <p>Gets the substring before the last occurrence of a separator.
	 * The separator is not returned.</p>
	 * <p/>
	 * <p>A <code>null</code> string input will return <code>null</code>.
	 * An empty ("") string input will return the empty string.
	 * An empty or <code>null</code> separator will return the input string.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.substringBeforeLast(null, *)      = null
	 * StringUtils.substringBeforeLast("", *)        = ""
	 * StringUtils.substringBeforeLast("abcba", "b") = "abc"
	 * StringUtils.substringBeforeLast("abc", "c")   = "ab"
	 * StringUtils.substringBeforeLast("a", "a")     = ""
	 * StringUtils.substringBeforeLast("a", "z")     = "a"
	 * StringUtils.substringBeforeLast("a", null)    = "a"
	 * StringUtils.substringBeforeLast("a", "")      = "a"
	 * </pre>
	 *
	 * @param str       the String to getValue a substring from, may be null
	 * @param separator the String to search for, may be null
	 * @return the substring before the last occurrence of the separator,
	 *         <code>null</code> if null String input
	 * @since 2.0
	 */
	public static String substringBeforeLast(String str, String separator) {
		if (isEmpty(str) || isEmpty(separator)) {
			return str;
		}
		int pos = str.lastIndexOf(separator);
		if (pos == -1) {
			return str;
		}
		return str.substring(0, pos);
	}

	/**
	 * <p>Gets the String that is nested in between two instances of the
	 * same String.</p>
	 * <p/>
	 * <p>A <code>null</code> input String returns <code>null</code>.
	 * A <code>null</code> tag returns <code>null</code>.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.substringBetween(null, *)            = null
	 * StringUtils.substringBetween("", "")             = ""
	 * StringUtils.substringBetween("", "tag")          = null
	 * StringUtils.substringBetween("tagabctag", null)  = null
	 * StringUtils.substringBetween("tagabctag", "")    = ""
	 * StringUtils.substringBetween("tagabctag", "tag") = "abc"
	 * </pre>
	 *
	 * @param str the String containing the substring, may be null
	 * @param tag the String before and after the substring, may be null
	 * @return the substring, <code>null</code> if no match
	 * @since 2.0
	 */
	public static String substringBetween(String str, String tag) {
		return substringBetween(str, tag, tag);
	}

	/**
	 * <p>Gets the String that is nested in between two Strings.
	 * Only the first match is returned.</p>
	 * <p/>
	 * <p>A <code>null</code> input String returns <code>null</code>.
	 * A <code>null</code> open/close returns <code>null</code> (no match).
	 * An empty ("") open and close returns an empty string.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.substringBetween("wx[b]yz", "[", "]") = "b"
	 * StringUtils.substringBetween(null, *, *)          = null
	 * StringUtils.substringBetween(*, null, *)          = null
	 * StringUtils.substringBetween(*, *, null)          = null
	 * StringUtils.substringBetween("", "", "")          = ""
	 * StringUtils.substringBetween("", "", "]")         = null
	 * StringUtils.substringBetween("", "[", "]")        = null
	 * StringUtils.substringBetween("yabcz", "", "")     = ""
	 * StringUtils.substringBetween("yabcz", "y", "z")   = "abc"
	 * StringUtils.substringBetween("yabczyabcz", "y", "z")   = "abc"
	 * </pre>
	 *
	 * @param str   the String containing the substring, may be null
	 * @param open  the String before the substring, may be null
	 * @param close the String after the substring, may be null
	 * @return the substring, <code>null</code> if no match
	 * @since 2.0
	 */
	public static String substringBetween(String str, String open, String close) {
		if (str == null || open == null || close == null) {
			return null;
		}
		int start = str.indexOf(open);
		if (start != -1) {
			int end = str.indexOf(close, start + open.length());
			if (end != -1) {
				return str.substring(start + open.length(), end);
			}
		}
		return null;
	}

	/**
	 * <p>Searches a String for substrings delimited by a start and end tag,
	 * returning all matching substrings in an array.</p>
	 * <p/>
	 * <p>A <code>null</code> input String returns <code>null</code>.
	 * A <code>null</code> open/close returns <code>null</code> (no match).
	 * An empty ("") open/close returns <code>null</code> (no match).</p>
	 * <p/>
	 * <pre>
	 * StringUtils.substringsBetween("[a][b][c]", "[", "]") = ["a","b","c"]
	 * StringUtils.substringsBetween(null, *, *)            = null
	 * StringUtils.substringsBetween(*, null, *)            = null
	 * StringUtils.substringsBetween(*, *, null)            = null
	 * StringUtils.substringsBetween("", "[", "]")          = []
	 * </pre>
	 *
	 * @param str   the String containing the substrings, null returns null, empty returns empty
	 * @param open  the String identifying the start of the substring, empty returns null
	 * @param close the String identifying the end of the substring, empty returns null
	 * @return a String ArrayEx of substrings, or <code>null</code> if no match
	 * @since 2.3
	 */
	public static String[] substringsBetween(String str, String open, String close) {
		if (str == null || isEmpty(open) || isEmpty(close)) {
			return null;
		}
		int strLen = str.length();
		if (strLen == 0) {
			return new String[0];
		}
		int closeLen = close.length();
		int openLen = open.length();
		List<String> list = new ArrayList<String>();
		int pos = 0;
		while (pos < strLen - closeLen) {
			int start = str.indexOf(open, pos);
			if (start < 0) {
				break;
			}
			start += openLen;
			int end = str.indexOf(close, start);
			if (end < 0) {
				break;
			}
			list.add(str.substring(start, end));
			pos = end + closeLen;
		}
		if (list.isEmpty()) {
			return null;
		}
		return list.toArray(new String[list.size()]);
	}

	/**
	 * 将字符串按指定划分符分割成数组 如果不是以划分符结尾，加上划分符,结果数组长度=划分符数 如果要分析的字符串长度=0,返回长度为0的数组
	 * @param sData 要分隔的字符串
	 * @param symbol 分隔符
	 * @return
	 */
	public static final String[] toArray(String sData, String symbol) {
		if (sData == null || symbol == null) {
			return null;
		}
		if (sData.length() == 0) {
			return new String[] { "" };
		}

		int sbLen = symbol.length();

		// 如果不是以结束符结尾，加上结束符
		if (sData.length() < sbLen || !sData.substring(sData.length() - sbLen).equals(symbol)) {
			sData = sData + symbol;
		}

		String sRst[] = null;

		int j = 0;
		int i = sData.indexOf(symbol);
		while (i > -1) {
			j++;
			i += sbLen;
			i = sData.indexOf(symbol, i);
		}
		sRst = new String[j];
		j = 0;
		int k = 0;
		i = sData.indexOf(symbol);
		while (i > -1) {
			sRst[j++] = sData.substring(k, i);
			k = i + sbLen;
			i = sData.indexOf(symbol, k);
		}
		return sRst;
	}

	/**
	 * <p>作为分隔方便的方法来返回集合</p>
	 * Convenience method to return a Collection as a delimited (e.g. CSV)
	 * String. E.g. useful for <code>toString()</code> implementations.
	 *
	 * @param coll  Collection to display
	 * @param delim delimiter to use (probably a ",")
	 */
	public static String toDelimitedString(Collection<String> coll, String delim) {
		return toDelimitedString(coll, delim, "", "");
	}

	/**
	 * Convenience method to return a Collection as a delimited (e.g. CSV)
	 * String. E.g. useful for <code>toString()</code> implementations.
	 *
	 * @param coll   Collection to display
	 * @param delim  delimiter to use (probably a ",")
	 * @param prefix string to start each element with
	 * @param suffix string to end each element with
	 */
	public static String toDelimitedString(Collection<String> coll, String delim, String prefix, String suffix) {
		if (coll == null || coll.size() == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		Iterator<String> it = coll.iterator();
		while (it.hasNext()) {
			sb.append(prefix).append(it.next()).append(suffix);
			if (it.hasNext()) {
				sb.append(delim);
			}
		}
		return sb.toString();
	}

	/**
	 * 转换字符串为整数，待转换字符串必须是数字内容，如"138","-9",而"abc123"为非法
	 * @param s 待转换字符串(参数取值在-2147483647到2147483647之间)
	 * @param def 当s非法时返回默认数字
	 * @return
	 */
	public static int toInt(String s, int def) {
		try {
			return Integer.parseInt(s);
		} catch (Exception e) {
			return def;
		}
	}

	/**
	 * 转换字符串为长整型，待转换字符串必须是数字内容，如"138","-9",而"abc123"为非法
	 * @param s 待转换字符串
	 * @param def 当s非法时返回默认数字
	 * @return
	 */
	public static long toLong(String s, long def) {
		try {
			return Long.parseLong(s);
		} catch (Exception e) {
			return def;
		}
	}

	/**
	 * 去空格
	 * @param strObj
	 * @return
	 */
	public static final String trim(Object strObj) {
		if (strObj == null) {
			return "";
		}
		return strObj.toString().trim();
	}

	/**
	 * <p>Removes control characters (char &lt;= 32) from both
	 * ends of this String, handling <code>null</code> by returning
	 * <code>null</code>.</p>
	 * <p/>
	 * <p>The String is trimmed using {@link String#trim()}.
	 * Trim removes start and end characters &lt;= 32.
	 * To strip whitespace use {@link #strip(String)}.</p>
	 * <p/>
	 * <p>To trim your choice of characters, use the
	 * {@link #strip(String, String)} methods.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.trim(null)          = null
	 * StringUtils.trim("")            = ""
	 * StringUtils.trim("     ")       = ""
	 * StringUtils.trim("abc")         = "abc"
	 * StringUtils.trim("    abc    ") = "abc"
	 * </pre>
	 *
	 * @param str the String to be trimmed, may be null
	 * @return the trimmed string, <code>null</code> if null String input
	 */
	public static String trim(String str) {
		return str == null ? null : str.trim();
	}

	/**
	 * <p>对数组中所有的Stirng进行trim操作</p>
	 * Trims array of strings. <code>null</code> array elements are ignored.
	 */
	public static void trimAll(String[] strings) {
		for (int i = 0; i < strings.length; i++) {
			String string = strings[i];
			if (string != null) {
				strings[i] = string.trim();
			}
		}
	}

	/**
	 * Trim whitespaces from the left.
	 */
	public static String trimLeft(String src) {
		int len = src.length();
		int st = 0;
		while (st < len && CharacterUtils.isWhitespace(src.charAt(st))) {
			st++;
		}
		return st > 0 ? src.substring(st) : src;
	}

	/**
	 * Trim whitespaces from the right.
	 */
	public static String trimRight(String src) {
		int len = src.length();
		int count = len;
		while (len > 0 && CharacterUtils.isWhitespace(src.charAt(len - 1))) {
			len--;
		}
		return len < count ? src.substring(0, len) : src;
	}

	/**
	 * 根据最大长度截取字符串
	 * @param src 目标字符串
	 * @param max 最大长度
	 * @return
	 */
	public static String truncate(String src, int maxLen) {
		if (src == null) {
			return "";
		}
		if (src.length() >= maxLen) {
			return src.substring(0, maxLen);
		}
		return src;
	}

	/**
	 * <p>把一个英文字符串的第一个字符变成小写字母</p>
	 * <p>Uncapitalizes a String changing the first letter to title case as
	 * per {@link Character#toLowerCase(char)}. No other letters are changed.</p>
	 * <p/>
	 * <p>For a word based algorithm, see {@link TextUtils#uncapitalize(String)}.
	 * A <code>null</code> input String returns <code>null</code>.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.uncapitalize(null)  = null
	 * StringUtils.uncapitalize("")    = ""
	 * StringUtils.uncapitalize("Cat") = "cat"
	 * StringUtils.uncapitalize("CAT") = "cAT"
	 * </pre>
	 *
	 * @param str the String to uncapitalize, may be null
	 * @return the uncapitalized String, <code>null</code> if null String input
	 * @see TextUtils#uncapitalize(String)
	 * @see #capitalize(String)
	 * @since 2.0
	 */
	public static String uncapitalize(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return str;
		}
		return new StringBuilder(strLen).append(Character.toLowerCase(str.charAt(0))).append(str.substring(1))
				.toString();
	}

	/**
	 * <p>转换一个String为大写String,如果是null返回空</p> 
	 * <p>Converts a String to upper case as per {@link String#toUpperCase()}.</p>
	 * <p/>
	 * <p>A <code>null</code> input String returns <code>null</code>.</p>
	 * <p/>
	 * <pre>
	 * StringUtils.upperCase(null)  = null
	 * StringUtils.upperCase("")    = ""
	 * StringUtils.upperCase("aBc") = "ABC"
	 * </pre>
	 *
	 * @param str the String to upper case, may be null
	 * @return the upper cased String, <code>null</code> if null String input
	 */
	public static String upperCase(String str) {
		if (str == null) {
			return null;
		}
		return str.toUpperCase();
	}

	/**
	 * 字符串转换为ASCII码
	 * @param orgString
	 * @return
	 */
	public static String string2ASCII(String orgString) {
		String outString = "";
		for (int i = 0; i < orgString.length(); i++) {// 输出结果
			outString = outString + orgString.codePointAt(i) + "-";
		}
		return outString.trim();
	}

	/**
	 * 字符串转换为ASCII码
	 * @param orgString
	 * @return
	 */
	public static String[] string2ASCII(String[] orgStrings) {
		List<String> result = new ArrayList<String>(orgStrings.length);
		for (String orgString : orgStrings) {
			String outString = "";
			for (int i = 0; i < orgString.length(); i++) {// 输出结果
				outString = outString + orgString.codePointAt(i) + "-";
			}
			result.add(outString.trim());
		}
		return result.toArray(new String[0]);
	}

	/**
	 * ASCII转换为字符串
	 * @param charList
	 * @return
	 */
	public static String ASCII2String(String charList) {
		//String s = "22307-35806-24555-20048";// ASCII码
		String outString = "";
		String[] chars = charList.split("-");

		for (int i = 0; i < chars.length; i++) {
			outString = outString + (char) Integer.parseInt(chars[i]);
		}

		return outString;
	}

	/**
	 * ASCII转换为字符串
	 * @param charList
	 * @return
	 */
	public static String[] ASCII2String(String[] strList) {
		//String s = "22307-35806-24555-20048";// ASCII码
		List<String> result = new ArrayList<String>(strList.length);
		for (String charList : strList) {
			String outString = "";
			String[] chars = charList.split("-");
			for (int i = 0; i < chars.length; i++) {
				outString = outString + (char) Integer.parseInt(chars[i]);
			}
			result.add(outString);
		}
		return result.toArray(new String[0]);
	}
}

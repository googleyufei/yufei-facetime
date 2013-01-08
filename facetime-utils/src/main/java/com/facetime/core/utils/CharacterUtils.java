package com.facetime.core.utils;

import java.io.UnsupportedEncodingException;

/**
* Various character and character sequence utilities.
 */
public class CharacterUtils {

	public static final char LF = '\n';

	public static final char CR = '\r';

	/**
	 * <p>Checks whether the character is ASCII 7 bit alphabetic.</p>
	 *
	 * <pre>
	 *   CharUtils.isAsciiAlpha('a')  = true
	 *   CharUtils.isAsciiAlpha('A')  = true
	 *   CharUtils.isAsciiAlpha('3')  = false
	 *   CharUtils.isAsciiAlpha('-')  = false
	 *   CharUtils.isAsciiAlpha('\n') = false
	 *   CharUtils.isAsciiAlpha('&copy;') = false
	 * </pre>
	 * 
	 * @param ch  the character to validate
	 * @return true if between 65 and 90 or 97 and 122 inclusive
	 */
	public static boolean isAlpha(char ch) {
		return ch >= 'A' && ch <= 'Z' || ch >= 'a' && ch <= 'z';
	}

	/**
	 * <p>Checks whether the character is ASCII 7 bit alphabetic lower case.</p>
	 *
	 * <pre>
	 *   CharUtils.isAsciiAlphaLower('a')  = true
	 *   CharUtils.isAsciiAlphaLower('A')  = false
	 *   CharUtils.isAsciiAlphaLower('3')  = false
	 *   CharUtils.isAsciiAlphaLower('-')  = false
	 *   CharUtils.isAsciiAlphaLower('\n') = false
	 *   CharUtils.isAsciiAlphaLower('&copy;') = false
	 * </pre>
	 * 
	 * @param ch  the character to validate
	 * @return true if between 97 and 122 inclusive
	 */
	public static boolean isAlphaLower(char ch) {
		return ch >= 'a' && ch <= 'z';
	}

	/**
	 * <p>Checks whether the character is ASCII 7 bit numeric.</p>
	 *
	 * <pre>
	 *   CharUtils.isAsciiAlphanumeric('a')  = true
	 *   CharUtils.isAsciiAlphanumeric('A')  = true
	 *   CharUtils.isAsciiAlphanumeric('3')  = true
	 *   CharUtils.isAsciiAlphanumeric('-')  = false
	 *   CharUtils.isAsciiAlphanumeric('\n') = false
	 *   CharUtils.isAsciiAlphanumeric('&copy;') = false
	 * </pre>
	 * 
	 * @param ch  the character to validate
	 * @return true if between 48 and 57 or 65 and 90 or 97 and 122 inclusive
	 */
	public static boolean isAlphanumeric(char ch) {
		return ch >= 'A' && ch <= 'Z' || ch >= 'a' && ch <= 'z' || ch >= '0' && ch <= '9';
	}

	/**
	 * <p>Checks whether the character is ASCII 7 bit alphabetic upper case.</p>
	 *
	 * <pre>
	 *   CharUtils.isAsciiAlphaUpper('a')  = false
	 *   CharUtils.isAsciiAlphaUpper('A')  = true
	 *   CharUtils.isAsciiAlphaUpper('3')  = false
	 *   CharUtils.isAsciiAlphaUpper('-')  = false
	 *   CharUtils.isAsciiAlphaUpper('\n') = false
	 *   CharUtils.isAsciiAlphaUpper('&copy;') = false
	 * </pre>
	 * 
	 * @param ch  the character to validate
	 * @return true if between 65 and 90 inclusive
	 */
	public static boolean isAlphaUpper(char ch) {
		return ch >= 'A' && ch <= 'Z';
	}

	/**
	 * <p>Checks whether the character is ASCII 7 bit.</p>
	 *
	 * <pre>
	 *   CharUtils.isAscii('a')  = true
	 *   CharUtils.isAscii('A')  = true
	 *   CharUtils.isAscii('3')  = true
	 *   CharUtils.isAscii('-')  = true
	 *   CharUtils.isAscii('\n') = true
	 *   CharUtils.isAscii('&copy;') = false
	 * </pre>
	 * 
	 * @param ch  the character to validate
	 * @return true if less than 128
	 */
	public static boolean isAscii(char ch) {
		return ch < 128;
	}

	public static boolean isCharAtEqual(char[] source, int index, char match) {
		if (index < 0 || index >= source.length)
			return false;
		return source[index] == match;
	}

	public static boolean isCharAtEqual(CharSequence source, int index, char match) {
		if (index < 0 || index >= source.length())
			return false;
		return source.charAt(index) == match;
	}

	/**
	 * <p>Checks whether the character is ASCII 7 bit control.</p>
	 *
	 * <pre>
	 *   CharUtils.isAsciiControl('a')  = false
	 *   CharUtils.isAsciiControl('A')  = false
	 *   CharUtils.isAsciiControl('3')  = false
	 *   CharUtils.isAsciiControl('-')  = false
	 *   CharUtils.isAsciiControl('\n') = true
	 *   CharUtils.isAsciiControl('&copy;') = false
	 * </pre>
	 * 
	 * @param ch  the character to validate
	 * @return true if less than 32 or equals 127
	 */
	public static boolean isControl(char ch) {
		return ch < 32 || ch == 127;
	}

	public static boolean isDigit(char c) {
		return c >= '0' && c <= '9';
	}

	public static boolean isLetter(char c) {
		return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z';
	}

	public static boolean isLetterOrDigit(char c) {
		return isDigit(c) || isLetter(c);
	}

	/**
	 * Returns <code>true</code> if specified character is lowercase ASCII.
	 * If user uses only ASCIIs, it is much much faster.
	 */
	public static boolean isLowercaseLetter(char c) {
		return c >= 'a' && c <= 'z';
	}

	/**
	 * <p>Checks whether the character is ASCII 7 bit numeric.</p>
	 *
	 * <pre>
	 *   CharUtils.isAsciiNumeric('a')  = false
	 *   CharUtils.isAsciiNumeric('A')  = false
	 *   CharUtils.isAsciiNumeric('3')  = true
	 *   CharUtils.isAsciiNumeric('-')  = false
	 *   CharUtils.isAsciiNumeric('\n') = false
	 *   CharUtils.isAsciiNumeric('&copy;') = false
	 * </pre>
	 * 
	 * @param ch  the character to validate
	 * @return true if between 48 and 57 inclusive
	 */
	public static boolean isNumeric(char ch) {
		return ch >= '0' && ch <= '9';
	}

	/**
	 * <p>Checks whether the character is ASCII 7 bit printable.</p>
	 *
	 * <pre>
	 *   CharUtils.isAsciiPrintable('a')  = true
	 *   CharUtils.isAsciiPrintable('A')  = true
	 *   CharUtils.isAsciiPrintable('3')  = true
	 *   CharUtils.isAsciiPrintable('-')  = true
	 *   CharUtils.isAsciiPrintable('\n') = false
	 *   CharUtils.isAsciiPrintable('&copy;') = false
	 * </pre>
	 * 
	 * @param ch  the character to validate
	 * @return true if between 32 and 126 inclusive
	 */
	public static boolean isPrintable(char ch) {
		return ch >= 32 && ch < 127;
	}

	/**
	 * Returns <code>true</code> if specified character is uppercase ASCII.
	 * If user uses only ASCIIs, it is much much faster.
	 */
	public static boolean isUppercaseLetter(char c) {
		return c >= 'A' && c <= 'Z';
	}

	/**
	 * Returns <code>true</code> if character is a white space (<= ' ').
	 * White space definition is taken from String class (see: <code>trim()</code>).
	 */
	public static boolean isWhitespace(char c) {
		return c <= ' ';
	}

	public static boolean isWordChar(char c) {
		return isDigit(c) || isLetter(c) || c == '_';
	}

	/**
	 * Returns ASCII symbol of a char. In case of overload, 0x3F is returned.
	 */
	public static int toAscii(char c) {
		if (c <= 0xFF)
			return c;
		else
			return 0x3F;
	}

	/**
	 * Converts char array into ASCII array.
	 * @see #toAscii(char)
	 */
	public static byte[] toAsciiArray(char[] carr) {
		if (carr == null)
			return null;
		byte[] barr = new byte[carr.length];
		for (int i = 0; i < carr.length; i++)
			barr[i] = (byte) toAscii(carr[i]);
		return barr;
	}

	/**
	 * Converts char sequence into ASCII array.
	 */
	public static byte[] toAsciiArray(CharSequence charSequence) {
		if (charSequence == null)
			return null;
		byte[] barr = new byte[charSequence.length()];
		for (int i = 0; i < barr.length; i++)
			barr[i] = (byte) toAscii(charSequence.charAt(i));
		return barr;
	}

	/**
	 * Converts char array into byte array by stripping high byte.
	 */
	public static byte[] toByteArray(char[] carr) {
		if (carr == null)
			return null;
		byte[] barr = new byte[carr.length];
		for (int i = 0; i < carr.length; i++)
			barr[i] = (byte) carr[i];
		return barr;
	}

	/**
	 * Converts char array to byte array using provided Encoding.
	 */
	public static byte[] toByteArray(char[] carr, String charset) throws UnsupportedEncodingException {
		return new String(carr).getBytes(charset);
	}

	/**
	 * Converts char sequence into byte array. Chars are truncated to byte size.
	 */
	public static byte[] toByteArray(CharSequence charSequence) {
		if (charSequence == null)
			return null;
		byte[] barr = new byte[charSequence.length()];
		for (int i = 0; i < barr.length; i++)
			barr[i] = (byte) charSequence.charAt(i);
		return barr;
	}

	/**
	 * Converts byte array to char array by simply extending.
	 */
	public static char[] toCharArray(byte[] barr) {
		if (barr == null)
			return null;
		char[] carr = new char[barr.length];
		for (int i = 0; i < barr.length; i++)
			carr[i] = (char) barr[i];
		return carr;
	}

	/**
	 * Converts byte array of specific Encoding to char array.
	 */
	public static char[] toCharArray(byte[] barr, String charset) throws UnsupportedEncodingException {
		return new String(barr, charset).toCharArray();
	}

	/**
	 * <p>Converts the character to the Integer it represents, throwing an
	 * exception if the character is not numeric.</p>
	 * 
	 * <p>This method coverts the char '1' to the int 1 and so on.</p>
	 *
	 * <pre>
	 *   CharUtils.toIntValue('3')  = 3
	 *   CharUtils.toIntValue('A')  = IllegalArgumentException
	 * </pre>
	 *
	 * @param ch  the character to convert
	 * @return the int symbol of the character
	 * @throws IllegalArgumentException if the character is not ASCII numeric
	 */
	public static int toIntValue(char ch) {
		if (isNumeric(ch) == false)
			throw new IllegalArgumentException("The character " + ch + " is not in the range '0' - '9'");
		return ch - 48;
	}

	/**
	 * <p>Converts the character to the Integer it represents, throwing an
	 * exception if the character is not numeric.</p>
	 * 
	 * <p>This method coverts the char '1' to the int 1 and so on.</p>
	 *
	 * <pre>
	 *   CharUtils.toIntValue('3', -1)  = 3
	 *   CharUtils.toIntValue('A', -1)  = -1
	 * </pre>
	 *
	 * @param ch  the character to convert
	 * @param defaultValue  the default symbol to use if the character is not numeric
	 * @return the int symbol of the character
	 */
	public static int toIntValue(char ch, int defaultValue) {
		if (isNumeric(ch) == false)
			return defaultValue;
		return ch - 48;
	}

	/**
	 * <p>Converts the character to the Integer it represents, throwing an
	 * exception if the character is not numeric.</p>
	 * 
	 * <p>This method coverts the char '1' to the int 1 and so on.</p>
	 *
	 * <pre>
	 *   CharUtils.toIntValue(null) = IllegalArgumentException
	 *   CharUtils.toIntValue('3')  = 3
	 *   CharUtils.toIntValue('A')  = IllegalArgumentException
	 * </pre>
	 *
	 * @param ch  the character to convert, not null
	 * @return the int symbol of the character
	 * @throws IllegalArgumentException if the Character is not ASCII numeric or is null
	 */
	public static int toIntValue(Character ch) {
		if (ch == null)
			throw new IllegalArgumentException("The character must not be null");
		return toIntValue(ch.charValue());
	}

	/**
	 * <p>Converts the character to the Integer it represents, throwing an
	 * exception if the character is not numeric.</p>
	 * 
	 * <p>This method coverts the char '1' to the int 1 and so on.</p>
	 *
	 * <pre>
	 *   CharUtils.toIntValue(null, -1) = -1
	 *   CharUtils.toIntValue('3', -1)  = 3
	 *   CharUtils.toIntValue('A', -1)  = -1
	 * </pre>
	 *
	 * @param ch  the character to convert
	 * @param defaultValue  the default symbol to use if the character is not numeric
	 * @return the int symbol of the character
	 */
	public static int toIntValue(Character ch, int defaultValue) {
		if (ch == null)
			return defaultValue;
		return toIntValue(ch.charValue(), defaultValue);
	}

	/**
	 * Lowers uppercase ASCII char.
	 */
	public static char toLowerAscii(char c) {
		if (isUppercaseLetter(c))
			c += (char) 0x20;
		return c;
	}

	/**
	 * Uppers lowercase ASCII char.
	 */
	public static char toUpperAscii(char c) {
		if (isLowercaseLetter(c))
			c -= (char) 0x20;
		return c;
	}

	/**
	 * <p>Converts the string to the unicode format '\u0020'.</p>
	 * 
	 * <p>This format is the Java source code format.</p>
	 *
	 * <pre>
	 *   CharUtils.unicodeEscaped(' ') = "\u0020"
	 *   CharUtils.unicodeEscaped('A') = "\u0041"
	 * </pre>
	 * 
	 * @param ch  the character to convert
	 * @return the escaped unicode string
	 */
	public static String unicodeEscaped(char ch) {
		if (ch < 0x10)
			return "\\u000" + Integer.toHexString(ch);
		else if (ch < 0x100)
			return "\\u00" + Integer.toHexString(ch);
		else if (ch < 0x1000)
			return "\\u0" + Integer.toHexString(ch);
		return "\\u" + Integer.toHexString(ch);
	}

	/**
	 * <p>Converts the string to the unicode format '\u0020'.</p>
	 * 
	 * <p>This format is the Java source code format.</p>
	 * 
	 * <p>If <code>null</code> is passed in, <code>null</code> will be returned.</p>
	 *
	 * <pre>
	 *   CharUtils.unicodeEscaped(null) = null
	 *   CharUtils.unicodeEscaped(' ')  = "\u0020"
	 *   CharUtils.unicodeEscaped('A')  = "\u0041"
	 * </pre>
	 * 
	 * @param ch  the character to convert, may be null
	 * @return the escaped unicode string, null if null input
	 */
	public static String unicodeEscaped(Character ch) {
		if (ch == null)
			return null;
		return unicodeEscaped(ch.charValue());
	}
}

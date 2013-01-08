package com.facetime.core.utils;

/**
 * 一个简单的随机字符串生成器<br></>
 * 用于 cookie pin
 */
public class RandomGenerator {

	public static String SEED1 = "0123456789";

	public static String SEED2 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public static String SEED3 = "abcdefghijklmnopqrstuvwxyz";

	public static String getPinNumber() {
		return getRandomStr(SEED1, 4);
	}

	public static String getRandomStr() {
		return getRandomStr(8);
	}

	public static String getRandomStr(int length) {
		return getRandomStr(SEED1 + SEED2 + SEED3, length);
	}

	public static String getRandomStr(String key, int length) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++)
			sb.append(key.charAt((int) (Math.random() * key.length())));
		return sb.toString();
	}
}

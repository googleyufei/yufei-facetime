package com.facetime.mgr.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 提供hash MD5算法加密
 */

public class HashUtil {
	private static MessageDigest digest = null;

	public HashUtil() {
	}

	/**
	 * 对字符串进行HASH(MD5)加密，注意，加密结果不可逆
	 * @param data 待加密数据
	 * @return 加密结果
	 */
	public synchronized static final String hash(String data) {
		if (digest == null) {
			try {
				digest = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException nsae) {
				System.err.println("Failed to load the MD5 MessageDigest. ");
				nsae.printStackTrace();
			}
		}
		digest.update(data.getBytes());
		return encodeHex(digest.digest());
	}

	/**
	 * 将数组转换成16进制字符串
	 * @param bytes
	 * @return
	 */
	private static final String encodeHex(byte[] bytes) {
		StringBuffer buf = new StringBuffer(bytes.length * 2);
		for (byte b : bytes) {
			if ((b & 0xff) < 0x10) {
				buf.append("0");
			}
			buf.append(Long.toString(b & 0xff, 16));
		}
		return buf.toString();
	}
}
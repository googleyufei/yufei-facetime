package com.facetime.core.utils;

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import com.facetime.core.security.Base64;
import com.sun.crypto.provider.SunJCE;

/**
 * 消息摘要加解密功能辅助类<br/>包含MD5 SHA 和加解密方法
 * 
 * @author dzb
 */
public class DigestUtils {

	public static final String DES_ALGORITHM = "DES";
	public static final String MD5_ALGORITHM = "MD5";
	public static final String SHA_ALGORITHM = "SHA-1";

	/**
	 * Used building output as Hex
	 */
	private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };

	/**
	 *
	 * @param key
	 * @param encryptedString
	 * @return
	 */
	public static String decrypt(Key key, String encryptedString) {
		try {
			Security.addProvider(new SunJCE());

			Cipher cipher = Cipher.getInstance(DES_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, key);

			byte[] encryptedBytes = Base64.decode(encryptedString);
			byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

			return new String(decryptedBytes, "UTF8");
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * Converts an array of bytes into an array of characters representing the hexidecimal values of each byte in order.
	 * The returned array will be double the length of the passed array, as it takes two characters to represent any
	 * given byte.
	 *
	 * @param data
	 *                  a byte[] to convert to Hex characters
	 * @return A char[] containing hexidecimal characters
	 */
	public static char[] encodeHex(byte[] data) {

		int l = data.length;

		char[] out = new char[l << 1];

		// two characters form the hex symbol.
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
			out[j++] = DIGITS[0x0F & data[i]];
		}

		return out;
	}

	/**
	 *
	 * @param key
	 * @param plainText
	 * @return
	 */
	public static String encrypt(Key key, String plainText) {

		try {
			Security.addProvider(new SunJCE());

			Cipher cipher = Cipher.getInstance(DES_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, key);

			byte[] decryptedBytes = plainText.getBytes("UTF8");
			byte[] encryptedBytes = cipher.doFinal(decryptedBytes);

			String encryptedString = Base64.encodeToString(encryptedBytes, false);

			return encryptedString;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 *
	 * @return
	 */
	public static Key generateKey() {
		try {
			Security.addProvider(new SunJCE());
			KeyGenerator generator = KeyGenerator.getInstance(DES_ALGORITHM);
			generator.init(56, new SecureRandom());
			return generator.generateKey();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 依据给定的<b>算法</b>返回消息摘要类<br/>
	 * Returns a MessageDigest for the given <code>algorithm</code>.
	 *
	 * @param algorithm 
	 * 				消息摘要算法名称<br/>
	 * 				The MessageDigest algorithm getName.
	 * @return An MD5 digest creating.
	 * @throws RuntimeException 
	 * 				捕捉没有算法<br/>
	 * 				when a {@link java.security.NoSuchAlgorithmException} is caught,
	 */
	public static MessageDigest getMessageDigest(String algorithm) {
		try {
			return MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * Calculates the MD5 digest and returns the symbol as a 16 element
	 * <code>byte[]</code>.
	 *
	 * @param data Data to digest
	 * @return MD5 digest
	 */
	public static byte[] md5(byte[] data) {
		return getMD5().digest(data);
	}

	/**
	 * Calculates the MD5 digest and returns the symbol as a 16 element
	 * <code>byte[]</code>.
	 *
	 * @param data Data to digest
	 * @return MD5 digest
	 */
	public static byte[] md5(String data) {
		return md5(data.getBytes());
	}

	/**
	 * Calculates the MD5 digest and returns the symbol as a 32 character
	 * hex string.
	 *
	 * @param data Data to digest
	 * @return MD5 digest as a hex string
	 */
	public static String md5Hex(byte[] data) {
		return new String(encodeHex(md5(data)));
	}

	/**
	 * Calculates the MD5 digest and returns the symbol as a 32 character
	 * hex string.
	 *
	 * @param data Data to digest
	 * @return MD5 digest as a hex string
	 */
	public static String md5Hex(String data) {
		return new String(encodeHex(md5(data)));
	}

	/**
	 * Calculates the SHA digest and returns the symbol as a
	 * <code>byte[]</code>.
	 *
	 * @param data Data to digest
	 * @return SHA digest
	 */
	public static byte[] sha(byte[] data) {
		return getSHA().digest(data);
	}

	/**
	 * Calculates the SHA digest and returns the symbol as a
	 * <code>byte[]</code>.
	 *
	 * @param data Data to digest
	 * @return SHA digest
	 */
	public static byte[] sha(String data) {
		return sha(data.getBytes());
	}

	/**
	 * Calculates the SHA digest and returns the symbol as a hex string.
	 *
	 * @param data Data to digest
	 * @return SHA digest as a hex string
	 */
	public static String shaHex(byte[] data) {
		return new String(encodeHex(sha(data)));
	}

	/**
	 * Calculates the SHA digest and returns the symbol as a hex string.
	 *
	 * @param data Data to digest
	 * @return SHA digest as a hex string
	 */
	public static String shaHex(String data) {
		return new String(encodeHex(sha(data)));
	}

	/**
	 * Returns an MD5 MessageDigest.
	 *
	 * @return An MD5 digest creating.
	 * @throws RuntimeException when a {@link java.security.NoSuchAlgorithmException} is caught,
	 */
	private static MessageDigest getMD5() {
		return getMessageDigest(MD5_ALGORITHM);
	}

	/**
	 * Returns an SHA digest.
	 *
	 * @return An SHA digest creating.
	 * @throws RuntimeException when a {@link java.security.NoSuchAlgorithmException} is caught,
	 */
	private static MessageDigest getSHA() {
		return getMessageDigest(SHA_ALGORITHM);
	}
}

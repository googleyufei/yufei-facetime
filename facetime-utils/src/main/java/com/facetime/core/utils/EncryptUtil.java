package com.facetime.core.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import com.facetime.core.file.ZipUtil;
import com.facetime.core.file.ZipUtils;

/**
 * @author shp
 * @since 2005.12.12
 */

public final class EncryptUtil {

	/**
	 * 将字符byte数组转换为16进制字符串
	 * @param bytes byte数组
	 * @return 转换后的16进制字符串
	 */
	public static String byte2hex(byte bytes[]) {
		StringBuffer retString = new StringBuffer();
		for (int i = 0; i < bytes.length; ++i) {
			retString.append(Integer.toHexString(0x0100 + (bytes[i] & 0x00FF)).substring(1).toUpperCase());
		}
		return retString.toString();
	}

	// 解密
	public static String Dec_RSA(String miwen, String dStr, String nStr) {
		StringBuffer mingwen = new StringBuffer();
		BigInteger d = new BigInteger(dStr);// 获取私钥的参数d,n
		BigInteger n = new BigInteger(nStr);
		BigInteger c = new BigInteger(miwen);
		BigInteger m = c.modPow(d, n);// 解密明文
		byte[] mt = m.toByteArray();// 计算明文对应的字符串并输出
		for (byte element : mt) {
			mingwen.append((char) element);
		}
		return mingwen.toString();
	}

	/**
	 * file中存放密文，解密后存放到file文件中
	 * @param file dest
	 * @return
	 */
	public static void decrypt(String file, String dest) throws Exception {
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.DECRYPT_MODE, getKey());
		InputStream is = new FileInputStream(file);
		OutputStream out = new FileOutputStream(dest);
		CipherOutputStream cos = new CipherOutputStream(out, cipher);
		byte[] buffer = new byte[1024];
		int r;
		while ((r = is.read(buffer)) >= 0) {
			cos.write(buffer, 0, r);
		}
		cos.close();
		out.close();
		is.close();
	}

	// 加密在RSA公钥中包含有两个整数信息：e和n。对于明文数字m,计算密文的公式是m的e次方再与n求模。
	public static String Enc_RSA(String mingwen, String eStr, String nStr) {
		String miwen = new String();
		try {
			BigInteger e = new BigInteger(eStr);
			BigInteger n = new BigInteger(nStr);
			byte[] ptext = mingwen.getBytes("UTF8"); // 获取明文的大整数
			BigInteger m = new BigInteger(ptext);
			BigInteger c = m.modPow(e, n);
			miwen = c.toString();
		} catch (UnsupportedEncodingException ex) {
			Logger.getLogger(ZipUtil.class.getName()).log(Level.SEVERE, null, ex);
		}
		return miwen;
	}

	/**
	 * file中存放明文，加密后存放到dest文件中
	 * @param file dest
	 * @return
	 */
	public static void encrypt(String file, String dest) throws Exception {
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.ENCRYPT_MODE, getKey());
		InputStream is = new FileInputStream(file);
		OutputStream out = new FileOutputStream(dest);
		CipherInputStream cis = new CipherInputStream(is, cipher);
		byte[] buffer = new byte[1024];
		int r;
		while ((r = cis.read(buffer)) > 0) {
			out.write(buffer, 0, r);
		}
		cis.close();
		is.close();
		out.close();
	}

	public static byte[] generateMacBytes(byte[] mabBytes) {
		int DES_BLOCK_SIZE = 8;

		/*
		 * 将MAB中的每8个字节分为一组（最后宜不足8个子节，则右补0x00）,用MAK作为DES密钥依次对除最后一组外的每一组进行如下操作： a) 进行DES运算 b)
		 * 将运算结果与后面一组的8个字节异或，结果取代后一组，继续进行操作。 对最后一组进行DES运算，得出8个字节的加密值
		 */
		// The DES/CBC method is just as above
		byte[] desKey = hex2byte("0123456789ABCDEF");
		// byte[] retnBytes = encoder.encode( mabBytes );
		byte[] retnBytes = new byte[mabBytes.length];
		byte[] procBytes = new byte[mabBytes.length];

		System.arraycopy(mabBytes, 0, procBytes, 0, mabBytes.length);

		int iBlkCnt = mabBytes.length / DES_BLOCK_SIZE;
		byte[] nextBlock = new byte[DES_BLOCK_SIZE];

		byte[] curBlock = new byte[DES_BLOCK_SIZE];
		for (int i = 0; i < iBlkCnt - 1; i++) {
			// get current block
			System.arraycopy(procBytes, i * DES_BLOCK_SIZE, curBlock, 0, DES_BLOCK_SIZE);
			// get next block
			System.arraycopy(procBytes, (i + 1) * DES_BLOCK_SIZE, nextBlock, 0, DES_BLOCK_SIZE);

			// 用MAK作为DES密钥依次对除最后一组外的每一组进行进行DES运算
			// byte[] encryptBlock = encoder.encode( curBlock );
			byte[] encryptBlock = doEncrypt(curBlock, desKey);
			// 将运算结果与后面一组的8个字节异或，结果取代后一组
			for (int j = 0; j < DES_BLOCK_SIZE; j++) {
				nextBlock[j] = (byte) (encryptBlock[j] ^ nextBlock[j]);
			}
			System.arraycopy(nextBlock, 0, procBytes, (i + 1) * DES_BLOCK_SIZE, DES_BLOCK_SIZE);

			// 将每一组进行进行DES运算后的结果存回去
			System.arraycopy(encryptBlock, 0, retnBytes, i * DES_BLOCK_SIZE, DES_BLOCK_SIZE);
		}

		// added by qzou for the MAB with only 1 block, 20060911 begin
		if (iBlkCnt < 2) {
			System.arraycopy(mabBytes, 0, nextBlock, 0, DES_BLOCK_SIZE);
		}
		// added by qzou for the MAB with only 1 block, 20060911 end

		// 　对最后一组进行DES运算，得出8个字节的加密值
		// byte[] encryptLastBlock = encoder.encode( nextBlock );
		byte[] encryptLastBlock = doEncrypt(nextBlock, desKey);
		// 将最后一组进行DES运算后的结果存回去
		System.arraycopy(encryptLastBlock, 0, retnBytes, mabBytes.length - DES_BLOCK_SIZE, DES_BLOCK_SIZE);

		return encryptLastBlock;
	}

	/**
	 * 解密 以String密文输入,String明文输出
	 * @param strMi
	 * @return
	 */
	public static String getDesString(String strMi) {

		String strMing = "";
		try {
			return new String(getDesCode(hex2byte(strMi)));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return strMing;
	}

	/**
	 * 加密String明文输入,String密文输出
	 * @param strMing
	 * @return
	 */
	public static String getEncString(String strMing) {
		String strMi = "";
		try {
			return byte2hex(getEncCode(strMing.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return strMi;
	}

	/**
	 * 从文件中获取key
	 * @param
	 * @return key
	 */
	public static Key getKey() {
		Key kp = null;
		try {
			// 相对路径 需要新建 conf 文件夹
			// String fileName = "conf/DesKey.xml";
			// InputStream is = Encrypt.class.getClassLoader().getResourceAsStream(fileName);
			// 绝对路径
			String fileName = System.getProperty("user.dir") + "/DesKey.txt";;
			FileInputStream is = new FileInputStream(fileName);

			ObjectInputStream oos = new ObjectInputStream(is);
			kp = (Key) oos.readObject();
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return kp;
	}

	public static byte[] getMABBytes(String macString) {
		int DES_BLOCK_SIZE = 8;
		byte[] tmpBytes = macString.getBytes();

		byte[] retnBytes;
		int iOrigLen = tmpBytes.length;
		int iMode = iOrigLen % DES_BLOCK_SIZE;
		int iAppend = 0;

		if (iMode != 0) {
			iAppend = DES_BLOCK_SIZE - iMode;
		}

		retnBytes = new byte[iOrigLen + iAppend];
		System.arraycopy(tmpBytes, 0, retnBytes, 0, iOrigLen);
		for (int i = iOrigLen; i < iOrigLen + iAppend; i++) {
			retnBytes[i] = (byte) 0x00;
		}
		return retnBytes;
	}

	/**
	 * 将16进制字符串转换为字符byte数组
	 * @param hex 16进制字符串
	 * @return 转换后的字符byte数组
	 */
	public static byte[] hex2byte(String hex) {
		byte[] bts = new byte[hex.length() / 2];
		for (int i = 0; i < bts.length; i++) {
			bts[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return bts;
	}

	public static void main(String[] args) {
		try {

			System.out.println("==" + EncryptUtil.MD5("admin"));
			System.out.println("==" + EncryptUtil.MD5("admin"));
			System.out.println("==" + EncryptUtil.MD5("admin"));

			System.out.println("test1");
			String a[] = Skey_RSA(512);
			System.out.println(a[0]);
			System.out.println(a[1]);
			System.out.println(a[2]);
			System.out.println(a[3]);
			System.out.println(a[4]);
			System.out.println("test2");
			String rsajm = Enc_RSA("abcd1234", a[0], a[1]);
			System.out.println(Dec_RSA(rsajm, a[2], a[1]));
			System.out.println("test3");

			EncryptUtil.saveDesKey();
			System.out.println("生成key");
			EncryptUtil.getKey().toString();
			System.out.println("获取key");
			EncryptUtil.encrypt("d:\\hello.txt", "d:\\encrypt.txt");
			System.out.println("加密");
			EncryptUtil.decrypt("d:\\encrypt.txt", "d:\\decrypt.txt");
			System.out.println("解密");

			String strEnc = EncryptUtil.getEncString("云海飞舞雲122"); // 加密字符串,返回String的密文
			System.out.println(strEnc);
			String strDes = EncryptUtil.getDesString(strEnc); // 把String 类型的密文解密
			System.out.println(strDes);
		}

		catch (java.lang.Exception e3) {
			e3.printStackTrace();
		}
	}

	/**
	 * MD5加密算法
	 * @param arg0 - 要加密的字符串
	 * @return 加密过后的字符串
	 */

	public static String MD5(String arg0) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp = arg0.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 随机产生DESkey 存放到DesKey.txt中
	 * @param
	 * @return
	 */
	public static void saveDesKey() {
		try {
			SecureRandom sr = new SecureRandom();
			// 为我们选择的DES算法生成一个KeyGenerator对象
			KeyGenerator kg = KeyGenerator.getInstance("DES");
			kg.init(sr);
			// 相对路径 需要新建 conf 文件夹
			// String fileName = "conf/DesKey.xml";
			// 绝对路径
			String fileName = System.getProperty("user.dir") + "/DesKey.txt";
			FileOutputStream fos = new FileOutputStream(fileName);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			// 生成密钥
			Key key = kg.generateKey();
			System.out.println(key.getAlgorithm());
			System.out.println(key.getEncoded().length);
			for (int i = 0; i < key.getEncoded().length; i++) {
				System.out.println(key.getEncoded()[i]);
			}

			System.out.println(key.getEncoded());

			System.out.println(key.getFormat());

			System.out.println(ZipUtil.bytesToHexString(key.getEncoded()));
			oos.writeObject(key);
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 创建密钥对生成器，指定加密和解密算法为RSA
	public static String[] Skey_RSA(int keylen) {// 输入密钥长度
		String[] output = new String[5]; // 用来存储密钥的e n d p q
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(keylen); // 指定密钥的长度，初始化密钥对生成器
			KeyPair kp = kpg.generateKeyPair(); // 生成密钥对
			RSAPublicKey puk = (RSAPublicKey) kp.getPublic();
			RSAPrivateCrtKey prk = (RSAPrivateCrtKey) kp.getPrivate();
			BigInteger e = puk.getPublicExponent();
			BigInteger n = puk.getModulus();
			BigInteger d = prk.getPrivateExponent();
			BigInteger p = prk.getPrimeP();
			BigInteger q = prk.getPrimeQ();
			output[0] = e.toString();
			output[1] = n.toString();
			output[2] = d.toString();
			output[3] = p.toString();
			output[4] = q.toString();
		} catch (NoSuchAlgorithmException ex) {
			Logger.getLogger(ZipUtils.class.getName()).log(Level.SEVERE, null, ex);
		}
		return output;
	}

	/**
	 * 加密算法
	 * @param plainText 加密对象
	 * @param desKey 密匙数据
	 * @return 加密后的字符数组
	 */
	private static byte[] doEncrypt(byte[] plainText, byte[] desKey) {
		try {

			// DES算法要求有一个可信任的随机数源
			// SecureRandom sr = new SecureRandom();
			/* 用某种方法获得密匙数据 */
			byte rawKeyData[] = desKey;
			// 从原始密匙数据创建DESKeySpec对象
			DESKeySpec dks = new DESKeySpec(rawKeyData);
			// 创建一个密匙工厂，然后用它把DESKeySpec转换成
			// 一个SecretKey对象
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey key = keyFactory.generateSecret(dks);
			// Cipher对象实际完成加密操作
			Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding"); // DES/CBC/NoPadding"); ;;;PKCS5Padding

			cipher.init(Cipher.ENCRYPT_MODE, key); // , sr);
			// 现在，获取数据并加密

			// 正式执行加密操作
			byte[] encryptedData = cipher.doFinal(plainText);

			return encryptedData;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 解密以byte[]密文输入,以byte[]明文输出
	 * @param byteD
	 * @return
	 */

	private static byte[] getDesCode(byte[] byteD) {
		Cipher cipher;
		byte[] byteFina = null;
		try {
			cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, EncryptUtil.getKey());
			byteFina = cipher.doFinal(byteD);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cipher = null;
		}
		return byteFina;
	}

	/**
	 * 加密以byte[]明文输入,byte[]密文输出
	 * @param byteS
	 * @return
	 */

	private static byte[] getEncCode(byte[] byteS) {

		byte[] byteFina = null;
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, EncryptUtil.getKey());
			byteFina = cipher.doFinal(byteS);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cipher = null;
		}
		return byteFina;
	}

}

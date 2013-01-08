/*
 * GUID.java
 *
 * Created on 2006�?12�?22�?, 上午12:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.facetime.core.utils;

import java.io.Serializable;
import java.util.UUID;

/**
 * 使用java.util.UUID替换了原有的GUIDGenerator实现。<br/> 
 * 当然，一如既往的支持无分隔符32位长度。
 * 
 * GUID_PROTOTYPE = "XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX";<br></>
 */

public final class UUIDUtils implements Serializable, Comparable<UUIDUtils> {

	private static final long serialVersionUID = 6010836986783019344L;

	private UUID _uuid;

	private UUIDUtils() {
	}

	public int compareTo(UUIDUtils obj) {
		UUIDUtils guid = obj;
		return _uuid.compareTo(guid._uuid);
	}

	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (obj.getClass() == getClass())
			return _uuid.equals(((UUIDUtils) obj)._uuid);
		else
			return false;
	}

	public int hashCode() {
		return _uuid.hashCode();
	}

	public String toString() {
		return toOneString(_uuid);
	}

	public static UUIDUtils create() {
		UUIDUtils guid = new UUIDUtils();
		guid._uuid = UUID.randomUUID();
		return guid;
	}

	public static void main(String[] args) {
		StopWatch sw = new StopWatch();
		sw.start("bbb");
		for (int i = 0; i < 100000; i++)
			UUID.randomUUID();
		sw.stop();
		sw.start("aaa");
		for (int i = 0; i < 100000; i++)
			UUIDUtils.nextUUID();
		sw.stop();
		System.out.println(sw.prettyPrint());

		System.out.println(UUID.randomUUID());
		String s = UUIDUtils.nextUUID();
		System.out.println(s);
		UUIDUtils guid = UUIDUtils.of(s.toUpperCase());
		System.out.println(guid);

	}

	public static String nextUUID() {
		return toOneString(UUID.randomUUID());
	}

	public static UUIDUtils of(String s) {
		if (s.indexOf("-") < 0) {
			//无分割符号的必须是32的长度
			if (s.length() != 32)
				throw new IllegalArgumentException("Invalid UUID string: " + s);
			StringBuilder sb = new StringBuilder(36);
			sb.append(s.substring(0, 8)).append("-").append(s.substring(8, 12)).append("-").append(s.substring(12, 16))
					.append("-").append(s.substring(16, 20)).append("-").append(s.substring(20));
			s = sb.toString();
		}
		UUID uid = UUID.fromString(s);
		UUIDUtils guid = new UUIDUtils();
		guid._uuid = uid;
		return guid;
	}

	private static String toOneString(UUID uuid) {
		String s = uuid.toString();
		StringBuilder sb = new StringBuilder(32);
		sb.append(s.substring(0, 8)).append(s.substring(9, 13)).append(s.substring(14, 18)).append(s.substring(19, 23))
				.append(s.substring(24));
		return sb.toString().toUpperCase();
	}
}

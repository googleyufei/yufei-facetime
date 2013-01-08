package com.shop.test;

import org.junit.Test;

/**
 *
 */
public class CommonTest {

	@Test
	public void unicodeTest() {
		String s = "" + 0 + 'a'; // 0=48,9=57
		// A=65,Z=90;a=97,z=122;空格=32
		int i = s.codePointAt(0);
		int j = s.codePointAt(1);
		// 利用这codePointAt(int index)方法
		System.out.printf("%d %d", i, j);
	}

}

package com.facetime.core.utils;

import com.facetime.core.collection.ArrayEx;

/**
 * 字符转义器
 *
 */
public class ValueEscaper {

	private final ArrayEx<Character> chars = ArrayEx.of(Character.class);
	private final ArrayEx<String> escapes = ArrayEx.of(String.class);

	/**
	 * 设定逃逸方式
	 * 
	 * @param c
	 *            要逃逸的字符
	 * @param s
	 *            如何逃逸
	 * @return 自身
	 */
	public ValueEscaper add(char c, String s) {
		chars.push(c);
		escapes.push(s);
		return this;
	}

	/**
	 * 根据逃逸的设置，对传入的字符串进行逃逸
	 * 
	 * @param cs
	 *            字符序列
	 * @return 逃逸后的字符序列
	 */
	public CharSequence escape(CharSequence cs) {
		StringBuilder sb = new StringBuilder();
		boolean find;
		for (int i = 0; i < cs.length(); i++) {
			char c = cs.charAt(i);
			find = false;
			for (int j = 0; j < chars.size(); j++)
				if (c == chars.items[j]) {
					sb.append(escapes.items[j]);
					find = true;
					break;
				}
			if (!find)
				sb.append(c);
		}
		return sb;
	}

	public static void main(String[] args) {
		ValueEscaper ve = new ValueEscaper();
		ve.add(':', "::");

		String s = "select * from a where a = 'a' and b = :a";
		s = ve.escape(s).toString();
		System.out.println(s);

	}
}

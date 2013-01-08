package com.facetime.core.io;

import java.io.IOException;
import java.io.Reader;

/**
 * 可接受一个CharSequence的StringReader实现<br></>
 * 为区别于JDK自身的StringReader 故命名为StringReader2
 */
public class StringReader2 extends Reader {

	private CharSequence cs;
	private int index;

	public StringReader2(CharSequence cs) {
		this.cs = cs;
		index = 0;
	}

	@Override
	public void close() throws IOException {}

	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		if (index >= cs.length())
			return -1;
		int count = 0;
		for (int i = off; i < (off + len); i++) {
			if (index >= cs.length())
				return count;
			cbuf[i] = cs.charAt(index++);
			count++;
		}
		return count;
	}
}

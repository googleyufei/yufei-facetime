package com.facetime.core.io;

import java.io.IOException;
import java.io.Writer;

/**
 * 使用StringBuilder实现一个简单的StringWriter<p></>
 * 为区别JDK自身的StringWriter 故命名StringWriter2
 */
public class StringWriter2 extends Writer {

	private StringBuilder sb;

	public StringWriter2(StringBuilder sb) {
		this.sb = sb;
	}

	@Override
	public void close() throws IOException {}

	@Override
	public void flush() throws IOException {}

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		for (int i = off; i < (off + len); i++) {
			sb.append(cbuf[i]);
		}
	}

	public StringBuilder getStringBuilder() {
		return sb;
	}
}

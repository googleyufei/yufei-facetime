package com.facetime.core.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

import com.facetime.core.utils.CharacterUtils;
import com.facetime.core.utils.DefaultSettings;

/**
 * Provides an OutputStream to an internal String. Internally converts bytes
 * to a Strings and stores them in an internal StringBuilder.
 */
public class StringOutputStream extends OutputStream implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * The internal destination StringBuilder.
	 */
	protected final StringBuilder buf;
	protected final String encoding;

	/**
	 * Creates new StringOutputStream, makes a new internal StringBuilder.
	 */
	public StringOutputStream() {
		super();
		buf = new StringBuilder();
		encoding = DefaultSettings.Encoding;
	}

	public StringOutputStream(StringBuilder input) {
		super();
		buf = input;
		encoding = DefaultSettings.Encoding;
		;
	}

	public StringOutputStream(StringBuilder input, String encoding) {
		super();
		buf = input;
		this.encoding = encoding;
	}

	/**
	* Sets the internal StringBuilder to null.
	*/
	@Override
	public void close() {
		buf.setLength(0);

	}

	/**
	 * Returns the content of the internal StringBuilder as a String, the result
	 * of all writing to this OutputStream.
	 *
	 * @return returns the content of the internal StringBuilder
	 */
	@Override
	public String toString() {
		return buf.toString();
	}

	/**
	 * Writes and appends byte array to StringOutputStream.
	 *
	 * @param b      byte array
	 */
	@Override
	public void write(byte[] b) throws IOException {

		buf.append(CharacterUtils.toCharArray(b, encoding));
	}

	/**
	 * Writes and appends a byte array to StringOutputStream.
	 *
	 * @param b      the byte array
	 * @param off    the byte array starting index
	 * @param len    the number of bytes from byte array to write to the stream
	 */
	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		if (off < 0 || len < 0 || off + len > b.length)
			throw new IndexOutOfBoundsException("Parameters out of bounds.");
		byte[] bytes = new byte[len];
		for (int i = 0; i < len; i++) {
			bytes[i] = b[off];
			off++;
		}
		buf.append(CharacterUtils.toCharArray(bytes, encoding));
	}

	/**
	 * Writes and appends a single byte to StringOutputStream.
	 *
	 * @param b      the byte as an int to fill
	 */
	@Override
	public void write(int b) {
		buf.append((char) b);
	}
}

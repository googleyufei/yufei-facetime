package com.facetime.core.utils;

/**
 * 放在不同场景的<b>类似</b>StringBuilder功能类,内部使用数组实现
 * <p>
 * <code>StringBand</code> is a faster alternative to <code>StringBuilder</code>.
 * Instead of adding strings, they are stored in internal array. Only at the
 * end of concatenation, when <code>toString</code> is invoked, strings are
 * joined together.
 * </p>
 *
 * @NOTE To make <code>StringBand</code> even faster, predict the number of used strings
 * (and not the final string size)!
 */
public class StringBand {

	private static final int DEFAULT_ARRAY_CAPACITY = 16;

	private String[] array;
	private int index;
	private int length;

	/**
	 * CreatingUtils an empty <code>StringBand</code>.
	 */
	public StringBand() {
		array = new String[DEFAULT_ARRAY_CAPACITY];
	}

	/**
	 * CreatingUtils an empty <code>StringBand</code> with provided capacity.
	 * Capacity refers to internal string array (i.e. number of
	 * joins) and not the total string size.
	 */
	public StringBand(int initialCapacity) {
		if (initialCapacity <= 0)
			throw new IllegalArgumentException("Invalid initial capacity");
		array = new String[initialCapacity];
	}

	/**
	 * CreatingUtils <code>StringBand</code> with provided content.
	 */
	public StringBand(String s) {
		this();
		array[0] = s;
		index = 1;
		length = s.length();
	}

	// ---------------------------------------------------------------- append

	/**
	 * Appends boolean symbol.
	 */
	public StringBand append(boolean b) {
		return append(b ? StringPool.TRUE : StringPool.FALSE);
	}

	/**
	 * Appends byte symbol.
	 */
	public StringBand append(byte b) {
		return append(Byte.toString(b));
	}

	/**
	 * Appends double symbol.
	 */
	public StringBand append(double d) {
		return append(Double.toString(d));
	}

	/**
	 * Appends float symbol.
	 */
	public StringBand append(float f) {
		return append(Float.toString(f));
	}

	/**
	 * Appends int symbol.
	 */
	public StringBand append(int i) {
		return append(Integer.toString(i));
	}

	/**
	 * Appends long symbol.
	 */
	public StringBand append(long l) {
		return append(Long.toString(l));
	}

	/**
	 * Appends string representation of an object.
	 * if <code>null</code>, 'null' string will be appended.
	 */
	public StringBand append(Object obj) {
		return append(String.valueOf(obj));
	}

	/**
	 * Appends short symbol.
	 */
	public StringBand append(short s) {
		return append(Short.toString(s));
	}

	/**
	 * Appends a string.
	 */
	public StringBand append(String s) {
		if (s == null)
			s = StringPool.NULL;

		if (index >= array.length)
			expandCapacity();

		array[index++] = s;
		length += s.length();

		return this;
	}

	// ---------------------------------------------------------------- size

	/**
	 * Returns array capacity.
	 */
	public int capacity() {
		return array.length;
	}

	/**
	 * Returns char at given position.
	 * This method is <b>not</b> fast!
	 */
	public char charAt(int pos) {
		int len = 0;
		for (int i = 0; i < index; i++) {
			int newlen = len + array[i].length();
			if (pos < newlen)
				return array[i].charAt(pos - len);
			len = newlen;
		}
		throw new IllegalArgumentException("Invalid char position symbol.");
	}

	/**
	 * Returns current index of string array.
	 */
	public int index() {
		return index;
	}

	/**
	 * Returns total string length;
	 */
	public int length() {
		return length;
	}

	// ---------------------------------------------------------------- values

	/**
	 * Specifies the new index.
	 */
	public void setIndex(int newIndex) {
		if (newIndex < 0)
			throw new ArrayIndexOutOfBoundsException(newIndex);

		if (newIndex > array.length) {
			String[] newArray = new String[newIndex];
			System.arraycopy(array, 0, newArray, 0, index);
			array = newArray;
		}

		if (newIndex > index)
			for (int i = index; i < newIndex; i++)
				array[i] = StringPool.EMPTY;
		else if (newIndex < index)
			for (int i = newIndex; i < index; i++)
				array[i] = null;

		index = newIndex;
		calculateLength();
	}

	/**
	 * Returns string at given position.
	 */
	public String stringAt(int index) {
		if (index >= this.index)
			throw new ArrayIndexOutOfBoundsException();
		return array[index];
	}

	/**
	 * Joins together all strings into one.
	 */
	public String toString() {
		if (index == 0)
			return StringPool.EMPTY;
		String s;

		if (index <= 3) {
			s = array[0];
			for (int i = 1; i < index; i++)
				s = s.concat(array[i]);
		} else {
			StringBuilder sb = new StringBuilder(length);
			for (int i = 0; i < index; i++)
				sb.append(array[i]);
			s = sb.toString();
		}

		return s;
	}

	// ---------------------------------------------------------------- utils

	/**
	 * Calculates length.
	 */
	protected void calculateLength() {
		int len = 0;
		for (int i = 0; i < index; i++)
			len += array[i].length();
		length = len;
	}

	/**
	 * Expands string array capacity by multiplying its size by 2.
	 */
	protected void expandCapacity() {
		String[] newArray = new String[array.length << 1];
		System.arraycopy(array, 0, newArray, 0, index);
		array = newArray;
	}

}

package com.facetime.core.utils;

import java.util.Arrays;

/**
 * Multi-Key <BR>可用于Map中的复合键值，自动生成hashcode。
 * <P> 注意：不是魔兽的MK...
 * 
 * @author dzb2k9 (dzb2k9@gmail.com)
 *
 */
public final class MK {

	private static final int PRIME = 31;

	private final Object[] values;

	private final int hashCode;

	/**
	 * CreatingUtils a new creating from the provided values. It is assumed that the values provided are good map keys
	 * themselves -- immutable, with proper implementations of equals() and hashCode().
	 *
	 * @param values
	 */
	public MK(Object... values) {
		this.values = values;
		hashCode = PRIME * Arrays.hashCode(this.values);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final MK other = (MK) obj;
		return Arrays.equals(values, other.values);
	}

	@Override
	public int hashCode() {
		return hashCode;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Multi-Key[");
		boolean first = true;
		for (Object o : values) {
			if (!first)
				sb.append(", ");
			sb.append(o);
			first = false;
		}
		sb.append("]");
		return sb.toString();
	}
}

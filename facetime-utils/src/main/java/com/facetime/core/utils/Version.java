package com.facetime.core.utils;

import java.io.Serializable;
import java.util.StringTokenizer;

/**
 * 版本信息类
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 */
public final class Version implements Serializable, Comparable<Version> {

	private static final long serialVersionUID = -5739492907539237198L;

	private final int major;

	private final int minor;

	private final int micro;

	public Version(String version) {
		final StringTokenizer tokenizer = new StringTokenizer(version, ".");
		final String[] levels = new String[tokenizer.countTokens()];
		for (int i = 0; i < levels.length; i++) {
			levels[i] = tokenizer.nextToken();
		}

		if ((0 == levels.length) || (3 < levels.length)) {
			throw new IllegalArgumentException("Malformed version string " + version);
		}

		final int major = Integer.parseInt(levels[0]);

		int minor = 0;
		if (1 < levels.length) {
			minor = Integer.parseInt(levels[1]);
		}

		int micro = 0;
		if (2 < levels.length) {
			micro = Integer.parseInt(levels[2]);
		}

		this.major = major;
		this.minor = minor;
		this.micro = micro;
	}

	public Version(final int major, final int minor, final int micro) {
		this.major = major;
		this.minor = minor;
		this.micro = micro;
	}

	public int compareTo(Version o) {
		if (equals(o)) {
			return 0;
		} else if (complies(o)) {
			return 1;
		} else {
			return -1;
		}
	}

	public boolean complies(final Version other) {
		if (major > other.major) {
			return true;
		} else if (major == other.major) {
			if (minor > other.minor) {
				return true;
			} else if (minor == other.minor) {
				if (micro > other.micro) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean equals(final Object other) {
		if (other instanceof Version) {
			return equals((Version) other);
		} else {
			return false;
		}
	}

	public boolean equals(final Version other) {
		if (major != other.major) {
			return false;
		} else if (minor != other.minor) {
			return false;
		} else if (micro != other.micro) {
			return false;
		} else {
			return true;
		}
	}

	public int getMajor() {
		return major;
	}

	public int getMicro() {
		return micro;
	}

	public int getMinor() {
		return minor;
	}

	@Override
	public String toString() {
		return major + "." + minor + "." + micro;
	}

	/**
	 * 便利构造
	 * @param version
	 * @return
	 * @throws NumberFormatException
	 * @throws IllegalArgumentException
	 */
	public static Version of(final String version) throws NumberFormatException, IllegalArgumentException {
		return new Version(version);
	}

}

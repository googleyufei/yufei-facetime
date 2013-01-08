package com.facetime.core.utils;

/**
 * 简单的模式匹配功能类<BR>
 * 
 * 如典型的 "x?xx*", "*x??xx" and "*xxx*" 模式类型.
 * <L> ? -- Single char
 * <L> * -- Wild char
 */
public class PatternMatchUtils {
	/**
	 * 
	 */
	private PatternMatchUtils() {
	};

	/**
	 * Checks whether a string matches a given wildcard pattern.
	 *
	 * @param str	input string
	 * @param pattern	pattern to match
	 * @return 			<code>true</code> if string matches the pattern, otherwise <code>false</code>
	 */
	public static boolean match(String str, String pattern) {
		return match(str, pattern, 0, 0);
	}

	/**
	 * Matches string to at least one pattern.
	 * Returns index of matched pattern, or <code>-1</code> otherwise.
	 * @see #match(String, String)
	 */
	public static int match(String str, String[] patterns) {
		for (int i = 0; i < patterns.length; i++) {
			if (match(str, patterns[i]) == true) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Real matching recursive function.
	 */
	public static boolean match(String string, String pattern, int stringStartNdx, int patternStartNdx) {
		int pNdx = patternStartNdx;
		int sNdx = stringStartNdx;
		int pLen = pattern.length();
		if (pLen == 1) {
			if (pattern.charAt(0) == '*') { // speed-up
				return true;
			}
		}
		int sLen = string.length();
		boolean nextIsNotWildcard = false;

		while (true) {

			// validate if end of string and/or pattern occurred
			if ((sNdx >= sLen) == true) { // end of string still may have pending '*' in pattern
				while ((pNdx < pLen) && (pattern.charAt(pNdx) == '*')) {
					pNdx++;
				}
				return pNdx >= pLen;
			}
			if (pNdx >= pLen) { // end of pattern, but not end of the string
				return false;
			}
			char p = pattern.charAt(pNdx); // pattern char

			// perform logic
			if (nextIsNotWildcard == false) {

				if (p == '\\') {
					pNdx++;
					nextIsNotWildcard = true;
					continue;
				}
				if (p == '?') {
					sNdx++;
					pNdx++;
					continue;
				}
				if (p == '*') {
					char pnext = 0; // next pattern char
					if (pNdx + 1 < pLen) {
						pnext = pattern.charAt(pNdx + 1);
					}
					if (pnext == '*') { // double '*' have the same effect as one '*'
						pNdx++;
						continue;
					}
					int i;
					pNdx++;

					// find recursively if there is any substring from the end of the
					// line that matches the rest of the pattern !!!
					for (i = string.length(); i >= sNdx; i--) {
						if (match(string, pattern, i, pNdx) == true) {
							return true;
						}
					}
					return false;
				}
			} else {
				nextIsNotWildcard = false;
			}
			// validate if pattern char and string char are equals
			if (p != string.charAt(sNdx)) {
				return false;
			}
			// everything matches for now, continue
			sNdx++;
			pNdx++;
		}
	}
}

package com.facetime.core.utils;

import java.lang.reflect.Array;
import java.util.List;

public class CheckUtil {

	public static boolean isValid(String str) {
		return str != null && !str.trim().isEmpty();
	}

	public static boolean isValid(Number param) {
		return param != null && param.intValue() > 0;
	}

	public static <E> boolean isValid(List<E> list) {
		return list != null && !list.isEmpty();
	}

	public static <T> boolean isValid(Class<T> clazz) {
		return clazz != null;
	}

	public static boolean isValid(Object obj) {
		if (obj instanceof String) {
			return isValid((String) obj);
		} else if (obj instanceof Integer) {
			return isValid((Integer) obj);
		} else if (obj instanceof Number) {
			return isValid((Number) obj);
		} else if (obj instanceof Array) {
			return isValid((Object[]) obj);
		} else {
			return obj != null;
		}
	}

	public static boolean isValid(Object[] objArray) {
		return objArray != null && objArray.length > 0;
	}

	public static boolean unisValid(String str) {
		return !isValid(str);
	}

	public static boolean unisValid(Number param) {
		return !isValid(param);
	}

	public static <E> boolean unValid(List<E> list) {
		return !isValid(list);
	}

	public static <E> boolean unisValid(Object[] objArray) {
		return !isValid(objArray);
	}

}

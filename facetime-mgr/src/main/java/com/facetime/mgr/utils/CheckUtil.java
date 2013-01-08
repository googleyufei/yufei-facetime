package com.facetime.mgr.utils;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class CheckUtil {

	public static <E> boolean isValid(List<E> list) {
		return list != null && !list.isEmpty();
	}

	public static boolean isValid(MultipartFile uploadFile) {
		return uploadFile != null && uploadFile.getSize() > 0;
	}

	public static boolean isValid(Number param) {
		return param != null && param.intValue() > 0;
	}

	public static boolean isValid(Object[] objArray) {
		return objArray != null && objArray.length > 0;
	}

	public static boolean isValid(String str) {
		return str != null && !str.trim().isEmpty();
	}

	public static <E> boolean notValid(List<E> list) {
		return !isValid(list);
	}

	public static boolean notValid(MultipartFile uploadFile) {
		return !isValid(uploadFile);
	}

	public static boolean notValid(Number param) {
		return !isValid(param);
	}

	public static <E> boolean notValid(Object[] objArray) {
		return !isValid(objArray);
	}

	public static boolean notValid(String str) {
		return !isValid(str);
	}

}

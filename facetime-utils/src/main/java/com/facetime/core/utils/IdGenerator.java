package com.facetime.core.utils;

import java.util.UUID;

import org.apache.commons.lang.math.RandomUtils;

public class IdGenerator {

	public static Integer intId() {
		return RandomUtils.nextInt(999999);
	}

	public static String strId() {
		return UUID.randomUUID().toString();
	}
}

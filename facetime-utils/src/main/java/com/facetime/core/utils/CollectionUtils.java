package com.facetime.core.utils;

import java.util.ArrayList;
import java.util.List;

public class CollectionUtils {

	public static final <T> List<T> newList(int size) {
		return new ArrayList<T>(size);
	}
}

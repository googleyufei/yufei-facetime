package com.facetime.core.utils;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

public class IvyFileGenreator {
	public static void main(String[] args) throws Exception {
		Assert.assertTrue((args.length == 4) || (args.length == 5));
		String ivyfile = null;
		if (args.length == 4) {
			ivyfile = "ivy.ftl";
		} else {
			ivyfile = args[4];
		}
		Map<String, String> root = new HashMap<String, String>();
		root.put("org", args[0]);
		root.put("name", args[1]);
		root.put("rev", args[2]);

		String filename = args[3];
		FreemarkerHelper.create(ivyfile, filename, root);
	}
}
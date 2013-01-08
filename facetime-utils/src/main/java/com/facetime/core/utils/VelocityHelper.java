package com.facetime.core.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Map;
import java.util.Map.Entry;

import junit.framework.Assert;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

/**
 * Velocity模版工具的工具类
 */
public class VelocityHelper {

	private static final String FILE_RESOURCE_LOADER_PATH = "file.resource.loader.path";

	/**
	 * 生成模版文件的工具方法
	 * 
	 * @param dataMap
	 *            保存数据的键值对
	 * @param destFile
	 *            生成的目标文件, 必须包含路径.
	 * @param templateFile
	 *            模版文件, 如果已经在velocity.properties中指定了file.resource.loader.path,
	 *            则可以是相对路径, 否则必须是绝对路径
	 */
	public static void create(Map<String, ?> dataMap, String destFile,
			String templateFile) {
		Assert.assertNotNull(templateFile);
		Assert.assertNotNull(destFile);
		try {
			String destDir = destFile.substring(0, destFile.lastIndexOf("/"));
			File saveDir = new File(destDir);
			if (!saveDir.exists()) {
				boolean saved = saveDir.mkdirs();
				if (!saved) {
					throw new AssertionError("mk dir failed.");
				}
			}
			VelocityContext context = new VelocityContext();
			for (Entry<String, ?> entry : dataMap.entrySet()) {
				context.put(entry.getKey(), entry.getValue());
			}
			File templateFileTmp = new File(templateFile);
			Template template = null;
			if (templateFileTmp.isAbsolute()) {
				template = Velocity.getTemplate(templateFile);
			} else {
				template = Velocity.getTemplate(FILE_RESOURCE_LOADER_PATH
						+ templateFile);
			}
			FileWriter fileWriter = new FileWriter(new File(destFile));
			BufferedWriter writer = new BufferedWriter(fileWriter);
			template.merge(context, writer);
			writer.flush();
			writer.close();
			fileWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

package com.shop.logic.bean;

import com.facetime.core.bean.BusinessBean;
import com.facetime.core.utils.CheckUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.shop.util.UploadUtil;

public class BaseBean implements BusinessBean {
	private static final long serialVersionUID = 1L;
	/** 要查询的页数 */
	private static Properties properties = new Properties();
	private static final Logger log = Logger.getLogger(UploadUtil.class);
	static {
		try {
			properties.load(BaseBean.class.getClassLoader()
					.getResourceAsStream("arrowuploadfiletype.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private int page;
	/** 是否是查询操作 */
	private String query;

	/**
	 * 校验上传的文件是否合法
	 * 
	 * @param file
	 * @param message
	 * @return
	 */
	public static boolean checkCommonFile(MultipartFile file, String message) {
		if (!CheckUtil.isValid(file)) {
			message = "请先选择文件!";
			return false;
		}
		if (!validateFileType(file)) {
			message = "只能上传flash/word/exe/pdf/TxT/xls/ppt";
			return false;
		}
		if (file.getSize() > 409600) {
			message = "文件大小不能超过400KB!";
			return false;
		}
		return true;
	}

	public static String checkImageFile(CommonsMultipartFile file) {
		String message = null;
		if (!CheckUtil.isValid(file)) {
			message = "请先选择文件!";
		}
		if (!validateImageFileType(file)) {
			log.debug("validate image file type fail");
			message = "文件格式只能为GIF/jpg/bmp/jpeg";
		}
		if (file.getSize() > 409600) {
			message = "文件大小不能超过400KB";
		}
		return message;
	}

	public static boolean validateFileType(MultipartFile file) {
		if (file == null) {
			log.debug("upload file is null.");
			return true;
		}
		log.debug("upload file ext:" + UploadUtil.getFileExt(file));
		log.debug("upload file content type:" + file.getContentType());
		List<String> arrowTypes = new ArrayList<String>();
		for (Object key : properties.keySet()) {
			String value = (String) properties.get(key);
			String[] values = value.split(",");
			for (String v : values) {
				arrowTypes.add(v.trim());
			}
		}
		log.debug("upload file content type match:"
				+ arrowTypes.contains(file.getContentType().toLowerCase()));

		return arrowTypes.contains(file.getContentType().toLowerCase())
				&& properties.keySet().contains(UploadUtil.getFileExt(file));
	}

	public static boolean validateImageFileType(CommonsMultipartFile file) {
		if (file == null) {
			log.debug("upload image file is null.");
			return true;
		}
		List<String> arrowImageTypes = Arrays.asList("image/bmp", "image/png",
				"image/gif", "image/jpg", "image/jpeg", "image/pjpeg");
		List<String> arrowExtension = Arrays.asList("gif", "JPG", "jpg", "bmp",
				"png");
		String ext = UploadUtil.getFileExt(file);
		log.debug("upload file ext:" + ext);
		log.debug("upload file content type:" + file.getContentType());
		log.debug("upload file content type match:"
				+ arrowImageTypes.contains(file.getContentType().toLowerCase()));
		return arrowImageTypes.contains(file.getContentType().toLowerCase())
				&& arrowExtension.contains(ext.toLowerCase());
	}

	public int getPage() {
		return page < 1 ? 1 : page;
	}

	public String getQuery() {
		return query;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public void setQuery(String query) {
		this.query = query;
	}
}

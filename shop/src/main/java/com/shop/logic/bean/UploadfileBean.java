package com.shop.logic.bean;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class UploadfileBean extends BaseBean {
	private CommonsMultipartFile uploadfile;
	private String[] fileids;
	private static final long serialVersionUID = 7548182790948064678L;

	public String[] getFileids() {
		return fileids;
	}

	public CommonsMultipartFile getUploadfile() {
		return uploadfile;
	}

	public void setFileids(String[] fileids) {
		this.fileids = fileids;
	}

	public void setUploadfile(CommonsMultipartFile uploadfile) {
		this.uploadfile = uploadfile;
	}

}

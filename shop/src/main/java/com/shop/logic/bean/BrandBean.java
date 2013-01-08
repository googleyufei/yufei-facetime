package com.shop.logic.bean;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class BrandBean extends BaseBean {
	private static final long serialVersionUID = -1544544979067791655L;

	private String code;
	private String name;
	private CommonsMultipartFile logofile;
	private String logoimagepath;

	public String getCode() {
		return code;
	}

	public CommonsMultipartFile getLogofile() {
		return logofile;
	}

	public String getLogoimagepath() {
		return logoimagepath;
	}

	public String getName() {
		return name;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setLogofile(CommonsMultipartFile logofile) {
		this.logofile = logofile;
	}

	public void setLogoimagepath(String logoimagepath) {
		this.logoimagepath = logoimagepath;
	}

	public void setName(String name) {
		this.name = name;
	}
}

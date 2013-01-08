package com.shop.logic.bean;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class ProductStyleBean extends BaseBean {
	private static final long serialVersionUID = 277556667826573717L;

	private Integer productstyleid;
	private Integer[] stylesids;
	private String stylename;
	private Integer productid;
	private CommonsMultipartFile imagefile;

	public CommonsMultipartFile getImagefile() {
		return imagefile;
	}

	public Integer getProductid() {
		return productid;
	}

	public Integer getProductstyleid() {
		return productstyleid;
	}

	public String getStylename() {
		return stylename;
	}

	public Integer[] getStylesids() {
		return stylesids;
	}

	public void setImagefile(CommonsMultipartFile imagefile) {
		this.imagefile = imagefile;
	}

	public void setProductid(Integer productid) {
		this.productid = productid;
	}

	public void setProductstyleid(Integer productstyleid) {
		this.productstyleid = productstyleid;
	}

	public void setStylename(String stylename) {
		this.stylename = stylename;
	}

	public void setStylesids(Integer[] stylesids) {
		this.stylesids = stylesids;
	}
}

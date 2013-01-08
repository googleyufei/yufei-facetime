package com.shop.logic.bean;


public class FrontProductBean extends BaseBean {
	private static final long serialVersionUID = -8568989129357815279L;

	private String sort;
	private Integer typeid;
	private String brandid;
	private String sex;
	private Integer productid;
	private String style;

	public String getBrandid() {
		return brandid;
	}

	public Integer getProductid() {
		return productid;
	}

	public String getSex() {
		return sex;
	}

	public String getSort() {
		return sort;
	}

	public String getStyle() {
		return style;
	}

	public Integer getTypeid() {
		return typeid;
	}

	public void setBrandid(String brandid) {
		this.brandid = brandid;
	}

	public void setProductid(Integer productid) {
		this.productid = productid;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public void setTypeid(Integer typeid) {
		this.typeid = typeid;
	}

}

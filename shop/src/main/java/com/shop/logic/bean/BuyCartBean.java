package com.shop.logic.bean;


public class BuyCartBean extends BaseBean {
	private static final long serialVersionUID = -2329410390898044225L;

	private Integer productid;
	private Integer styleid;
	private String directUrl;

	public String getDirectUrl() {
		return directUrl;
	}

	public Integer getProductid() {
		return productid;
	}

	public Integer getStyleid() {
		return styleid;
	}

	public void setBuyitemid(String buyitemid) {
		if (buyitemid != null) {
			String[] ids = buyitemid.split("-");
			if (ids.length == 2) {
				productid = new Integer(ids[0]);
				styleid = new Integer(ids[1]);
			}
		}
	}

	public void setDirectUrl(String directUrl) {
		this.directUrl = directUrl;
	}

	public void setProductid(Integer productid) {
		this.productid = productid;
	}

	public void setStyleid(Integer styleid) {
		this.styleid = styleid;
	}
}

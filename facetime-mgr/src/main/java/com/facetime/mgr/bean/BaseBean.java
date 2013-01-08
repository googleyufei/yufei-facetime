package com.facetime.mgr.bean;

import com.facetime.core.bean.BusinessBean;

public class BaseBean implements BusinessBean {
	private static final long serialVersionUID = 1L;
	private int page;
	/** 是否是查询操作 */
	private String query;

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

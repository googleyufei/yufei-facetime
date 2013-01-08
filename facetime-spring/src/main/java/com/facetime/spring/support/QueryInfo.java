package com.facetime.spring.support;

import java.util.List;

import com.facetime.core.bean.BusinessBean;

/**
 * 查询信息封装类
 *
 * @author yufei
 *
 * @param <T>
 */
public class QueryInfo<T> implements BusinessBean {

	private static final long serialVersionUID = 1L;
	private String beanName = null;
	private int currentPageNo = 0;
	private int pageCount = 0;
	private int pageSize = 15;
	private String key = null;
	private List<T> params = null;
	private String[] properties = null;
	private int recordCount = 0;
	private String sql = null;

	public String getBeanName() {
		return beanName;
	}

	public int getCurrentPageNo() {
		return currentPageNo;
	}

	public String getKey() {
		return key;
	}

	public long getNextPage() {
		if (currentPageNo == pageCount) {
			return currentPageNo;
		}
		return currentPageNo + 1;
	}

	public int getPageCount() {
		return pageCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public List<T> getParams() {
		return params;
	}

	public int getPreviousPage() {
		if (currentPageNo == 1) {
			return currentPageNo;
		}
		return currentPageNo - 1;
	}

	public String[] getProperties() {
		return properties;
	}

	public int getRecordCount() {
		return recordCount;
	}

	public String getSql() {
		return sql;
	}

	public void setBeanName(String beanNameValue) {
		beanName = beanNameValue;
	}

	public void setCurrentPageNo(int currentPageNo) {
		this.currentPageNo = currentPageNo;
	}

	public void setKey(String keyValue) {
		key = keyValue;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setParams(List<T> paramsValue) {
		params = paramsValue;
	}

	public void setProperties(String[] propertiesValue) {
		properties = propertiesValue;
	}

	public void setRecordCount(int pageCount) {
		recordCount = pageCount;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}
}
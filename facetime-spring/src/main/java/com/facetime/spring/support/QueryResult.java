package com.facetime.spring.support;

import java.util.List;

import com.facetime.core.bean.BusinessBean;

public class QueryResult<T> implements BusinessBean {

	private static final long serialVersionUID = -1178319655678039439L;
	private List<T> resultList;
	private long totalRecord;

	public QueryResult() {
		super();
	}

	public QueryResult(List<T> resultList, long totalRecord) {
		super();
		this.resultList = resultList;
		this.totalRecord = totalRecord;
	}

	public List<T> getResultList() {
		return this.resultList;
	}

	public void setResultList(List<T> resultList) {
		this.resultList = resultList;
	}

	public long getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(long totalRecord) {
		this.totalRecord = totalRecord;
	}
}

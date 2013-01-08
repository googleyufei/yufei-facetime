package com.shop.domain;

import com.facetime.spring.support.Page;

import java.util.List;

public class PageView<T> {
	private List<T> records;
	private PageIndex pageindex;
	private int totalpage = 1;
	private int maxresult = 12;
	private int currentpage = 1;
	private int totalrecord;
	private int pagecode = 10;

	public PageView(int maxresult, int currentpage) {
		this.maxresult = maxresult;
		this.currentpage = currentpage;
	}

	public int getCurrentpage() {
		return currentpage;
	}

	public int getMaxresult() {
		return maxresult;
	}

	public int getPagecode() {
		return pagecode;
	}

	public PageIndex getPageindex() {
		return pageindex;
	}

	public List<T> getRecords() {
		return records;
	}

	public long getTotalpage() {
		return totalpage;
	}

	public long getTotalrecord() {
		return totalrecord;
	}

	public void setPagecode(int pagecode) {
		this.pagecode = pagecode;
	}

	public void setQueryResult(Page<T> page) {
		setTotalrecord(page.getRecordCount());
		setRecords(page.getQueryResult());
	}

	public void setRecords(List<T> records) {
		this.records = records;
	}

	public void setTotalpage(int totalpage) {
		this.totalpage = totalpage;
		this.pageindex = PageIndex.getPageIndex(pagecode, currentpage, totalpage);
	}

	public void setTotalrecord(long totalrecord) {
		this.totalrecord = (int) totalrecord;
		setTotalpage(this.totalrecord % this.maxresult == 0 ? this.totalrecord / this.maxresult : this.totalrecord
				/ this.maxresult + 1);
	}
}

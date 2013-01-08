package com.shop.domain;

public class PageIndex {
	private int startindex;
	private int endindex;

	public PageIndex(int startindex, int endindex) {
		this.startindex = startindex;
		this.endindex = endindex;
	}

	public static PageIndex getPageIndex(int viewpagecount, int currentPage,
			int totalpage) {
		int startpage = currentPage
				- (viewpagecount % 2 == 0 ? viewpagecount / 2 - 1
						: viewpagecount / 2);
		int endpage = currentPage + viewpagecount / 2;
		if (startpage < 1) {
			startpage = 1;
			if (totalpage >= viewpagecount)
				endpage = viewpagecount;
			else
				endpage = totalpage;
		}
		if (endpage > totalpage) {
			endpage = totalpage;
			if ((endpage - viewpagecount) > 0)
				startpage = endpage - viewpagecount + 1;
			else
				startpage = 1;
		}
		return new PageIndex(startpage, endpage);
	}

	public int getStartindex() {
		return startindex;
	}

	public void setStartindex(int startindex) {
		this.startindex = startindex;
	}

	public int getEndindex() {
		return endindex;
	}

	public void setEndindex(int endindex) {
		this.endindex = endindex;
	}
}

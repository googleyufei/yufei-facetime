package com.facetime.mgr.common;

import com.facetime.mgr.bean.UserModel;
import com.facetime.spring.support.Page;

import javax.servlet.http.HttpServletRequest;

/**
 * 所有的ActionForm的根类
 */
public abstract class BaseForm {

	private static final long serialVersionUID = 1L;
	/** 操作名称 */
	private String action;
	/** 开始时间 */
	private String beginDate;
	/** 清算时间 */
	private String cleardate;
	/** 结束时间 */
	private String endDate;
	/** 菜单编号 */
	private String menuno;
	/** 分页中的页大小 */
	private Integer pageSize = Page.getBeginPageSize();
	/** 分页中的跳转页 */
	private Integer toPage = 1;
	/** 银联机构号 */
	private String unionorgid;

	public String getAction() {
		return action;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public String getCleardate() {
		return cleardate;
	}

	public String getEndDate() {
		return endDate;
	}

	public String getLoginUserid(HttpServletRequest request) {
		return getUserModel(request).getUsername();
	}

	public String getMenuno() {
		return menuno;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public Integer getToPage() {
		return toPage;
	}

	public String getUnionorgid() {
		return unionorgid;
	}

	public UserModel getUserModel(HttpServletRequest request) {
		return (UserModel) request.getSession().getAttribute(UserModel.LOGIN_USER_KEY);
	}

	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * be careful: brower send beginDate format is like this: yyyyMMdd
	 * @param beginDate
	 */
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public void setCleardate(String cleardate) {
		this.cleardate = cleardate;
	}

	/**
	 * be careful: brower send endDate format is like this: yyyyMMdd
	 * @param endDate
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public void setMenuno(String menuno) {
		this.menuno = menuno;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public void setToPage(Integer toPage) {
		this.toPage = toPage;
	}

	public void setUnionorgid(String unionorgid) {
		this.unionorgid = unionorgid;
	}
}

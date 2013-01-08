package com.facetime.mgr.taglib;

import com.facetime.core.utils.FreemarkerHelper;
import com.facetime.spring.support.Page;
import com.facetime.spring.support.SpringContextUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author wzhuo 生成分页的Tag 分页里面包含的元素pageSize和toPage必须属于formList!!! Parameters1 : action 执行的.do文件，可选 Parameters2 :
 * formName * form的名称，默认为formList,必须 Parameters3 : pageName page的名称，由Hibernate生成的page，必须 Parameters4 : submitFunction
 * 提交表达的函数，可选 可以是函数名（例如parent.Query()）或者是javascript脚本（例如：alert('hello')） 如果submitFunction为空，通过
 * formName.submit()提交表单，否则调用submitFunction包含的脚本
 */
public class PageTag extends TagSupport {

	private static final long serialVersionUID = 1L;

	protected String action = null;

	protected String formName = "";

	protected String pageName = null;

	protected String submitFunction = null;

	@Override
	public int doEndTag() throws javax.servlet.jsp.JspException {
		return EVAL_PAGE;
	}

	@Override
	public int doStartTag() throws javax.servlet.jsp.JspException {
		Object pageObject = pageContext.getAttribute(pageName, PageContext.REQUEST_SCOPE);
		Page<?> query = null;
		if (pageObject.getClass() == Page.class) {
			query = (Page<?>) pageObject;
		}
		if (query == null) {
			query = new Page<Object>();
		}

		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("pageNum", query.getCurrentPageNo());
		dataMap.put("pageSize", query.getPageSize());
		dataMap.put("pageCount", query.getPageCount());
		dataMap.put("recordCount", query.getRecordCount());

		dataMap.put("rootPath", request.getContextPath());
		dataMap.put("imgBasePath", request.getContextPath() + "/images/page/");

		dataMap.put("msgTotal", SpringContextUtils.message("page.nav.total"));
		dataMap.put("msgSizeOfPage", SpringContextUtils.message("page.nav.sizeOfPage"));
		dataMap.put("msgSize", SpringContextUtils.message("page.nav.size"));
		dataMap.put("msgGo", SpringContextUtils.message("page.nav.go"));
		dataMap.put("msgPage", SpringContextUtils.message("page.nav.page"));
		dataMap.put("pageNumErr", SpringContextUtils.message("page.nav.pageNum.notexist"));

		List<Integer> pageRank = new ArrayList<Integer>();
		for (int i = Page.getBeginPageSize(); i < Page.getMaxPageSize(); i = i + Page.getAddPageSize()) {
			pageRank.add(i);
		}
		dataMap.put("pageRank", pageRank);

		if (submitFunction == null) {
			submitFunction = formName + ".submit();";
		}
		dataMap.put("submitFunction", submitFunction);
		dataMap.put("formName", formName);
		String sb = FreemarkerHelper.contentAfterCreate(ButtonTag.class, "PageTag.ftl", "PageTag.html", dataMap);
		JspWriter writer = pageContext.getOut();
		try {
			writer.print(sb);
		} catch (IOException ie) {
			throw new JspException(ie.toString());
		}
		return EVAL_BODY_INCLUDE;
	}

	public String getAction() {
		return action;
	}

	public String getFormName() {
		return formName;
	}

	public String getPageName() {
		return pageName;
	}

	public String getSubmitFunction() {
		return submitFunction;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public void setSubmitFunction(String submitFunction) {
		this.submitFunction = submitFunction;
	}
}

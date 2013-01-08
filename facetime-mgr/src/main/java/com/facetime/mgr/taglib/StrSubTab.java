package com.facetime.mgr.taglib;

import com.facetime.core.utils.DateUtil;

import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * 时间日期格式化的组件
 * 
 * name: 页面中bean的名称
 * property:页面中bean的属性名称
 * type:分为三种情况，datetime(日期时间)、date(日期)、默认不填(此时代表截取字符串)
 * beginIndex:开始位置，type为默认时必填
 * endIndex:结束位置，type为默认时必填
 */

public class StrSubTab extends TagSupport {

	private static final long serialVersionUID = 1L;

	protected String beginIndex;

	protected String endIndex;

	protected String name;

	protected String property;

	protected String type;

	@Override
	public int doEndTag() throws JspException {
		try {
			Object bean = pageContext.findAttribute(name);
			String str = "";
			Object value = PropertyUtils.getProperty(bean, property);
			if (value != null) {
				if (type.equals("datetime")) {
					str = DateUtil.dateToStr((Date) value);
				} else if (type.equals("date")) {
					str = DateUtil.getDateStr((Date) value);
				} else {
					str = (String) value;
					int i = Integer.parseInt(beginIndex);
					int j = Integer.parseInt(endIndex);
					if (i <= j && j <= str.length()) {
						str = str.substring(i, j);
					}
				}
			}
			pageContext.getOut().print(str);
		} catch (Exception e) {
			throw new JspTagException("IOException" + e.toString());
		}
		return super.doEndTag();
	}

	@Override
	public int doStartTag() throws JspTagException {
		return EVAL_BODY_INCLUDE;
	}

	public String getBeginIndex() {
		return beginIndex;
	}

	public String getEndIndex() {
		return endIndex;
	}

	public String getName() {
		return name;
	}

	public String getProperty() {
		return property;
	}

	public String getType() {
		return type;
	}

	public void setBeginIndex(String beginIndex) {
		this.beginIndex = beginIndex;
	}

	public void setEndIndex(String endIndex) {
		this.endIndex = endIndex;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public void setType(String type) {
		this.type = type;
	}

}

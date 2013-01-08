package com.facetime.mgr.taglib;

import com.facetime.mgr.common.BusnDataDir;

import java.io.IOException;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Description: 利用数据字典业务类BusnDataDir解析bean的属性对应的代码值
 * path: 数据字典路径,必须 beanName:JavaBean 保存到request或session或page中的ID名,必须
 * property:需要解析的JavaBean的属性名，必须 scope:该javaBean保存的范围，默认为：null,会当作page处理 调用例子:<view:dataDir
 * path="equipState.tml" beanName="event" property="type" scope="page"/>
 */

public class DataDirTag extends TagSupport {

	private static final long serialVersionUID = 1L;

	protected String beanName;

	protected String path;

	protected String scope = null;

	private String property;

	@Override
	public int doEndTag() throws javax.servlet.jsp.JspException {
		return EVAL_PAGE;
	}

	@Override
	public int doStartTag() throws javax.servlet.jsp.JspException {
		Object codeValue = TagUtils.lookup(pageContext, beanName, property, scope);
		if (codeValue == null) {
			return SKIP_BODY;
		}

		JspWriter writer = pageContext.getOut();
		try {
			writer.print(parseCode(codeValue.toString()));
		} catch (IOException ie) {
			throw new JspException(ie.toString());
		}
		return EVAL_BODY_INCLUDE;
	}

	public String getBeanName() {
		return beanName;
	}

	public String getPath() {
		return path;
	}

	public String getProperty() {
		return property;
	}

	public String getScope() {
		return scope;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	/**
	 * 根据数据字典类解析代码
	 *
	 * @param codeValue
	 * @return
	 */
	private String parseCode(String codeValue) {
		Map<String, String> dataDirMap = BusnDataDir.getChildValueMap(path);
		if (dataDirMap == null) {
			return codeValue;
		}
		Object obj = dataDirMap.get(codeValue);
		return obj != null ? obj.toString() : codeValue;
	}
}
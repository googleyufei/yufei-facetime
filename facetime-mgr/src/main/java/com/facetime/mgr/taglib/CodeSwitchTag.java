package com.facetime.mgr.taglib;

import com.facetime.mgr.common.BusnDataDir;

import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

/*
 * name:为<logic:iterate id="logmgr" name="queryAll" indexId="index">中的name
 * property:为<bean:write name="logmgr" property="userid" />中的property
 * hashMap:缓存对象名称，保存方式两种：request.getSession().setAttribute("hmap",map) 或request.setAttribute("hmap",map)
 * type:分为两种情况，session、默认不填,对应上面两种保存方式　
 * 使用实例如下：
 * <logic:iterate id="logmgr" name="queryAll" indexId="index">
 * 		<view:EnToZh name="logmgr" property="operid" hashMap="hmap" type="session"/>
 * </logic:iterate>
 */
public class CodeSwitchTag extends TagSupport {

	private static final long serialVersionUID = 1L;

	/**数据字典的path或者scope中的属性名*/
	protected String hashMap;

	/**页面中bean的名字*/
	protected String name;

	/**页面中bean的属性名*/
	protected String property;

	/**数据类型, dataDir代表数据字典, 其他情况代表存在scope中*/
	protected String type;

	@Override
	@SuppressWarnings("unchecked")
	public int doEndTag() throws JspException {
		try {

			Map<String, String> map = null;
			if (type.equals("dataDir")) {
				map = BusnDataDir.getChildValueMap(hashMap);
			} else {
				map = (Map<String, String>) pageContext.getAttribute(hashMap);
			}

			JspWriter out = pageContext.getOut();
			Object bean = pageContext.findAttribute(name);
			String[] attrNames = StringUtils.split(property, ",");
			StringBuffer key = new StringBuffer();
			for (String attrName : attrNames) {
				key.append(String.valueOf(PropertyUtils.getProperty(bean, attrName)));
			}
			String keyVal = String.valueOf(key);
			if (map == null || map.get(keyVal) == null) {
				out.print(keyVal.indexOf("null") == -1 ? keyVal : "");
				return super.doEndTag();
			}
			out.print(map.get(keyVal.trim()));
		} catch (Exception e) {
			throw new JspTagException("IOException" + e.toString());
		}

		return super.doEndTag();
	}

	@Override
	public int doStartTag() throws JspTagException {

		return EVAL_BODY_INCLUDE;

	}

	public String getHashMap() {
		return hashMap;
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

	public void setHashMap(String hashMap) {
		this.hashMap = hashMap;
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

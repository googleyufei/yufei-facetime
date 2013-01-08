package com.facetime.mgr.taglib;

import com.facetime.core.utils.FreemarkerHelper;
import com.facetime.core.utils.StringUtils;
import com.facetime.mgr.common.BusnDataDir;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * <view:dmselect name="txnId" dicId="txnId" scope="request" width="230" height="150"  separator=","  allvalue="ALL_VALUE" allkey="<%=strAllLabel%>" />
 * 该标签会生成一个hiddenField，hiddenField的初始值为allvalue,
 * 当什么都没选为空串，
 * 否则为用separator分隔的选项值序列，最后不用separator，例如1,2的形式而不是1,2,
 */

public class MultiSelectTag extends TagSupport {

	private static final long serialVersionUID = 1L;
	/**表示"全选"的label,默认设置为ALL*/
	private String allkey;
	/**表示"全选"的value,默认ALL_VALUE*/
	private String allvalue;
	/**页面对象*/
	private String beanName;
	/**默认选项*/
	private String defaultValue;
	/**选择的字典集合*/
	private String dicId;
	/**选择区域高度*/
	private int height;
	/**控件名*/
	private String name;
	/**各个选项之间的分隔符,为空或长度小于1时默认设置为','*/
	private String separator;
	/**选择区样式*/
	private String sltStyle;
	/**文本筐样式*/
	private String txtStyle;
	/**选择区域宽度*/
	private int width;

	@Override
	public int doEndTag() throws JspException {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		Map<String, String> map = getDataMap();
		dataMap.put("ketSet", map.keySet());
		dataMap.put("dataMap", map);
		dataMap.put("upImg", "▲");
		dataMap.put("downImg", "▼");
		List<String> values = null;
		if (StringUtils.isValid(beanName)) {
			try {
				Object attrValue = PropertyUtils.getProperty(pageContext.findAttribute(beanName), name);
				if (attrValue != null) {
					defaultValue = getAllvalue().equals(attrValue) ? null : attrValue.toString();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (StringUtils.isValid(defaultValue)) {
			values = Arrays.asList(defaultValue.split(","));
		}
		dataMap.put("defaultInfo", defaultInfo(map, values));
		dataMap.put("name", getName());
		dataMap.put("separator", getSeparator());
		dataMap.put("width", getWidth());
		dataMap.put("height", getHeight());
		dataMap.put("sltStyle", getSltStyle());
		dataMap.put("txtStyle", getTxtStyle());
		dataMap.put("allkey", getAllkey());
		dataMap.put("allvalue", getAllvalue());
		dataMap.put("defaultValue", getDefaultValue());
		dataMap.put("values", values);

		dataMap.put("txtName", getName() + "Txt");
		dataMap.put("layName", getName() + "Lay");
		dataMap.put("chkName", getName() + "Chk");
		String html = FreemarkerHelper.contentAfterCreate(ButtonTag.class, "MultiSelectTag.ftl", "temp.html", dataMap);
		try {
			pageContext.getOut().print(html);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return super.doEndTag();
	}

	/**
	* <p>标记开始时自动执行的函数
	* 这里主要是从scope指定的容器里面根据collectionId获得数据集合
	*  @return int
	* @throws JspTagException
	*/
	@Override
	public int doStartTag() throws JspTagException {
		return EVAL_BODY_INCLUDE;
	}

	public String getAllkey() {
		if (allkey == null || allkey.length() < 1) {
			allkey = "ALL";
		}
		return allkey;
	}

	public String getAllvalue() {
		if (allvalue == null || allvalue.length() < 1) {
			allvalue = "";
		}
		return allvalue;
	}

	public String getBeanName() {
		return beanName;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public String getDicId() {
		return dicId;
	}

	public int getHeight() {
		if (height <= 0) {
			height = 100;
		}
		return height;
	}

	public String getName() {
		return name;
	}

	public String getSeparator() {
		if (separator == null || separator.length() < 1) {
			separator = ",";
		}
		return separator;
	}

	public String getSltStyle() {
		return sltStyle;
	}

	public String getTxtStyle() {
		return txtStyle;
	}

	public int getWidth() {
		if (width <= 0) {
			width = 100;
		}
		return width;
	}

	public void setAllkey(String allkey) {
		this.allkey = allkey;
	}

	public void setAllvalue(String allvalue) {
		this.allvalue = allvalue;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public void setDicId(String dicId) {
		this.dicId = dicId;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	public void setSltStyle(String sltStyle) {
		this.sltStyle = sltStyle;
	}

	public void setTxtStyle(String txtStyle) {
		this.txtStyle = txtStyle;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	private String defaultInfo(Map<String, String> map, List<String> values) {
		if (values == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (String key : values) {
			sb.append(map.get(key)).append(" ");
		}
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	private Map<String, String> getDataMap() throws JspTagException {
		Map<String, String> temp = BusnDataDir.getChildValueMap(dicId);
		if (temp == null) {
			temp = (Map<String, String>) pageContext.findAttribute(dicId); // 注意, 该方法会从小到大的范围查找
		}
		if (temp == null || !(temp instanceof java.util.Map)) {
			throw new JspTagException("DDMultiSelectTag: dicId " + dicId + " not found in all scope.");
		}
		return temp;
	}
}
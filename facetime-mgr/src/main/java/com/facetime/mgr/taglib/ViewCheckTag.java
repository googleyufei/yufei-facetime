package com.facetime.mgr.taglib;

import com.facetime.core.utils.FreemarkerHelper;
import com.facetime.mgr.common.BusnDataDir;
import com.facetime.mgr.domain.SysDatadir;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * <code>
 * <view:viewCheck id="status" name="status" size="width:90%" path="equipState.tml" /> 
 * </code>
 * 
 * @author yufei
 *
 */
public class ViewCheckTag extends TagSupport {

	private static final long serialVersionUID = 1L;

	/**数据字典的key*/
	private String dirCode = "";

	/**数据字典的值*/
	private String dirTxt = "";

	private String id;

	/**hidden对应的名字*/
	private String name;

	/**text对应的名字*/
	private String nameTxt;

	/**数据字典路径*/
	private String path;

	private String size;

	/**文本框样式*/
	private String style;

	@Override
	public int doEndTag() throws JspException {
		nameTxt = "txt" + name;
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("nameTxt", nameTxt);
		dataMap.put("size", size);
		dataMap.put("name", name);
		dataMap.put("path", path);
		dataMap.put("dirCode", dirCode);
		dataMap.put("dirTxt", dirTxt);
		getInputValues();

		JspWriter writer = pageContext.getOut();
		try {
			writer.print(FreemarkerHelper.contentAfterCreate(ButtonTag.class, "ViewCheckTag.ftl", "temp.html", dataMap));
		} catch (IOException ie) {
			throw new JspException(ie.toString());
		}
		return EVAL_PAGE;
	}

	@Override
	public int doStartTag() throws javax.servlet.jsp.JspException {
		return EVAL_BODY_INCLUDE;
	}

	@Override
	public String getId() {
		return id;
	}

	public void getInputValues() {
		dirCode = "";
		dirTxt = "";
		List<SysDatadir> dataList = BusnDataDir.queryChildren(path);

		for (SysDatadir dir : dataList) {
			if (dirCode.equals("")) {
				dirCode = dir.getValue();
				dirTxt = dir.getNote();
			} else {
				dirCode += "," + dir.getValue();
				dirTxt += " " + dir.getNote();
			}
		}
	}

	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}

	public String getSize() {
		return size;
	}

	public String getStyle() {
		return style;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public void setStyle(String style) {
		this.style = style;
	}
}

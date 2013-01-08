package com.facetime.mgr.taglib;

import com.facetime.core.utils.FreemarkerHelper;
import com.facetime.mgr.logic.DataDirManager;
import com.facetime.mgr.logic.MenuOperateManager;
import com.facetime.spring.support.SpringContextUtils;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Description: 利用相关模块的数据列表解析组织select控件 <br>
 * selType：select的类型，属于哪种 <br>
 * style：样式 <br>
 * readonly:是否只读<br>
 * path: 参数 <br>
 * beforeOption: 是否有“全选“<br>
 * onchange：选择改变事件<br>
 * defaultValue：默认值，如果默认值不是已知，需要从javabean获取则不能用该defaultValue属性，需要自己写， 调用例子:<br>
 * <view:viewSelect name="txntype" id="txntype" style="width:150"
 * selType="dataDir" path="busn.tradeType" beforeOption='all' /><br>
 * <code>
 *  <logic:notEmpty name="profitSetForm" property="txntype">
 *   <script language="javascript">
 *      profitSetForm.txntype.value='${profitSetForm.txntype}';
 *      </script>
 *   </logic:notEmpty>
 *   </code> <br>
 */
@SuppressWarnings("serial")
public class ViewSelectTag extends TagSupport {

	private String beforeOption;

	private String defaultValue;

	private String disabled;

	private String id;

	private String name;

	private String noneOption;

	private String onchange;

	private String outputHtml;

	private String path;

	private String selType;

	private String style;

	@Override
	public int doEndTag() throws JspTagException {
		try {
			pageContext.getOut().print(outputHtml.toString());
		} catch (Exception ex) {
			throw new JspTagException("ViewSelectTag:doEndTag caught: " + ex);
		}
		return EVAL_PAGE;
	}

	@Override
	public int doStartTag() throws JspTagException {
		String tree = treeValue();
		if (tree == null) {
			throw new JspTagException("ViewSelectTag: selType " + selType + " not found in scope.");
		}

		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("id", id);
		dataMap.put("name", name);
		dataMap.put("onchange", onchange);
		dataMap.put("style", style);
		dataMap.put("disabled", disabled);
		dataMap.put("beforeOption", beforeOption);
		dataMap.put("msgInfo", SpringContextUtils.message("label.select.all"));
		if (defaultValue == null) {
			dataMap.put("tree", tree);
		}
		if (defaultValue != null) {
			int i = tree.indexOf("value='" + defaultValue);
			if (i < 0) {
				i = tree.indexOf("value=\"" + defaultValue);
			}
			if (i > -1) {
				StringBuilder sb = new StringBuilder();
				sb.append(tree.substring(0, i));
				sb.append("value='").append(defaultValue).append("' selected ");
				i = i + 8 + defaultValue.length();
				sb.append(tree.substring(i));
				dataMap.put("smp", sb.toString());
			}
		}
		dataMap.put("noneOption", noneOption);
		dataMap.put("noneSelect", SpringContextUtils.message("label.select.none"));
		outputHtml = FreemarkerHelper.contentAfterCreate(ButtonTag.class, "ViewSelectTag.ftl", "ViewSelectTag.html",
				dataMap);
		return SKIP_BODY;
	}

	public String getBeforeOption() {
		return beforeOption;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public String getDisabled() {
		return disabled;
	}

	@Override
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getNoneOption() {
		return noneOption;
	}

	public String getOnchange() {
		return onchange;
	}

	public String getPath() {
		return path;
	}

	public String getSelType() {
		return selType;
	}

	public String getStyle() {
		return style;
	}

	public void setBeforeOption(String beforeOption) {
		this.beforeOption = beforeOption;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNoneOption(String noneOption) {
		this.noneOption = noneOption;
	}

	public void setOnchange(String onchange) {
		this.onchange = onchange;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setSelType(String selType) {
		this.selType = selType;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	private String treeValue() {
		String tree = null;
		if (selType.equals("dataDir")) {
			DataDirManager dataDirManager = SpringContextUtils.locate(DataDirManager.class);
			tree = dataDirManager.getOptionByPath(path);
		} else if (selType.equals("operId")) {
			MenuOperateManager menuOperateManager = SpringContextUtils.locate(MenuOperateManager.class);
			tree = menuOperateManager.getMenuOperID();
		}
		return tree;
	}

}

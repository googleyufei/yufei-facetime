package com.facetime.mgr.taglib;

import com.facetime.mgr.bean.UserModel;
import com.facetime.mgr.common.TagResources;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * 当配置的权限还不能够满足用户的需要时, 可以使用这个标签进行细粒度的控制. 
 * 这个Tag的很多属性都是可以去掉的, 它只要判断用户是否具有操作的权限就可以了.
 * 比如imageUrl, labelValue, shortKey,visible
 * 示例:  在orgInfo_list.jsp
 * <code>
 * 	<view:purview operId="<%=Constants.OPER_MODIFY_VALUE%>" imageUrl="../../images/share/export.gif" controlType="button" labelValue="button.export" controlName="btnExport" shortcutKey="E" visible="false">
	 	<input type="button" name="modify" onClick="MODIFY()" value='<bean:message key="button.modify"/>' class="MyButton" accesskey="M" image="../../images/share/modify.gif" >
	 	<input type="button" name="cardtype" onClick="setCardType()" value='<spring:message code="orginfo.cardSetup" bundle="org"/> ' class="MyButton" accesskey="C" image="../../images/share/setting.gif" >
     </view:purview>
 * </code>
 * 
 * @author yufei
 *
 */
public class PurviewTag extends TagSupport {

	private static final long serialVersionUID = 1L;

	/**输出控件的名字ID*/
	private String controlName;

	/** 权限控制类型（1--按钮，2---图片，3--超级连接）*/
	private String controlType;

	/**输出的图片的地址*/
	private String imageUrl;

	/**输出显示文字*/
	private String labelValue;

	/**操作点ID*/
	private String operId;

	/**输入按钮的快捷键*/
	private String shortcutKey;

	/**是否可见*/
	private boolean visible;

	@Override
	public int doStartTag() throws JspException {
		int processBodyOrNot = SKIP_BODY;
		UserModel user = (UserModel) pageContext.getAttribute(UserModel.LOGIN_USER_KEY, PageContext.SESSION_SCOPE);
		String menuId = (String) pageContext.getAttribute("menuno", PageContext.SESSION_SCOPE);
		// 判断当前功能点，用户是否有权限,如果拥有权限，则输出后面的按钮
		if (user.hasAuthorized(menuId, operId)) {
			processBodyOrNot = EVAL_BODY_INCLUDE;
		} else if (getVisible()) {
			// 判断控制类别，根据类别不同输出不同
			// 如果设置为不可见
			JspWriter out = pageContext.getOut();
			try {
				if (getControlType().equals("image")) {
					if (getControlName() != null) {
						out.print("<img src='" + getImageUrl() + "' name='" + getControlName() + "'>");
					} else {
						out.print("<img src='" + getImageUrl() + "'>");
					}
				} else if (getLabelValue() != null) {
					if (getControlType().equals("button")) {
						StringBuilder html = new StringBuilder();
						html.append("<input type='button'  name='");
						html.append(controlType.equals("button") ? getControlName() : "button" + getOperId()).append(
								"'");
						html.append(" disabled value='").append(TagResources.message(pageContext, getLabelValue()))
								.append("'");
						html.append("class='MyButton' image='").append(imageUrl).append("'");
						html.append(" AccessKey='").append(getShortcutKey()).append("'>");
					}
					if (getControlType().equals("url")) {
						out.print(getLabelValue());
					}
				} else {
					processBodyOrNot = SKIP_BODY;
				}
			} catch (Exception e) {
			}
		} else {
			processBodyOrNot = SKIP_BODY;
		}
		return processBodyOrNot;
	}

	public String getControlName() {
		return controlName;
	}

	public String getControlType() {
		return controlType;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public String getLabelValue() {
		return labelValue;
	}

	public String getOperId() {
		return operId;
	}

	public String getShortcutKey() {
		return shortcutKey;
	}

	public boolean getVisible() {
		return visible;
	}

	public void setControlName(String controlName) {
		this.controlName = controlName;
	}

	public void setControlType(String controlType) {
		this.controlType = controlType;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public void setLabelValue(String labelValue) {
		this.labelValue = labelValue;
	}

	public void setOperId(String operId) {
		this.operId = operId;
	}

	public void setShortcutKey(String shortcutKey) {
		this.shortcutKey = shortcutKey;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}

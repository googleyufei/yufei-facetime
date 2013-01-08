package com.facetime.mgr.taglib;

import com.facetime.core.utils.FreemarkerHelper;
import com.facetime.core.utils.StringUtils;
import com.facetime.mgr.bean.UserModel;
import com.facetime.mgr.common.BusnDataDir;
import com.facetime.mgr.domain.MenuOperate;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

public class ButtonTag extends TagSupport {
	private static final long serialVersionUID = 1L;
	protected String menuid;
	protected String type;

	@Override
	public int doEndTag() throws JspException {
		String realMenuId = "";
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		if (type != null && type.equals("session")) {
			realMenuId = (String) request.getSession().getAttribute(menuid);
		} else if (type != null && type.equals("attribure")) {
			realMenuId = (String) pageContext.getAttribute(menuid);
		} else {
			realMenuId = request.getParameter(menuid);
		}
		List<MenuOperate> list = null;
		if (StringUtils.isValid(realMenuId)) {
			UserModel userModel = (UserModel) request.getSession().getAttribute(UserModel.LOGIN_USER_KEY);
			if (userModel == null) {
				return super.doEndTag();
			}
			list = BusnDataDir.MENU_OPERID_MAP.get(userModel.getUsername()).get(realMenuId);
		}
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("menuList", list);
		dataMap.put("rootPath", request.getContextPath());
		String content = FreemarkerHelper.contentAfterCreate(ButtonTag.class, "ButtonTag.ftl", "temp.html", dataMap);
		try {
			pageContext.getOut().println(content);
		} catch (IOException e) {
			throw new JspTagException("IOException" + e.toString());
		}
		return super.doEndTag();
	}

	@Override
	public int doStartTag() throws JspTagException {
		return EVAL_BODY_INCLUDE;
	}

	public String getMenuid() {
		return menuid;
	}

	public String getType() {
		return type;
	}

	public void setMenuid(String menuid) {
		this.menuid = menuid;
	}

	public void setType(String type) {
		this.type = type;
	}
}

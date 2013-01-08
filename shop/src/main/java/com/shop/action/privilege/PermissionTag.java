package com.shop.action.privilege;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * 权限检查标签类
 */
public class PermissionTag extends TagSupport {
	private static final long serialVersionUID = -5285732412976711256L;

	private String module;
	private String privilege;

	@Override
	public int doStartTag() throws JspException {
		return EVAL_BODY_INCLUDE;
	}

	public String getModule() {
		return module;
	}

	public String getPrivilege() {
		return privilege;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}
}

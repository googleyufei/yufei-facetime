package com.shop.action.manage;

import com.facetime.mgr.bean.EmployeeBean;
import com.facetime.mgr.domain.SysUser;
import com.facetime.mgr.logic.LoginManager;
import com.facetime.mgr.utils.CheckUtil;
import com.facetime.spring.action.Action;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

public class EmployeeLoginAction extends Action {

	@Autowired
	private LoginManager loginManager;

	@RequestMapping("/employee/login")
	public String login(EmployeeBean bean, HttpServletRequest request) throws Exception {
		// 没有一个员工时, 先进性初始化
		if (defaultLogic.count(SysUser.class) == 0) {
			return "redirect:/system/init.do";
		}
		if (!CheckUtil.isValid(bean.getUsername()) || !CheckUtil.isValid(bean.getPassword())) {
			return "department/logon";
		}

		if (loginManager.validate(bean.getUsername(), bean.getPassword())) {
			request.getSession().setAttribute("employee", defaultLogic.findById(SysUser.class, bean.getUsername()));
			return "redirect:/control/center/main.do";
		} else {
			request.setAttribute("message", "您的用户名或者密码错误!");
			return "department/logon";
		}
	}

	@RequestMapping("/employee/logout")
	public String logout(HttpServletRequest request) throws Exception {
		request.getSession().removeAttribute("employee");
		return "redirect:/employee/login.do";
	}
}

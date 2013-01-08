package com.facetime.mgr.action;

import static com.facetime.core.utils.DateUtil.TIMESTAMP_FORMAT_STR;
import static com.facetime.core.utils.DateUtil.date2date;
import static com.facetime.core.utils.DateUtil.dateToStr;
import static com.facetime.core.utils.DateUtil.dayAdd;

import com.facetime.mgr.bean.SysLoginLogForm;
import com.facetime.mgr.bean.UserModel;
import com.facetime.mgr.common.MsgPage;
import com.facetime.mgr.domain.SysLoginLog;
import com.facetime.mgr.logic.SysLoginLogManager;
import com.facetime.spring.action.Action;
import com.facetime.spring.support.Page;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SysLoginLogAction extends Action {

	@RequestMapping("/pages/sysloginlog/mainframe")
	public String mainframe(HttpServletRequest request) throws Exception {
		// 用于功能权限控制
		String menuno = request.getParameter("menuno");
		if (menuno == null) {
			menuno = (String) request.getSession().getAttribute(UserModel.KEY_MENUNO);
		}
		request.getSession().setAttribute(UserModel.KEY_MENUNO, menuno);
		return "logmgr/sysLoginLogMgr";
	}

	@RequestMapping("/pages/sysloginlog/clear")
	public String clear(HttpServletRequest request) {
		defaultLogic.delete(SysLoginLog.class);
		return MsgPage.view(request, "/shop/pages/sysloginlog/list.do", "clear.ok");
	}

	@RequestMapping("/pages/sysloginlog/delete")
	public String delete(HttpServletRequest request) {
		String[] delItem = StringUtils.split(request.getParameter("idList"), ",");
		defaultLogic.deleteByIds(SysLoginLog.class, delItem);
		return MsgPage.view(request, "/shop/pages/sysloginlog/list.do", "delete.ok");
	}

	@RequestMapping("/pages/sysloginlog/logout")
	public String logout(HttpServletRequest request) {
		UserModel loginUser = (UserModel) request.getSession().getAttribute(UserModel.LOGIN_USER_KEY);
		if (loginUser == null) {
			return "department/logout";
		}
		String loginid = loginUser.getLoginid();
		// 把退出信息保存在登录日志表里
		SysLoginLog login = locate(SysLoginLogManager.class).findById(SysLoginLog.class, loginid);
		login.setLogouttime(date2date(new Date(), TIMESTAMP_FORMAT_STR));
		locate(SysLoginLogManager.class).update(login);
		request.getSession().removeAttribute(UserModel.LOGIN_USER_KEY);
		return "logout";
	}

	@RequestMapping("/pages/sysloginlog/list")
	public String list(SysLoginLogForm form, HttpServletRequest request) {
		if (form.getLoginbegintime() == null && form.getLoginendtime() == null) {
			form.setLoginbegintime(dateToStr(dayAdd(new Date(), -1)));
			form.setLoginendtime(dateToStr(new Date()));
		}
		Page<SysLoginLog> currPage = locate(SysLoginLogManager.class).getPage(form);
		request.setAttribute("currPage", currPage);
		request.setAttribute("queryAll", currPage.getQueryResult());
		request.setAttribute("rowNum", currPage.getNeedRowNum());
		return "logmgr/sysLoginLogList";
	}
}

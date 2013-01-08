package com.facetime.mgr.action;

import com.facetime.core.conf.ConfigUtils;
import com.facetime.core.utils.DateUtil;
import com.facetime.core.utils.StringUtils;
import com.facetime.mgr.bean.EmployeeBean;
import com.facetime.mgr.bean.UserModel;
import com.facetime.mgr.common.HashUtil;
import com.facetime.mgr.domain.SysLoginLog;
import com.facetime.mgr.domain.SysUser;
import com.facetime.mgr.logic.LoginManager;
import com.facetime.spring.action.Action;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 登陆控制
 * @author arco
 * @version 4.0
 */
@Controller
public class LoginAction extends Action {

	private static final int LOGIN_SUCCESS = 1;
	/** 密码不正确 */
	private static final int PWD_NOT_CORRECT = 0;
	/** 密码已过期 */
	private static final int PWD_IS_EXPIRED = -5;
	/** 非法用户 */
	private static final int USER_NOT_VALID = -2;
	/** 用户不存在 */
	private static final int USER_NOT_EXISTS = -1;
	/** 没有输入密码 */
	private static final int NO_PWD = -9;
	/** 没有输入用户名 */
	private static final int NO_USERNAME = -8;

	@Autowired
	private LoginManager loginManager;

	@RequestMapping(value = "/employee/login")
	public String checkuser(HttpServletRequest request, EmployeeBean loginForm, ModelMap model) throws Exception {
		// 没有一个员工时, 先进性初始化
		if (loginManager.count(SysUser.class) == 0) {
			return "redirect:/system/init.do";
		}
		int result = validate(loginForm);
		SysUser user = null;
		switch (result) {
		case NO_USERNAME:
			model.addAttribute("message", message("index.checkForm.loginName"));
			break;
		case NO_PWD:
			model.addAttribute("message", message("index.checkForm.password"));
			break;
		case USER_NOT_VALID:
			model.addAttribute("message", message("loginAction.user.invalid"));
			break;
		case USER_NOT_EXISTS:
			model.addAttribute("message", message("loginAction.user.notexist"));
			break;
		case PWD_IS_EXPIRED:
			model.addAttribute("message", message("loginAction.password.unsafe"));
			user = defaultLogic.findById(SysUser.class, loginForm.getUsername());
			user.setValid(false);// 禁掉用户
			loginManager.update(user);
			break;
		case PWD_NOT_CORRECT:
			model.addAttribute("message", message("loginAction.password.unfit"));
			user = defaultLogic.findById(SysUser.class, loginForm.getUsername());
			user.setLoginfaile(user.getLoginfaile() + 1);
			loginManager.update(user);
			break;
		default:
			user = defaultLogic.findById(SysUser.class, loginForm.getUsername());
		}

		UserModel userModel = new UserModel();
		if (user != null) {
			user.setLoginfaile(0);
			user.setLogindate(DateUtil.date2date(new Date(), DateUtil.TIMESTAMP_FORMAT_STR));
			if (user.getModifydate() == null) {
				user.setModifydate(new Date());
			}
			loginManager.update(user);

			BeanUtils.copyProperties(userModel, user);
			loginManager.setupLoginUser(userModel);
			userModel.setHostip(request.getRemoteAddr());
			userModel.setLogintime(DateUtil.date2date(new Date(), DateUtil.TIMESTAMP_FORMAT_STR));
		}
		// 登录失败
		if (result != LOGIN_SUCCESS) {
			if (user != null)
				insertLoginLog(userModel, "N");
			model.addAttribute("errorflag", String.valueOf(result));
			return "department/logon";
		}

		userModel.setLogindate(user.getLogindate());
		userModel.setLoginfaile(user.getLoginfaile());
		if (StringUtils.isValid(user.getUrl())) {
			String str = user.getUrl();
			userModel.setUrl(request.getContextPath() + str);
		} else
			userModel.setUrl(ConfigUtils.getProperty("user.page"));

		// 距离密码修改还剩余多少天，如果不及时修改密码，用户就不能登录
		int i_day = DateUtil.dayCountBetween(user.getModifydate(), new Date());
		userModel.setDays(Integer.parseInt(ConfigUtils.getProperty("days", 90)) - i_day);
		request.getSession().setAttribute(UserModel.LOGIN_USER_KEY, userModel);
		insertLoginLog(userModel, "Y");
		return "redirect:/control/center/main.do";
	}

	private int validate(EmployeeBean loginForm) {
		// 判断登录
		int result = LOGIN_SUCCESS;
		// 如果用户名或密码没有填写
		if (!StringUtils.isValid(loginForm.getUsername()))
			result = NO_USERNAME;
		else if (!StringUtils.isValid(loginForm.getPassword()))
			result = NO_PWD;
		else {
			// 密码有效期天数
			int maxDay = Integer.parseInt(ConfigUtils.getProperty("days", 90));
			String userpwd = HashUtil.hash(loginForm.getPassword());
			SysUser user = defaultLogic.findById(SysUser.class, loginForm.getUsername());
			if (user != null && user.getModifydate() == null)
				user.setModifydate(new Date());

			if (user == null) {
				result = USER_NOT_EXISTS;
			} else if (!user.getPassword().equals(userpwd)) {
				result = PWD_NOT_CORRECT;
			} else if (!user.isValid()) {
				result = USER_NOT_VALID;
			} else if (DateUtil.dayCountBetween(user.getModifydate(), new Date()) > maxDay) {
				if (!user.getUsername().equals("admin"))
					result = PWD_IS_EXPIRED;
			}
		}

		return result;
	}

	/**
	 * 记录登录日志
	 */
	private void insertLoginLog(UserModel model, String result) {
		try {
			SysLoginLog syslog = new SysLoginLog();
			syslog.setHostip(model.getHostip());
			syslog.setUserid(model.getUsername());
			syslog.setUsername(model.getUsername());
			syslog.setLogintime(model.getLogintime());
			syslog.setResult(result);
			defaultLogic.save(syslog);
			model.setLoginid(syslog.getLoginid());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/employee/logout")
	public String logout(HttpServletRequest request) throws Exception {
		request.getSession().removeAttribute("employee");
		return "redirect:/employee/login.do";
	}

}

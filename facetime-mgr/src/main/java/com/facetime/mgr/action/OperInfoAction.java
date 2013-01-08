package com.facetime.mgr.action;

import com.facetime.mgr.bean.OperInfoForm;
import com.facetime.mgr.bean.UserModel;
import com.facetime.mgr.common.HashUtil;
import com.facetime.mgr.common.MsgPage;
import com.facetime.mgr.domain.MenuInfo;
import com.facetime.mgr.domain.SysUser;
import com.facetime.mgr.logic.OperInfoManager;
import com.facetime.spring.action.Action;
import com.facetime.spring.support.Page;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pages/um/operinfo")
public class OperInfoAction extends Action {

	@RequestMapping("/add")
	public String add(HttpServletRequest request, OperInfoForm form) throws Exception {
		SysUser operator = new SysUser();
		BeanUtils.copyProperties(operator, form);
		operator.setPassword(HashUtil.hash(form.getUserpwd()));
		operator.setEmail(form.getEmail());
		operator.setCreatedate(new Date());
		locate(OperInfoManager.class).save(operator);
		// 更新操作员信息缓存
		return MsgPage.view(request, "/shop/pages/um/operinfo/list.do", "add.ok");
	}

	@RequestMapping("/addUI")
	public String addUI(HttpServletRequest request, OperInfoForm form) {
		return "um/addOperInfo";
	}

	@RequestMapping("/delete")
	public String delete(HttpServletRequest request, OperInfoForm form) {
		String operids[] = request.getParameterValues("checkbox");
		locate(OperInfoManager.class).deleteUser(operids);
		return MsgPage.view(request, "/shop/pages/um/operinfo/list.do", "delete.ok");
	}

	@RequestMapping("/list")
	public String list(HttpServletRequest request, OperInfoForm form) {
		Page<SysUser> page = locate(OperInfoManager.class).getOperInfo1(form, form.getToPage(), form.getPageSize());
		request.setAttribute("operLst", page.getQueryResult());
		request.setAttribute("currPage", page);
		request.setAttribute("rowNum", page.getNeedRowNum());
		return "um/operInfoList";
	}

	@RequestMapping("/mainframe")
	public String mainframe(HttpServletRequest request) throws Exception {
		// 用于功能权限控制
		String menuno = request.getParameter("menuno");
		if (menuno == null) {
			menuno = (String) request.getSession().getAttribute(UserModel.KEY_MENUNO);
		}
		request.getSession().setAttribute(UserModel.KEY_MENUNO, menuno);
		return "um/operInfoMgr";
	}

	@RequestMapping("/modify")
	public String modify(HttpServletRequest request, OperInfoForm form) throws Exception {
		OperInfoForm operInfoForm = form;
		SysUser operator = new SysUser();
		BeanUtils.copyProperties(operator, operInfoForm);
		operator.setPassword(HashUtil.hash(form.getUserpwd()));
		operator.setCreatedate(new Date(System.currentTimeMillis()));
		locate(OperInfoManager.class).update(operator);
		// 更新操作员信息缓存
		return MsgPage.view(request, "/shop/pages/um/operinfo/list.do", "errorMsg.updateSuccess");
	}

	@RequestMapping("/modifyUI")
	public String modifyUI(HttpServletRequest request, OperInfoForm form) throws Exception {
		String userid = request.getParameter("userid");
		SysUser user = locate(OperInfoManager.class).findById(SysUser.class, userid);
		OperInfoForm operInfoForm = form;
		BeanUtils.copyProperties(operInfoForm, user);
		return "um/updateOperInfo";
	}

	@RequestMapping("/pwdInit")
	public String pwdInit(HttpServletRequest request, OperInfoForm form) {
		String[] ids = StringUtils.split(request.getParameter("user"), ",");
		locate(OperInfoManager.class).resetPwd(ids);
		return MsgPage.view(request, "/shop/pages/um/operinfo/list.do", "modifypassword.success");
	}

	@RequestMapping("/pwdSave")
	public String pwdSave(HttpServletRequest request, OperInfoForm form) {
		UserModel loginUser = (UserModel) request.getSession().getAttribute(
				com.facetime.mgr.bean.UserModel.LOGIN_USER_KEY);
		form.setUsername(loginUser.getUsername());
		int returnid = locate(OperInfoManager.class).modifyPassword(form.getUsername(), form.getPassword(),
				form.getNewPassword());
		if (returnid == -1) {
			return MsgPage.view(request, "/shop/pages/um/operinfo/list.do", "modify.faile");
		} else {
			return MsgPage.view(request, "/shop/pages/um/operinfo/list.do", "modify.ok");
		}
	}

	@RequestMapping("/pwdSetting")
	public String pwdSetting(HttpServletRequest request) {
		return "um/pwdSetting";
	}

	@RequestMapping("/pwdManager")
	public String pwdManager(HttpServletRequest request) {
		return "um/pwdManager";
	}

	@RequestMapping("/setPage")
	public String setPage(HttpServletRequest request, OperInfoForm form) {
		// 需更新的用户的ID
		String userid = request.getParameter("user");
		List<MenuInfo> list = locate(OperInfoManager.class).getAllMenuLeafInfo(userid);
		request.setAttribute("menuLeafInfoList", list);
		return "um/menuLeafInfo";
	}

	@RequestMapping("/unlock")
	public String unlock(HttpServletRequest request, OperInfoForm form) {
		String userid = request.getParameter("userid");
		String[] userids = userid.split(",");
		locate(OperInfoManager.class).unlockPwd(userids);
		return MsgPage.view(request, "/shop/pages/um/operinfo/list.do", "auth.error.unlock");
	}
}

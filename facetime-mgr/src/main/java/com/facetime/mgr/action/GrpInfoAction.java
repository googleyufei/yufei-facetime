package com.facetime.mgr.action;

import com.facetime.mgr.bean.GrpInfoForm;
import com.facetime.mgr.bean.GrpRoleForm;
import com.facetime.mgr.bean.UserModel;
import com.facetime.mgr.bean.UsrGrpForm;
import com.facetime.mgr.common.MsgPage;
import com.facetime.mgr.domain.UsrGroup;
import com.facetime.mgr.domain.UsrGrpRole;
import com.facetime.mgr.domain.UsrUsrGrp;
import com.facetime.mgr.logic.GrpRoleManager;
import com.facetime.mgr.logic.UsrGrpManager;
import com.facetime.spring.action.Action;
import com.facetime.spring.logic.Logic;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pages/um/grpinfo")
public class GrpInfoAction extends Action {

	@RequestMapping("/addUI")
	public String addUI(HttpServletRequest request, GrpInfoForm form) {
		return "um/addGrpInfo";
	}

	@RequestMapping("/add")
	public String add(HttpServletRequest request, GrpInfoForm form) {
		UsrGroup grp = new UsrGroup();
		grp.setGrpname(form.getGrpname());
		locate(Logic.class, "defaultLogic").save(grp);
		return "um/addGrpInfo";
	}

	@RequestMapping("/addGrpRole")
	public String addGrpRole(HttpServletRequest request, GrpInfoForm form) {
		String[] temp = request.getParameterValues("checkbox");
		String grpcode = request.getParameter("grpcode");
		List<UsrGrpRole> grpRoleLst = new ArrayList<UsrGrpRole>();
		if (temp == null) { // 如果组全部角色删除
			locate(GrpRoleManager.class).removeGrpRole(grpcode);
		} else {
			for (String element : temp) {
				UsrGrpRole grprole = new UsrGrpRole();
				grprole.setGrpcode(grpcode);
				grprole.setRolecode(element);
				grpRoleLst.add(grprole);
			}
		}
		locate(GrpRoleManager.class).saveGrpRole(grpcode, grpRoleLst);
		request.setAttribute("rowNum", String.valueOf(grpRoleLst.size()));
		return "redirect:/pages/um/grpinfo/list.do";
	}

	@RequestMapping("/delete")
	public String delete(HttpServletRequest request, GrpInfoForm form) {
		String[] temp = form.getGrpcode().split(",");
		for (String element : temp) {
			locate(Logic.class, "defaultLogic").deleteById(UsrGroup.class, element);
		}
		return MsgPage.view(request, "/shop/pages/um/grpinfo/list.do", "delete.ok");
	}

	@RequestMapping("/usrgrp/list")
	public String usrlist(HttpServletRequest request, UsrGrpForm form) {
		UserModel loginUser = (UserModel) request.getSession().getAttribute(
				com.facetime.mgr.bean.UserModel.LOGIN_USER_KEY);
		List<UsrGrpForm> usrGrpLst = locate(UsrGrpManager.class).findUsrGrp(form.getUserid(), loginUser.getUsername());
		request.getSession().setAttribute("usrGrpLst", usrGrpLst);
		request.setAttribute("rowNum", String.valueOf(usrGrpLst.size()));
		return "um/grpInfoList";
	}

	@RequestMapping("/list")
	public String list(HttpServletRequest request) {
		List<UsrGroup> grpLst = locate(Logic.class, "defaultLogic").findList(UsrGroup.class);
		request.getSession().setAttribute("grpLst", grpLst);
		request.setAttribute("rowNum", String.valueOf(grpLst.size()));
		return "um/grpInfoList";
	}

	@RequestMapping("/mainframe")
	public String mainframe(HttpServletRequest request) throws Exception {
		// 用于功能权限控制
		String menuno = request.getParameter("menuno");
		if (menuno == null) {
			menuno = (String) request.getSession().getAttribute(UserModel.KEY_MENUNO);
		}
		request.getSession().setAttribute(UserModel.KEY_MENUNO, menuno);
		return "um/grpInfoMgr";
	}

	@RequestMapping("/modify")
	public String modify(HttpServletRequest request, UsrGrpForm form) {
		String[] temp = request.getParameterValues("checkbox");
		String userid = request.getParameter("userid");
		List<UsrUsrGrp> usrGrpLst = new ArrayList<UsrUsrGrp>();
		if (temp != null) {
			for (String element : temp) {
				UsrUsrGrp usrgrp = new UsrUsrGrp();
				usrgrp.setGrpcode(element);
				usrgrp.setUserid(userid);
				usrGrpLst.add(usrgrp);
			}
		}
		locate(UsrGrpManager.class).updateGrpRole(userid, usrGrpLst);
		return MsgPage.view(request, "/shop/pages/um/usrgrp/list.do", "errorMsg.updateSuccess");
	}

	@RequestMapping("/modify")
	public String modify(HttpServletRequest request, GrpInfoForm form) {
		UsrGroup grp = new UsrGroup();
		grp.setGrpcode(form.getGrpcode());
		grp.setGrpname(form.getGrpname());
		locate(Logic.class, "defaultLogic").update(grp);
		return MsgPage.view(request, "/shop/pages/um/grpinfo/list.do", "modifyOk");
	}

	@RequestMapping("/modifyUI")
	public String modifyUI(HttpServletRequest request, GrpInfoForm form) {
		UsrGroup grp = locate(Logic.class, "defaultLogic").findById(UsrGroup.class, form.getGrpcode());
		form.setGrpcode(grp.getGrpcode());
		form.setGrpname(grp.getGrpname());
		request.setAttribute("grpInfo", form);
		return "um/updateGrpInfo";
	}

	@RequestMapping("/setRole")
	public String setRole(HttpServletRequest request, GrpInfoForm form) {
		String grpCode = request.getParameter("grpcode");
		List<GrpRoleForm> grpRoleLst = locate(GrpRoleManager.class).findGrpRole(grpCode);
		request.setAttribute("grpRoleLst", grpRoleLst);
		request.setAttribute("rowNum", String.valueOf(grpRoleLst.size()));
		return "um/grpRole";
	}

}

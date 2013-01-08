package com.facetime.mgr.action;

import com.facetime.mgr.bean.MenuFuncVO;
import com.facetime.mgr.bean.RoleInfoForm;
import com.facetime.mgr.bean.RolefuncVO;
import com.facetime.mgr.bean.UserModel;
import com.facetime.mgr.common.MsgPage;
import com.facetime.mgr.domain.UsrRole;
import com.facetime.mgr.domain.UsrRoleFunction;
import com.facetime.mgr.logic.RoleInfoManager;
import com.facetime.spring.action.Action;
import com.facetime.spring.logic.Logic;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pages/um/roleinfo")
public class RoleInfoAction extends Action {

	@RequestMapping("/addUI")
	public String addUI(HttpServletRequest request, RoleInfoForm form) {
		return "um/addRoleInfo";
	}

	@RequestMapping("/add")
	public String add(HttpServletRequest request, RoleInfoForm form) {
		UsrRole role = new UsrRole();
		role.setRolename(form.getRolename());
		locate(Logic.class, "defaultLogic").save(role);
		return MsgPage.view(request, "/shop/pages/um/roleinfo/list.do", "add.ok");
	}

	@RequestMapping("/delete")
	public String delete(HttpServletRequest request, RoleInfoForm form) {
		String[] roles = request.getParameterValues("checkbox");
		for (String role : roles) {
			locate(RoleInfoManager.class).deleteRole(role);
		}
		return MsgPage.view(request, "/shop/pages/um/roleinfo/list.do", "delete.ok");
	}

	@RequestMapping("/modify")
	public String modify(HttpServletRequest request, RoleInfoForm form) {
		RoleInfoForm addForm = form;
		UsrRole role = new UsrRole();
		role.setRolecode(addForm.getRolecode());
		role.setRolename(addForm.getRolename());
		locate(Logic.class, "defaultLogic").update(role);
		return MsgPage.view(request, "/shop/pages/um/roleinfo/list.do", "modify.ok");
	}

	@RequestMapping("/modifyUI")
	public String modifyUI(HttpServletRequest request, RoleInfoForm form) throws Exception {
		UsrRole role = locate(Logic.class, "defaultLogic").findById(UsrRole.class, form.getRolecode());
		BeanUtils.copyProperties(form, role);
		return "um/updateRoleInfo";
	}

	@RequestMapping("/purview")
	public String purview(HttpServletRequest request, RoleInfoForm form) {
		List<RolefuncVO> menuTree = locate(RoleInfoManager.class).getMenuTree();
		List<UsrRoleFunction> rolefuncLst = locate(RoleInfoManager.class).getRoleFuncs(form.getRolecode());
		for (RolefuncVO vo : menuTree) {
			List<MenuFuncVO> operlst = vo.getOperLst();
			vo.setRoleid(form.getRolecode());
			for (int i = 0; i < operlst.size(); i++) {
				MenuFuncVO mfVO = operlst.get(i);
				for (int j = 0; j < rolefuncLst.size(); j++) {
					UsrRoleFunction rf = rolefuncLst.get(j);
					if (mfVO.getFuncid().equals(rf.getFuncid())) {
						mfVO.setChecked("checked");
					}
				}
			}
		}
		request.getSession().setAttribute("menuTree", menuTree);
		return "um/rolepurview";
	}

	@RequestMapping("/mainframe")
	public String mainframe(HttpServletRequest request) throws Exception {
		// 用于功能权限控制
		String menuno = request.getParameter("menuno");
		if (menuno == null) {
			menuno = (String) request.getSession().getAttribute(UserModel.KEY_MENUNO);
		}
		request.getSession().setAttribute(UserModel.KEY_MENUNO, menuno);
		return "um/roleInfoMgr";
	}

	@RequestMapping("/list")
	public String list(HttpServletRequest request, RoleInfoForm form) {
		List<UsrRole> roleLst = locate(RoleInfoManager.class).getRoles();
		request.setAttribute("rowNum", String.valueOf(roleLst.size()));
		request.setAttribute("roleLst", roleLst);
		return "um/roleInfoList";
	}

	@RequestMapping("/setRoleFunc")
	public String setRoleFunc(HttpServletRequest request, RoleInfoForm form) {
		int count = Integer.parseInt(request.getParameter("count"));
		String[] temp = new String[10];
		List<String> funcids = new ArrayList<String>();
		for (int i = 0; i <= count; i++) {
			temp = request.getParameterValues("checkbox" + i);
			if (temp != null) {
				for (String element : temp) {
					funcids.add(element);
				}
			}
		}
		String rolecode = request.getParameter("rolecode");
		locate(RoleInfoManager.class).updateRoleFunc(rolecode, funcids);
		return MsgPage.view(request, "/shop/pages/um/roleinfo/list.do", "action.isOk");
	}
}

package com.facetime.mgr.action;

import com.facetime.mgr.bean.OrgInfoForm;
import com.facetime.mgr.bean.UserModel;
import com.facetime.mgr.common.BusnDataDir;
import com.facetime.mgr.common.MsgPage;
import com.facetime.mgr.domain.OrgInfo;
import com.facetime.mgr.logic.OrgInfoManager;
import com.facetime.spring.action.Action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pages/org/")
public class OrgInfoAction extends Action {

	@RequestMapping("/mainframe")
	public String mainframe(HttpServletRequest request, OrgInfoForm orgInfoForm) throws Exception {
		// 用于功能权限控制
		String menuno = request.getParameter("menuno");
		if (menuno == null) {
			menuno = (String) request.getSession().getAttribute(UserModel.KEY_MENUNO);
		}
		request.getSession().setAttribute(UserModel.KEY_MENUNO, menuno);
		return list(request, orgInfoForm);
	}

	@RequestMapping("/addUI")
	public String addUI(HttpServletRequest request, OrgInfoForm orgInfoForm) throws Exception {
		String orgid = request.getParameter("orgid");
		String parentid = request.getParameter("parentid");
		if (parentid != null) {
			orgInfoForm.setParentid(parentid);
		} else if (orgid != null) {
			List<OrgInfo> orgInfoList = locate(OrgInfoManager.class).querybyoneorgInfo(orgid);
			for (OrgInfo info : orgInfoList) {
				BeanUtils.copyProperties(orgInfoForm, info);
			}
		}
		request.setAttribute("orgInfoForm", orgInfoForm);
		return "org/orgInfoInsert";
	}

	@RequestMapping("/back")
	public String back(HttpServletRequest request, OrgInfoForm orgInfoForm) {
		String parentId = locate(OrgInfoManager.class).findParentid(request.getParameter("parentid"));
		orgInfoForm.setParentid(parentId);
		UserModel loginUser = (UserModel) request.getSession().getAttribute(UserModel.LOGIN_USER_KEY);
		// 如果返回上一级的父节点是登录用户所属机构
		List<OrgInfo> orgInfoList = null;
		if (request.getParameter("parentid").equals(loginUser.getOrgid())) {
			orgInfoList = locate(OrgInfoManager.class).querybyoneorgInfo(loginUser.getOrgid());
		} else {
			orgInfoList = locate(OrgInfoManager.class).getOrgInfoList(parentId);
		}
		request.setAttribute("countAllNum", Integer.toString(orgInfoList.size()));
		request.setAttribute("orgInfoList", orgInfoList);
		getOrgName(request, parentId);
		return "org/orgInfoList";
	}

	// 返回机构全称
	private void getOrgName(HttpServletRequest request, String parentid) {
		String parentName = locate(OrgInfoManager.class).findParentName(parentid);
		if (parentName != null) {
			request.getSession().setAttribute("parentName", parentName);
		}
	}

	@RequestMapping("/add")
	public String add(HttpServletRequest request, OrgInfoForm orgInfoForm) throws Exception {
		OrgInfo orgInfo = new OrgInfo();
		BeanUtils.copyProperties(orgInfo, orgInfoForm);

		if (orgInfo.getParentid().equals("") || orgInfo.getParentid() == null) {
			orgInfo.setParentid(BusnDataDir.TOP_PARENTID);
		}

		orgInfo.setLevel(locate(OrgInfoManager.class).getChildLevel(orgInfo));
		orgInfo.setOrder(locate(OrgInfoManager.class).getChildOrder(orgInfo));

		if (locate(OrgInfoManager.class).isOnlyOrgid(orgInfo)) {
			locate(OrgInfoManager.class).save(orgInfo);
			locate(OrgInfoManager.class).updateChildNum(orgInfo.getParentid(),
					(locate(OrgInfoManager.class).getChildNum(orgInfo.getParentid()) + 1));
			return MsgPage.view(request, "/shop/pages/org/list.do", "add.ok");
			// 更新缓存
		} else {
			return MsgPage.view(request, "/shop/pages/org/list.do", "orginfo.exist");
		}
	}

	// 显示机构列表
	@RequestMapping("/list")
	public String list(HttpServletRequest request, OrgInfoForm form) throws Exception {
		UserModel loginUser = (UserModel) request.getSession().getAttribute(UserModel.LOGIN_USER_KEY);
		List<OrgInfo> orgInfoList = null;
		if (com.facetime.core.utils.StringUtils.isEmpty(form.getParentid())) {
			form.setParentid(locate(OrgInfoManager.class).findParentid(loginUser.getOrgid()));
			request.getSession().setAttribute("hasChild", form.getParentid());
			orgInfoList = locate(OrgInfoManager.class).querybyoneorgInfo(loginUser.getOrgid());
		} else {
			orgInfoList = locate(OrgInfoManager.class).getListByHql(loginUser.getOrgid(), form.getParentid());
		}
		request.setAttribute("countAllNum", Integer.toString(orgInfoList.size()));
		request.setAttribute("orgInfoList", orgInfoList);
		getOrgName(request, form.getParentid());
		return "org/orgInfoList";
	}

	@RequestMapping("/modify")
	public String modify(HttpServletRequest request, OrgInfoForm orgInfoForm) throws Exception {
		OrgInfo orgInfo = new OrgInfo();
		BeanUtils.copyProperties(orgInfo, orgInfoForm);
		orgInfo.setChildnum(locate(OrgInfoManager.class).getChildNum(orgInfo.getOrgid()));
		if (orgInfo.getParentid().equals("") || orgInfo.getParentid() == null) {
			orgInfo.setParentid(BusnDataDir.TOP_PARENTID);
		}
		locate(OrgInfoManager.class).update(orgInfo);
		return MsgPage.view(request, "/shop/pages/org/list.do?parentid=" + orgInfo.getParentid(), "modify.ok");
	}

	@RequestMapping("/delete")
	public String delete(HttpServletRequest request, OrgInfoForm orgInfoForm) {
		String[] ids = request.getParameterValues("deleteOrgid");
		String parentid = request.getParameter("parentid");
		for (String id2 : ids) {
			String id[] = StringUtils.split(locate(OrgInfoManager.class).getAllChildOrgid(id2), ",");
			for (String element : id) {
				locate(OrgInfoManager.class).deleteById(OrgInfo.class, element);
			}
		}
		// 更新父节点的子节点数
		locate(OrgInfoManager.class).updateChildNum(parentid,
				locate(OrgInfoManager.class).getChildNum(parentid) - ids.length);
		return MsgPage.view(request, "/shop/pages/org/list.do?parentid=" + parentid, "delete.ok");
	}
}

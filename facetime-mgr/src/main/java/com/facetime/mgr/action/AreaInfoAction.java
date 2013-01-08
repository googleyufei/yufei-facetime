package com.facetime.mgr.action;

import com.facetime.mgr.bean.AreaInfoForm;
import com.facetime.mgr.bean.UserModel;
import com.facetime.mgr.common.BusnDataDir;
import com.facetime.mgr.common.MsgPage;
import com.facetime.mgr.domain.AreaInfo;
import com.facetime.mgr.logic.AreaInfoManager;
import com.facetime.spring.action.Action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pages/area")
public class AreaInfoAction extends Action {

	@RequestMapping("/delete")
	public String delete(HttpServletRequest request, AreaInfoForm form) {
		if (!isExitingOrg(request, form)) {
			removeGeogArea(request, form);
			return MsgPage.view(request, "/shop/pages/area/list.do", "delete.ok");
		} else {
			return MsgPage.view(request, "back.del.faile", "delete.ok");
		}
	}

	@RequestMapping("/list")
	public String list(HttpServletRequest request, AreaInfoForm form) {
		String parentid = request.getParameter("parentid");
		UserModel loginUser = (UserModel) request.getSession().getAttribute(UserModel.LOGIN_USER_KEY);
		String areaid = loginUser.getAreaid();
		List<AreaInfo> areaInfoList = null;
		if (parentid == null || parentid.equals("")) {
			parentid = locate(AreaInfoManager.class).findParentid(areaid);
			request.getSession().setAttribute("hasChild", parentid);
			areaInfoList = locate(AreaInfoManager.class).querybyoneGeogAreaList(areaid);
		} else {
			areaInfoList = locate(AreaInfoManager.class).getListByHql(areaid, parentid);
		}
		form.setParentid(parentid);
		request.setAttribute("countAllNum", Integer.toString(areaInfoList.size()));
		request.setAttribute("areaInfoList", areaInfoList);
		getGeogAreaName(request,
				form.getParentid().equals(BusnDataDir.TOP_PARENTID) ? areaid : form.getParentid());
		return "area/areaInfoList";
	}

	// 返回区域名称
	private void getGeogAreaName(HttpServletRequest request, String parentid) {
		String parentName = locate(AreaInfoManager.class).findParentName(parentid);
		if (parentName != null) {
			request.getSession().setAttribute("parentName", parentName);
		}
	}

	@RequestMapping("/mainframe")
	public String mainframe(HttpServletRequest request, AreaInfoForm form) throws Exception {
		// 用于功能权限控制
		String menuno = request.getParameter("menuno");
		if (menuno == null) {
			menuno = (String) request.getSession().getAttribute(UserModel.KEY_MENUNO);
		}
		request.getSession().setAttribute(UserModel.KEY_MENUNO, menuno);

		return list(request, form);
	}

	@RequestMapping("/insert")
	public String insert(HttpServletRequest request, AreaInfoForm form) throws Exception {
		AreaInfo areaInfo = new AreaInfo();
		BeanUtils.copyProperties(areaInfo, form);
		if (areaInfo.getParentid().equals("") || areaInfo.getParentid() == null) {
			areaInfo.setParentid(BusnDataDir.TOP_PARENTID);
		}
		areaInfo.setLevel(locate(AreaInfoManager.class).getChildLevel(areaInfo));
		areaInfo.setOrder(locate(AreaInfoManager.class).getChildOrder(areaInfo));
		if (locate(AreaInfoManager.class).isOnlyAreaid(areaInfo.getAreaid())) {
			locate(AreaInfoManager.class).save(areaInfo);
			locate(AreaInfoManager.class).updateChildNum(areaInfo.getParentid(),
					(locate(AreaInfoManager.class).getChildNum(areaInfo.getParentid()) + 1));
		}
		return "area/areaInfoInsert";
	}

	// 判断机构下是否存在终端信息
	private boolean isExitingOrg(HttpServletRequest request, AreaInfoForm form) {
		boolean result = false;
		String[] ids = request.getParameterValues("deleteAreaid");
		result = locate(AreaInfoManager.class).isExitingOrg(ids);
		return result;
	}

	@RequestMapping("/modifyUI")
	public String modifyUI(HttpServletRequest request, AreaInfoForm form) throws Exception {
		request.setAttribute("method", "update");
		List<AreaInfo> list = locate(AreaInfoManager.class).querybyoneGeogAreaList(form.getAreaid());
		for (AreaInfo area : list) {
			BeanUtils.copyProperties(form, area);
		}
		return "area/areaInfoModify.jsp";
	}

	@RequestMapping("/addUI")
	public String addUI(HttpServletRequest request, AreaInfoForm form) {
		request.setAttribute("method", "insert");
		return "area/areaInfoInsert";
	}

	@RequestMapping("/remove")
	public String removeGeogArea(HttpServletRequest request, AreaInfoForm form) {
		String[] ids = request.getParameterValues("deleteAreaid");
		String parentid = request.getParameter("parentid");
		for (String id : ids) {
			// 删除相应的地图信息与区域信息
			locate(AreaInfoManager.class).delMapByGeog(locate(AreaInfoManager.class).getAllChildGeogid(id));
		}
		// 更新父节点的子节点数
		locate(AreaInfoManager.class).updateChildNum(parentid,
				(locate(AreaInfoManager.class).getChildNum(parentid) - ids.length));
		return MsgPage.view(request, "/shop/pages/area/list.do", "delete.ok");
	}

	// 返回上一级
	@RequestMapping("/back")
	public String back(HttpServletRequest request, AreaInfoForm form) {
		String parentId = locate(AreaInfoManager.class).findParentid(request.getParameter("parentid"));
		form.setParentid(parentId);
		UserModel loginUser = (UserModel) request.getSession().getAttribute(UserModel.LOGIN_USER_KEY);
		List<AreaInfo> areaInfoList = null;
		if (request.getParameter("parentid").equals(loginUser.getAreaid())) {
			areaInfoList = locate(AreaInfoManager.class).querybyoneGeogAreaList(loginUser.getAreaid());
		} else {
			areaInfoList = locate(AreaInfoManager.class).getGeogAreaList(parentId);
		}
		request.setAttribute("areaInfoList", areaInfoList);
		request.setAttribute("countAllNum", Integer.toString(areaInfoList.size()));
		getGeogAreaName(request, parentId);
		return "area/areaInfoList";
	}

	@RequestMapping("/modify")
	public String modify(HttpServletRequest request, AreaInfoForm form) throws Exception {
		AreaInfo areaInfo = new AreaInfo();
		BeanUtils.copyProperties(areaInfo, form);
		areaInfo.setChildnum(locate(AreaInfoManager.class).getChildNum(areaInfo.getAreaid()));
		if (areaInfo.getParentid().equals("") || areaInfo.getParentid() == null) {
			areaInfo.setParentid(BusnDataDir.TOP_PARENTID);
		}
		locate(AreaInfoManager.class).update(areaInfo);
		return MsgPage.view(request, "/shop/pages/area/list.do", "modify.ok");
	}
}

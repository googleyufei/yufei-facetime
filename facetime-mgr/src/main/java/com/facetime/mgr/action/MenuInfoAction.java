package com.facetime.mgr.action;

import com.facetime.mgr.bean.MenuFunctionForm;
import com.facetime.mgr.bean.MenuInfoForm;
import com.facetime.mgr.bean.SortRequestForm;
import com.facetime.mgr.bean.UserModel;
import com.facetime.mgr.common.BusnDataDir;
import com.facetime.mgr.common.MsgPage;
import com.facetime.mgr.domain.MenuFunction;
import com.facetime.mgr.domain.MenuInfo;
import com.facetime.mgr.logic.MenuInfoManager;
import com.facetime.spring.action.Action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 菜单管理
 */
@Controller
public class MenuInfoAction extends Action {

	/** */
	private static final String MENU_INFO_EDIT = "menu/menuInfoEdit";

	/**
	 * 新增菜单
	 */
	@RequestMapping("/pages/menu/addUI")
	public String addUI(MenuInfoForm menuForm) throws Exception {
		MenuInfoForm newForm = new MenuInfoForm();
		newForm.setParentid(menuForm.getParentid());
		newForm.setFloor(menuForm.getFloor());
		// 把newForm的属性值copy到menuForm
		BeanUtils.copyProperties(menuForm, newForm);
		return MENU_INFO_EDIT;
	}

	/**
	 * 删除选择的菜单
	 */
	@RequestMapping("/pages/menu/delete")
	public String delselected(MenuInfoForm menuInfoForm, ModelMap model) throws Exception {
		String[] ids = StringUtils.split(menuInfoForm.getMenuid(), ",");
		MenuInfo menu = beanLocator.locate(MenuInfoManager.class).findById(MenuInfo.class, ids[0]);
		String url = "/shop/pages/menu/list.do?menuid=" + menu.getParentid();
		beanLocator.locate(MenuInfoManager.class).delAll(ids);
		return MsgPage.view(model, url, "delete.ok");
	}

	@RequestMapping("/pages/menu/mainframe")
	public String mainframe(HttpServletRequest request) throws Exception {
		// 用于功能权限控制
		String menuno = request.getParameter("menuno");
		if (menuno == null) {
			menuno = (String) request.getSession().getAttribute(UserModel.KEY_MENUNO);
		}
		request.getSession().setAttribute(UserModel.KEY_MENUNO, menuno);
		return "menu/menuInfoMgr";
	}

	@RequestMapping("/pages/menu/save")
	public String save(MenuInfoForm menuInfoForm, HttpServletRequest request) throws Exception {
		MenuInfo menuInfo = new MenuInfo();
		BeanUtils.copyProperties(menuInfo, menuInfoForm);
		// 设置图片信息
		if (menuInfo != null && menuInfo.getActionto() != null) {
			menuInfo.setActionto(menuInfo.getActionto().trim());
		}
		menuInfo.setOrder(beanLocator.locate(MenuInfoManager.class).queryChildnum(menuInfo.getParentid()) + 1);
		int iResult = beanLocator.locate(MenuInfoManager.class).addItem(menuInfo);
		String message = null;
		switch (iResult) {
		case 0:
			message = message("menuForm.error.sameKey");
			break;
		case 1:
			message = message("save.ok");
			break;
		default:
		}
		// 保存后继续新增
		if (request.getParameter("newAnOther") != null || iResult != 1) {
			MenuInfoForm newForm = new MenuInfoForm();
			newForm.setParentid(menuInfoForm.getParentid());
			newForm.setFloor(menuInfoForm.getFloor());
			// 把newForm的属性值copy到menuForm
			BeanUtils.copyProperties(menuInfoForm, newForm);
			return MENU_INFO_EDIT;
		}
		request.setAttribute("message", message);
		return "share/message";
	}

	/**
	 * 保存菜单的功能点
	 */
	@RequestMapping("/pages/menu/saveOperate")
	public String saveOperate(HttpServletRequest request) throws Exception {
		String[] temp = request.getParameterValues("checkbox");
		String menuid = request.getParameter("menuid");
		List<MenuFunction> menuFunLst = new ArrayList<MenuFunction>();
		if (temp != null) {
			for (String element : temp) {
				MenuFunction menufun = new MenuFunction();
				menufun.setOperid(element);
				menufun.setMenuid(menuid);
				menuFunLst.add(menufun);
			}
		}
		beanLocator.locate(MenuInfoManager.class).updateMenuOper(menuid, menuFunLst);
		return MsgPage.view(request,
				"/shop/pages/menu/list.do?menuid=" + locate(MenuInfoManager.class).getParentId(menuid), "save.ok");
	}

	/**
	 * 保存菜单的排列顺序
	 */
	@RequestMapping("/pages/menu/sort")
	public String sort(SortRequestForm sForm, HttpServletRequest request) throws Exception {
		MenuInfo menuInfo = defaultLogic.findById(MenuInfo.class, StringUtils.split(sForm.getIdList(), ",")[0]);
		int iResult = beanLocator.locate(MenuInfoManager.class).reorderItems(sForm.getIdList());
		if (iResult <= 0) {
			return MsgPage.view(request, "/shop/pages/menu/list.do?menuid=" + menuInfo.getParentid(),
					"menuForm.error.notExist");
		} else {
			return MsgPage.view(request, "/shop/pages/menu/list.do?menuid=" + menuInfo.getParentid(), "save.ok");
		}
	}

	/**
	 * 查询列表
	 */
	@RequestMapping("/pages/menu/list")
	public String list(MenuInfoForm menuInfoForm, HttpServletRequest request) throws Exception {
		// 如果父ID为空，设置最顶层父ID
		if (menuInfoForm.getMenuid() == null) {
			menuInfoForm.setParentid(BusnDataDir.TOP_PARENT_ID);
			menuInfoForm.setFloor("1");
		} else if (request.getParameter("control") != null && request.getParameter("control").equals("back")) {
			MenuInfo menuinfo = beanLocator.locate(MenuInfoManager.class).findById(menuInfoForm.getMenuid());
			menuInfoForm.setParentid(menuinfo.getParentid());
			menuInfoForm.setFloor(String.valueOf(menuinfo.getFloor()));
		} else if (menuInfoForm.getMenuid().equals("0")) { // 如果是最顶层节点,即父节点为0
			menuInfoForm.setParentid("0");
			menuInfoForm.setFloor("1");
		} else {
			MenuInfo menuinfo = beanLocator.locate(MenuInfoManager.class).findById(menuInfoForm.getMenuid());
			menuInfoForm.setParentid(menuinfo.getMenuid());
			menuInfoForm.setFloor(String.valueOf(menuinfo.getFloor() + 1));
		}

		List<MenuInfo> childList = beanLocator.locate(MenuInfoManager.class).queryChildList(menuInfoForm.getParentid());
		request.setAttribute("rowNum", String.valueOf(childList.size()));
		request.setAttribute("menuList", childList);
		request.setAttribute("menuNavigation",
				beanLocator.locate(MenuInfoManager.class).getNavigation(menuInfoForm.getParentid()));

		return "menu/menuInfoList";
	}

	/**
	 * 跳转到功能点设置页面
	 */
	@RequestMapping("/pages/menu/setting")
	public String setting(MenuInfoForm form, HttpServletRequest request) throws Exception {
		List<MenuFunctionForm> menuOperLst = beanLocator.locate(MenuInfoManager.class).queryMenuOperate(
				form.getMenuid());
		request.setAttribute("menuOperLst", menuOperLst);
		return "menu/menuFunction";
	}

	/**
	 * 跳转到排序页面
	 */
	@RequestMapping("/pages/menu/sortUI")
	public String sortUI(SortRequestForm form, HttpServletRequest request) throws Exception {
		List<MenuInfo> menuInfoLst = beanLocator.locate(MenuInfoManager.class).queryChildList(form.getMenuid());
		request.setAttribute("sortRequestForm", form);
		request.setAttribute("menuInfoLst", menuInfoLst);
		return "menu/menuInfoSort";
	}

	@RequestMapping("/pages/menu/update")
	public String update(MenuInfoForm menuInfoForm, HttpServletRequest request) throws Exception {
		MenuInfo menuInfo = new MenuInfo();
		BeanUtils.copyProperties(menuInfo, menuInfoForm);
		// 修改操作
		int iResult = beanLocator.locate(MenuInfoManager.class).updateItem(menuInfo);
		switch (iResult) {
		case -1:
			return MsgPage.view(request, "/shop/pages/menu/list.do?menuid=" + menuInfo.getParentid(),
					"menuForm.error.notExist");
		case 0:
			return MsgPage.view(request, "/shop/pages/menu/list.do?menuid=" + menuInfo.getParentid(),
					"menuForm.error.sameKey", menuInfoForm.getMenuid());
		case 1:
		default:
			return MsgPage.view(request, "/shop/pages/menu/list.do?menuid=" + menuInfo.getParentid(), "save.ok");
		}
	}

	/**
	 * 跳转到修改菜单信息页面
	 */
	@RequestMapping("/pages/menu/updateUI")
	public String updateUI(MenuInfoForm form, HttpServletRequest request) throws Exception {
		MenuInfo obj = beanLocator.locate(MenuInfoManager.class).findById(form.getMenuid());
		BeanUtils.copyProperties(form, obj);
		request.setAttribute("menuPath", beanLocator.locate(MenuInfoManager.class).getPath(form.getMenuid()));
		request.setAttribute("menuInfoForm", form);
		return MENU_INFO_EDIT;
	}

}

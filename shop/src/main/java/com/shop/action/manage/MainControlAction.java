package com.shop.action.manage;

import com.facetime.mgr.bean.UserModel;
import com.facetime.mgr.common.BusnDataDir;
import com.facetime.mgr.domain.MenuInfo;
import com.facetime.mgr.domain.MenuOperate;
import com.facetime.mgr.logic.LoginManager;
import com.facetime.spring.action.Action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainControlAction extends Action {

	@Autowired
	private LoginManager loginManager;

	@RequestMapping("/control/center/end")
	public String end() {
		return "controlcenter/end";
	}

	@RequestMapping("/control/center/main")
	public String mainControl(HttpServletRequest request) {
		String menuid = request.getParameter("menuid");
		MenuInfo menuinfo = defaultLogic.findById(MenuInfo.class, menuid);
		UserModel userModel = (UserModel) request.getSession().getAttribute(UserModel.LOGIN_USER_KEY);
		List<MenuInfo> mainpanl = new ArrayList<MenuInfo>();
		if (userModel != null && userModel.getGroupids().length > 0)
			loginManager.getleafmenu(menuid, userModel.getGroupids(), mainpanl);
		request.setAttribute("menuinfo", menuinfo);
		request.setAttribute("mainpanl", mainpanl);
		return "controlcenter/default";
	}

	@RequestMapping("/control/center/left")
	public String menu(HttpServletRequest request) {
		List<MenuInfo> menulist = new ArrayList<MenuInfo>();
		UserModel userModel = (UserModel) request.getSession().getAttribute(UserModel.LOGIN_USER_KEY);

		Map<String, List<MenuOperate>> hp = loginManager.findMenuFuncMap(userModel.getUsername());
		BusnDataDir.MENU_OPERID_MAP.remove(userModel.getUsername());
		BusnDataDir.MENU_OPERID_MAP.put(userModel.getUsername(), hp);

		String lefttree = "";
		if (userModel.getGroupids().length > 0) {
			menulist = loginManager.getMenubyGrp(userModel.getGroupids());
			for (int i = 0; i < menulist.size(); i++) {
				MenuInfo menuinfo = (MenuInfo) menulist.get(i);
				lefttree += loginManager.createTree(menuinfo.getMenuid(), userModel.getGroupids(),
						request.getContextPath());
			}
		}
		request.setAttribute("lefttree", lefttree);
		return "controlcenter/menu";
	}

	@RequestMapping("/control/center/right")
	public String right() {
		return "controlcenter/right";
	}

	@RequestMapping("/control/center/top")
	public String top() {
		return "controlcenter/top";
	}
}

package com.facetime.mgr.action;

import com.facetime.core.utils.StringUtils;
import com.facetime.mgr.bean.MenuOperateForm;
import com.facetime.mgr.common.MsgPage;
import com.facetime.mgr.domain.MenuOperate;
import com.facetime.mgr.logic.MenuOperateManager;
import com.facetime.spring.action.Action;
import com.facetime.spring.support.Locator;
import com.facetime.spring.support.Page;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MenuOperateAction extends Action {
	@Autowired
	private Locator locator;

	@RequestMapping("/pages/menuoperate/addUI")
	public String addUI(HttpServletRequest request) throws Exception {
		request.setAttribute("menuOperateForm", new MenuOperateForm());
		return "menu/menuOperateEdit";
	}

	@RequestMapping("/pages/menuoperate/mainframe")
	public String mainframe(HttpServletRequest request) throws Exception {
		String menuno = request.getParameter("menuno");
		request.getSession().setAttribute("menuoperate.menuno", menuno);
		return "menu/menuOperateMgr";
	}

	@RequestMapping("/pages/menuoperate/save")
	public String save(MenuOperateForm menuOperateForm, HttpServletRequest request) throws Exception {
		MenuOperate menuOperate = new MenuOperate();
		BeanUtils.copyProperties(menuOperate, menuOperateForm);
		locator.locate(MenuOperateManager.class).save(menuOperate);
		request.setAttribute("menuOperateForm", new MenuOperateForm());
		return "menu/menuOperateEdit";
	}

	@RequestMapping("/pages/menuoperate/update")
	public String update(MenuOperateForm menuOperateForm, HttpServletRequest request) throws Exception {
		MenuOperate menuOperate = new MenuOperate();
		BeanUtils.copyProperties(menuOperate, menuOperateForm);
		locator.locate(MenuOperateManager.class).update(menuOperate);
		return MsgPage.view(request, "/shop/pages/menuoperate/list.do", "save.ok");
	}

	@RequestMapping("/pages/menuoperate/delete")
	public String delete(HttpServletRequest request) {
		String operid = request.getParameter("idList");
		if (StringUtils.isValid(operid)) {
			locate(MenuOperateManager.class).delAll(operid);
		}
		return MsgPage.view(request, "/shop/pages/menuoperate/list.do", "delete.ok");
	}

	@RequestMapping("/pages/menuoperate/list")
	public String list(MenuOperateForm form, HttpServletRequest request) throws Exception {
		String operid = request.getParameter("idList");
		if (operid != null && !operid.equals("")) {
			locator.locate(MenuOperateManager.class).delAll(operid);
		}
		Page<MenuOperate> page = locator.locate(MenuOperateManager.class).getPage(form.getToPage(), form.getPageSize());
		request.setAttribute("currPage", page);
		request.getSession().setAttribute("grpLst", page.getQueryResult());
		request.setAttribute("rowNum", String.valueOf(page.getNeedRowNum()));
		return "menu/menuOperateList";
	}

	@RequestMapping("/pages/menuoperate/updateUI")
	public String updateUI(MenuOperateForm form, HttpServletRequest request) throws Exception {
		String objcode = request.getParameter("objcode");
		MenuOperate obj = locator.locate(MenuOperateManager.class).findById(MenuOperate.class, objcode);
		BeanUtils.copyProperties(form, obj);
		request.setAttribute("menuOperateForm", form);
		return "menu/menuOperateEdit";
	}
}

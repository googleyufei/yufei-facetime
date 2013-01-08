package com.facetime.mgr.action;

import com.facetime.mgr.bean.DataDirForm;
import com.facetime.mgr.bean.UserModel;
import com.facetime.mgr.common.BusnDataDir;
import com.facetime.mgr.common.MsgPage;
import com.facetime.mgr.domain.SysDatadir;
import com.facetime.mgr.logic.DataDirManager;
import com.facetime.spring.action.Action;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 跳转到数据字典管理界面: 主界面 新增界面 修改界面
 */
@Controller
public class DataDirAction extends Action {

	@RequestMapping("/pages/datadir/addUI")
	public String addUI(DataDirForm form, HttpServletRequest request) throws Exception {
		DataDirForm newForm = new DataDirForm();
		newForm.setParentid(form.getParentid());
		// 把newForm的属性值copy到dirForm
		BeanUtils.copyProperties(form, newForm);
		request.setAttribute("dirPath", locate(DataDirManager.class).getPath(form.getParentid()));
		return "datadir/dataDirInfo";
	}

	@RequestMapping("/pages/datadir/delete")
	public String delete(HttpServletRequest request, DataDirForm form) {
		int iCount = locate(DataDirManager.class).delAll(form.getId().split(","));
		return MsgPage.view(request, "/shop/pages/datadir/list.do?id=" + form.getParentid(), "dataDir.del.ok",
				String.valueOf(iCount));
	}

	@RequestMapping("/pages/datadir/list")
	public String list(DataDirForm form, HttpServletRequest request) {
		// 如果父ID为空，设置最顶层父ID
		if (form.getId() == null) {
			form.setId(BusnDataDir.TOP_PARENT_ID);
			form.setParentid(BusnDataDir.TOP_PARENT_ID);
		} else if ("back".equals(form.getMark())) {
			form.setId(locate(DataDirManager.class).getParentId(form.getId()));
			form.setParentid(locate(DataDirManager.class).getParentId(form.getId()));
		} else {
			form.setParentid(locate(DataDirManager.class).getParentId(form.getId()));
		}
		List<SysDatadir> childList = locate(DataDirManager.class).queryChildList(form.getId());
		for (SysDatadir dir : childList) {
			if (dir.getValue() != null && !dir.getValue().equals("")) {
				dir.setValue(dir.getValue().replace(",", "<br>"));
			}
		}
		request.setAttribute("dirList", childList);
		request.setAttribute("rowNum", String.valueOf(childList.size()));
		request.setAttribute("dirNavigation", locate(DataDirManager.class).getNavigation(form.getParentid()));
		request.setAttribute("dataDirForm", form);
		return "datadir/dataDirList";
	}

	@RequestMapping("/pages/datadir/mainframe")
	public String mainframe(HttpServletRequest request) {
		// 用于功能权限控制
		String menuno = request.getParameter("menuno");
		if (menuno == null) {
			menuno = (String) request.getSession().getAttribute(UserModel.KEY_MENUNO);
		}
		request.getSession().setAttribute(UserModel.KEY_MENUNO, menuno);
		return "datadir/dataDirMgr";
	}

	@RequestMapping("/pages/datadir/modify")
	public String modify(HttpServletRequest request, DataDirForm form) throws Exception {
		SysDatadir dir = new SysDatadir();
		BeanUtils.copyProperties(dir, form);
		int iResult = locate(DataDirManager.class).updateItem(dir);
		String url = "/shop/pages/datadir/list.do?id=" + form.getParentid();
		switch (iResult) {
		case -1:
			return MsgPage.view(request, url, "dataDir.error.notExist");
		case 0:
			return MsgPage.view(request, url, "dataDir.error.sameKey");
		case 1:
		default:
			return MsgPage.view(request, url, "save.ok");
		}
	}

	@RequestMapping("/pages/datadir/modifyUI")
	public String modifyUI(HttpServletRequest request, DataDirForm form) throws IllegalAccessException,
			InvocationTargetException {
		SysDatadir dir = defaultLogic.findById(SysDatadir.class, form.getId());
		BeanUtils.copyProperties(form, dir);
		request.setAttribute("dirPath", locate(DataDirManager.class).getPath(form.getId()));
		request.setAttribute("dataDirForm", form);
		return "datadir/dataDirInfo";
	}

	@RequestMapping("/pages/datadir/refresh")
	public String refresh(HttpServletRequest request) throws Exception {
		BusnDataDir.refresh();
		return MsgPage.view(request, "", "dataDir.pre.ok");
	}

	@RequestMapping("/pages/datadir/save")
	public String save(HttpServletRequest request, DataDirForm form) throws Exception {
		SysDatadir dir = new SysDatadir();
		BeanUtils.copyProperties(dir, form);
		int iResult = locate(DataDirManager.class).addItem(dir);
		String messageKey = null;
		switch (iResult) {
		case 0:
			messageKey = "dataDir.error.sameKey";
			BusnDataDir.refresh();
		case 1:
			messageKey = "save.ok";
		}
		if (request.getParameter("newAnOther") != null || iResult != 1) {
			request.setAttribute("dirPath", locate(DataDirManager.class).getPath(form.getParentid()));
			return MsgPage.view(request, "/shop/pages/datadir/addUI.do?parentid=" + form.getParentid(), messageKey);
		}
		return "redirect:/pages/datadir/list.do?id=" + form.getParentid();
	}

	@RequestMapping("/pages/datadir/sort")
	public String sort(HttpServletRequest request, DataDirForm form) {
		int iResult = locate(DataDirManager.class).reorderItems(form.getIdList());
		if (iResult <= 0) {
			return MsgPage.view(request, "/shop/pages/datadir/list.do?id=" + form.getParentid(),
					"menuForm.error.notExist");
		} else {
			return MsgPage.view(request, "/shop/pages/datadir/list.do?id=" + form.getParentid(), "save.ok");
		}
	}

	@RequestMapping("/pages/datadir/sortUI")
	public String sortUI(HttpServletRequest request, DataDirForm form) {
		if (form.getParentid() == null) {
			form.setParentid(BusnDataDir.TOP_PARENT_ID);
		}
		List<SysDatadir> dataDirInfoLst = locate(DataDirManager.class).queryChildList(form.getParentid());
		request.setAttribute("dataDirForm", form);
		request.setAttribute("dataDirInfoLst", dataDirInfoLst);
		return "datadir/dataDirInfoSort";
	}
}

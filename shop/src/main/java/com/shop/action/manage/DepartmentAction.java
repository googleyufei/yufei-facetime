package com.shop.action.manage;

import com.facetime.spring.action.Action;
import com.facetime.spring.support.Limitable.PageBy;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shop.action.privilege.Permission;
import com.shop.domain.PageView;
import com.shop.domain.privilege.Department;
import com.shop.logic.bean.DepartmentBean;
import com.shop.util.SiteUrl;

@Controller
public class DepartmentAction extends Action {

	private static final String PAGE_EDIT = "department/editdepartment";
	private static final String PAGE_MSG = "share/message";
	private static final String PAGE_ADDUI = "department/adddepartment";
	private static final String PAGE_LIST = "department/departmentlist";

	@RequestMapping("/control/department/add")
	@Permission(module = "department", privilege = "insert")
	public String addDepartment(DepartmentBean bean, HttpServletRequest request) throws Exception {
		Department department = new Department();
		department.setName(bean.getName());
		this.getDefaultLogic().save(department);

		request.setAttribute("message", "添加新部门成功!");
		request.setAttribute("urladdress", SiteUrl.readUrl("control.department.list"));
		return PAGE_MSG;
	}

	@RequestMapping("/control/department/addUI")
	@Permission(module = "department", privilege = "insert")
	public String addUI() throws Exception {
		return PAGE_ADDUI;
	}

	@RequestMapping("/control/department/delete")
	@Permission(module = "department", privilege = "delete")
	public String delete(DepartmentBean bean, HttpServletRequest request) throws Exception {
		this.defaultLogic.findById(Department.class, bean.getDepartmentid());
		request.setAttribute("message", "删除部门成功!");
		request.setAttribute("urladdress", SiteUrl.readUrl("control.department.list"));
		return PAGE_MSG;
	}

	@RequestMapping("/control/department/edit")
	@Permission(module = "department", privilege = "update")
	public String edit(DepartmentBean bean, HttpServletRequest request) throws Exception {
		Department department = defaultLogic.findById(Department.class, bean.getDepartmentid());
		department.setName(bean.getName());
		this.getDefaultLogic().update(department);

		request.setAttribute("message", "修改部门信息成功!");
		request.setAttribute("urladdress", SiteUrl.readUrl("control.department.list"));
		return PAGE_MSG;
	}

	@RequestMapping("/control/department/editUI")
	@Permission(module = "department", privilege = "update")
	public String editUI(DepartmentBean bean, HttpServletRequest request) throws Exception {
		Department department = defaultLogic.findById(Department.class, bean.getDepartmentid());
		request.setAttribute("departmentName", department.getName());
		return PAGE_EDIT;
	}

	@RequestMapping("/control/department/list")
	@Permission(module = "department", privilege = "view")
	public String list(DepartmentBean bean, HttpServletRequest request) throws Exception {
		PageView<Department> pageView = new PageView<Department>(12, bean.getPage());
		pageView.setQueryResult(this.getDefaultLogic().findPage(Department.class,
				PageBy.of(pageView.getCurrentpage(), pageView.getMaxresult())));
		request.setAttribute("pageView", pageView);
		return PAGE_LIST;
	}
}

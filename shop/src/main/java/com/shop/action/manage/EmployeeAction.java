package com.shop.action.manage;

import com.facetime.mgr.bean.EmployeeBean;
import com.facetime.mgr.domain.SysUser;
import com.facetime.mgr.logic.LoginManager;
import com.facetime.mgr.utils.CheckUtil;
import com.facetime.spring.action.Action;
import com.facetime.spring.support.Limitable.PageBy;
import com.facetime.spring.support.QueryFilter;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ServletContextAware;

import com.shop.action.privilege.Permission;
import com.shop.domain.PageView;
import com.shop.domain.privilege.Department;
import com.shop.domain.privilege.EmployeePrivilegeGrp;
import com.shop.domain.privilege.PrivilegeGroup;
import com.shop.logic.bean.BaseBean;
import com.shop.util.SiteUrl;
import com.shop.util.UploadUtil;

@Controller
public class EmployeeAction extends Action implements ServletContextAware {

	@Autowired
	private LoginManager loginManager;
	private ServletContext application;

	@RequestMapping("/control/employee/edit")
	@Permission(module = "employee", privilege = "update")
	public String edit(EmployeeBean bean, ModelMap model) throws Exception {
		String errorMsg = BaseBean.checkImageFile(bean.getPicture());
		if (CheckUtil.isValid(bean.getPicture()) && errorMsg != null) {
			model.addAttribute("urladdress", SiteUrl.readUrl("control.employee.editUI"));
			model.addAttribute("employeeBean", bean);
			model.addAttribute("message", errorMsg);
			return "share/message";
		}
		if (CheckUtil.isValid(bean.getPicture())) {
			String fileName = UploadUtil.writeFileToDisk(bean.getPicture(), application,
					getPrefixPath(bean.getUsername()));
			bean.setImageName(fileName);
			bean.setImagePath(getPrefixPath(bean.getUsername()) + "/" + fileName);
		}
		loginManager.update(bean);
		model.addAttribute("message", "修改员工信息成功!");
		model.addAttribute("urladdress", SiteUrl.readUrl("control.employee.list"));
		return "share/message";
	}

	@RequestMapping("/control/employee/editUI")
	@Permission(module = "employee", privilege = "update")
	public String editUI(EmployeeBean bean, HttpServletRequest request) throws Exception {
		loginManager.fillBean(bean);
		request.setAttribute("employeeBean", bean);
		request.setAttribute("departments", defaultLogic.findList(Department.class));
		return "department/editemployee";
	}

	@RequestMapping("/control/employee/exist")
	public String exist(@RequestParam("username") String username, HttpServletRequest request) throws Exception {
		request.setAttribute("exist", defaultLogic.findById(SysUser.class, username) != null);
		return "department/usernameIsExsit";
	}

	@RequestMapping("/control/employee/leave")
	@Permission(module = "employee", privilege = "leave")
	public String leave(@RequestParam("username") String username, HttpServletRequest request) throws Exception {
		defaultLogic.deleteById(SysUser.class, username);
		request.setAttribute("message", "成功删除员工信息!");
		request.setAttribute("urladdress", SiteUrl.readUrl("control.employee.list"));
		return "share/message";
	}

	@RequestMapping("/control/employee/list")
	@Permission(module = "employee", privilege = "view")
	public String list(EmployeeBean bean, HttpServletRequest request) throws Exception {
		PageView<SysUser> pageView = new PageView<SysUser>(12, bean.getPage());
		pageView.setQueryResult(loginManager.getEmployeeResult(bean,
				new PageBy(pageView.getCurrentpage(), pageView.getMaxresult())));
		request.setAttribute("pageView", pageView);
		return "department/employeelist";
	}

	@RequestMapping("/control/employee/privilege/setup")
	@Permission(module = "employee", privilege = "privilegeGroupSet")
	public String privilegeGroupSetup(EmployeeBean bean, ModelMap model) throws Exception {
		SysUser employee = defaultLogic.findById(SysUser.class, bean.getUsername());
		this.getDefaultLogic().delete(EmployeePrivilegeGrp.class, new QueryFilter("employeeId", bean.getUsername()));
		if (CheckUtil.isValid(bean.getGroupids())) {
			List<EmployeePrivilegeGrp> grpList = new ArrayList<EmployeePrivilegeGrp>();
			for (String groupid : bean.getGroupids()) {
				EmployeePrivilegeGrp grp = new EmployeePrivilegeGrp();
				grp.setEmployeeId(bean.getUsername());
				grp.setPrivilegeGrpId(groupid);
				grpList.add(grp);
			}
			this.getDefaultLogic().save(grpList);
		}
		loginManager.update(employee);
		model.addAttribute("message", "设置权限组成功");
		model.addAttribute("urladdress", SiteUrl.readUrl("control.employee.list"));
		return "share/message";
	}

	@RequestMapping("/control/employee/privilege/setUI")
	@Permission(module = "employee", privilege = "privilegeGroupSet")
	public String privilegeGroupSetupUI(String username, ModelMap model) throws Exception {
		SysUser employee = defaultLogic.findById(SysUser.class, username);
		List<PrivilegeGroup> grpList = this.defaultLogic.findByIds(
				PrivilegeGroup.class,
				defaultLogic.findIdArray(EmployeePrivilegeGrp.class,
						new QueryFilter("employeeId", employee.getUsername())));
		model.addAttribute("usergroups", grpList);
		model.addAttribute("groups", this.getDefaultLogic().findList(PrivilegeGroup.class));
		return "privilege/setPrivilegeGroup";
	}

	@RequestMapping("/control/employee/query")
	@Permission(module = "employee", privilege = "view")
	public String query(HttpServletRequest request) throws Exception {
		request.setAttribute("departments", defaultLogic.findList(Department.class));
		return "department/query";
	}

	@RequestMapping("/control/employee/reg")
	@Permission(module = "employee", privilege = "insert")
	public String reg(EmployeeBean bean, ModelMap model) throws Exception {
		String errorMsg = BaseBean.checkImageFile(bean.getPicture());
		if (errorMsg != null) {
			model.addAttribute("urladdress", SiteUrl.readUrl("control.employee.addUI"));
			model.addAttribute("message", errorMsg);
			return "share/message";
		}
		String fileName = UploadUtil.writeFileToDisk(bean.getPicture(), application, getPrefixPath(bean.getUsername()));
		bean.setImageName(fileName);
		loginManager.save(bean);
		model.addAttribute("message", "注册新员工成功!");
		model.addAttribute("urladdress", SiteUrl.readUrl("control.employee.list"));
		return "share/message";
	}

	@RequestMapping("/control/employee/regUI")
	@Permission(module = "employee", privilege = "insert")
	public String regUI(EmployeeBean bean, HttpServletRequest request) throws Exception {
		request.setAttribute("departments", defaultLogic.findList(Department.class));
		return "department/addemployee";
	}

	@Override
	public void setServletContext(ServletContext paramServletContext) {
		application = paramServletContext;
	}

	private String getPrefixPath(String username) {
		return "/images/employee/" + username;
	}
}

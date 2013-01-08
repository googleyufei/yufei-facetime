package com.shop.action.privilege;

import com.facetime.core.utils.CheckUtil;
import com.facetime.core.utils.IdGenerator;
import com.facetime.spring.action.Action;
import com.facetime.spring.support.Limitable.PageBy;
import com.facetime.spring.support.QueryFilter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shop.domain.PageView;
import com.shop.domain.privilege.PrivilegeGroup;
import com.shop.domain.privilege.SystemPrivilege;
import com.shop.domain.privilege.SystemPrivilegeGroup;
import com.shop.logic.bean.PrivilegeGroupBean;
import com.shop.util.SiteUrl;

@Controller
public class PrivilegeGroupAction extends Action {

	@RequestMapping("/control/privilege/add")
	@Permission(module = "privilegeGroup", privilege = "insert")
	public String add(PrivilegeGroupBean bean, ModelMap model) throws Exception {
		PrivilegeGroup group = new PrivilegeGroup();
		group.setGroupid(IdGenerator.strId());
		group.setName(bean.getName());
		setGroupPrivileges(group, bean.getPrivilegeIds());
		this.getDefaultLogic().save(group);
		model.addAttribute("message", "成功添加权限组!");
		model.addAttribute("urladdress",
				SiteUrl.readUrl("control.privilegegroup.list"));
		return "share/message";
	}

	@RequestMapping("/control/privilege/addUI")
	@Permission(module = "privilegeGroup", privilege = "insert")
	public String addUI(ModelMap model) throws Exception {
		model.addAttribute("privileges",
				this.getDefaultLogic().findList(SystemPrivilege.class));
		return "privilege/addPrivilegeGroup";
	}

	@RequestMapping("/control/privilege/delete")
	@Permission(module = "privilegeGroup", privilege = "delete")
	public String delete(PrivilegeGroupBean bean, ModelMap model)
			throws Exception {
		PrivilegeGroup group = this.defaultLogic.findById(PrivilegeGroup.class,
				bean.getGroupid());
		defaultLogic.delete(SystemPrivilegeGroup.class, new QueryFilter(
				"groupId", group.getGroupid()));
		defaultLogic.deleteById(PrivilegeGroup.class, bean.getGroupid());
		model.addAttribute("message", "成功删除权限组!");
		model.addAttribute("urladdress",
				SiteUrl.readUrl("control.privilegegroup.list"));
		return "share/message";
	}

	@RequestMapping("/control/privilege/edit")
	@Permission(module = "privilegeGroup", privilege = "update")
	public String edit(PrivilegeGroupBean bean, ModelMap model)
			throws Exception {
		PrivilegeGroup group = find(bean.getGroupid());
		group.setName(bean.getName());
		defaultLogic.delete(SystemPrivilegeGroup.class, new QueryFilter(
				"groupId", group.getGroupid()));
		setGroupPrivileges(group, bean.getPrivilegeIds());
		this.getDefaultLogic().update(group);
		model.addAttribute("message", "成功修改权限组!");
		model.addAttribute("urladdress",
				SiteUrl.readUrl("control.privilegegroup.list"));
		return "share/message";
	}

	@RequestMapping("/control/privilege/editUI")
	@Permission(module = "privilegeGroup", privilege = "update")
	public String editUI(PrivilegeGroupBean bean, ModelMap model)
			throws Exception {
		PrivilegeGroup group = find(bean.getGroupid());
		bean.setName(group.getName());

		List<SystemPrivilege> privilegeList = defaultLogic.findByIds(
				SystemPrivilege.class, defaultLogic.findIdArray(
						SystemPrivilegeGroup.class, new QueryFilter("groupId",
								group.getGroupid())));
		model.addAttribute("groupBean", group);
		model.addAttribute("selectprivileges", privilegeList);
		model.addAttribute("privileges",
				this.getDefaultLogic().findList(SystemPrivilege.class));
		return "privilege/editPrivilegeGroup";
	}

	@RequestMapping("/control/privilege/list")
	@Permission(module = "privilegeGroup", privilege = "view")
	public String list(PrivilegeGroupBean bean, ModelMap model)
			throws Exception {
		PageView<PrivilegeGroup> pageView = new PageView<PrivilegeGroup>(12,
				bean.getPage());
		pageView.setQueryResult(this.getDefaultLogic().findPage(
				PrivilegeGroup.class,
				PageBy.of(pageView.getCurrentpage(), pageView.getMaxresult())));
		model.addAttribute("pageView", pageView);
		return "privilege/listPrivilegeGroup";
	}

	private PrivilegeGroup find(String groupId) {
		return this.defaultLogic.findById(PrivilegeGroup.class, groupId);
	}

	/**
	 * 将各个权限设置到权限组中
	 */
	private void setGroupPrivileges(PrivilegeGroup group, String[] privilegeIds) {
		if (CheckUtil.isValid(privilegeIds)) {
			List<SystemPrivilege> privileges = this.getDefaultLogic()
					.findByIds(SystemPrivilege.class, privilegeIds);
			List<SystemPrivilegeGroup> grpList = new ArrayList<SystemPrivilegeGroup>();
			for (SystemPrivilege privilege : privileges) {
				SystemPrivilegeGroup privilegeGrp = new SystemPrivilegeGroup();
				privilegeGrp.setGroupId(group.getGroupid());
				privilegeGrp.setPrivilegeId(privilege.getId());
				grpList.add(privilegeGrp);
			}
			defaultLogic.save(grpList);
		}
	}
}

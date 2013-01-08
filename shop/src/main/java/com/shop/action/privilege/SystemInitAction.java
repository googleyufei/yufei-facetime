package com.shop.action.privilege;

import com.facetime.mgr.common.HashUtil;
import com.facetime.mgr.domain.IDCard;
import com.facetime.mgr.domain.SysUser;
import com.facetime.spring.action.Action;
import com.facetime.spring.support.QueryFilter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shop.domain.book.GeneratedOrderid;
import com.shop.domain.privilege.Department;
import com.shop.domain.privilege.EmployeePrivilegeGrp;
import com.shop.domain.privilege.PrivilegeGroup;
import com.shop.domain.privilege.SystemPrivilege;
import com.shop.domain.privilege.SystemPrivilegeGroup;
import com.shop.util.SiteUrl;

/**
 * 初始化 (此action是在系统安装完后就执行)
 */
@Controller
public class SystemInitAction extends Action {

	@RequestMapping("/system/init")
	public String execute(HttpServletRequest request) throws Exception {
		initGeneratedOrder();
		initSystemPrivilege();
		initPrivilegeGroup();
		initAdmin();

		request.setAttribute("message", "初始化完成");
		request.setAttribute("urladdress", SiteUrl.readUrl("employee.login.ui"));
		return "share/message";
	}

	/**
	 * 初始化管理员账号
	 */
	private void initAdmin() {
		if (this.getDefaultLogic().count(SysUser.class) > 0) {
			return;
		}

		SysUser user = new SysUser();
		user.setUsername("admin");
		user.setPassword(HashUtil.hash("123456"));
		user.setEmail("googleyufei@qq.com");
		user.setCreatedate(new Date());
		user.setRealname("余飞");
		user.setGender("0");
		IDCard idcard = new IDCard("213213", "北京", new Date());
		defaultLogic.save(idcard);
		user.setCardno(idcard.getCardno());
		user.setDegree("1");

		Department department = new Department();
		department.setName("系统管理部");
		defaultLogic.save(department);
		user.setDepartmentId(department.getId());
		defaultLogic.save(user);

		String[] grpIdArray = this.getDefaultLogic().findIdArray(PrivilegeGroup.class);
		List<EmployeePrivilegeGrp> grpList = new ArrayList<EmployeePrivilegeGrp>();
		for (String id : grpIdArray) {
			EmployeePrivilegeGrp grp = new EmployeePrivilegeGrp();
			grp.setEmployeeId(user.getUsername());
			grp.setPrivilegeGrpId(id);
			grpList.add(grp);
		}
		this.getDefaultLogic().save(grpList);
	}

	private void initGeneratedOrder() {
		long count = this.getDefaultLogic().count(GeneratedOrderid.class, QueryFilter.valueOf("id", "order"));
		if (count == 0) {
			GeneratedOrderid go = new GeneratedOrderid();
			go.setId("order");
			this.getDefaultLogic().save(go);
		}
	}

	/**
	 * 初始化系统权限组
	 */
	private void initPrivilegeGroup() {
		if (defaultLogic.count(PrivilegeGroup.class) == 0) {
			PrivilegeGroup group = new PrivilegeGroup();
			group.setGroupid("admin");
			group.setName("系统权限组");
			this.getDefaultLogic().save(group);

			List<SystemPrivilege> privilegeList = defaultLogic.findList(SystemPrivilege.class);
			List<SystemPrivilegeGroup> grpList = new ArrayList<SystemPrivilegeGroup>();
			for (SystemPrivilege privilege : privilegeList) {
				SystemPrivilegeGroup privilegeGrp = new SystemPrivilegeGroup();
				privilegeGrp.setGroupId(group.getGroupid());
				privilegeGrp.setPrivilegeId(privilege.getId());
				grpList.add(privilegeGrp);
			}
			defaultLogic.save(grpList);
		}
	}

	/**
	 * 初始化系统权限
	 */
	private void initSystemPrivilege() {
		if (defaultLogic.count(SystemPrivilege.class) != 0) {
			return;
		}
		List<SystemPrivilege> privileges = new ArrayList<SystemPrivilege>();
		privileges.add(new SystemPrivilege("department", "view", "部门查看"));
		privileges.add(new SystemPrivilege("department", "insert", "部门添加"));
		privileges.add(new SystemPrivilege("department", "update", "部门修改"));
		privileges.add(new SystemPrivilege("department", "delete", "部门删除"));

		privileges.add(new SystemPrivilege("employee", "view", "员工查看"));
		privileges.add(new SystemPrivilege("employee", "insert", "员工添加"));
		privileges.add(new SystemPrivilege("employee", "update", "员工修改"));
		privileges.add(new SystemPrivilege("employee", "leave", "员工离职设置"));
		privileges.add(new SystemPrivilege("employee", "privilegeGroupSet", "员工权限设置"));

		privileges.add(new SystemPrivilege("privilegeGroup", "view", "权限组查看"));
		privileges.add(new SystemPrivilege("privilegeGroup", "insert", "权限组添加"));
		privileges.add(new SystemPrivilege("privilegeGroup", "update", "权限组修改"));
		privileges.add(new SystemPrivilege("privilegeGroup", "delete", "权限组删除"));

		privileges.add(new SystemPrivilege("order", "view", "订单查看"));
		privileges.add(new SystemPrivilege("order", "turnDelivered", "订单转已发货"));
		privileges.add(new SystemPrivilege("order", "turnReceived", "订单转已收货"));
		privileges.add(new SystemPrivilege("order", "turnWaitdeliver", "订单转等待发货"));
		privileges.add(new SystemPrivilege("order", "confirmOrder", "审核通过订单"));
		privileges.add(new SystemPrivilege("order", "modifyContactInfo", "订单联系信息修改"));
		privileges.add(new SystemPrivilege("order", "allUnLock", "批量解锁订单"));
		privileges.add(new SystemPrivilege("order", "cancelOrder", "取消订单"));
		privileges.add(new SystemPrivilege("order", "confirmPayment", "财务确认订单已付款"));
		privileges.add(new SystemPrivilege("order", "modifyDeliverInfo", "订单配送信息修改"));
		privileges.add(new SystemPrivilege("order", "modifyPaymentWay", "支付方式修改"));
		privileges.add(new SystemPrivilege("order", "modifyDeliverWay", "配送方式修改"));
		privileges.add(new SystemPrivilege("order", "modifyProductAmount", "商品购买数量修改"));
		privileges.add(new SystemPrivilege("order", "modifyDeliverFee", "配送费修改"));
		privileges.add(new SystemPrivilege("order", "deleteOrderItem", "删除订单项"));

		privileges.add(new SystemPrivilege("brand", "view", "品牌查看"));
		privileges.add(new SystemPrivilege("brand", "insert", "品牌添加"));
		privileges.add(new SystemPrivilege("brand", "update", "品牌信息修改"));

		privileges.add(new SystemPrivilege("product", "view", "产品查看"));
		privileges.add(new SystemPrivilege("product", "insert", "产品添加"));
		privileges.add(new SystemPrivilege("product", "update", "产品信息修改"));
		privileges.add(new SystemPrivilege("product", "visible", "产品上/下架"));
		privileges.add(new SystemPrivilege("product", "commend", "产品推荐/不推荐"));

		privileges.add(new SystemPrivilege("productType", "view", "产品类别查看"));
		privileges.add(new SystemPrivilege("productType", "insert", "产品类别添加"));
		privileges.add(new SystemPrivilege("productType", "update", "产品类别修改"));

		privileges.add(new SystemPrivilege("buyer", "view", "网站用户查看"));
		privileges.add(new SystemPrivilege("buyer", "enable", "网站用户启用"));
		privileges.add(new SystemPrivilege("buyer", "delete", "网站用户禁用"));

		this.getDefaultLogic().save(privileges.toArray(new SystemPrivilege[] {}));
	}

}

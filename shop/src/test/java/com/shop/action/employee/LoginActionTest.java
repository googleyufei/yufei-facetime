package com.shop.action.employee;

import static com.facetime.core.conf.SysLogger.shopLogger;

import com.facetime.mgr.bean.UserModel;
import com.facetime.mgr.domain.SysUser;
import com.facetime.mgr.logic.LoginManager;
import com.facetime.spring.support.QueryFilter;
import com.facetime.spring.test.BaseActionTest;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

/**
 * 员工登录测试类
 */
public class LoginActionTest extends BaseActionTest {

	@Test
	public void testLoginNotUser() throws Exception {
		send("/employee/login", QueryFilter.valueOf("username", "admin"), QueryFilter.valueOf("password", "123456"));
		Assert.assertEquals("redirect:/system/init.do", view.getViewName());
		shopLogger.info("test login() success!");
	}

	@Test
	public void testList() throws Exception {
		send("/control/department/list");
		Assert.assertEquals("department/departmentlist", view.getViewName());
	}

	@Test
	public void testLoginCreateUser() throws Exception {
		send("/system/init");

		Assert.assertEquals("share/message", view.getViewName());
		LoginManager loginManager = beanLocator.locate(LoginManager.class);
		List<SysUser> userList = loginManager.findList(SysUser.class);
		Assert.assertEquals(userList.size(), 1);
		Assert.assertEquals(userList.get(0).getUsername(), "admin");
	}

	@Test
	public void testLoginSucc() {
		send("/system/init");
		send("/employee/login", QueryFilter.valueOf("username", "admin"), QueryFilter.valueOf("password", "123456"));

		Assert.assertEquals("redirect:/control/center/main.do", view.getViewName());
		UserModel user = (UserModel) request.getSession().getAttribute(UserModel.LOGIN_USER_KEY);
		Assert.assertNotNull(user);
		Assert.assertEquals("admin", user.getUsername());
	}

	@Test
	public void testsetLeftMenu() {
		send("/employee/login", QueryFilter.valueOf("username", "admin"), QueryFilter.valueOf("password", "123456"));
		send("/setLeftMenu", QueryFilter.valueOf("username", "admin"), QueryFilter.valueOf("password", "123456"));
	}
}

package com.shop.logic.employee;

import com.facetime.mgr.domain.MenuOperate;
import com.facetime.mgr.logic.LoginManager;
import com.facetime.spring.test.BaseLogicTest;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 用户业务逻辑类测试用例
 */
public class LoginManagerTest extends BaseLogicTest {

	@Autowired
	private LoginManager loginManager;
	private static final String USER = "admin";

	@Test
	public void findMneuFuncMap() {
		Map<String, List<MenuOperate>> hp = loginManager.findMenuFuncMap(USER);
	}
}

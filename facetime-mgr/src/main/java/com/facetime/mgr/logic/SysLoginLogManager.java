package com.facetime.mgr.logic;

import com.facetime.mgr.bean.SysLoginLogForm;
import com.facetime.mgr.domain.SysLoginLog;
import com.facetime.spring.logic.Logic;
import com.facetime.spring.support.Page;

import java.util.Date;

public interface SysLoginLogManager extends Logic {

	/**
	 * 从登录日志表获取用户登录时间
	 * @param userid
	 * @return
	 */
	public Date getLonginDate(String userid);

	/**
	 * 从登陆日志表获取退出时间
	 * @param userid
	 * @return
	 */
	public Long getLongOutDate(String userid);

	public Page<SysLoginLog> getPage(SysLoginLogForm loginForm);
}

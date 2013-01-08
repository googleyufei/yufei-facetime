package com.facetime.mgr.logic;

import com.facetime.mgr.bean.OperInfoForm;
import com.facetime.mgr.domain.MenuInfo;
import com.facetime.mgr.domain.SysUser;
import com.facetime.spring.logic.Logic;
import com.facetime.spring.support.Page;

import java.util.List;
import java.util.Map;

public interface OperInfoManager extends Logic {

	public void deleteUser(String[] userids);

	public List<MenuInfo> getAllMenuLeafInfo(String userid);

	public Page<SysUser> getOperInfo1(OperInfoForm form, int pageNum, int pageSize);

	public abstract Map<String, String> getSysUserMap();

	public int modifyPassword(String userId, String oldPwd, String newPwd);

	/**
	 * 密码重置, 默认为111111.
	 */
	public void resetPwd(String[] userids);

	public void savePage(String userid, String url);

	/**
	 * 解锁
	 */
	public void unlockPwd(String[] userids);
}

package com.facetime.mgr.logic.impl;

import com.facetime.core.utils.StringUtils;
import com.facetime.mgr.bean.OperInfoForm;
import com.facetime.mgr.common.HashUtil;
import com.facetime.mgr.domain.MenuInfo;
import com.facetime.mgr.domain.SysUser;
import com.facetime.mgr.domain.UsrUsrGrp;
import com.facetime.mgr.logic.MenuInfoManager;
import com.facetime.mgr.logic.OperInfoManager;
import com.facetime.spring.logic.LogicImpl;
import com.facetime.spring.support.LogicUtils;
import com.facetime.spring.support.PMLO;
import com.facetime.spring.support.Page;
import com.facetime.spring.support.QueryFilter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OperInfoManagerImpl extends LogicImpl implements OperInfoManager {

	@Override
	public void deleteUser(String[] userids) {
		this.delete(UsrUsrGrp.class, new QueryFilter("userid", PMLO.IN, userids));
		this.delete(SysUser.class, new QueryFilter("userid", PMLO.IN, userids));
	}

	@Override
	public List<MenuInfo> getAllMenuLeafInfo(String userid) {
		return this.locate(MenuInfoManager.class).getAllMenuLeafInfo(userid);
	}

	@Override
	public Page<SysUser> getOperInfo1(OperInfoForm form, int pageNum, int pageSize) {
		List<QueryFilter> filters = new ArrayList<QueryFilter>();
		filters.add(new QueryFilter("username", PMLO.IN, "%" + form.getUsername() + "%", StringUtils.isValid(form
				.getUsername())));
		Page<SysUser> page = this.findPage(SysUser.class, filters.toArray(new QueryFilter[] {}),
				LogicUtils.pageby(pageNum, pageSize));
		return page;
	}

	@Override
	public Map<String, String> getSysUserMap() {
		List<SysUser> list = this.findList(SysUser.class);
		Map<String, String> hp = new HashMap<String, String>();
		for (SysUser user : list) {
			hp.put(user.getUsername(), user.getRealname());
		}
		return hp;
	}

	@Override
	public int modifyPassword(String userId, String oldPwd, String newPwd) {
		SysUser user = this.findById(SysUser.class, userId);
		oldPwd = HashUtil.hash(oldPwd);
		newPwd = HashUtil.hash(newPwd);
		if (user.getPassword().equals(oldPwd)) {
			user.setModifydate(new Date());
			user.setPassword(newPwd);
		} else {
			return -1;
		}
		this.update(user);
		return 0;
	}

	@Override
	public void resetPwd(String[] userids) {
		for (String userid : userids) {
			SysUser user = this.findById(SysUser.class, userid);
			user.setPassword(HashUtil.hash("111111"));
			this.update(user);
		}
	}

	@Override
	public void savePage(String userid, String url) {
		SysUser user = this.findById(SysUser.class, userid);
		if (user != null) {
			user.setUrl(url);
			this.update(user);
		}
	}

	@Override
	public void unlockPwd(String[] userids) {
		for (String userid : userids) {
			SysUser user = this.findById(SysUser.class, userid);
			user.setModifydate(new Date());
			user.setValid(true);
			this.update(user);
		}
	}
}

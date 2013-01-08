package com.facetime.mgr.logic.impl;

import com.facetime.mgr.bean.UsrGrpForm;
import com.facetime.mgr.domain.UsrGroup;
import com.facetime.mgr.domain.UsrUsrGrp;
import com.facetime.mgr.logic.UsrGrpManager;
import com.facetime.spring.logic.LogicImpl;
import com.facetime.spring.support.QueryFilter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UsrGrpManagerImpl extends LogicImpl implements UsrGrpManager {

	@Override
	public List<UsrGrpForm> findUsrGrp(String userid, String loginUser) {
		String groupCode = "";
		// 判断设置权限的用户是否已经有权限
		List<UsrUsrGrp> usrGrpLst = this.findList(UsrUsrGrp.class, new QueryFilter("userid", userid));
		// 获取登录用户能够执行的权限
		List<?> groupLst = null;
		if (loginUser.equals("admin"))
			groupLst = this.findList(UsrGroup.class);
		else
			groupLst = this.findList(UsrUsrGrp.class, new QueryFilter("userid", loginUser));
		List<UsrGrpForm> result = new ArrayList<UsrGrpForm>();
		for (int i = 0; i < groupLst.size(); i++) {
			UsrGrpForm form = new UsrGrpForm();
			if (loginUser.equals("admin")) {
				UsrGroup group = (UsrGroup) groupLst.get(i);
				form.setGrpcode(group.getGrpcode());
				form.setGrpname(group.getGrpname());
			} else {
				UsrUsrGrp usrUsrgrp = (UsrUsrGrp) groupLst.get(i);
				groupCode = usrUsrgrp.getGrpcode();
				form.setGrpcode(groupCode);
				// 取得组别名称
				form.setGrpname(this.findById(UsrGroup.class, groupCode).getGrpname());
			}

			// 给form的checked赋值
			if (usrGrpLst.size() > 0)
				for (int j = 0; j < usrGrpLst.size(); j++) {
					UsrUsrGrp usrgrp = usrGrpLst.get(j);
					if (usrgrp.getGrpcode().equals(form.getGrpcode()))
						form.setChecked("checked");
				}
			form.setUserid(userid);
			result.add(form);
		}
		return result;
	}

	@Override
	public void updateGrpRole(String userid, List<UsrUsrGrp> usrGrpLst) {
		removeUsrGrp(userid);
		this.save(usrGrpLst);
	}

	private int removeUsrGrp(String userid) {
		return this.delete(UsrUsrGrp.class, new QueryFilter("userid", userid));
	}
}

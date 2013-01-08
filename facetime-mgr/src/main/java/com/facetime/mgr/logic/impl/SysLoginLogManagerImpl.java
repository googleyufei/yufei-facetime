package com.facetime.mgr.logic.impl;

import com.facetime.core.utils.DateUtil;
import com.facetime.core.utils.StringUtils;
import com.facetime.mgr.bean.SysLoginLogForm;
import com.facetime.mgr.domain.SysLoginLog;
import com.facetime.mgr.logic.SysLoginLogManager;
import com.facetime.spring.logic.LogicImpl;
import com.facetime.spring.support.LogicUtils;
import com.facetime.spring.support.PMLO;
import com.facetime.spring.support.Page;
import com.facetime.spring.support.QueryFilter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SysLoginLogManagerImpl extends LogicImpl implements SysLoginLogManager {

	@Override
	public Date getLonginDate(String userid) {
		Page<SysLoginLog> result = findPage(SysLoginLog.class, new QueryFilter("userid", userid),
				LogicUtils.orderDesc("logintime"), LogicUtils.pageby(1, 1));
		List<SysLoginLog> list = result.getQueryResult();
		return list.size() > 0 ? list.get(0).getLogintime() : null;
	}

	@Override
	public Long getLongOutDate(String userid) {
		Page<SysLoginLog> result = this.findPage(SysLoginLog.class, new QueryFilter("userid", userid),
				LogicUtils.orderDesc("logintime"), LogicUtils.pageby(1, 1));
		List<SysLoginLog> list = result.getQueryResult();
		Long logoutTime = list.size() > 0 ? list.get(0).getLogouttime().getTime() : Long.parseLong("-1800000");
		return logoutTime;
	}

	@Override
	public Page<SysLoginLog> getPage(SysLoginLogForm form) {
		List<QueryFilter> filters = new ArrayList<QueryFilter>();
		filters.add(new QueryFilter("username", PMLO.LIKE, StringUtils.likeOf(form.getUsername()), StringUtils
				.isValid(form.getUsername())));
		filters.add(new QueryFilter("hostip", PMLO.LIKE, StringUtils.likeOf(form.getHostip()), StringUtils.isValid(form
				.getHostip())));
		filters.add(new QueryFilter("logintime", PMLO.GE, DateUtil.str2timestamp(form.getLoginbegintime()), StringUtils
				.isValid(form.getLoginbegintime())));
		filters.add(new QueryFilter("logintime", PMLO.LE, DateUtil.str2timestamp(form.getLoginendtime()), StringUtils
				.isValid(form.getLoginendtime())));
		filters.add(new QueryFilter("logouttime", PMLO.GE, DateUtil.str2timestamp(form.getLogoutbegintime()),
				StringUtils.isValid(form.getLogoutbegintime())));
		filters.add(new QueryFilter("logouttime", PMLO.LE, DateUtil.str2timestamp(form.getLogoutendtime()), StringUtils
				.isValid(form.getLogoutendtime())));
		Page<SysLoginLog> qr = this.findPage(SysLoginLog.class, filters.toArray(new QueryFilter[] {}),
				LogicUtils.orderDesc("logintime"), LogicUtils.pageby(form.getToPage(), form.getPageSize()));
		return qr;
	}
}

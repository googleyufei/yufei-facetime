package com.facetime.mgr.logic.impl;

import com.facetime.core.utils.DateUtil;
import com.facetime.core.utils.StringUtils;
import com.facetime.mgr.bean.SysOperLogForm;
import com.facetime.mgr.domain.SysOperLog;
import com.facetime.mgr.logic.SysOperLogManager;
import com.facetime.spring.logic.LogicImpl;
import com.facetime.spring.support.LogicUtils;
import com.facetime.spring.support.PMLO;
import com.facetime.spring.support.Page;
import com.facetime.spring.support.QueryFilter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SysOperLogManagerImpl extends LogicImpl implements SysOperLogManager {

	@Override
	public int delAllOperLog() {
		return this.deleteAll(SysOperLog.class);
	}

	@Override
	public void deleteItem(String operid) {
		this.deleteById(SysOperLog.class, operid);
	}

	@Override
	public int delOperLog(String[] ids) {
		return this.deleteByIds(SysOperLog.class, ids);
	}

	@Override
	public SysOperLog getFristSysOperLog() {
		Page<SysOperLog> result = this.findPage(SysOperLog.class, LogicUtils.orderDesc("logtime"), LogicUtils.pageby(1, 1));
		List<SysOperLog> list = result.getQueryResult();
		return list.size() > 0 ? list.get(0) : null;
	}

	@Override
	public List<SysOperLog> getListById(String userid) {
		return findList(SysOperLog.class, new QueryFilter("userid", userid));
	}

	@Override
	public Page<SysOperLog> getPage(SysOperLogForm form) {
		List<QueryFilter> filters = new ArrayList<QueryFilter>();
		filters.add(new QueryFilter("note", PMLO.LIKE, StringUtils.likeOf(form.getNote()), StringUtils.isValid(form
				.getNote())));
		filters.add(new QueryFilter("username", PMLO.LIKE, StringUtils.likeOf(form.getUserid()), StringUtils
				.isValid(form.getUserid())));
		filters.add(new QueryFilter("logtime", PMLO.GE, DateUtil.str2timestamp(form.getLogbegintime()), StringUtils
				.isValid(form.getLogbegintime())));
		filters.add(new QueryFilter("logtime", PMLO.LE, DateUtil.str2timestamp(form.getLogendtime()), StringUtils
				.isValid(form.getLogendtime())));
		Page<SysOperLog> result = this.findPage(SysOperLog.class, filters.toArray(new QueryFilter[] {}),
				LogicUtils.orderDesc("logtime"), LogicUtils.pageby(form.getToPage(), form.getPageSize()));
		return result;
	}

	@Override
	public SysOperLog getSysOperLogNear(String userid) {
		Page<SysOperLog> result = this.findPage(SysOperLog.class, new QueryFilter("userid", userid),
				LogicUtils.orderDesc("logtime"), LogicUtils.pageby(1, 1));
		List<SysOperLog> list = result.getQueryResult();
		return list.size() > 0 ? list.get(0) : null;
	}
}

package com.facetime.mgr.logic;

import com.facetime.mgr.bean.SysOperLogForm;
import com.facetime.mgr.domain.SysOperLog;
import com.facetime.spring.logic.Logic;
import com.facetime.spring.support.Page;

import java.util.List;

public interface SysOperLogManager extends Logic {

	/**
	 * 清除所有操作日志
	 */
	public int delAllOperLog();

	public void deleteItem(String operid);

	/**
	 * 清除所选操作日志
	 */
	public int delOperLog(String[] ids);

	/**
	 * 从操作日志表中获取第一条记录操作时间降序排序 就是这个方法导致生产上出现严重的性能问题!
	 */
	public SysOperLog getFristSysOperLog();

	public List<SysOperLog> getListById(String userid);

	public Page<SysOperLog> getPage(SysOperLogForm sysOperLogForm);

	/**
	 * 获取登录用户最近的一条记录
	 * @param userid
	 * @return
	 */
	public SysOperLog getSysOperLogNear(String userid);
}

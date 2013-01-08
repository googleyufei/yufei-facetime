package com.facetime.mgr.logic.impl;

import com.facetime.mgr.domain.MenuFunction;
import com.facetime.mgr.domain.MenuOperate;
import com.facetime.mgr.domain.UsrRoleFunction;
import com.facetime.mgr.logic.MenuOperateManager;
import com.facetime.spring.logic.LogicImpl;
import com.facetime.spring.support.LogicUtils;
import com.facetime.spring.support.PMLO;
import com.facetime.spring.support.Page;
import com.facetime.spring.support.QueryFilter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 菜单功能点信息操作接口实现
 * @author arco
 */
@Service
@Transactional
public class MenuOperateManagerImpl extends LogicImpl implements MenuOperateManager {

	@Override
	public void delAll(String idList) {
		String[] aryIds = StringUtils.split(idList, ",");
		// 先删除MENU_FUNCTION对应的USR_ROLEFUNC
		List<String> funcidList = this.findPart(MenuFunction.class, new QueryFilter("operid", PMLO.IN, aryIds),
				new String[] { "funcid" });
		if (funcidList.size() > 0) {
			this.delete(UsrRoleFunction.class, new QueryFilter("funcid", PMLO.IN, funcidList.toArray()));
			this.deleteByIds(MenuFunction.class, funcidList.toArray(new String[] {}));
		}
		deleteByIds(MenuOperate.class, aryIds);
	}

	@Override
	public Map<String, String> getMenuOperateMap() {
		List<MenuOperate> list = this.findList(MenuOperate.class);
		Map<String, String> map = new HashMap<String, String>();
		for (MenuOperate operlog : list) {
			map.put(operlog.getOperid(), operlog.getOpername());
		}
		return map;
	}

	/** {@inheritDoc} */
	@Override
	public String getMenuOperID() {
		List<MenuOperate> list = this.findList(MenuOperate.class);
		StringBuffer sbfXml = new StringBuffer();
		for (MenuOperate operlog : list) {
			sbfXml.append("<option ");
			sbfXml.append("value='");
			if (operlog.getOperid() != null) {
				sbfXml.append(StringEscapeUtils.escapeXml(operlog.getOperid()));
			}
			sbfXml.append("'>");
			sbfXml.append(operlog.getOpername());
			sbfXml.append("</option>");
		}
		return sbfXml.toString();
	}

	@Override
	public String getNameByID(String operid) {
		MenuOperate oper = this.findById(MenuOperate.class, operid);
		return oper.getOpername();
	}

	/** {@inheritDoc} */
	@Override
	public Map<String, String> getOperMap() {
		Map<String, String> map = new HashMap<String, String>();
		List<MenuOperate> list = this.findList(MenuOperate.class);
		for (MenuOperate oper : list) {
			map.put(oper.getOperid(), oper.getOpername());
		}
		return map;
	}

	@Override
	public Page<MenuOperate> getPage(int pageNum, int pageSize) throws DataAccessException {
		return this.findPage(MenuOperate.class, LogicUtils.pageby(pageNum, pageSize), LogicUtils.orderby("operid"));
	}

}

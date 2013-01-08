package com.facetime.mgr.logic;

import com.facetime.mgr.domain.OrgInfo;
import com.facetime.spring.logic.Logic;

import java.util.List;
import java.util.Map;

public interface OrgInfoManager extends Logic {

	public abstract List<OrgInfo> findByAreaid(String[] areaids);

	String findOrgname(String orgid);

	String findParentid(String orgid);

	/**
	 * 根据机构ID查找其所有父机构简称 如:中国农业银行->省农行
	 */
	String findParentName(String parentid);

	/**
	 * 取出机构ID本身及其下面所有子机构ID 生成格式如：000000001，00000001001，00000001002
	 */
	String getAllChildOrgid(String orgid);

	List<OrgInfo> getAllChildren(String parentid);

	/**
	 * 同一父节点下的孩子节点的级别
	 */
	int getChildLevel(OrgInfo orgInfo);

	/**
	 * 取得父节点的下一级子节点个数 orgid=parentid
	 */
	int getChildNum(String parentid);

	/**
	 * 同一父节点下的孩子节点的最大序号加 1
	 */
	int getChildOrder(OrgInfo orgInfo);

	/**
	 * 取得父节点的下一级子节点个数 parentid=parentid
	 */
	int getCountChild(String parentid);

	/**
	 * 根据用户权限与当前机构的父节点得到满足条件的机构列表
	 */
	List<OrgInfo> getListByHql(String orgid, String parentid);

	List<OrgInfo> getOrgInfoList(String parentid);

	/**
	 * 机构信息保存至HashMap中:key机构ID，value:机构名称
	 */
	Map<String, String> getOrgInfoMap();

	String getOrgSelTree(String orgId);

	String getOrgSelTreeAll(String orgId);

	/**
	 * 取得机构ID 下直属机构ID ，生成格式如：（000000001，00000001001，00000001002）
	 */
	String getUnderChildOrgid(String orgid);

	boolean isOnlyOrgid(OrgInfo orgInfo);

	List<OrgInfo> querybyoneorgInfo(String orgid);

	void updateChildNum(String orgid, int i);
}

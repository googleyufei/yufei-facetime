package com.facetime.mgr.logic;

import com.facetime.mgr.domain.AreaInfo;
import com.facetime.spring.logic.Logic;

import java.util.List;
import java.util.Map;

public interface AreaInfoManager extends Logic {

	/**
	 * 删除区域的同时,删除区域地图信息与标注信息
	 */
	public int delMapByGeog(String[] areaid);

	public abstract String findAreaName(String areaid);

	public String findParentid(String areaid);

	/**
	 * 根据区域ID查找其父机构简称
	 */
	public String findParentName(String parentid);

	/**
	 * 取出区域ID本身及其下面所有子机构ID，生成格式如：（000000001，00000001001，00000001002）
	 */
	public String[] getAllChildGeogid(String geogid);

	public int getChildLevel(AreaInfo geogArea);

	/**
	 * 取得父节点的下一级子节点个数 areaid=parentid
	 */
	public int getChildNum(String parentid);

	/**
	 * 同一父节点下的孩子节点的最大序号加 1
	 */
	public int getChildOrder(AreaInfo geogArea);

	/**
	 * 找出geogid的下一级所有子机构信息
	 */
	public List<AreaInfo> getGeogAreaList(String parentid);

	/**
	 * 区域信息保存至HashMap中:key机构areaid，value:区域名称
	 */
	public abstract Map<String, String> getGeogAreaMap();

	public abstract String getGeogAreaTree(String parentid);

	/**
	 * 根据用户权限与所属区域父节点查找区域列表
	 */
	public List<AreaInfo> getListByHql(String areaid, String parentid);

	// 判断机构下是否存在终端信息
	public boolean isExitingOrg(String[] ids);

	/**
	 * 判断区域ID是否唯一
	 */
	public boolean isOnlyAreaid(String areaid);

	/**
	 * 根据区域ID查询一个对象
	 */
	public List<AreaInfo> querybyoneGeogAreaList(String areaid);

	/**
	 * 更新父节点的子节点数
	 */
	public void updateChildNum(String areaid, int i);
}

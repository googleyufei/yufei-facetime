package com.facetime.mgr.common;

import static com.facetime.core.conf.SysLogger.facetimeLogger;

import com.facetime.mgr.domain.MenuOperate;
import com.facetime.mgr.domain.SysDatadir;
import com.facetime.mgr.logic.DataDirManager;
import com.facetime.mgr.logic.MenuInfoManager;
import com.facetime.mgr.logic.MenuOperateManager;
import com.facetime.spring.support.SpringContextUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 提供常用代码对照MAP对象 交易类型 交易应答码 币种数据代码 卡种类型 终端状态 开机率计算参数 提供常用代码变量，约定当作常量使用，除本类之外不能修改。 有：交易成功响应码
 * 以后如果需要其他的代码对照MAP对象，参照原来的做法增加
 */
public class BusnDataDir {
	/** 项目间分隔符 */
	public static final String ITEM_SEPARATOR = ".";
	/** 非数据字典的缓存资源路径前缀 */
	public static final String MAP_PREFIX = "BusnDataDir.";
	/** 根目录路径 */
	public static final String ROOT = "\\";
	public static final String TOP_ID = "999";
	/** 数据字典最顶层项目的父ID */
	public static final String TOP_PARENT_ID = "0";
	/** 本行机构最顶层ID */
	public static final String TOP_PARENTID = "0";

	/** 数据字典专用缓存 */
	private static Map<String, Map<String, String>> dataDicMap;
	private static DataDirManager dataDirManager;
	/** 用于保存系统专用的缓存map对象 */
	private static Map<String, Map<String, ?>> mapMgr;
	/** 菜单功能操作menuid所对应的用户ID所具有的操作ID */
	public static Map<String, Map<String, List<MenuOperate>>> MENU_OPERID_MAP;

	public static final String getChildValue(String path, String key) {
		Map<String, String> foundMap = getChildValueMap(path);
		if (foundMap.containsKey(key)) {
			return foundMap.get(key);
		}
		return null;
	}

	/**
	 * 根据路径返回value note对照表 , 以value为Map的key, note为Map的值
	 * @param path
	 * @return
	 */
	public static final Map<String, String> getChildValueMap(String path) {
		if (dataDicMap.containsKey(path)) {
			return dataDicMap.get(path);
		}
		Map<String, String> foundMap = dataDirManager.getChildValueMap(path);
		dataDicMap.put(path, foundMap);
		return foundMap;
	}

	public static final Map<String, String> getKeyValueMap(String key) {
		return dataDirManager.getKeyValueMap(key);
	}

	/**
	 * 返回非数据字典的缓存数据Map
	 * @param path
	 * @return
	 */
	public final static Map<String, ?> getMap(String path) {
		if (mapMgr.containsKey(path)) {
			return mapMgr.get(path);
		}
		return null;
	}

	/**
	 * 返回非数据字典的缓存数据的值
	 * @param path 保存在缓存中的数据PATH
	 * @param key 该数据的KEY
	 * @return
	 */
	public final static Object getMapValue(String path, String key) {
		if (!path.startsWith(MAP_PREFIX)) {
			throw new AssertionError("this method can only find path of start with:" + MAP_PREFIX);
		}
		Map<String, ?> map = getMap(path);
		if (map == null) {
			throw new AssertionError("there is no map for path:" + path);
		}
		return map.get(key);
	}

	/**
	 * 返回没有子节点的数据字典的值
	 * @param path
	 * @return
	 */
	public static final String getValueByPath(String path) {
		Map<String, String> foundMap = getChildValueMap(path);
		if (foundMap == null) {
			return null;
		}
		Collection<String> list = foundMap.values();
		for (String value : list) {
			return value;
		}
		return null;
	}

	public static void init() {
		dataDirManager = SpringContextUtils.locate(DataDirManager.class);

		dataDicMap = new HashMap<String, Map<String, String>>();
		mapMgr = new HashMap<String, Map<String, ?>>(100);
		MENU_OPERID_MAP = new HashMap<String, Map<String, List<MenuOperate>>>();

		MenuOperateManager manager = SpringContextUtils.locate(MenuOperateManager.class);
		mapMgr.put("BusnDataDir.operMap", manager.getMenuOperateMap());

		MenuInfoManager menuInfoManager = SpringContextUtils.locate(MenuInfoManager.class);
		mapMgr.put("BusnDataDir.menuIdMap", menuInfoManager.getMenuIdRelation());

		facetimeLogger.info("BusnDataDir init OK !");
	}

	/**
	 * 将非数据字典的资源放到BusnDataDir中管理
	 * @param path
	 * @param map
	 */
	public static final void putMap(String path, Map<String, ?> map) {
		Map<String, ?> foundMap = getMap(path);
		if (foundMap != null) {
			return;
		}
		mapMgr.put(path, map);
	}

	/**
	 * 将非数据字典的数据设置到缓存中
	 * @param path
	 * @param key
	 * @param value
	 */
	public final static void putMapValue(String path, String key, Object value) {
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) getMap(path);
		if (map == null) {
			throw new AssertionError("there is no map for path:" + path);
		}
		map.put(key, value);
	}

	public static final List<SysDatadir> queryChildren(String path) {
		List<SysDatadir> datadirlist = new ArrayList<SysDatadir>();
		SysDatadir dataDir = dataDirManager.findByPath(path);
		datadirlist = dataDirManager.queryChildList(dataDir.getId());
		return datadirlist;
	}

	public static void refresh() {
		mapMgr.clear();
		dataDicMap.clear();
	}
}

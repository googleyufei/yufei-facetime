package com.facetime.core.utils;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * 参数值包装类，有标识使用map存放具体值，key值忽略大小写
 * 普通的ParamPack的PackName如果没有在构造的时候赋值，那么就会自动得到一个随机字符串作为名字
 * <ul>
 * <li>如果以'PV_'起头表示是参数(ParamValue)
 * <li>如果以'VV_'起头表示是变量(VariableValue)
 * <li>如果以'OA_'表示以对象数组为基础创建,此时autoArray为true,同时ParamPack为Immutable的,不能再继续写值
 * </ul>
 * 这些都是SQL数据引擎的保留字，自命名要注意规避绕行
 * @author dzb
 *
 */
@SuppressWarnings("serial")
public class ParamPack implements Serializable {

	public static final ParamPack NONE = new ParamPack("NONE");

	private final String name;
	private final Map<String, Object> params;
	private boolean autoArray = false;

	/**
	 * @param name
	 *      用来标识这个pack
	 */
	public ParamPack(String name) {
		this.name = name;
		params = new LinkedHashMap<String, Object>(12);
	}

	public ParamPack(String name, int capality) {
		this.name = name;
		params = new LinkedHashMap<String, Object>(capality);
	}

	/**
	 * 复制本一个与本Pack相同的实例
	 * @return
	 */
	public ParamPack duplicate() {
		ParamPack pack = new ParamPack(name);
		pack.putAll(params);
		return pack;
	}

	public Object get(String key) {
		return params.get(key);
	}

	public String getAsString(String key) {
		Object o = get(key);
		return o == null ? null : String.valueOf(o);
	}

	public String getPackName() {
		return name;
	}

	public boolean isAutoArray() {
		return autoArray;
	}

	public Set<String> keys() {
		return params.keySet();
	}

	public String[] names() {
		return params.keySet().toArray(new String[size()]);
	}

	public ParamPack putAll(Map<String, Object> map) {
		if (autoArray)
			throw LE.makeThrow(
					"ParamPack[%s] is a immutable auto-object-array. Can not set value to a immutable ParamPack instance.",
					name);
		params.putAll(map);
		return this;
	}

	public ParamPack putAll(ParamPack pack) {
		if (autoArray)
			throw LE.makeThrow(
					"ParamPack[%s] is a immutable auto-object-array. Can not set value to a immutable ParamPack instance.",
					name);
		params.putAll(pack.params);
		return this;
	}

	/**
	 * 返回pack内部的Map引用，修改这个引用的值会引起pack内部值的变化
	 * 在不改变这个值的前提下使用这个方法较toMap()速度快
	  * @return
	 */
	public Map<String, Object> ref() {
		return params;
	}

	public ParamPack remove(String key) {
		if (autoArray)
			throw LE.makeThrow(
					"ParamPack[%s] is a immutable auto-object-array. Can not set value to a immutable ParamPack instance.",
					name);
		params.remove(key);
		return this;
	}

	public ParamPack set(String key, Object value) {
		if (autoArray)
			throw LE.makeThrow(
					"ParamPack[%s] is a immutable auto-object-array. Can not set value to a immutable ParamPack instance.",
					name);
		params.put(key, value);
		return this;
	}

	public int size() {
		return params.size();
	}

	/**
	 * 将pack转换成Map使用
	 *
	 * @return
	 */
	public Map<String, Object> toMap() {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.putAll(params);
		return map;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(params.size() * 20);
		sb.append("ParamPack[").append(name).append("]\n{ ");
		for (String name : params.keySet())
			sb.append(name).append("=").append(params.get(name)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	public Object[] values() {
		return params.values().toArray(new Object[size()]);
	}

	public static void main(String[] args) {
		System.out.println(LE.array().length);

		ParamPack pp = ParamPack.pack().set("1", "1");
		ParamPack p = ParamPack.of(pp);
		System.out.println(p);
	}

	public static ParamPack of(Object... params) {
		if (params == null || params.length == 0)
			return ParamPack.NONE;
		if (params[0] instanceof ParamPack)
			return (ParamPack) params[0];
		ParamPack pack = ParamPack.pack("OV_" + RandomGenerator.getRandomStr(4), params.length); //[__] is a magic name, DO *NOT* use this name apparently. 
		for (int i = 0; i < params.length; i++)
			pack.set(String.valueOf(i), params[i]);
		pack.autoArray = true;
		return pack;
	}

	public static ParamPack pack() {
		ParamPack pp = new ParamPack(RandomGenerator.getRandomStr(6));
		return pp;
	}

	public static ParamPack pack(int capality) {
		ParamPack pp = new ParamPack(RandomGenerator.getRandomStr(6), capality);
		return pp;
	}

	public static ParamPack pack(String name, int capacity) {
		ParamPack pp = new ParamPack(name, capacity);
		return pp;
	}

}

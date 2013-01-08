package com.facetime.spring.support;

import java.util.ArrayList;
import java.util.List;

public class QueryFilter {

	private PMLO operate;
	private String property;
	private Object value;
	/** 是否为TRUE标志 */
	private boolean valid = true;

	public static final List<QueryFilter> list() {
		return new ArrayList<QueryFilter>(5);
	}

	public QueryFilter(String property, Object value) {
		this(property, PMLO.EQ, value, true);
	}

	public QueryFilter(String property, Object value, boolean valid) {
		this(property, PMLO.EQ, value, valid);
	}

	public QueryFilter(String property, PMLO operate) {
		this(property, operate, null, true);
	}

	/**
	 * 构造子
	 * @param property
	 * @param operate
	 * @param valid 条件是否为<tt>true</tt>标志, 但它为<tt>true</tt>时, 该QueryFilter才起作用
	 */
	public QueryFilter(String property, PMLO operate, boolean valid) {
		this(property, operate, null, valid);
	}

	public QueryFilter(String property, PMLO operate, Object value) {
		this(property, operate, value, true);
	}

	public QueryFilter(String property, PMLO operate, Object value, boolean valid) {
		super();
		this.property = property;
		this.operate = operate;
		this.value = value;
		this.valid = valid;
	}

	public static final QueryFilter valueOf(String property, Object value) {
		return new QueryFilter(property, PMLO.EQ, value, true);
	}

	public static final QueryFilter valueOf(String property, Object value, boolean valid) {
		return new QueryFilter(property, PMLO.EQ, value, valid);
	}

	public static final QueryFilter valueOf(String property, PMLO operate) {
		return new QueryFilter(property, operate);
	}

	public static final QueryFilter valueOf(String property, PMLO operate, Object value) {
		return new QueryFilter(property, operate, value, true);
	}

	/**
	 * 静态构造子
	 * @param property
	 * @param operate
	 * @param valid 条件是否为<tt>true</tt>标志, 但它为<tt>true</tt>时, 该QueryFilter才起作用
	 */
	public static final QueryFilter valueOf(String property, PMLO operate, Object value, boolean valid) {
		return new QueryFilter(property, operate, value, valid);
	}

	public void add(String property, Object value) {
		this.add(property, PMLO.EQ, value);
	}

	public void add(String property, PMLO operate) {
		this.add(property, operate, null);
	}

	public PMLO getOperate() {
		return operate;
	}

	public String getProperty() {
		return property;
	}

	public String getTranslateOperate() {
		if (operate == null) {
			throw new InternalError("operate in QueryFilter must be not null.");
		}
		if (operate == PMLO.IN) {
			Object[] values = null;
			if (value instanceof List) {
				values = ((List<?>) value).toArray();
			} else {
				values = (Object[]) value;
			}
			if (values.length == 1) {
				return this.translate(PMLO.EQ);
			}
			return this.translate(operate, (Object[]) value);
		}
		return this.translate(operate);
	}

	public Object getValue() {
		return value;
	}

	public boolean isValid() {
		return valid;
	}

	private void add(String property, PMLO operate, Object value) {
		this.property = property;
		this.operate = operate;
		this.value = value;
	}

	private String translate(PMLO operate) {
		StringBuilder result = new StringBuilder(" ");
		if (operate == null) {
			throw new IllegalArgumentException("operate PMLO cann't be null.");
		}
		result.append(operate.getOperate());
		result.append(operate.isSingle() ? " " : " ? ");
		return result.toString();
	}

	/**
	 * 构建IN关系的查询
	 * @param inOperate
	 * @param propertyValues
	 * @return
	 */
	@SuppressWarnings("unused")
	private String translate(PMLO inOperate, Object[] propertyValues) {
		StringBuilder result = new StringBuilder(" ");
		if (inOperate == null) {
			throw new IllegalArgumentException("operate PMLO cann't be null.");
		}
		result.append(inOperate.getOperate()).append(" ( ");
		for (Object propertyValue : propertyValues) {
			result.append("?").append(",");
		}
		result.replace(result.length() - 1, result.length(), ") ");
		return result.toString();
	}

}

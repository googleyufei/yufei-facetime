package com.facetime.spring.support;

import java.util.ArrayList;
import java.util.List;

import com.facetime.spring.support.Limitable.Order;
import com.facetime.spring.support.Limitable.OrderBy;
import com.facetime.spring.support.Limitable.PageBy;

/**
 * 
 *
 * @author yufei
 * @Create_by 2012-12-3
 * @Design_by eclipse  
 */
public abstract class LogicUtils {

	public static final QueryFilter filterby(String property, Object value) {
		return QueryFilter.valueOf(property, value);
	}

	public static final QueryFilter filterby(String property, Object value, boolean valid) {
		return QueryFilter.valueOf(property, value, valid);
	}

	public static final QueryFilter filterby(String property, PMLO pmlo) {
		return QueryFilter.valueOf(property, pmlo);
	}

	public static final QueryFilter filterby(String property, PMLO pmlo, Object value) {
		return QueryFilter.valueOf(property, pmlo, value);
	}

	public static final QueryFilter filterby(String property, PMLO pmlo, Object value, boolean valid) {
		return QueryFilter.valueOf(property, pmlo, value, valid);
	}

	public static final List<QueryFilter> filterList() {
		return new ArrayList<QueryFilter>(5);
	}

	public static final OrderBy orderAsc(String attrName) {
		return OrderBy.asc(attrName);
	}

	public static final OrderBy orderby(String attrName) {
		return OrderBy.asc(attrName);
	}

	public static final OrderBy orderby(String attrName, Order orderType) {
		return new OrderBy(attrName, orderType);
	}

	public static final OrderBy orderDesc(String attrName) {
		return OrderBy.of(attrName, Order.DESC);
	}

	public static final PageBy pageby(int pageNum, int pageSize) {
		return new PageBy(pageNum, pageSize);
	}

	public static final QueryFilter[] toArray(List<QueryFilter> filters) {
		return filters.toArray(new QueryFilter[] {});
	}

}

package com.facetime.spring.logic;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.facetime.core.bean.BusinessObject;
import com.facetime.spring.support.Limitable;
import com.facetime.spring.support.Limitable.PageBy;
import com.facetime.spring.support.Page;
import com.facetime.spring.support.QueryFilter;

/**
 * 
 * 逻辑接口的空实现
 * @author yufei
 * @Create_by 2012-12-7
 * @Design_by eclipse  
 */
public class EmptyLogic implements Logic {

	@Override
	public <T extends BusinessObject> long count(Class<T> entityClass) {

		return 0;
	}

	@Override
	public <T extends BusinessObject> long count(Class<T> entityClass, QueryFilter filter) {

		return 0;
	}

	@Override
	public <T extends BusinessObject> long count(Class<T> entityClass, QueryFilter[] filters) {

		return 0;
	}

	@Override
	public <T extends BusinessObject> long count(String hql, Object[] values) {

		return 0;
	}

	@Override
	public <T extends BusinessObject> int delete(Class<T> entityClass, QueryFilter... filters) {

		return 0;
	}

	@Override
	public <T extends BusinessObject> int delete(Collection<T> entityList) {

		return 0;
	}

	@Override
	public <T extends BusinessObject> int delete(T entity) {

		return 0;
	}

	@Override
	public <T extends BusinessObject> int deleteAll(Class<T> entityClass) {

		return 0;
	}

	@Override
	public <T extends BusinessObject> int deleteById(Class<T> entityClass, Serializable id) {

		return 0;
	}

	@Override
	public <T extends BusinessObject> int deleteByIds(Class<T> entityClass, Serializable[] ids) {

		return 0;
	}

	@Override
	public <T extends BusinessObject> T findById(Class<T> entityClass, Serializable id) {

		return null;
	}

	@Override
	public <T extends BusinessObject> List<T> findByIds(Class<T> entityClass, Serializable[] ids) {

		return null;
	}

	@Override
	public <E> List<E> findHQL(String hql) {

		return null;
	}

	@Override
	public <E> List<E> findHQL(String hql, Object[] values) {

		return null;
	}

	@Override
	public <E> List<E> findHQL(String hql, Object[] values, PageBy pageby) {

		return null;
	}

	@Override
	public String[] findIdArray(Class<? extends BusinessObject> entityClass, QueryFilter... filters) {

		return null;
	}

	@Override
	public <T extends BusinessObject> List<T> findList(Class<T> entityClass, Limitable... limitbys) {

		return null;
	}

	@Override
	public <T extends BusinessObject> List<T> findList(Class<T> entityClass, QueryFilter filter, Limitable... limitbys) {

		return null;
	}

	@Override
	public <T extends BusinessObject> List<T> findList(Class<T> entityClass, QueryFilter[] filters,
			Limitable... limitbys) {

		return null;
	}

	@Override
	public <T extends BusinessObject> Page<T> findPage(Class<T> entityClass, Limitable... limitbys) {

		return null;
	}

	@Override
	public <T extends BusinessObject> Page<T> findPage(Class<T> entityClass, QueryFilter filter, Limitable... limitbys) {

		return null;
	}

	@Override
	public <T extends BusinessObject> Page<T> findPage(Class<T> entityClass, QueryFilter[] filters,
			Limitable... limitbys) {

		return null;
	}

	@Override
	public <T extends BusinessObject> Page<T> findPage(String hql, Object[] values, PageBy pageby) {

		return null;
	}

	@Override
	public <T extends BusinessObject, E> List<E> findPart(Class<T> entityClass, QueryFilter filter, String[] attrNames,
			Limitable... limitbys) {

		return null;
	}

	@Override
	public <T extends BusinessObject, E> List<E> findPart(Class<T> entityClass, QueryFilter[] filters,
			String[] attrNames, Limitable... limitbys) {

		return null;
	}

	@Override
	public <T extends BusinessObject, E> List<E> findPart(Class<T> entityClass, String[] attrNames,
			Limitable... limitbys) {

		return null;
	}

	@Override
	public <T extends BusinessObject> T findUnique(Class<T> entityClass, QueryFilter... filters) {

		return null;
	}

	@Override
	public String getIdentifierName(Class<? extends BusinessObject> entityClass) {

		return null;
	}

	@Override
	public <T extends BusinessObject> void save(Collection<T> entityList) {

	}

	@Override
	public <T extends BusinessObject> void save(T... entity) {

	}

	@Override
	public <T extends BusinessObject> void update(Class<T> entityClass, QueryFilter[] filters, String[] attrNames,
			Object[] attrValues) {

	}

	@Override
	public <T extends BusinessObject> void update(Collection<T> entityList) {

	}

	@Override
	public <T extends BusinessObject> void update(T[] entity) {

	}

	@Override
	public <T extends BusinessObject> void update(T entity, String... attrNames) {

	}

	@Override
	public <T extends BusinessObject> void update(String hql, Object... values) {

	}

	@Override
	public <T extends BusinessObject> void delete(String hql, Object... values) {
	}

}

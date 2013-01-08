package com.facetime.spring.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.facetime.core.bean.BusinessObject;
import com.facetime.spring.support.Limitable;
import com.facetime.spring.support.Page;
import com.facetime.spring.support.QueryFilter;
import com.facetime.spring.support.Limitable.PageBy;

/**
 * 数据访问层的ROOT接口, 方法依照增删改查的顺序排列, 参数采用深度遍历法
 *
 * @author YUFEI
 */
public abstract interface Dao {

	/**
	 * 统计指定类型实体数量
	 *
	 * @param entityClass
	 *            实体类型
	 */
	<T extends BusinessObject> long count(Class<T> entityClass);

	/**
	 * 统计执行类型和条件的实体属性
	 *
	 * @param entityClass
	 * @param filter
	 */
	<T extends BusinessObject> long count(Class<T> entityClass, QueryFilter filter);

	/**
	 * 统计执行类型和条件的实体属性
	 *
	 * @param entityClass
	 * @param filters
	 */
	<T extends BusinessObject> long count(Class<T> entityClass, QueryFilter[] filters);

	/**
	 * 统计执行类型和条件的实体属性
	 *
	 * @param entityClass
	 * @param filters
	 */
	<T extends BusinessObject> long count(String hql, Object[] values);

	/**
	 * 删除指定类型和条件集合的实体对象
	 *
	 * @param entityClass
	 * @param filters
	 */
	<T extends BusinessObject> int delete(Class<T> entityClass, QueryFilter... filters);

	/**
	 * 删除实体对象集合
	 *
	 * @param entityList
	 */
	<T extends BusinessObject> int delete(Collection<T> entityList);

	/**
	 * 删除实体对象
	 *
	 * @param entity
	 */
	<T extends BusinessObject> int delete(T entity);

	/**
	 * 删除实体对象
	 *
	 * @param entity
	 */
	<T extends BusinessObject> int deleteAll(Class<T> entityClass);

	/**
	 * 删除指定实体类型和ID集合的对象
	 *
	 * @param entityClass
	 * @param ids
	 */
	<T extends BusinessObject> int deleteById(Class<T> entityClass, Serializable id);

	/**
	 * 删除指定实体类型和ID集合的对象
	 *
	 * @param entityClass
	 * @param ids
	 */
	<T extends BusinessObject> int deleteByIds(Class<T> entityClass, Serializable[] ids);

	/**
	 * 查询指定类型和ID的实体对象
	 *
	 * @param entityClass
	 *            实体类型
	 * @param id
	 *            ID
	 */
	<T extends BusinessObject> T findById(Class<T> entityClass, Serializable id);

	/**
	 * 查询指定类型和ID的实体对象集合
	 *
	 * @param entityClass
	 *            实体类型
	 * @param id
	 *            ID
	 */
	<T extends BusinessObject> List<T> findByIds(Class<T> entityClass, Serializable[] ids);

	/**
	 * 执行执行的不带参数的HQL查询
	 *
	 * @param <E>
	 * @param hql
	 *            指定的不带参数HQL
	 */
	<E> List<E> findHQL(String hql);

	/**
	 * 执行指定的带参数的HQL查询
	 *
	 * @param <E>
	 * @param hql
	 *            指定的带参数HQL
	 * @param values
	 *            参数值数组
	 */
	<E> List<E> findHQL(String hql, Object[] values);

	/**
	 * 执行指定的带参数,要分页的HQL查询
	 *
	 * @param <E>
	 * @param hql
	 *            指定的带参数HQL
	 * @param values
	 *            参数值数据
	 * @param pageby
	 *            分页
	 */
	<E> List<E> findHQL(String hql, Object[] values, PageBy pageby);

	/**
	 * 返回指定条件的实体ID的数组, 注意Hibernate默认返回的是String数组
	 */
	String[] findIdArray(Class<? extends BusinessObject> entityClass, QueryFilter... filters);

	/**
	 * 查询指定类型, 并对结果集限制的全部实体集合
	 *
	 * @param entityClass
	 * @param limitbys
	 */
	<T extends BusinessObject> List<T> findList(Class<T> entityClass, Limitable... limitbys);

	/**
	 * 查询指定类型和条件并对结果集进行限制的实体集合
	 *
	 * @param entityClass
	 * @param filter
	 * @param limitbys
	 */
	<T extends BusinessObject> List<T> findList(Class<T> entityClass, QueryFilter filter, Limitable... limitbys);

	/**
	 * 查询指定类型和条件, 并对结果集限制的实体集合
	 */
	<T extends BusinessObject> List<T> findList(Class<T> entityClass, QueryFilter[] filters, Limitable... limitbys);

	/**
	 * 查询分页显示结果
	 */
	<T extends BusinessObject> Page<T> findPage(Class<T> entityClass, Limitable... limitbys);

	<T extends BusinessObject> Page<T> findPage(Class<T> entityClass, QueryFilter filter, Limitable... limitbys);

	<T extends BusinessObject> Page<T> findPage(Class<T> entityClass, QueryFilter[] filters, Limitable... limitbys);

	<T extends BusinessObject> Page<T> findPage(String hql, Object[] values, PageBy pageby);

	/**
	 * 查询指定类型/条件/限制的实体, 返回指定的属性名的属性值集合
	 *
	 * @param entityClass
	 *            实体类型
	 * @param filter
	 *            条件
	 * @param attrNames
	 *            属性名数组
	 * @param limitbys
	 *            限制
	 */
	<T extends BusinessObject, E> List<E> findPart(Class<T> entityClass, QueryFilter filter, String[] attrNames,
			Limitable... limitbys);

	/**
	 * 查询指定类型/条件/限制的实体, 返回指定的属性名的属性值集合
	 */
	<T extends BusinessObject, E> List<E> findPart(Class<T> entityClass, QueryFilter[] filters, String[] attrNames,
			Limitable... limitbys);

	/**
	 * 查询指定类型/限制的实体, 返回指定的属性名的属性值集合
	 */
	<T extends BusinessObject, E> List<E> findPart(Class<T> entityClass, String[] attrNames, Limitable... limitbys);

	/**
	 * 查询指定类型和条件, 并对结果集限制的实体集合
	 *
	 * @param entityClass
	 * @param filters
	 * @param limitbys
	 */
	<T extends BusinessObject> T findUnique(Class<T> entityClass, QueryFilter... filters);

	/**
	 * 获取指定类型的实体ID属性名
	 *
	 * @param entityClass
	 *            实体类型
	 */
	String getIdentifierName(Class<? extends BusinessObject> entityClass);

	/**
	 * 保存实体对象集合
	 */
	<T extends BusinessObject> void save(Collection<T> entityList);

	/**
	 * 保存实体对象
	 */
	<T extends BusinessObject> void save(T... entity);

	/**
	 * 更新指定类型和条件数组的实体的某些属性的值
	 */
	@Deprecated
	<T extends BusinessObject> void update(Class<T> entityClass, QueryFilter[] filters, String[] attrNames,
			Object[] attrValues);

	/**
	 * 更新指定实体对象集合
	 */
	<T extends BusinessObject> void update(Collection<T> entityList);

	/**
	 * 更新实体对象
	 */
	<T extends BusinessObject> void update(T[] entity);

	/**
	 * 更新指定实体对象的某些属性
	 */
	<T extends BusinessObject> void update(T entity, String... attrNames);

	<T extends BusinessObject> void update(String hql, Object... values);

	<T extends BusinessObject> void delete(String hql, Object... values);
}

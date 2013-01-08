package com.facetime.spring.dao;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.Assert;

import com.facetime.core.bean.BusinessObject;
import com.facetime.core.utils.CheckUtil;
import com.facetime.core.utils.StringUtils;
import com.facetime.spring.support.Limitable;
import com.facetime.spring.support.PMLO;
import com.facetime.spring.support.Page;
import com.facetime.spring.support.QueryFilter;
import com.facetime.spring.support.Limitable.OrderBy;
import com.facetime.spring.support.Limitable.PageBy;

/**
 * 数据访问层的默认实现类
 * @author YUFEI
 */
public class DaoImpl extends AnnotationHibernateDaoSupport implements Dao {

	private static int computedPageCount(int recordCount, int pageSize) {
		int div = recordCount / pageSize;
		int mod = recordCount % pageSize;
		int pageCount = mod != 0 ? div + 1 : div;
		return pageCount;
	}

	@Override
	public <T extends BusinessObject> long count(Class<T> entityClass) {
		return this.executeCount(entityClass, null);
	}

	@Override
	public <T extends BusinessObject> long count(Class<T> entityClass, QueryFilter filter) {
		return this.executeCount(entityClass, new QueryFilter[] { filter });
	}

	@Override
	public <T extends BusinessObject> long count(Class<T> entityClass, QueryFilter[] filters) {
		return this.executeCount(entityClass, filters);
	}

	@Override
	public <T extends BusinessObject> long count(String hql, Object[] values) {
		List<Object> total = this.findHQL(HQLBuilder.getCountQueryString(hql), values);
		return total.get(0) instanceof Long ? ((Long) total.get(0)).intValue() : (Integer) total.get(0);
	}

	@Override
	public <T extends BusinessObject> int delete(Class<T> entityClass, QueryFilter... filters) {
		Assert.notNull(entityClass);
		Assert.notNull(filters);
		return executeDelete(entityClass, filters);
	}

	@Override
	public <T extends BusinessObject> int delete(Collection<T> entityList) {
		getHibernateTemplate().execute(this.batchOperate(entityList, "delete"));
		return entityList.size();
	}

	@Override
	public <T extends BusinessObject> int delete(T entity) {
		getHibernateTemplate().delete(entity);
		return 1;
	}

	@Override
	public <T extends BusinessObject> int deleteAll(Class<T> entityClass) {
		return executeDelete(entityClass, new QueryFilter[] {});
	}

	@Override
	public <T extends BusinessObject> int deleteById(Class<T> entityClass, Serializable id) {
		if (id instanceof Serializable[]) {
			throw new AssertionError("the id value can't be array.");
		}
		return executeDelete(entityClass, new QueryFilter[] { new QueryFilter(getIdentifierName(entityClass), id) });
	}

	@Override
	public <T extends BusinessObject> int deleteByIds(Class<T> entityClass, Serializable[] ids) {
		return executeDelete(entityClass, new QueryFilter[] { new QueryFilter(getIdentifierName(entityClass), PMLO.IN,
				ids) });
	}

	@Override
	public <T extends BusinessObject> void delete(String hql, Object... values) {
		this.getHibernateTemplate().bulkUpdate(hql, values);
	}

	@Override
	public <T extends BusinessObject> T findById(Class<T> entityClass, Serializable id) {
		List<T> result = this.executeFind(entityClass, new QueryFilter[] { new QueryFilter(
				getIdentifierName(entityClass), id) });
		return result != null && result.size() == 1 ? result.get(0) : null;
	}

	@Override
	public <T extends BusinessObject> List<T> findByIds(Class<T> entityClass, Serializable[] ids) {
		List<T> result = this.executeFind(entityClass, new QueryFilter[] { new QueryFilter(
				getIdentifierName(entityClass), PMLO.IN, ids) });
		return result;
	}

	@Override
	public <E> List<E> findHQL(String hql) {
		return this.executeFindHQL(hql, null, null);
	}

	@Override
	public <E> List<E> findHQL(String hql, Object[] values) {
		return this.executeFindHQL(hql, values, null);
	}

	@Override
	public <E> List<E> findHQL(String hql, Object[] values, PageBy pageby) {
		return this.executeFindHQL(hql, values, pageby);
	}

	@Override
	public String[] findIdArray(Class<? extends BusinessObject> entityClass, QueryFilter... filters) {
		List<? extends BusinessObject> entityList = this.executeFind(entityClass, filters, new Limitable[] {});
		List<String> idList = new ArrayList<String>();
		for (BusinessObject entity : entityList) {
			try {
				idList.add(BeanUtils.getProperty(entity, getIdentifierName(entityClass)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return idList.toArray(new String[] {});
	}

	@Override
	public <T extends BusinessObject> List<T> findList(Class<T> entityClass, Limitable... limitbys) {
		return this.executeFind(entityClass, null, limitbys);
	}

	@Override
	public <T extends BusinessObject> List<T> findList(Class<T> entityClass, QueryFilter filter, Limitable... limitbys) {
		return this.executeFind(entityClass, new QueryFilter[] { filter }, limitbys);
	}

	@Override
	public <T extends BusinessObject> List<T> findList(Class<T> entityClass, QueryFilter[] filters,
			Limitable... limitbys) {
		return this.executeFind(entityClass, filters, limitbys);
	}

	@Override
	public <T extends BusinessObject> Page<T> findPage(Class<T> entityClass, Limitable... limitbys) {
		return this.executeFindPage(entityClass, null, limitbys);
	}

	@Override
	public <T extends BusinessObject> Page<T> findPage(Class<T> entityClass, QueryFilter filter, Limitable... limitbys) {
		return this.executeFindPage(entityClass, new QueryFilter[] { filter }, limitbys);
	}

	@Override
	public <T extends BusinessObject> Page<T> findPage(Class<T> entityClass, QueryFilter[] filters,
			Limitable... limitbys) {
		return this.executeFindPage(entityClass, filters, limitbys);
	}

	@Override
	public <T extends BusinessObject> Page<T> findPage(String hql, Object[] values, PageBy pageby) {
		return this.executeFindPageByHQL(hql, values, pageby);
	}

	@Override
	public <T extends BusinessObject, E> List<E> findPart(Class<T> entityClass, QueryFilter filter, String[] attrNames,
			Limitable... limitbys) {
		return this.executeFindPart(entityClass, attrNames, new QueryFilter[] { filter }, limitbys);
	}

	@Override
	public <T extends BusinessObject, E> List<E> findPart(Class<T> entityClass, QueryFilter[] filters,
			String[] attrNames, Limitable... limitbys) {
		return this.executeFindPart(entityClass, attrNames, filters, limitbys);
	}

	@Override
	public <T extends BusinessObject, E> List<E> findPart(Class<T> entityClass, String[] attrNames,
			Limitable... limitbys) {
		return this.executeFindPart(entityClass, attrNames, null, limitbys);
	}

	@Override
	public <T extends BusinessObject> T findUnique(Class<T> entityClass, QueryFilter... filters) {
		List<T> entityList = this.executeFind(entityClass, filters, new Limitable[] {});
		return entityList.size() == 0 ? null : entityList.get(0);
	}

	@Override
	@SuppressWarnings("unchecked")
	public String getIdentifierName(Class<? extends BusinessObject> entityClass) {
		ClassMetadata meta = getSessionFactory().getClassMetadata(entityClass.getName());
		if (meta != null) {
			return meta.getIdentifierPropertyName();
		}
		if (entityClass.getSuperclass() != null && BusinessObject.class.isAssignableFrom(entityClass.getSuperclass())) {
			return getIdentifierName((Class<? extends BusinessObject>) entityClass.getSuperclass());
		}
		return null;
	}

	@Override
	public <T extends BusinessObject> void save(Collection<T> entityList) {
		getHibernateTemplate().execute(this.batchOperate(entityList, "save"));
	}

	@Override
	public <T extends BusinessObject> void save(T... entitys) {
		if (entitys.length == 1) {
			getHibernateTemplate().save(entitys[0]);
		} else {
			getHibernateTemplate().execute(this.batchOperate(Arrays.asList(entitys), "save"));
		}
	}

	@Override
	public <T extends BusinessObject> void update(Class<T> entityClass, QueryFilter[] filters, String[] attrNames,
			Object[] attrValues) {
		executeUpdate(entityClass, attrNames, attrValues, filters);
	}

	@Override
	public <T extends BusinessObject> void update(Collection<T> entityList) {
		getHibernateTemplate().execute(this.batchOperate(entityList, "update"));
	}

	@Override
	public <T extends BusinessObject> void update(String hql, Object... values) {
		this.getHibernateTemplate().bulkUpdate(hql, values);
	}

	@Override
	public <T extends BusinessObject> void update(T entity, String... attrNames) {
		if (attrNames == null || attrNames.length == 0) {
			this.getHibernateTemplate().update(entity);
		} else {
			executeUpdate(entity.getClass(), attrNames, getPropertyValues(entity, attrNames),
					new QueryFilter[] { new QueryFilter(getIdentifierName(entity.getClass()),
							getIdentifierValue(entity)) });
		}
	}

	@Override
	public <T extends BusinessObject> void update(T[] entitys) {
		if (entitys.length == 1) {
			getHibernateTemplate().update(entitys[0]);
		} else {
			getHibernateTemplate().execute(this.batchOperate(Arrays.asList(entitys), "update"));
		}
	}

	/**
	 * 批量操作公共方法
	 * @param entityList 实体集合
	 * @param operateName 操作名称, 只能是save/update/delete
	 * @return
	 */
	private <T extends BusinessObject> HibernateCallback<T> batchOperate(final Collection<T> entityList,
			final String operateName) {
		return new HibernateCallback<T>() {

			@Override
			public T doInHibernate(Session session) throws HibernateException, SQLException {
				Iterator<? extends BusinessObject> iterator = entityList.iterator();
				for (int i = 0; iterator.hasNext(); i++) {
					BusinessObject entity = iterator.next();
					if ("save".equals(operateName)) {
						session.save(entity);
					} else if ("update".equals(operateName)) {
						session.update(entity);
					} else if ("delete".equals(operateName)) {
						session.delete(entity);
					}
					if (i % 50 == 0) {
						session.flush();
						session.clear();
					}
				}
				return null;
			}
		};
	}

	private <T> Page<T> createPage(PageBy pageby, List<T> resultList, int recordCount) {
		Page<T> page = new Page<T>();
		page.setCurrentPageNo(pageby.getPageNum());
		page.setRecordCount(recordCount);
		page.setPageCount(computedPageCount(recordCount, pageby.getPageSize()));
		page.setPageSize(pageby.getPageSize());
		if (recordCount > 0) {
			page.setQueryResult(resultList);
		}
		return page;
	}

	private <T extends BusinessObject> long executeCount(Class<T> entityClass, QueryFilter[] filters) {
		String idName = getIdentifierName(entityClass);
		StringBuilder hql = new StringBuilder();
		Object[] values = new Object[] {};

		hql.append("select count(obj.").append(idName).append(")");
		hql.append(" from ").append(entityClass.getName()).append(" as obj ");
		if (filters != null && filters.length > 0) {
			hql.append(HQLBuilder.buildQueryFilterSubHQL(filters));
			values = HQLBuilder.getParamterValues(null, filters);
		}
		List<Object> total = this.findHQL(hql.toString(), values);
		return total.get(0) instanceof Long ? ((Long) total.get(0)).intValue() : (Integer) total.get(0);
	}

	private int executeDelete(Class<? extends BusinessObject> entityClass, QueryFilter[] filters) {
		if (filters == null || filters.length <= 0) {
			return getHibernateTemplate().bulkUpdate(HQLBuilder.buildDeleteHQL(entityClass, filters));
		}
		getHibernateTemplate().bulkUpdate(HQLBuilder.buildDeleteHQL(entityClass, filters),
				HQLBuilder.getParamterValues(null, filters));
		return 1;
	}

	@SuppressWarnings("unchecked")
	private <T extends BusinessObject, E> List<E> executeDiffFind(Class<T> entityClass, QueryFilter[] filters,
			String[] attrNames, OrderBy[] orderbys, PageBy pageby) {
		String findHQL = null;
		if (attrNames == null) {
			findHQL = HQLBuilder.buildFindSubHQL(entityClass, filters, orderbys);
		} else {
			findHQL = HQLBuilder.buildRetAttrSubHQL(entityClass, attrNames, filters, orderbys);
		}
		Object[] values = CheckUtil.isValid(filters) ? HQLBuilder.getParamterValues(null, filters) : null;
		List<E> result = null;
		if (pageby == null) {
			result = CheckUtil.isValid(values) ? getHibernateTemplate().find(findHQL, values) : getHibernateTemplate()
					.find(findHQL);
		} else {
			result = getHibernateTemplate().executeFind(executePageletQuery(findHQL, values, pageby));
		}
		return result != null ? result : new ArrayList<E>();
	}

	/**
	 * 执行查询执行条件的实体集合
	 * @param <T>
	 * @param entityClass
	 * @param filters
	 * @param limitbys
	 * @return
	 */
	private <T extends BusinessObject> List<T> executeFind(Class<T> entityClass, QueryFilter[] filters,
			Limitable... limitbys) {
		return this.executeDiffFind(entityClass, filters, null, getOrderBys(limitbys), getPageby(limitbys));
	}

	@SuppressWarnings("unchecked")
	private <E> List<E> executeFindHQL(String hql, Object[] values, PageBy pageby) {
		if (pageby == null) {
			return values == null ? getHibernateTemplate().find(hql) : getHibernateTemplate().find(hql, values);
		}
		return getHibernateTemplate().executeFind(executePageletQuery(hql, values, pageby));
	}

	/**
	 * 执行查询QueryResult
	 * @param <T>
	 * @param entityClass
	 * @param filters
	 * @param limitbys
	 * @return
	 */
	private <T extends BusinessObject> Page<T> executeFindPage(Class<T> entityClass, QueryFilter[] filters,
			Limitable... limitbys) {
		OrderBy[] orderbys = getOrderBys(limitbys);
		PageBy pageby = getPageby(limitbys);
		Assert.notNull(pageby, " pageby can't be null !");
		List<T> resultList = this.executeDiffFind(entityClass, filters, null, orderbys, pageby);
		int recordCount = (int) this.count(entityClass, filters);
		return createPage(pageby, resultList, recordCount);
	}

	private <T extends BusinessObject> Page<T> executeFindPageByHQL(String hql, Object[] values, PageBy pageby) {
		assert pageby != null : " pageby can't be null !";
		List<T> resultList = this.executeFindHQL(hql, values, pageby);
		int recordCount = (int) this.count(hql, values);
		return createPage(pageby, resultList, recordCount);
	}

	/**
	 * 执行查询指定属性
	 * @param <E>
	 * @param entityClass
	 * @param attrNames
	 * @param filters
	 * @param limitbys
	 * @return
	 */
	private <E> List<E> executeFindPart(Class<? extends BusinessObject> entityClass, String[] attrNames,
			QueryFilter[] filters, Limitable... limitbys) {
		return this.executeDiffFind(entityClass, filters, attrNames, getOrderBys(limitbys), getPageby(limitbys));
	}

	/**
	 * 执行分页查询的方法
	 * @param findHQL 查询HQL
	 * @param values 参数值数组
	 * @param pageby 分页
	 * @return
	 */
	private <T extends BusinessObject> HibernateCallback<List<T>> executePageletQuery(final String findHQL,
			final Object[] values, final PageBy pageby) {
		return new HibernateCallback<List<T>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<T> doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(findHQL);
				query.setFirstResult((pageby.getPageNum() - 1) * pageby.getPageSize());
				query.setMaxResults(pageby.getPageSize());
				if (CheckUtil.isValid(values)) {
					for (int i = 0; i < values.length; i++) {
						query.setParameter(i, values[i]);
					}
				}
				return query.list();
			}
		};
	}

	/**
	 * 更新指定类型和条件的实体的某些属性名的属性值
	 * @param entityClass 实体类型
	 * @param attrNames 属性名
	 * @param attrValues 属性值
	 * @param filters 条件
	 */
	private void executeUpdate(Class<? extends BusinessObject> entityClass, String[] attrNames, Object[] attrValues,
			QueryFilter[] filters) {
		Assert.notNull(entityClass);
		Assert.isTrue(attrNames.length == attrValues.length, "The count of parameters is not matched.");
		final String updateHql = HQLBuilder.buildUpdateHQL(entityClass, filters, attrNames, attrValues);
		final Object[] paramValues = HQLBuilder.getParamterValues(attrValues, filters);
		getHibernateTemplate().bulkUpdate(updateHql, paramValues);
	}

	private Object getIdentifierValue(BusinessObject entity) {
		Object idValue = null;
		try {
			idValue = PropertyUtils.getProperty(entity, getIdentifierName(entity.getClass()));
		} catch (Exception e) {
			throw new DaoException("id property is not existed.");
		}
		return idValue;
	}

	private OrderBy[] getOrderBys(Limitable... limitbys) {
		List<OrderBy> orderbys = new ArrayList<OrderBy>();
		if (limitbys != null && limitbys.length > 0) {
			for (Limitable limitby : limitbys) {
				if (limitby.getClass() == OrderBy.class) {
					orderbys.add((OrderBy) limitby);
				}
			}
		}
		return orderbys.toArray(new OrderBy[] {});
	}

	private PageBy getPageby(Limitable... limitbys) {
		PageBy pageby = null;
		if (limitbys != null && limitbys.length > 0) {
			for (Limitable limitby : limitbys) {
				if (limitby.getClass() == PageBy.class) {
					pageby = (PageBy) limitby;
				}
			}
		}
		return pageby;
	}

	/**
	 * Return specify entity object's properties values.
	 * @param entity persist object
	 * @param attrNames user input properties names
	 */
	private String getPropertyValue(BusinessObject entity, String attrName) {
		try {
			return BeanUtils.getProperty(entity, attrName);
		} catch (Exception e) {
			throw new DaoException(e.getMessage());
		}
	}

	/**
	 * Return specify entity object's properties values.
	 * @param entity persist object
	 * @param attrNames user input properties names
	 */
	private Object[] getPropertyValues(BusinessObject entity, String[] attrNames) {
		List<Object> values = new ArrayList<Object>();
		for (String propertyName : attrNames) {
			values.add(getPropertyValue(entity, propertyName));
		}
		return values.toArray();
	}

	/**
	 * Validate whether user use '.' expression in HQL statement.
	 * @param entityClass
	 * @param property
	 */
	@SuppressWarnings("unchecked")
	private boolean notTableJoin(Class<? extends BusinessObject> entityClass, String property) {
		if (property.indexOf('.') == -1) {
			return true;
		}
		String[] props = StringUtils.split(property, ".", 2);
		try {
			Class<?> relativeClass = entityClass.getDeclaredField(props[0]).getDeclaringClass();
			return getIdentifierName((Class<? extends BusinessObject>) relativeClass).equalsIgnoreCase(props[1]);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		return false;
	}
}

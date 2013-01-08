package com.facetime.spring.test;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.facetime.spring.dao.Dao;
import com.facetime.spring.support.Locator;

@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public abstract class BaseActionTest extends AbstractActionTest {

	@Autowired
	@Qualifier("beanLocator")
	protected Locator beanLocator;

	@Autowired
	@Qualifier("defaultDao")
	protected Dao defaultDao;

	@Autowired
	@Qualifier("sessionFactory")
	protected SessionFactory sessionFactory;

	public BaseActionTest() {
	}

	public <T extends Dao> T locate(Class<T> clazz) {
		return beanLocator.locate(clazz);
	}

	public <T extends Dao> T locate(Class<T> clazz, String beanName) {
		return beanLocator.locate(clazz, beanName);
	}
}

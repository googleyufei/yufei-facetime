package com.facetime.spring.test;

import org.hibernate.SessionFactory;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.facetime.spring.dao.Dao;
import com.facetime.spring.support.Locator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:beans.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public abstract class BaseLogicTest {

	@Autowired
	@Qualifier("beanLocator")
	protected Locator beanLocator;

	@Autowired
	@Qualifier("defaultDao")
	protected Dao defaultDao;

	@Autowired
	@Qualifier("sessionFactory")
	private SessionFactory sessionFactory;

	@BeforeTransaction
	public void initUser() {
		// beanLocator.locate(LoginManager.class).initAdmin();
	}

	protected void flush() {
		this.sessionFactory.getCurrentSession().flush();
	}

	protected Dao getDefaultDao() {
		return this.defaultDao;
	}

	protected <T> T locate(Class<T> locatingClass) {
		return this.beanLocator.locate(locatingClass);
	}
}

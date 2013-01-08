package com.facetime.spring.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.support.DaoSupport;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class AnnotationHibernateDaoSupport extends DaoSupport {

	private HibernateDaoSupport daoSupport = new HibernateDaoSupport() {
	};

	public HibernateTemplate getHibernateTemplate() {
		return daoSupport.getHibernateTemplate();
	}

	public SessionFactory getSessionFactory() {
		return daoSupport.getSessionFactory();
	}

	@Autowired
	public void setSessionFactory(@Qualifier("sessionFactory") SessionFactory sessionFactory) {
		daoSupport.setSessionFactory(sessionFactory);
	}

	@Override
	protected void checkDaoConfig() throws IllegalArgumentException {
		if (daoSupport == null) {
			throw new IllegalArgumentException("sessionFactory or hibernateTemplate is required.");
		}
	}
}

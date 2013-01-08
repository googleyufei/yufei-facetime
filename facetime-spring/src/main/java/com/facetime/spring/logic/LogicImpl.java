package com.facetime.spring.logic;

import com.facetime.spring.dao.Dao;
import com.facetime.spring.dao.DaoImpl;
import com.facetime.spring.support.Locator;

/**
 * 默认的业务层接口实现类
 *
 * @author YUFEI
 */
public class LogicImpl extends DaoImpl implements Logic {

	private Locator locator;

	public Locator getLocator() {
		return locator;
	}

	public <T extends Dao> T locate(Class<T> clazz) {
		return getLocator().locate(clazz);
	}

	public void setLocator(Locator locator) {
		this.locator = locator;
	}
}

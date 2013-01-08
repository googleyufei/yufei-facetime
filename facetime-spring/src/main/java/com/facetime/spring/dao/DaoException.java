package com.facetime.spring.dao;

import com.facetime.spring.support.InternelException;

/**
 * 数据访问层抛出的异常
 * 
 * @author YUFEI
 * 
 */
public class DaoException extends InternelException {

	private static final long serialVersionUID = 1497586079589997059L;

	private Dao dao;

	public DaoException(Dao dao) {
		this.dao = dao;
	}

	public DaoException(Dao dao, String message) {
		super(message);
		this.dao = dao;
	}

	public DaoException(String message) {
		super(message);
	}

	public DaoException(String message, Throwable cause, Dao dao) {
		super(message, cause);
		this.dao = dao;
	}

	public DaoException(Throwable cause, Dao dao) {
		super(cause);
		this.dao = dao;
	}

	public Dao getDao() {
		return dao;
	}
}

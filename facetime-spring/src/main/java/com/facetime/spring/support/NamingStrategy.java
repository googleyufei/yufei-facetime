package com.facetime.spring.support;

import org.hibernate.cfg.ImprovedNamingStrategy;

public class NamingStrategy extends ImprovedNamingStrategy {

	private static final String FK_SUFFIX = "_id";

	private static final String FK_PREFIX = "fk_";

	private static final long serialVersionUID = 8540955950903809611L;

	private static final String TABLE_PREFIX = "";
	private static final String COLUMN_PREFIX = "c_";

	@Override
	public String classToTableName(String className) {
		return TABLE_PREFIX + super.classToTableName(className.replace("Entity", ""));
	}

	@Override
	public String propertyToColumnName(String propertyName) {
		return COLUMN_PREFIX + super.propertyToColumnName(propertyName);
	}

	@Override
	public String foreignKeyColumnName(String propertyName, String propertyEntityName, String propertyTableName,
			String referencedColumnName) {
		return FK_PREFIX
				+ super.foreignKeyColumnName(propertyName, propertyEntityName, propertyTableName, referencedColumnName)
				+ FK_SUFFIX;
	}

}

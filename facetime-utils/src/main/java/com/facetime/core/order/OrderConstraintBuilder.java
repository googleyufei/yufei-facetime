package com.facetime.core.order;

/**
 * Constructs order constraints .<BR></>
 *
 * 构建一个OrderConstraint对象
 */
public final class OrderConstraintBuilder {
	/**
	 * Adds an <i>after:getId</i> constraint.
	 */
	public static OrderConstraint after(String id) {
		return new OrderConstraint().after(id);
	}

	/**
	 * Adds an <i>after:*</i> constraint.
	 */
	public static OrderConstraint afterAll() {
		return new OrderConstraint().afterAll();
	}

	/**
	 * Adds a <i>before:getId</i> constraint.
	 */
	public static OrderConstraint before(String id) {
		return new OrderConstraint().before(id);
	}

	/**
	 * Adds a <i>before:*</i> constraint.
	 */
	public static OrderConstraint beforeAll() {
		return new OrderConstraint().beforeAll();
	}
}

package com.facetime.core.order;

import com.facetime.core.utils.StringUtils;

/**
 * A wrapper that allows objects of a target type to be ordered. Each Orderable
 * object is given a unique getId(label) and a set of pre-requisites (objects which should
 * be ordered earlier) and post-requisites (objects which should be ordered
 * later).
 * 
 * @param <T>
 */
public class OrderNode<T> {

	private final String label;
	private final String constraints;
	private final T target;

	/**
	 * 数字规定从1开始
	 * @param label
	 *            unique identifier for the target object
	 * @param target
	 *            the object to be ordered; this may also be null (in which case
	 *            the getId represents a placeholder)
	 */

	public OrderNode(String label, T target, String constraints) {
		assert StringUtils.isNotBlank(label);
		this.label = label;
		this.target = target;
		this.constraints = constraints;
	}

	public static <T> OrderNode<T> of(String label, T target, String constraints) {
		return new OrderNode(label, target, constraints);
	}

	public String getId() {
		return label;
	}

	public T getTarget() {
		return target;
	}

	public String getConstraints() {
		return constraints;
	}

	@Override
	public boolean equals(Object o) {
		//使用id比较
		return this.getId().equals(((OrderNode) o).getId());
	}

	@Override
	public String toString() {

		StringBuilder buffer = new StringBuilder("Orderable[");
		buffer.append(label);
		buffer.append(" ");
		buffer.append(constraints);
		buffer.append(" ");
		buffer.append(target.toString());
		buffer.append("]");

		return buffer.toString();
	}

}

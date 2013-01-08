package com.facetime.core.order;

import java.util.ArrayList;
import java.util.List;

import com.facetime.core.utils.LE;

/**
 * 排序限定描述的代表对象
 *
 */
public class OrderConstraint {
	
	private static final String ALL = "*";
	private List<String> constraints = new ArrayList<String>();

	/**
	 * Adds an <i>after:getId</i> constraint.
	 */
	public OrderConstraint after(String id) {
		constraints.add("after:" + id);
		return this;
	}

	/**
	 * Adds an <i>after:*</i> constraint.
	 */
	public OrderConstraint afterAll() {
		return after(ALL);
	}

	/**
	 * Adds a <i>before:getId</i> constraint.
	 */
	public OrderConstraint before(String id) {
		constraints.add("before:" + id);
		return this;
	}

	/**
	 * Adds a <i>before:*</i> constraint.
	 */
	public OrderConstraint beforeAll() {
		return before(ALL);
	}

	/**
	 * Returns all constraints as array of strings.
	 */
	public String[] build() {
		return constraints.toArray(new String[] {});
	}

    public String buildToString() {
        return LE.join("|", constraints).toString();
    }
}

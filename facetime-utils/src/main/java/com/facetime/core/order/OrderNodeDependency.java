// Copyright 2006, 2007 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.facetime.core.order;

import java.util.List;

import com.facetime.core.collection.CollectionFactory;

/**
 * 被{@link Orderer}用来建立OrderNode的依赖的类。
 * <>p</>
 * Used by {@link Orderer} to establish
 * backward dependencies for {@link OrderNode} objects.
 * 
 * @param <T>
 */

class OrderNodeDependency<T> {

	//private final Log logger;
	private final OrderNode<T> orderable;

	private final List<OrderNodeDependency<T>> dependencies = CollectionFactory.newList();

	OrderNodeDependency(OrderNode<T> orderable) {
		//this.logger = logger;
		this.orderable = orderable;
	}

	/**
	 * Returns the underlying {@link OrderNode}'s getId.
	 */
	public String getId() {
		return orderable.getId();
	}

	void addDependency(OrderNodeDependency<T> node) {
		if (node.isReachable(this)) {
			//FIXME throw Exception ???
			//logger.warn("Unable to fill '%s' as a dependency of '%s', as that forms a dependency cycle ('%<s' depends on itself via '%1$s'). The dependency has been ignored.",node.getId(), this.getId());
			return;
		}

		// Make this node depend on the other node.
		// That forces the other node's orderable
		// to appear before this node's orderable.

		dependencies.add(node);
	}

	boolean isReachable(OrderNodeDependency<T> node) {
		if (this == node)
			return true;

		// Quick fast pass for immediate dependencies

		for (OrderNodeDependency<T> d : dependencies) {
			if (d == node)
				return true;
		}

		// Slower second pass looks for
		// indirect dependencies

		for (OrderNodeDependency<T> d : dependencies) {
			if (d.isReachable(node))
				return true;
		}

		return false;
	}

	/**
	 * Returns the {@link OrderNode} objects for this node ordered based on
	 * dependencies.
	 */
	List<OrderNode<T>> getOrdered() {
		List<OrderNode<T>> result = CollectionFactory.newList();

		fillOrder(result);

		return result;
	}

	private void fillOrder(List<OrderNode<T>> list) {
		if (list.contains(orderable))
			return;

		// Recusively fill dependencies

		for (OrderNodeDependency<T> node : dependencies) {
			node.fillOrder(list);
		}

		list.add(orderable);
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder(String.format("[%s", getId()));
		boolean first = true;
		for (OrderNodeDependency<T> node : dependencies) {
			buffer.append(first ? ": " : ", ");
			buffer.append(node.toString());
			first = false;
		}
		buffer.append("]");
		return buffer.toString();
	}

}

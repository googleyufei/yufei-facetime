package com.facetime.core.order;

import static com.facetime.core.collection.CollectionFactory.newList;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.facetime.core.collection.CollectionFactory;
import com.facetime.core.lock.OneShotLock;
import com.facetime.core.utils.PatternMatchUtils;
import com.facetime.core.utils.StringUtils;

/**
 * 对OrderNode进行依据"约束"进行排序，约束的“依赖”使用的是每一个Node的唯一的Id值。<BR></>
 * 同时支持表达式排序和浮点数的Index排序
 * <>p</>
 * Used to order objects into an "execution" order. Each object must have a
 * unique getId. It may specify a list of constraints which identify the ordering
 * of the objects.
 */
public class Orderer<T> {

	private final OneShotLock lock = new OneShotLock();
	//标明使用是否使用表达式还是数字  0 表示使用expr 1 表示使用 float -1表示还没有判定
	private int floatOrder = -1;
	private final List<OrderNode> orderables = CollectionFactory.newList();
	private final Map<String, OrderNode<T>> idToOrderable = CollectionFactory.newCaseInsensitiveMap();
	private Map<String, OrderNodeDependency<T>> dependencyNodesById;

	// Special node that is always dead last: all other nodes are a dependency
	// of the trailer.

	private OrderNodeDependency<T> trailer;

	interface DependencyLinker<T> {
		void link(OrderNodeDependency<T> source, OrderNodeDependency<T> target);
	}

	// before: source is added as a dependency of target, so source will
	// appear before target.

	final DependencyLinker<T> _before = new DependencyLinker<T>() {
		public void link(OrderNodeDependency<T> source, OrderNodeDependency<T> target) {
			target.addDependency(source);
		}
	};

	// after: target is added as a dependency of source, so source will appear
	// after target.

	final DependencyLinker<T> _after = new DependencyLinker<T>() {
		public void link(OrderNodeDependency<T> source, OrderNodeDependency<T> target) {
			source.addDependency(target);
		}
	};

	public Orderer() {
	}

	/**
	 * Adds an object to be ordered.
	 * 
	 * @param orderable
	 */
	public Orderer<T> add(OrderNode<T> orderable) {
		lock.check();
		//检查是否已经确定类型
		if (floatOrder < 0) {
			// 0 表示使用 float 1表示使用expr
			floatOrder = StringUtils.isNumeric(orderable.getConstraints()) ? 1 : 0;
		}
		String id = orderable.getId();
		if (idToOrderable.containsKey(id)) {
			//FIXME dzb throw exception???
			//logger.warn("Could not fill object with duplicate getId '%s'.  The duplicate object has been ignored.", getId);
			return this;
		}
		idToOrderable.put(id, orderable);
		orderables.add(orderable);
		return this;
	}

	public void override(OrderNode<T> orderable) {
		lock.check();

		String id = orderable.getId();

		OrderNode<T> existing = idToOrderable.get(id);

		if (existing == null)
			throw new IllegalArgumentException(String.format(
					"Override for object '%s' is invalid as it does not match an existing object.", id));

		orderables.remove(existing);
		orderables.add(orderable);

		idToOrderable.put(id, orderable);
	}

	/**
	 * Adds an object to be ordered.
	 * 
	 * @param id
	 *            unique, qualified getId for the target
	 * @param target
	 *            the object to be ordered (or null as a placeholder)
	 * @param constraints
	 *            optional, variable constraints
	 * @see #add(OrderNode)
	 */

	public void add(String id, T target, String constraints) {
		lock.check();

		add(new OrderNode<T>(id, target, constraints));
	}

	public void override(String id, T target, String constraints) {
		lock.check();

		override(new OrderNode<T>(id, target, constraints));
	}

	public List<T> getOrdered() {
		lock.lock();
		List<T> result = newList();
		if (floatOrder == 0) {
			initializeGraph();
			for (OrderNode<T> orderable : trailer.getOrdered()) {
				T target = orderable.getTarget();
				// 跳过空的或者用于占位的
				if (target != null)
					result.add(target);
			}
		} else {
			//按优先级排序
			Collections.sort(orderables, new Comparator<OrderNode>() {
				public int compare(OrderNode o1, OrderNode o2) {
					float fo1 = Float.valueOf(o1.getConstraints());
					float fo2 = Float.valueOf(o2.getConstraints());
					if (fo1 == fo2)
						return 0;
					return (fo1 > fo2) ? 1 : -1;
				}
			});

			for (OrderNode<T> order : orderables) {
				T target = order.getTarget();
				if (target != null) {
					result.add(target);
				}
			}
		}

		return result;
	}

	private void initializeGraph() {
		dependencyNodesById = CollectionFactory.newCaseInsensitiveMap();
		trailer = new OrderNodeDependency<T>(new OrderNode<T>("*-trailer-*", null, null));
		addNodes();
		addDependencies();
	}

	private void addNodes() {
		for (OrderNode<T> orderable : orderables) {
			OrderNodeDependency<T> node = new OrderNodeDependency<T>(orderable);
			dependencyNodesById.put(orderable.getId(), node);
			trailer.addDependency(node);
		}
	}

	private void addDependencies() {
		for (OrderNode<T> orderable : orderables) {
			addDependencies(orderable);
		}
	}

	private void addDependencies(OrderNode<T> orderable) {
		String sourceId = orderable.getId();
		String[] constraints = orderable.getConstraints() == null ? new String[0] : orderable.getConstraints().split(
				"\\|");
		for (String constraint : constraints) {
			addDependencies(sourceId, constraint);
		}
	}

	private void addDependencies(String sourceId, String constraint) {
		int colonx = constraint.indexOf(':');

		String type = colonx > 0 ? constraint.substring(0, colonx) : null;

		DependencyLinker<T> linker = null;

		if ("after".equals(type))
			linker = _after;
		else if ("before".equals(type))
			linker = _before;

		if (linker == null) {
			//logger.warn("Could not parse ordering constraint '%s' (for '%s'). The constraint has been ignored.", constraint, sourceId);
			return;
		}

		String patternList = constraint.substring(colonx + 1);

		linkNodes(sourceId, patternList, linker);
	}

	private void linkNodes(String sourceId, String patternList, DependencyLinker<T> linker) {
		Collection<OrderNodeDependency<T>> nodes = findDependencies(sourceId, patternList);
		OrderNodeDependency<T> source = dependencyNodesById.get(sourceId);

		for (OrderNodeDependency<T> target : nodes) {
			linker.link(source, target);
		}
	}

	private Collection<OrderNodeDependency<T>> findDependencies(String sourceId, String patternList) {

		//IdMatcher matcher = buildMatcherForPattern(patternList);
		String[] patterns = patternList.split(",");

		Collection<OrderNodeDependency<T>> result = newList();

		for (String id : dependencyNodesById.keySet()) {
			if (sourceId.equals(id))
				continue;

			if (PatternMatchUtils.match(id, patterns) >= 0)
				result.add(dependencyNodesById.get(id));
		}

		return result;
	}

	//	private IdMatcher buildMatcherForPattern(String patternList) {
	//		List<IdMatcher> matchers = newList();
	//
	//		for (String pattern : patternList.split(",")) {
	//			IdMatcher matcher = new IdMatcherImpl(pattern.trim());
	//
	//			matchers.fill(matcher);
	//		}
	//
	//		return matchers.size() == 1 ? matchers.get(0) : new OrIdMatcher(
	//				matchers);
	//	}

}

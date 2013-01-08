package com.facetime.core.coercion;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.facetime.core.collection.CollectionFactory;
import com.facetime.core.logging.Log;
import com.facetime.core.logging.LogFactory;
import com.facetime.core.resource.ResourceScanner;
import com.facetime.core.resource.impl.ClasspathURLConverterImpl;
import com.facetime.core.resource.impl.DefaultResourceScanner;
import com.facetime.core.utils.CE;
import com.facetime.core.utils.ClassUtils;
import com.facetime.core.utils.DefaultSettings;
import com.facetime.core.utils.LE;
import com.facetime.core.utils.TypeExtractor;

/**
 * 一个创建 CoercionTuple 的工厂类。它的使用方式是：
 * <p/>
 * <pre>
 * TypeCoercer.getSymbol().coerce(obj, fromType, toType);
 * </pre>
 * Contributes a set of standard type coercions to the {@link Coercer} service:
 * <ul>
 * <li>Object to String</li>
 * <li>String to Double</li>
 * <li>String to BigDecimal</li>
 * <li>BigDecimal to Double</li>
 * <li>Double to BigDecimal</li>
 * <li>String to BigInteger</li>
 * <li>BigInteger to Long</li>
 * <li>String to Long</li>
 * <li>Long to Byte</li>
 * <li>Long to Short</li>
 * <li>Long to Integer</li>
 * <li>Double to Long</li>
 * <li>Double to Float</li>
 * <li>Float to Double</li>
 * <li>Long to Double</li>
 * <li>String to Boolean ("false" is always false, other non-blank strings are true)</li>
 * <li>Long to Boolean (true if long value is non zero)</li>
 * <li>Null to Boolean (always false)</li>
 * <li>Collection to Boolean (false if empty)</li>
 * <li>Object[] to List</li>
 * <li>primitive[] to List</li>
 * <li>Object to List (by wrapping as a singleton list)</li>
 * <li>String to File</li>
 * <li>String to {@link net.street.common.util.TimeInterval}</li>
 * <li>{@link net.street.common.util.TimeInterval} to Long</li>
 * <li>Object to Object[] (wrapping the object as an array)</li>
 * <li>Collection to Object[] (via the toArray() method)
 * </ul>
 */
public class Coercer {

	private static final Log log = LogFactory.getLog();

	private static Coercer coercer = new Coercer();

	/**
	 * @return 单例
	 */
	public static Coercer get() {
		return coercer;
	}

	/**
	 * 如何抽取对象的类型级别
	 */
	private TypeExtractor extractor;
	/**
	 * CoercionTuple 的搜索路径
	 */
	private List<String> paths;
	/**
	 * CoercionTuple 的配置
	 */
	private Object setting;

	private Map<Class<?>, String> settings = CollectionFactory.newMap();

	/**
	 * 设置转换的配置
	 * <p/>
	 * 配置对象所有的共有方法都会被遍历。只要这个方法有一个且只有一个参数，并且该参数 是一个 org.nutz.basic_tuples.CoercionTuple
	 * 接口的实现类。那么就会被认为是对该种 CoercionTuple 的一个 配置。
	 * <p/>
	 * 当初始化这个 CoercionTuple 之前，会用这个方法来设置一下你的 CoercionTuple （如果你的 CoercionTuple 需要配置的话）
	 *
	 * @param obj 配置对象。可以是任意的 Java 对象。
	 */
	public synchronized Coercer setSetting(Object obj) {
		if (obj != null) {
			setting = obj;
			this.reload();
		}
		return this;
	}

	/**
	 * 设置自定义的对象类型提取器逻辑
	 *
	 * @param te 类型提取器
	 */
	public synchronized Coercer setTypeExtractor(TypeExtractor te) {
		extractor = te;
		return this;
	}

	/**
	 * 你的的 CoercionTuple 可能存在在不同包下，你可以把每个包下的随便一个 CoercionTuple 作为例子放到一个列表里。 这样， Nutz.CoercionTuple
	 * 就能找到同一包下其他的 CoercionTuple 了。
	 * <p/>
	 * 你的 CoercionTuple 存放在 CLASSPAH 下或者 Jar 包里都是没有问题的
	 *
	 * @param paths CoercionTuple 例子列表
	 */
	public synchronized Coercer setPaths(List<String> paths) {
		if (paths != null) {
			this.paths = paths;
			reload();
		}
		return this;
	}

	/**
	 * 将 CoercionTuple 的寻找路径恢复成默认值。
	 */
	public synchronized Coercer resetPaths() {
		paths = new ArrayList<String>();
		paths.add("net/street/common/coercion/basic_tuples");
		reload();
		return this;
	}

	/**
	 * 增加 CoercionTuple 的寻找路径。
	 *
	 * @param paths 示例 CoercionTuple
	 */
	public synchronized Coercer addPaths(String... paths) {
		if (null != paths) {
			for (String path : paths)
				if (path != null)
					this.paths.add(path);
			reload();
		}
		return this;
	}

	private Coercer() {
		setting = new DefaultSettings();
		resetPaths();
	}

	private void reload() {
		if (paths == null || paths.size() == 0) {
			resetPaths();
			return;
		}

		for (Method m1 : setting.getClass().getMethods()) {
			Class<?>[] pts = m1.getParameterTypes();
			if (pts.length == 1 && CoercionTuple.class.isAssignableFrom(pts[0]))
				settings.put(pts[0], m1.getName());
		}

		ResourceScanner locator = new DefaultResourceScanner(new ClasspathURLConverterImpl());

		this.tuples = new HashMap<Class<?>, Map<Class<?>, CoercionTuple<?, ?>>>();
		ArrayList<Class<?>> tupleClasses = new ArrayList<Class<?>>();
		for (String path : paths) {
			//加载基本转换器
			Collection<String> conercion_names = locator.scanClassNames(path);

			for (String name : conercion_names) {
				Class<?> clazz = ClassUtils.loadClass(name);
				if (clazz == null)
					continue;
				if (CE.of(clazz).isOf(CoercionTuple.class)) {
					tupleClasses.add(clazz);
				}
			}
		}

		for (Class<?> clazz : tupleClasses) {
			try {
				if (Modifier.isAbstract(clazz.getModifiers()))
					continue;
				CoercionTuple<?, ?> tuple = (CoercionTuple<?, ?>) clazz.newInstance();
				register(tuple);
			} catch (Throwable e) {
				if (log.isWarnEnabled())
					log.warn("Fail to create basic_tuples [%s] because: %s", clazz, e.getMessage());
			}
		}
		if (log.isDebugEnabled()) {
			log.debug("Using %s basic_tuples for Coercion", this.tuples.size());
		}
	}

	/**
	 * 注册转换元组
	 *
	 * @param tuple
	 */
	public void register(CoercionTuple<?, ?> tuple) {

		Map<Class<?>, CoercionTuple<?, ?>> map2 = this.tuples.get(tuple.getFromType());
		if (map2 == null) {
			map2 = new HashMap<Class<?>, CoercionTuple<?, ?>>();
			this.tuples.put(tuple.getFromType(), map2);
		}
		boolean b = map2.containsKey(tuple.getTargetType());
		//有就不加
		if (b)
			return;

		String m = settings.get(tuple.getClass());
		if (null == m) {
			for (Map.Entry<Class<?>, String> entry : settings.entrySet()) {
				Class<?> cc = entry.getKey();
				if (cc.isAssignableFrom(tuple.getClass())) {
					m = settings.get(cc);
					break;
				}
			}
		}
		if (m != null) {
			LE.invoke(setting, m, tuple);
		}
		map2.put(tuple.getTargetType(), tuple);
	}

	/**
	 * First index is "from" (source) The second index is "to" (target)
	 */
	private Map<Class<?>, Map<Class<?>, CoercionTuple<?, ?>>> tuples;

	/**
	 * 转换一个 Bean, 从一个指定的类型到另外的类型
	 *
	 * @param src      源对象
	 * @param toType   目标类型
	 * @return 目标对象
	 * @throws FailToCoerceException 如果没有找到转换器，或者转换失败
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> T coerce(Object src, Class<T> toType) throws FailToCoerceException {
		if (src == null)
			return null;

		Class<?> fromType = src.getClass();

		if (toType.isInstance(src)) {
			return toType.cast(src);
		}

		CE<?> from = CE.of(fromType, extractor);

		if (from.isOf(toType)) // Use language built-in cases
			return (T) src;

		Class<?> targetClass = toType;
		if (toType.isPrimitive()) {
			targetClass = CE.of(toType).getWrapperType();
		}
		CoercionTuple tuple = find(from, targetClass);
		if (tuple == null) {
			throw new FailToCoerceException(String.format(
					"Can not find basic_tuples for '%s'=>'%s' in (%d) because:\n%s",
					ClassUtils.getClassNameForJava(fromType), ClassUtils.getClassNameForJava(toType), tuples.size(),
					"Fail to find matched basic_tuples"));
		}
		try {
			return (T) tuple.coerce(src);
		} catch (FailToCoerceException e) {
			throw e;
		} catch (Exception e) {
			throw new FailToCoerceException(String.format("Fail to coerce from <%s> to <%s> for {%s} because:\n%s:%s",
					ClassUtils.getClassNameForJava(fromType), ClassUtils.getClassNameForJava(toType), src, e.getClass()
							.getSimpleName(), e.getMessage()), e);
		}
	}

	/**
	 * 判断一个类型是否可以被转换成另外一个类型
	 * <p>
	 * 判断的依据就是看是否可以直接被转型，以及能不能找到一个专有的转换器
	 *
	 * @param fromType
	 *            起始类型
	 * @param toType
	 *            目标类型
	 * @return 是否可以转换
	 */
	public boolean isCoercable(Class<?> fromType, Class<?> toType) {
		if (CE.of(fromType).isOf(toType))
			return true;
		CoercionTuple<?, ?> tuple = this.find(fromType, toType);
		return tuple != null;
	}

	/**
	 * 获取一个转换器
	 *
	 * @param from 源类型
	 * @param to   目标类型
	 * @return 转换器
	 */
	public <F, T> CoercionTuple<F, T> find(Class<F> from, Class<T> to) {
		return find(CE.of(from), to);
	}

	@SuppressWarnings("unchecked")
	private <F, T> CoercionTuple<F, T> find(CE<F> from, Class<T> toType) {
		CE<T> ce_to = CE.of(toType, extractor);
		CoercionTuple<F, T> tuple = null;
		Class<?>[] fromTypes = from.getExtractTypes();
		Class<?>[] toTypes = ce_to.getExtractTypes();
		for (Class<?> fromType : fromTypes) {
			Map<Class<?>, CoercionTuple<?, ?>> m2 = tuples.get(fromType);
			if (m2 != null)
				for (Class<?> tt : toTypes) {
					tuple = (CoercionTuple<F, T>) m2.get(tt);
					if (tuple != null)
						break;
				}
			if (tuple != null)
				break;
		}
		return tuple;
	}

}

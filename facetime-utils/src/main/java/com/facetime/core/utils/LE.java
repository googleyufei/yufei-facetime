package com.facetime.core.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.facetime.core.coercion.Coercer;
import com.facetime.core.coercion.FailToCoerceException;
import com.facetime.core.file.IOUtils;
import com.facetime.core.io.StringInputStream;
import com.facetime.core.io.StringOutputStream;
import com.facetime.core.io.StringReader2;
import com.facetime.core.io.StringWriter2;

/**
 * Language Enhancement<p> 语言增强
 * User: dzb
 * Date: 11-8-1
 * Time: 下午9:08
 */
public class LE {

	/**
	 * @return 一个未实现的运行时异常
	 */
	public static RuntimeException notImplementYet() {
		return new RuntimeException("No implements yet! May be *do not* need implement.");
	}

	/**
	 * @return 一个不可能的运行时异常
	 */
	public static RuntimeException impossible() {
		return new RuntimeException("It is impossible! But it happpend. Check codes.");
	}

	/**
	 * 根据格式化字符串，生成运行时异常
	 *
	 * @param format
	 *            格式
	 * @param args
	 *            参数
	 * @return 运行时异常
	 */
	public static RuntimeException makeThrow(String format, Object... args) {
		return new RuntimeException(String.format(format, args));
	}

	/**
	 * 根据格式化字符串，生成一个指定的异常。
	 *
	 * @param classOfT
	 *            异常类型， 需要有一个字符串为参数的构造函数
	 * @param format
	 *            格式
	 * @param args
	 *            参数
	 * @return 异常对象
	 */
	public static <T extends Throwable> T makeThrow(Class<T> classOfT, String format, Object... args) {
		return CE.of(classOfT).create(String.format(format, args));
	}

	/**
	 * 将抛出对象包裹成运行时异常，并增加自己的描述
	 *
	 * @param e
	 *            抛出对象
	 * @param fmt
	 *            格式
	 * @param args
	 *            参数
	 * @return 运行时异常
	 */
	public static RuntimeException wrapThrow(Throwable e, String fmt, Object... args) {
		return new RuntimeException(String.format(fmt, args), e);
	}

	/**
	 * 用运行时异常包裹抛出对象，如果抛出对象本身就是运行时异常，则直接返回。
	 * <p>
	 * 如果是 InvocationTargetException，那么将其剥离，只包裹其 TargetException
	 *
	 * @param e
	 *            抛出对象
	 * @return 运行时异常
	 */
	public static RuntimeException wrapThrow(Throwable e) {
		if (e instanceof RuntimeException)
			return (RuntimeException) e;
		if (e instanceof InvocationTargetException)
			return wrapThrow(((InvocationTargetException) e).getTargetException());
		return new RuntimeException(e);
	}

	/**
	 * 用一个指定可抛出类型来包裹一个抛出对象。这个指定的可抛出类型需要有一个构造函数 接受 Throwable 类型的对象
	 *
	 * @param e
	 *            抛出对象
	 * @param wrapper
	 *            包裹类型
	 * @return 包裹后对象
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Throwable> T wrapThrow(Throwable e, Class<T> wrapper) {
		if (wrapper.isAssignableFrom(e.getClass()))
			return (T) e;
		return CE.of(wrapper).create(e);
	}

	/**
	 * 解包到最根的异常
	 * @param e
	 * 			要解包的异常
	 * @return
	 * 		解包后的异常
	 */
	public static Throwable unwrapThrow(Throwable e) {
		if (e == null)
			return null;
		if (e instanceof InvocationTargetException) {
			InvocationTargetException itE = (InvocationTargetException) e;
			if (itE.getTargetException() != null)
				return unwrapThrow(itE.getTargetException());
		}
		if (e.getCause() != null)
			return unwrapThrow(e.getCause());
		return e;
	}

	/**
	 * 在一个异常堆栈中定位一个指定类型的Exception<p>
	 *
	 * @param t    
	 * 		外层的异常
	 * @param type 
	 * 		要查找的异常类型
	 * @return 
	 * 		第一个给定类型的异常，或者是NULL
	 */
	public static <T extends Throwable> T findCause(Throwable t, Class<T> type) {
		Throwable current = t;

		while (current != null) {
			if (type.isInstance(current))
				return type.cast(current);
			// Not a match, work down.
			current = current.getCause();
		}
		return null;
	}

	/**
	 * 把Field对象变成可被存取的。多加了点判断，使得方法<code>setAccessible(true)仅在需要的时候才被调用<br>
	 * 避免在JVM的SecurityManager被激活的情况下少出麻烦。 
	 *
	 * @param field 
	 * 			要进行Accessible的字段
	 * @see java.lang.reflect.Field#setAccessible
	 */
	public static void makeAccessible(Field field) {
		if (!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
			field.setAccessible(true);
		}
	}

	public static void makeAccessible(Method method) {
		if (!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers())) {
			method.setAccessible(true);
		}
	}

	/**
	 * 判断两个对象是否相等。 这个函数用处是:
	 * <ul>
	 * <li>可以容忍 null, 如果两个对象都是null，则认为相等
	 * <li>辨识Identifiable接口对象实例，自动使用getId()方式比较
	 * <li>可以容忍不同类型的 Number
	 * <li>对数组，集合，Map 会深层比较
	 * </ul>
	 * 重写的 equals方法优先
	 *
	 * @param a1
	 *            比较对象1
	 * @param a2
	 *            比较对象2
	 * @return 是否相等
	 */
	@SuppressWarnings("unchecked")
	public static boolean equals(Object a1, Object a2) {
		if (a1 == null && a2 == null)
			return true;
		if (a1 == null || a2 == null)
			return false;

		if (a1.equals(a2))
			return true;

		CE<?> ce1 = CE.of(a1);

		if (ce1.isStringLike()) {
			return a1.toString().equals(a2.toString());
		}
		if (ce1.isDateLike()) {
			Calendar calendar1 = Calendar.getInstance();
			calendar1.setTime((java.util.Date) a1);
			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTime((java.util.Date) a2);
			if (calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
					&& calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)
					&& calendar1.get(Calendar.DATE) == calendar2.get(Calendar.DATE)) {
				return true;
			} else {
				return false;
			}
		}
		if (ce1.isTimeLike()) {
			return a1.equals(a2);
		}
		if (ce1.isNumber()) {
			return a2 instanceof Number && a1.toString().equals(a2.toString());
		}

		if ((a1 instanceof Identifiable) && (a2 instanceof Identifiable)) {
			return equals(((Identifiable) a1).getId(), ((Identifiable) a2).getId());
		}

		if (!a1.getClass().isAssignableFrom(a2.getClass()) && !a2.getClass().isAssignableFrom(a1.getClass()))
			return false;

		if (a1 instanceof Map && a2 instanceof Map) {
			Map<?, ?> m1 = (Map<?, ?>) a1;
			Map<?, ?> m2 = (Map<?, ?>) a2;
			if (m1.size() != m2.size())
				return false;
			for (Map.Entry<?, ?> e : m1.entrySet()) {
				Object key = e.getKey();
				if (!m2.containsKey(key) || !equals(m1.get(key), m2.get(key)))
					return false;
			}
			return true;
		} else if (a1.getClass().isArray()) {
			if (a2.getClass().isArray()) {
				int len = Array.getLength(a1);
				if (len != Array.getLength(a2))
					return false;
				for (int i = 0; i < len; i++) {
					if (!equals(Array.get(a1, i), Array.get(a2, i)))
						return false;
				}
				return true;
			} else if (a2 instanceof List) {
				return equals(a1, LE.coll2array((List<Object>) a2, Object.class));
			}
			return false;
		} else if (a1 instanceof List) {
			if (a2 instanceof List) {
				List<?> l1 = (List<?>) a1;
				List<?> l2 = (List<?>) a2;
				if (l1.size() != l2.size())
					return false;
				int i = 0;
				for (Iterator<?> it = l1.iterator(); it.hasNext();) {
					if (!equals(it.next(), l2.get(i++)))
						return false;
				}
				return true;
			} else if (a2.getClass().isArray()) {
				return equals(LE.coll2array((List<Object>) a1, Object.class), a2);
			}
			return false;
		} else if (a1 instanceof Collection && a2 instanceof Collection) {
			Collection<?> c1 = (Collection<?>) a1;
			Collection<?> c2 = (Collection<?>) a2;
			if (c1.size() != c2.size())
				return false;
			return c1.containsAll(c2) && c2.containsAll(c1);
		}
		return false;
	}

	/**
	 * 必须是同一类型的Bean比较<br>
	 * 深层次使用LE.equals方法对比Field的实际值，对某些特别的值进行了特别的trim，处理具体看{@link #equal_trick_trim}方法，如"NULL"字符串认为就是null
	 * @param bean1
	 * 			比较对象1
	 * @param bean2
	 * 			比较对象2
	 * @return
	 * 			两个Bean的内容是否相同
	 */
	public static boolean equalsForBean(Object bean1, Object bean2) {

		if (bean1.getClass() != bean2.getClass())
			return false;

		CE ce = CE.of(bean1.getClass());

		Field[] fields = ce.getFields();

		Set<String> fns = new HashSet<String>();

		for (Field field : fields) {
			fns.add(field.getName());
			try {
				//特别的trim处理
				LE.makeAccessible(field);
				Object v1 = equal_trick_trim(field.get(bean1));
				Object v2 = equal_trick_trim(field.get(bean2));
				if (!LE.equals(v1, v2)) {
					return false;
				}
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 使用CE查找，调用某个对象(包括父类)的任意指定的方法 <br></>
	 * 可以是public 也可以是private ...
	 * 
	 * @param src  
	 * 			被调用对象实例（类型和CE类型相同）
	 * @param name  
	 * 			方法名称
	 * @param value 
	 * 			方法参数值数组
	 * 
	 * @return 方法调用的返回
	 */
	public static Object invoke(Object src, String name, Object... value) {
		Class<?>[] param_cls = LE.evalToTypes(value);
		CE<?> ce = CE.of(src);
		try {
			Method method = ce.findMethod(name, param_cls);
			LE.makeAccessible(method);
			return method.invoke(src, value);
		} catch (NoSuchMethodException e) {
			throw new FailToInvokingException(e, "Can not find any method [ %s ( %s ) ] on class[ %s ]", name,
					value.getClass(), src.getClass());
		} catch (Throwable e) {
			throw new FailToInvokingException(e, "Invoke method [ %s ] on class[ %s ] error.", name, src.getClass());
		}
	}

	/**
	 * 获取对象的属性名称数组
	 *
	 * @return Array of property names, or null if an error occurred.
	 */
	public static String[] getPropertyNames(Class<?> clazz) {
		return getPropertyNames(clazz, false);
	}

	private static String[] getPropertyNames(Class<?> clazz, boolean only4Copy) {
		List<String> ret_all = new ArrayList<String>();
		List<String> ret = new ArrayList<String>();
		Field[] fields = CE.of(clazz).getFields(Object.class);
		for (Field fld : fields) {
			if (fld.getModifiers() == Modifier.PUBLIC) {
				ret.add(fld.getName());
			}
			ret_all.add(fld.getName());
		}
		BeanInfo info = null;
		try {
			info = Introspector.getBeanInfo(clazz);
		} catch (IntrospectionException e) {
		}
		if (info != null) {
			PropertyDescriptor[] properties = info.getPropertyDescriptors();
			for (int i = 0; i < properties.length; i++) {
				PropertyDescriptor pd = properties[i];
				if (ret_all.contains(pd.getName()) && !ret.contains(pd.getName())) {
					if (!only4Copy || (pd.getReadMethod() != null && pd.getWriteMethod() != null))
						ret.add(properties[i].getName());
				}
			}
		}
		return ret.toArray(new String[ret.size()]);
	}

	/**
	 * 获取一个对象的属性值(除了传统的Getter，还可以容忍PUBLIC Field)。<br></>
	 * 不查找方法(没有使用CE)，直接尝试调用一个对象各类可能的方法(多是getter应用)<br></>
	 * 按照  obj.property. obj.getProperty(), obj.isProperty(), obj.property(),. 的顺序依次尝试执行
	 * 如果有异常扔出 FailToInvokingException
	 * @param object 
	 * 			实际对象本身
	 * @param property 
	 * 			对象的属性
	 * @return
	 * 			对象的属性值 
	 */

	public static Object getPropertyValue(Object object, String property) {
		Class<?> clzz = object.getClass();
		try {
			Field fld = object.getClass().getField(property);
			return fld.get(object);
		} catch (Throwable t) {
			Method m = null;
			try {
				m = clzz.getMethod("get" + StringUtils.capitalize(property));
			} catch (NoSuchMethodException t1) {
				try {
					m = clzz.getMethod("is" + StringUtils.capitalize(property));
				} catch (NoSuchMethodException t2) {
					try {
						m = clzz.getMethod(property);
					} catch (NoSuchMethodException e) {
						throw new FailToGetValueException(
								"Can NOT find any public property or getter named [ %s ] on class[ %s ]", property,
								object.getClass());
					}
				}
			}
			try {
				return m.invoke(object);
			} catch (Throwable e) {
				throw new FailToGetValueException(e, "Get property[%s] value on class[%s] failed.", property,
						object.getClass());
			}
		}
	}

	/**
	 * 设置对象的属性值
	 * @param bean
	 * @param property
	 * @param value
	 */
	public static void setPropertyValue(Object bean, String property, Object value) {
		Class<?> clss = bean.getClass();
		try {
			Field fld = clss.getField(property);
			fld.set(bean, value);
		} catch (Throwable t) {
			Method m = null;
			try {
				PropertyDescriptor pd = new PropertyDescriptor(property, clss);
				m = pd.getWriteMethod();
			} catch (IntrospectionException e) {
				throw new FailToSetValueException(e, "Failed to find writeMethod of property[%s] on class[%s] failed.",
						property, bean.getClass());
			}

			if (null == m) {
				throw new FailToSetValueException("Failed to find writeMethod of property[%s] on class[%s] failed.",
						property, bean.getClass());
			}

			try {
				m.invoke(bean, value);
			} catch (Throwable e) {
				throw new FailToSetValueException(e, "Set property[%s] value[%s] with type[%s] on class[%s] failed.",
						property, value, value == null ? "NULL type" : LE.toString(CE.of(value.getClass())
								.getExtractTypes(), ""), bean.getClass());
			}

		}
	}

	/**
	 * 两个对象必须是相同类型的Bean 即getClass()相等，具备相同的方法属性签名，进行copy动作，可用于clone方法的内部实现
	 * @param src
	 * 			源对象
	 * @param to
	 * 			目标对象（必须已经实例化）
	 */
	public static void copy(Object src, Object to) {

		if (src.getClass() != to.getClass())
			return;
		String[] props = LE.getPropertyNames(to.getClass(), true);
		for (String prop : props) {
			Object value = LE.getPropertyValue(src, prop);
			LE.setPropertyValue(to, prop, value);
		}
	}

	/**
	 * 对一些特别的参数进行的trick_trim动作。
	 * @param v
	 * @return
	 */
	public static Object equal_trick_trim(Object v) {

		if (v == null) {
			return v;
		}

		if (v instanceof String) {
			if (v.equals("null") || ((String) v).trim().length() == 0) {
				return null;
			}
		}

		if (v instanceof Boolean || boolean.class == v.getClass()) {
			if (!((Boolean) v).booleanValue()) {
				return null;
			}
		}

		return v;
	}

	/**
	 * 判断一个数组内是否包括某一个对象。 它的比较将通过 equals(Object,Object) 方法
	 *
	 * @param array
	 *            数组
	 * @param ele
	 *            对象
	 * @return true 包含 false 不包含
	 */
	public static <T> boolean contains(T[] array, T ele) {
		if (null == array)
			return false;
		for (T e : array) {
			if (equals(e, ele))
				return true;
		}
		return false;
	}

	/**
	 * 从一个文本输入流读取所有内容，并将该流关闭
	 *
	 * @param reader
	 *            文本输入流
	 * @return 输入流所有内容
	 */
	public static String readAll(Reader reader) {
		if (!(reader instanceof BufferedReader))
			reader = new BufferedReader(reader);
		try {
			StringBuilder sb = new StringBuilder();

			char[] data = new char[64];
			int len;
			while (true) {
				if ((len = reader.read(data)) == -1)
					break;
				sb.append(data, 0, len);
			}
			return sb.toString();
		} catch (IOException e) {
			throw LE.wrapThrow(e);
		} finally {
			IOUtils.close(reader);
		}
	}

	/**
	 * 将一段字符串写入一个文本输出流，并将该流关闭
	 *
	 * @param writer
	 *            文本输出流
	 * @param str
	 *            字符串
	 */
	public static void writeAll(Writer writer, String str) {
		try {
			writer.write(str);
			writer.flush();
		} catch (IOException e) {
			throw LE.wrapThrow(e);
		} finally {
			IOUtils.close(writer);
		}
	}

	/**
	 * 根据一段文本模拟出一个输入流对象 <br/>
	 *
	 * @param cs
	 *            文本
	 * @return 输出流对象
	 */
	public static InputStream ins(CharSequence cs) {
		return new StringInputStream(cs);
	}

	/**
	 * 根据一段文本模拟出一个文本输入流对象
	 *
	 * @param cs
	 *            文本
	 * @return 文本输出流对象
	 */
	public static Reader inr(CharSequence cs) {
		return new StringReader2(cs);
	}

	/**
	 * 根据一个 StringBuilder 模拟一个文本输出流对象
	 *
	 * @param sb
	 *            StringBuilder 对象
	 * @return 文本输出流对象
	 */
	public static Writer opw(StringBuilder sb) {
		return new StringWriter2(sb);
	}

	/**
	 * 根据一个 StringBuilder 模拟一个输出流对象
	 *
	 * @param sb
	 *            StringBuilder 对象
	 * @return 输出流对象
	 */
	public static StringOutputStream ops(StringBuilder sb) {
		return new StringOutputStream(sb);
	}

	/**
	 * 较方便的创建一个数组，比如：
	 *
	 * <pre>
	 * Pet[] pets = LE.array(pet1, pet2, pet3);
	 * </pre>
	 *
	 * @param eles
	 *            可变参数
	 * @return 数组对象
	 */
	public static <T> T[] array(T... eles) {
		return eles;
	}

	/**
	 * Returns true if the <code>Method</code> creating is a getter, meaning if
	 * it's getName starts with "get" or "is", takes no parameters, and returns a
	 * symbol. False if not.
	 *
	 * @param method The method to validate if it is a getter method.
	 * @return True if the <code>Method</code> creating is a getter. False if
	 *         not.
	 */
	public static boolean isGetter(Method method) {

		if (method.getParameterTypes().length > 0) {
			return false;
		}

		if (method.getReturnType() == void.class || method.getReturnType() == null) {
			return false;
		}

		return method.getName().startsWith("get") || method.getName().startsWith("is");
	}

	/**
	 * Returns true if the <code>Method</code> creating is a setter method,
	 * meaning if the method getName starts with "set" and it takes exactly one
	 * parameter.
	 *
	 * @param member The <code>Method</code> creating to validate if is a setter
	 *               method.
	 * @return True if the <code>Method</code> creating is a setter. False if
	 *         not.
	 */
	public static boolean isSetter(Method member) {

		if (!member.getName().startsWith("set")) {
			return false;
		}
		if (member.getParameterTypes().length != 1) {
			return false;
		}
		return true;
	}

	/**
	 * 是否规定的忽略字段
	 * 
	 * @param f
	 * @return
	 */
	public static boolean isIgnoredField(Field f) {
		int mods = f.getModifiers();
		return Modifier.isStatic(mods) || Modifier.isFinal(mods) || Modifier.isTransient(mods)
				|| f.getName().startsWith("this$");
	}

	/**
	 * 判断一个对象是否是数组
	 * @param o
	 * 			任意对象
	 * @return 是否数组
	 */
	public static boolean isArray(Object o) {
		if (o == null)
			return false;
		return o.getClass().isArray();
	}

	/**
	 * 判断一个对象是否为空。它支持如下对象类型：
	 * <ul>
	 * <li>null : 一定为空
	 * <li>数组
	 * <li>集合
	 * <li>Map
	 * <li>其他对象 : 一定不为空
	 * </ul>
	 *
	 * @param o
	 *         	任意对象
	 * @return 是否为空
	 */
	public static boolean isEmpty(Object o) {
		if (o == null)
			return true;
		if (o instanceof Collection<?>)
			return ((Collection<?>) o).isEmpty();
		if (o instanceof Map<?, ?>)
			return ((Map<?, ?>) o).isEmpty();
		if (o.getClass().isArray())
			return Array.getLength(o) == 0;
		if (o instanceof String)
			return StringUtils.isEmpty((String) o);
		return false;
	}

	/**
	 * 判断一个对象是否不为空。
	 * @param obj
	 *            任意对象
	 * @return 是否不为空
	 */
	public static boolean isNotEmpty(Object obj) {
		return !isEmpty(obj);
	}

	/**
	 * 判断一个数组是否是空数组
	 *
	 * @param ary
	 *            数组
	 * @return null 或者空数组都为 true 否则为 false
	 */
	public static <T> boolean isEmptyArray(T[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 较方便的创建一个列表，比如：
	 *
	 * <pre>
	 * List《Pet》 pets = LE.list(pet1, pet2, pet3);
	 * </pre>
	 *
	 * 注，这里的 List，是 ArrayList 的实例
	 *
	 * @param eles
	 *            可变参数
	 * @return 列表对象
	 */
	public static <T> ArrayList<T> list(T... eles) {
		ArrayList<T> list = new ArrayList<T>(eles.length);
		for (T ele : eles)
			list.add(ele);
		return list;
	}

	/**
	 * 将多个数组，合并成一个数组。如果这些数组为空，则返回 null
	 *
	 * @param arys
	 *            数组对象
	 * @return 合并后的数组对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] merge(T[]... arys) {
		Queue<T> list = new LinkedList<T>();
		for (T[] ary : arys)
			if (null != ary)
				for (T e : ary)
					if (null != e)
						list.add(e);
		if (list.isEmpty())
			return null;
		Class<T> type = (Class<T>) list.peek().getClass();
		return list.toArray((T[]) Array.newInstance(type, list.size()));
	}

	/**
	 * 将一个数组转换成字符串
	 * <p>
	 * 所有的元素都被格式化字符串包裹。 这个格式话字符串只能有一个占位符， %s, %d 等，均可，请视你的数组内容而定
	 *
	 * @param fmt
	 *            格式
	 * @param arrays
	 *            数组
	 * @return 拼合后的字符串
	 */
	public static <T> StringBuilder joinBy(String fmt, T[] arrays) {
		StringBuilder sb = new StringBuilder();
		for (T obj : arrays)
			sb.append(String.format(fmt, obj));
		return sb;
	}

	/**
	 * 将一个数组转换成字符串
	 * <p>
	 * 所有的元素都被格式化字符串包裹。 这个格式话字符串只能有一个占位符， %s, %d 等，均可，请视你的数组内容而定
	 * <p>
	 * 每个元素之间，都会用一个给定的字符分隔
	 *
	 * @param ptn
	 *            格式
	 * @param glue
	 *            分隔符
	 * @param arrays
	 *            数组
	 * @return 拼合后的字符串
	 */
	public static <T> StringBuilder joinBy(String ptn, Object glue, T[] arrays) {
		StringBuilder sb = new StringBuilder();
		for (T obj : arrays)
			sb.append(String.format(ptn, obj)).append(glue);
		if (sb.length() > 0)
			sb.deleteCharAt(sb.length() - String.valueOf(glue).length());
		return sb;
	}

	/**
	 * 将一个数组转换成字符串
	 * <p>
	 * 每个元素之间，都会用一个给定的字符分隔
	 *
	 * @param glue
	 *            分隔符
	 * @param arrays
	 *            数组
	 * @return 拼合后的字符串
	 */
	public static <T> StringBuilder join(Object glue, T[] arrays) {
		StringBuilder sb = new StringBuilder();
		if (null == arrays || 0 == arrays.length)
			return sb;

		sb.append(arrays[0]);
		for (int i = 1; i < arrays.length; i++)
			sb.append(glue).append(arrays[i]);

		return sb;
	}

	/**
	* 将一个数组的部分元素转换成字符串
	* <p>
	* 每个元素之间，都会用一个给定的字符分隔
	*
	*
	* @param glue
	*            分隔符
	* @param objs
	*            数组
	* @param offset
	*            开始元素的下标
	* @param len
	*            元素数量
	* @return 拼合后的字符串
	*/
	public static <T> StringBuilder join(Object glue, T[] objs, int offset, int len) {
		StringBuilder sb = new StringBuilder();
		if (null == objs || len < 0 || 0 == objs.length)
			return sb;

		if (offset < objs.length) {
			sb.append(objs[offset]);
			for (int i = 1; i < len && i + offset < objs.length; i++) {
				sb.append(glue).append(objs[i + offset]);
			}
		}
		return sb;
	}

	/**
	 * 将一个长整型数组转换成字符串
	 * <p>
	 * 每个元素之间，都会用一个给定的字符分隔
	 *
	 * @param glue
	 *            粘合符
	 * @param vals
	 *            数组
	 * @return 拼合后的字符串
	 */
	public static StringBuilder join(Object glue, long[] vals) {
		StringBuilder sb = new StringBuilder();
		if (null == vals || 0 == vals.length)
			return sb;

		sb.append(vals[0]);
		for (int i = 1; i < vals.length; i++)
			sb.append(glue).append(vals[i]);

		return sb;
	}

	/**
	 * 将一个整型数组转换成字符串
	 * <p>
	 * 每个元素之间，都会用一个给定的字符分隔
	 *
	 * @param glue
	 *            粘合符
	 * @param vals
	 *            数组
	 * @return 拼合后的字符串
	 */
	public static StringBuilder join(Object glue, int[] vals) {
		StringBuilder sb = new StringBuilder();
		if (null == vals || 0 == vals.length)
			return sb;

		sb.append(vals[0]);
		for (int i = 1; i < vals.length; i++)
			sb.append(glue).append(vals[i]);

		return sb;
	}

	/**
	 * 将一个数组所有元素拼合成一个字符串
	 *
	 * @param arrays
	 *            数组
	 * @return 拼合后的字符串
	 */
	public static <T> StringBuilder join(T[] arrays) {
		StringBuilder sb = new StringBuilder();
		for (T e : arrays)
			sb.append(String.valueOf(e));
		return sb;
	}

	/**
	 * 将一个数组部分元素拼合成一个字符串
	 *
	 *
	 * @param array
	 *            数组
	 * @param offset
	 *            开始元素的下标
	 * @param len
	 *            元素数量
	 * @return 拼合后的字符串
	 */
	public static <T> StringBuilder join(T[] array, int offset, int len) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			sb.append(String.valueOf(array[i + offset]));
		}
		return sb;
	}

	/**
	 * 将一个集合转换成字符串
	 * <p>
	 * 每个元素之间，都会用一个给定的字符分隔
	 *
	 * @param glue
	 *            分隔符
	 * @param coll
	 *            集合
	 * @return 拼合后的字符串
	 */
	public static <T> StringBuilder join(Object glue, Collection<T> coll) {
		StringBuilder sb = new StringBuilder();
		if (null == coll || coll.isEmpty())
			return sb;
		Iterator<T> it = coll.iterator();
		sb.append(it.next());
		while (it.hasNext())
			sb.append(glue).append(it.next());
		return sb;
	}

	/**
	 * 将一个集合变成 Map。
	 *
	 * @param typeOfMap
	 *            Map 的类型
	 * @param coll
	 *            集合对象
	 * @param keyFieldName
	 *            采用集合中元素的哪个一个字段为键。
	 * @return Map 对象
	 */
	public static <T extends Map<Object, Object>> Map<?, ?> coll2map(Class<T> typeOfMap, Collection<?> coll,
			String keyFieldName) {
		if (null == coll)
			return null;
		Map<Object, Object> map = createMap(typeOfMap);
		if (coll.size() > 0) {
			Iterator<?> it = coll.iterator();
			Object obj = it.next();
			Object key = LE.getPropertyValue(obj, keyFieldName);
			map.put(key, obj);
			for (; it.hasNext();) {
				obj = it.next();
				key = LE.getPropertyValue(obj, keyFieldName);
				map.put(key, obj);
			}
		}
		return map;
	}

	/**
	 * 将集合编程变成指定类型的列表
	 *
	 * @param col
	 *            集合对象
	 * @param eleType
	 *            列表类型
	 * @return 列表对象
	 */
	public static <E> List<E> coll2list(Collection<?> col, Class<E> eleType) {
		if (null == col)
			return null;
		List<E> list = new ArrayList<E>(col.size());
		for (Object obj : col)
			list.add(Coercer.get().coerce(obj, eleType));
		return list;
	}

	/**
	 * 将集合变成数组，数组的类型为集合的第一个元素的类型。如果集合为空，则返回 null
	 *
	 * @param coll
	 *            集合对象
	 * @return 数组
	 */
	@SuppressWarnings("unchecked")
	public static <E> E[] coll2array(Collection<E> coll) {
		if (coll == null || coll.size() == 0)
			return (E[]) new Object[0];

		Class<E> eleType = (Class<E>) LE.first(coll).getClass();
		return coll2array(coll, eleType);
	}

	/**
	 * 将集合变成指定类型的数组
	 *
	 * @param coll
	 *            集合对象
	 * @param elemType
	 *            数组元素类型
	 * @return 数组
	 */
	@SuppressWarnings("unchecked")
	public static <E> E[] coll2array(Collection<?> coll, Class<E> elemType) {
		if (null == coll)
			return null;
		Object re = Array.newInstance(elemType, coll.size());
		int i = 0;
		for (Iterator<?> it = coll.iterator(); it.hasNext();) {
			Object obj = it.next();
			if (null == obj)
				Array.set(re, i++, null);
			else
				Array.set(re, i++, Coercer.get().coerce(obj, elemType));
		}
		return (E[]) re;
	}

	/**
	 * 将一个数组变成 Map
	 *
	 * @param typeOfMap
	 *            Map 的类型
	 * @param array
	 *            数组
	 * @param keyFieldName
	 *            采用集合中元素的哪个一个字段为键。
	 * @return Map 对象
	 */
	public static <T extends Map<Object, Object>> Map<?, ?> array2map(Class<T> typeOfMap, Object array,
			String keyFieldName) {
		if (array == null)
			return null;
		Map<Object, Object> map = createMap(typeOfMap);
		int len = Array.getLength(array);
		if (len > 0) {
			Object obj = Array.get(array, 0);
			//CE<?> ce = CE.of(obj.getClass());
			for (int i = 0; i < len; i++) {
				obj = Array.get(array, i);
				Object key = LE.getPropertyValue(obj, keyFieldName);
				map.put(key, obj);
			}
		}
		return map;
	}

	private static <T extends Map<Object, Object>> Map<Object, Object> createMap(Class<T> mapClass) {
		Map<Object, Object> map;
		try {
			map = mapClass.newInstance();
		} catch (Exception e) {
			map = new HashMap<Object, Object>();
		}
		if (!mapClass.isAssignableFrom(map.getClass())) {
			throw LE.makeThrow("Fail to create map [%s]", mapClass.getName());
		}
		return map;
	}

	/**
	 * 将数组转换成一个列表。
	 *
	 * @param array
	 *            原始数组
	 * @return 新列表
	 */
	public static <T> List<T> array2list(T[] array) {
		if (null == array)
			return null;
		List<T> re = new ArrayList<T>(array.length);
		for (T obj : array)
			re.add(obj);
		return re;
	}

	/**
	 * 将数组转换成一个列表。将会采用Coercion 来深层转换数组元素
	 *
	 * @param array
	 *            原始数组
	 * @param eleType
	 *            新列表的元素类型
	 * @return 新列表
	 *
	 */
	public static <T, E> List<E> array2list(Object array, Class<E> eleType) {
		if (null == array)
			return null;
		int len = Array.getLength(array);
		List<E> re = new ArrayList<E>(len);
		for (int i = 0; i < len; i++) {
			Object obj = Array.get(array, i);
			re.add(Coercer.get().coerce(obj, eleType));
		}
		return re;
	}

	/**
	 * 将数组转换成另外一种类型的数组。将会采用 CoercionTuple 来深层转换数组元素
	 *
	 * @param array
	 *            原始数组
	 * @param eleType
	 *            新数组的元素类型
	 * @return 新数组
	 * @throws com.facetime.core.coercion.street.common.coercion.FailToCoerceException
	 */
	public static Object array2array(Object array, Class<?> eleType) throws FailToCoerceException {
		if (array == null)
			return null;
		int len = Array.getLength(array);
		Object re = Array.newInstance(eleType, len);
		for (int i = 0; i < len; i++) {
			Array.set(re, i, Coercer.get().coerce(Array.get(array, i), eleType));
		}
		return re;
	}

	/**
	 * 将一组对象，变成一组类型
	 *
	 * @param args
	 *            对象数组
	 * @return 类型数组
	 */
	public static Class<?>[] evalToTypes(Object... args) {
		if (args == null || args.length == 0)
			return new Class<?>[0];
		Class<?>[] types = new Class[args.length];
		int i = 0;
		for (Object arg : args)
			types[i++] = null == arg ? Object.class : arg.getClass();
		return types;
	}

	/**
	 * 将一个 Object[] 数组，变成一个真正的数组 T[]
	 *
	 * @param args
	 *            数组
	 * @return 新数组,如果数组中包括了 null，或者数组的类型不一致，则返回旧数组
	 */
	public static Object evalArgToRealArray(Object... args) {

		if (args == null || args.length == 0 || args[0] == null)
			return null;

		Object result = null;
		/*
		 * Check inside the arguments list, to see if all element is in same
		 * type
		 */
		Class<?> type = null;
		for (Object arg : args) {
			if (arg == null)
				break;
			if (null == type) {
				type = arg.getClass();
				continue;
			}
			if (arg.getClass() != type) {
				type = null;
				break;
			}
		}
		/*
		 * If all argument elements in same type, make a new ArrayEx by the Type
		 */
		if (type != null) {
			result = Array.newInstance(type, args.length);
			for (int i = 0; i < args.length; i++) {
				Array.set(result, i, args[i]);
			}
			return result;
		}
		return args;
	}

	/**
	 * 一个方法的参数类型同一个给定的参数数组是否可以匹配
	 *
	 * @param methodParamTypes
	 *            参数类型列表
	 * @param args
	 *            参数
	 * @return 匹配类型
	 *
	 * @see MatchType
	 */
	public static MatchType matchParamTypes(Class<?>[] methodParamTypes, Object... args) {
		return matchParamTypes(methodParamTypes, evalToTypes(args));
	}

	/**
	 * 匹配一个函数声明的参数类型数组和一个调用参数数组
	 *
	 * @param paramTypes
	 *            函数声明参数数组
	 * @param argTypes
	 *            调用参数数组
	 * @return 匹配类型
	 *
	 * @see MatchType
	 */
	public static MatchType matchParamTypes(Class<?>[] paramTypes, Class<?>[] argTypes) {
		int len = argTypes == null ? 0 : argTypes.length;
		if (len == 0 && paramTypes.length == 0)
			return MatchType.YES;
		if (paramTypes.length == len) {
			for (int i = 0; i < len; i++)
				if (!CE.of(argTypes[i]).isOf((paramTypes[i])))
					return MatchType.NO;
			return MatchType.YES;
		} else if (len + 1 == paramTypes.length) {
			if (!paramTypes[len].isArray())
				return MatchType.NO;
			for (int i = 0; i < len; i++)
				if (!CE.of(argTypes[i]).isOf((paramTypes[i])))
					return MatchType.NO;
			return MatchType.LACK;
		}
		return MatchType.NO;
	}

	/**
	 * 根据参数类型数组的最后一个类型（一定是数组，表示变参），为最后一个变参生成一个空数组
	 *
	 * @param pts
	 *            函数参数类型列表
	 * @return 变参空数组
	 */
	public static Object[] blankArrayArg(Class<?>[] pts) {
		return (Object[]) Array.newInstance(pts[pts.length - 1].getComponentType(), 0);
	}

	/**
	 * 将数组转换成Object[] 数组。将会采用 CoercionTuple 来深层转换数组元素
	 *
	 * @param args
	 *            原始数组
	 * @param pts
	 *            新数组的元素类型
	 * @return 新数组
	 * @throws com.facetime.core.coercion.street.common.coercion.FailToCoerceException
	 *
	 */
	public static <T> Object[] array2ObjectArray(T[] args, Class<?>[] pts) throws FailToCoerceException {
		if (null == args)
			return null;
		Object[] newArgs = new Object[args.length];
		for (int i = 0; i < args.length; i++) {
			newArgs[i] = Coercer.get().coerce(args[i], pts[i]);
		}
		return newArgs;
	}

	/**
	 * 将描述数字的字符串转换为数字对象，变成数字对象，现支持的格式为：
	 * <ul>
	 * <li>null - 整数 0</li>
	 * <li>23.78 - 浮点 Float</li>
	 * <li>0x45 - 16进制整数 Integer</li>
	 * <li>78L - 长整数 Long</li>
	 * <li>69 - 普通整数 Integer</li>
	 * </ul>
	 *
	 * @param s
	 *            参数
	 * @return 数字对象
	 */
	public static Number toNumber(String s) {
		// null 值
		if (null == s)
			return 0;

		s = s.toLowerCase();
		// 浮点
		if (s.indexOf('.') != -1) {
			char c = s.charAt(s.length() - 1);
			if (c == 'f') {
				return Float.valueOf(s);
			}
			return Double.valueOf(s);
		}

		// 16进制整数
		if (s.startsWith("0x")) {
			try {
				return Integer.valueOf(s.substring(2), 16);
			} catch (Exception e) {
				return new BigInteger(s.substring(2), 16);
			}
		}
		// 普通整数
		Long re = Long.parseLong(s);
		// 范围判断
		if (Integer.MAX_VALUE >= re && re >= Integer.MIN_VALUE) {
			return re.intValue();
		}
		return re;
	}

	/**
	 * 将字符数组强制转换成字节数组。如果字符为双字节编码，则会丢失信息
	 *
	 * @param cs
	 *            字符数组
	 * @return 字节数组
	 */
	public static byte[] toBytes(char[] cs) {
		byte[] bs = new byte[cs.length];
		for (int i = 0; i < cs.length; i++)
			bs[i] = (byte) cs[i];
		return bs;
	}

	/**
	 * 将整数数组强制转换成字节数组。整数的高位将会被丢失
	 *
	 * @param is
	 *            整数数组
	 * @return 字节数组
	 */
	public static byte[] toBytes(int[] is) {
		byte[] bs = new byte[is.length];
		for (int i = 0; i < is.length; i++)
			bs[i] = (byte) is[i];
		return bs;
	}

	/**
	 * 打断 each 循环
	 */
	public static void exist() throws ExitLoop {
		throw new ExitLoop();
	}

	/**
	 * 用回调的方式，遍历一个对象，可以支持遍历
	 * <ul>
	 * <li>数组
	 * <li>集合
	 * <li>Map
	 * <li>单一元素
	 * </ul>
	 *
	 * @param elems
	 *            对象
	 * @param callback
	 *            回调
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> void each(Object elems, Each<T> callback) {
		if (null == elems || null == callback)
			return;
		try {
			Class<T> eType = getTypeParam(callback.getClass(), 0);
			if (elems.getClass().isArray()) {
				int len = Array.getLength(elems);
				for (int i = 0; i < len; i++)
					try {
						callback.loop(i, (T) Array.get(elems, i), len);
					} catch (ExitLoop e) {
						break;
					}
			} else if (elems instanceof Collection) {
				int len = ((Collection) elems).size();
				int i = 0;
				for (Iterator<T> it = ((Collection) elems).iterator(); it.hasNext();)
					try {
						callback.loop(i++, it.next(), len);
					} catch (ExitLoop e) {
						break;
					}
			} else if (elems instanceof Map) {
				Map map = (Map) elems;
				int len = map.size();
				int i = 0;
				if (null != eType && eType != Object.class && eType.isAssignableFrom(Map.Entry.class)) {
					for (Object v : map.entrySet())
						try {
							callback.loop(i++, (T) v, len);
						} catch (ExitLoop e) {
							break;
						}

				} else {
					for (Object v : map.entrySet())
						try {
							callback.loop(i++, (T) ((Map.Entry) v).getValue(), len);
						} catch (ExitLoop e) {
							break;
						}
				}
			} else if (elems instanceof Iterator<?>) {
				Iterator<?> it = (Iterator<?>) elems;
				int i = 0;
				while (it.hasNext()) {
					try {
						callback.loop(i++, (T) it.next(), -1);
					} catch (ExitLoop e) {
						break;
					}
				}
			} else
				try {
					callback.loop(0, (T) elems, 1);
				} catch (ExitLoop e) {
				}
		} catch (LoopException e) {
			throw LE.wrapThrow(e.getCause());
		}
	}

	/**
	 * 取得第一个对象，无论是 数组，集合还是 Map。如果是一个一般 JAVA 对象，则返回自身
	 *
	 * @param obj
	 *            任意对象
	 * @return 第一个代表对象
	 */
	public static Object first(Object obj) {
		final Object[] re = new Object[1];
		each(obj, new Each<Object>() {
			public void loop(int i, Object obj, int length) throws ExitLoop {
				re[0] = obj;
				LE.exist();
			}
		});
		return re[0];
	}

	/**
	 * 获取集合中的第一个元素，如果集合为空，返回 null
	 *
	 * @param coll
	 *            集合
	 * @return 第一个元素
	 */
	public static <T> T first(Collection<T> coll) {
		if (null == coll || coll.isEmpty())
			return null;
		return coll.iterator().next();
	}

	/**
	 * 获得表中的第一个名值对
	 *
	 * @param map
	 *            表
	 * @return 第一个名值对
	 */
	public static <K, V> Map.Entry<K, V> first(Map<K, V> map) {
		if (null == map || map.isEmpty())
			return null;
		return map.entrySet().iterator().next();
	}

	/**
	 * 将一个抛出对象的异常堆栈，显示成一个字符串
	 *
	 * @param e
	 *            抛出对象
	 * @return 异常堆栈文本
	 */
	public static String getStackTrace(Throwable e) {
		StringBuilder sb = new StringBuilder();
		StringOutputStream sbo = new StringOutputStream(sb);
		PrintStream ps = new PrintStream(sbo);
		e.printStackTrace(ps);
		ps.flush();
		return sbo.toString();
	}

	/**
	 * 将字符串解析成 boolean 值，支持更多的字符串
	 * <ul>
	 * <li>1 | 0
	 * <li>yes | no
	 * <li>on | off
	 * <li>true | false
	 * </ul>
	 *
	 * @param s
	 * @return 布尔值
	 */
	public static boolean parseBoolean(String s) {
		if (null == s || s.length() == 0)
			return false;
		if ("0".equals(s))
			return false;
		s = s.toLowerCase();
		return "true".equals(s) || "yes".equals(s) || "t".equals(s) || "on".equals(s);
	}

	/**
	 * 对Thread.sleep(long)的简单封装,不抛出任何异常
	 *
	 * @param millisecond
	 *            休眠时间
	 */
	public void sleepQuite(long millisecond) {
		try {
			if (millisecond > 0)
				Thread.sleep(millisecond);
		} catch (Throwable e) {
		}
	}

	/**
	 * 对obj进行castToString()操作,如果为null返回def中定义的值
	 *
	 * @param src
	 * @param def
	 *            如果src==null返回的内容
	 * @return 任意对象的toString, 先用coercion转，转不了就toString()
	 */
	public static String toString(Object src, String def) {
		if (src == null)
			return def;
		try {
			return Coercer.get().coerce(src, String.class);
		} catch (FailToCoerceException e) {
			return String.valueOf(src);
		}
	}

	/**
	 * 得到实际Type类型对应的Class
	 */
	public static Class<?> asClass(Type actualType) {

		if (actualType instanceof Class)
			return (Class<?>) actualType;

		if (actualType instanceof ParameterizedType) {
			final Type rawType = ((ParameterizedType) actualType).getRawType();
			// The sun implementation returns getRawType as Class<?>, but there is room in the interface for it to be
			// some other Type. We'll assume it's a Class.
			// TODO: consider logging or throwing our own exception for that day when "something else" causes some confusion
			return (Class) rawType;
		}

		if (actualType instanceof GenericArrayType) {
			final Type type = ((GenericArrayType) actualType).getGenericComponentType();
			return Array.newInstance(asClass(type), 0).getClass();
		}

		if (actualType instanceof TypeVariable) {
			// Support for List<T extends Number>
			// There is always at least one bound. If no bound is specified in the source then it will be Object.class
			return asClass(((TypeVariable) actualType).getBounds()[0]);
		}

		if (actualType instanceof WildcardType) {
			final WildcardType wildcardType = (WildcardType) actualType;
			final Type[] bounds = wildcardType.getLowerBounds();
			if (bounds != null && bounds.length > 0) {
				return asClass(bounds[0]);
			}
			// If there is no lower bounds then the only thing that makes sense is Object.
			return Object.class;
		}
		throw new RuntimeException(String.format("Unable to convert %s to Class.", actualType));
	}

	/**
	 * 获取一个类的泛型参数数组，如果这个类没有泛型参数，返回 null
	 */
	public static Type[] extractTypeParams(Class<?> clazz) {
		if (clazz == null || clazz == Object.class)
			return null;
		// 看看父类
		Type superclass = clazz.getGenericSuperclass();
		if (null != superclass && superclass instanceof ParameterizedType)
			return ((ParameterizedType) superclass).getActualTypeArguments();

		// 看看接口
		Type[] interfaces = clazz.getGenericInterfaces();
		for (Type inf : interfaces) {
			if (inf instanceof ParameterizedType) {
				return ((ParameterizedType) inf).getActualTypeArguments();
			}
		}
		return extractTypeParams(clazz.getSuperclass());
	}

	private static final Pattern PTN = Pattern.compile("(<)(.+)(>)");

	/**
	 * 获取一个字段的泛型参数数组，如果这个字段没有泛型，返回空数组
	 *
	 * @param field
	 *            字段
	 * @return 泛型参数数组
	 */
	public static Class<?>[] getGenericTypes(Field field) {
		String gts = field.toGenericString();
		Matcher m = PTN.matcher(gts);
		if (m.find()) {
			String s = m.group(2);
			String[] ss = StringUtils.splitIgnoreBlank(s, ",");
			if (ss.length > 0) {
				Class<?>[] re = new Class<?>[ss.length];
				for (int i = 0; i < ss.length; i++) {
					String className = ss[i];
					if (className.length() > 0 && className.charAt(0) == '?')
						re[i] = Object.class;
					else {
						int pos = className.indexOf('<');
						if (pos < 0)
							re[i] = ClassUtils.loadClass(className);
						else
							re[i] = ClassUtils.loadClass(className.substring(0, pos));
					}
				}
				return re;
			}
		}
		return new Class<?>[0];
	}

	/**
	 * 获取一个字段的某一个泛型参数，如果没有，返回 null
	 *
	 * @param field
	 *            字段
	 * @return 泛型参数数
	 */
	public static Class<?> getGenericTypes(Field field, int index) {
		Class<?>[] types = getGenericTypes(field);
		if (null == types || types.length <= index)
			return null;
		return types[index];
	}

	/**
	 * 获取一个类的某个一个泛型参数
	 *
	 * @param klass
	 *            类
	 * @param index
	 *            参数下标 （从 0 开始）
	 * @return 泛型参数类型
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getTypeParam(Class<?> klass, int index) {
		Type[] types = extractTypeParams(klass);
		if (index >= 0 && index < types.length) {
			Type t = types[index];
			Class<T> tClass = (Class<T>) LE.asClass(t);
			if (tClass == null)
				throw LE.makeThrow("Type '%s' is not a Class", t.toString());
			return tClass;
		}
		throw LE.makeThrow("Class type param out of range %d/%d", index, types.length);
	}

	/**
	 * @param clazz
	 *            类型
	 * @return 一个类型的包路径
	 */
	public static String getPath(Class<?> clazz) {
		return clazz.getName().replace('.', '/');
	}

	/**
	 * @param parameterTypes
	 *            函数的参数类型数组
	 * @return 参数的描述符
	 */
	public static String getParamDescriptor(Class<?>[] parameterTypes) {
		StringBuilder sb = new StringBuilder();
		sb.append('(');
		for (Class<?> pt : parameterTypes)
			sb.append(getTypeDescriptor(pt));
		sb.append(')');
		String s = sb.toString();
		return s;
	}

	/**
	 * @param method
	 *            方法
	 * @return 这个方法的描述符
	 */
	public static String getMethodDescriptor(Method method) {
		return getParamDescriptor(method.getParameterTypes()) + getTypeDescriptor(method.getReturnType());
	}

	/**
	 * @param c
	 *            构造函数
	 * @return 构造函数的描述符
	 */
	public static String getConstructorDescriptor(Constructor<?> c) {
		return getParamDescriptor(c.getParameterTypes()) + "V";
	}

	/**
	 * @param klass
	 *            类型
	 * @return 获得一个类型的描述符
	 */
	public static String getTypeDescriptor(Class<?> klass) {
		if (klass.isPrimitive()) {
			if (klass == void.class)
				return "V";
			else if (klass == int.class)
				return "I";
			else if (klass == long.class)
				return "J";
			else if (klass == byte.class)
				return "B";
			else if (klass == short.class)
				return "S";
			else if (klass == float.class)
				return "F";
			else if (klass == double.class)
				return "D";
			else if (klass == char.class)
				return "C";
			else
				/* if(_clazz == boolean.class) */
				return "Z";
		}
		StringBuilder sb = new StringBuilder();
		if (klass.isArray()) {
			return sb.append('[').append(getTypeDescriptor(klass.getComponentType())).toString();
		}
		return sb.append('L').append(getPath(klass)).append(';').toString();
	}

	/**
	 * 匹配类型：
	 * <p/>
	 * <ul>
	 * <li>YES: 匹配
	 * <li>LACK: 参数不足
	 * <li>NO: 不匹配
	 * </ul>
	 */
	public static enum MatchType {
		/**
		 * 匹配
		 */
		YES,
		/**
		 * 不足
		 */
		LACK,
		/**
		 * 不匹配
		 */
		NO,
		/**
		 * 长度相同，但是需要转换
		 */
		NEED_CAST
	}

	public static void main(String[] args) {
		String ss = " ";
		System.out.println(LE.isEmpty(ss));
	}
}

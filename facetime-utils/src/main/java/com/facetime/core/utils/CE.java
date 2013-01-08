package com.facetime.core.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.facetime.core.collection.ArrayEx;
import com.facetime.core.creating.Creating;
import com.facetime.core.creating.CreatingContext;
import com.facetime.core.creating.CreatingException;
import com.facetime.core.creating.CreatingUtils;
import com.facetime.core.utils.LE.MatchType;

/**
 * Class Enhancement<>p</>
 * 增强的Class<?>
 * User: dzb
 * Date: 11-8-1
 * Time: 下午9:31
 */
public class CE<T> {

	static class DefaultTypeExtractor implements TypeExtractor {

		public Class<?>[] extract(CE<?> ce) {
			Class<?> theType = ce.getClassType();
			List<Class<?>> re = new ArrayList<Class<?>>(5);

			// 原生类型，增加其外覆类
			if (theType.isPrimitive()) {
				re.add(ce.getWrapperType());
				// 数字
				if (theType != boolean.class && theType != char.class) {
					re.add(Number.class);
				}
			}
			// 日历
			else if (ce.isOf(Calendar.class)) {
				re.add(Calendar.class);
			}
			// 其他类型，直接增加，并试图判断其抽象类
			else {
				re.add(theType);
				// 枚举
				if (ce._clazz.isEnum()) {
					re.add(Enum.class);
				}
				// 数组
				else if (ce._clazz.isArray()) {
					re.add(Array.class);
				}
				// 字符串
				else if (ce.isStringLike())
					re.add(CharSequence.class);
				// 数字
				else if (ce.isNumber()) {
					re.add(Number.class);
				}
				// Map
				else if (ce.isOf(Map.class)) {
					re.add(Map.class);
				}
				// 列表
				else if (ce.isOf(List.class)) {
					re.add(List.class);
					re.add(Collection.class);
				}
				// 集合
				else if (ce.isOf(Collection.class)) {
					re.add(Collection.class);
				}
			}
			// 最后确保 Object 一定被加上了
			if (theType != Object.class)
				re.add(Object.class);

			return re.toArray(new Class<?>[re.size()]);
		}
	}

	private final static DefaultTypeExtractor defaultTypeExtractor = new DefaultTypeExtractor();

	/**
	 * 包裹一个类
	 * @param className 类名
	 * @return
	 */
	public static <T> CE<T> of(String className) {

		try {
			Class c = Class.forName(className);
			return CE.of(c);
		} catch (ClassNotFoundException e) {
			throw LE.makeThrow(e.getMessage());
		}
	}

	/**
	 * 包裹一个类
	 *
	 * @param classOfType
	 *            类
	 * @return ce
	 */
	public static <T> CE<T> of(Class<T> classOfType) {
		return classOfType == null ? null : new CE<T>(classOfType).setTypeExtractor(defaultTypeExtractor);
	}

	/**
	 * 生成一个对象的 ce
	 *
	 * @param obj
	 *            对象。
	 * @return ce， 如果 对象 null，则返回 null
	 */
	@SuppressWarnings("unchecked")
	public static <T> CE<T> of(T obj) {
		return obj == null ? null : (CE<T>) of(obj.getClass());
	}

	/**
	 * 包裹一个类，并设置自定义的类型提炼逻辑
	 *
	 * @param classOfT
	 * @param typeExtractor
	 * @return ce
	 * @see TypeExtractor
	 */
	public static <T> CE<T> of(Class<T> classOfT, TypeExtractor typeExtractor) {
		return null == classOfT ? null : new CE<T>(classOfT)
				.setTypeExtractor(typeExtractor == null ? defaultTypeExtractor : typeExtractor);
	}

	private Class<T> _clazz;

	private TypeExtractor typeExtractor;

	/**
	 * 设置自己的类型提炼逻辑
	 *
	 * @param typeExtractor
	 * @return ce
	 * @see TypeExtractor
	 */
	public CE<T> setTypeExtractor(TypeExtractor typeExtractor) {
		this.typeExtractor = typeExtractor;
		return this;
	}

	private CE(Class<T> classOfT) {
		_clazz = classOfT;
	}

	/**
	 * 获取一个字段。这个字段可以是当前类型或者其父类的私有字段。
	 *
	 * @param name
	 *            字段名
	 * @return 字段
	 * @throws NoSuchFieldException
	 */
	public Field findField(String name) throws NoSuchFieldException {
		//遍历查找模式    	
		//    	Field[] fields = getFields();
		//    	for (Field field : fields) {
		//    		if (field.getName().equals(name)) {
		//    			return field;
		//    		}
		//    	}
		//直接调用模式
		Class<?> cc = _clazz;
		while (cc != null && cc != Object.class) {
			try {
				return cc.getDeclaredField(name);
			} catch (NoSuchFieldException e) {
				cc = cc.getSuperclass();
			}
		}
		throw new NoSuchFieldException(String.format("Can NOT find field [%s] in class [%s] and it's parents classes",
				name, _clazz.getName()));
	}

	/**
	 * 依据一个特殊的注解来获取一个字段。第一遇到的会被返回.如果没找到抛出NoSuchFieldException异常。
	 *
	 * @param ann
	 *            注解
	 * @return 字段
	 * @throws NoSuchFieldException
	 */
	public Field findField(Class<? extends Annotation> ann) throws NoSuchFieldException {
		Field[] fields = getFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(ann))
				return field;
		}
		throw new NoSuchFieldException(String.format("Can NOT find field [@%s] in class [%s] and it's parents classes",
				ann.getName(), _clazz.getName()));
	}

	/**
	 * 获取一组声明了特殊注解的字段
	 *
	 * @param ann
	 *            注解类型
	 * @return 字段数组
	 */
	public Field[] getFieldsByAnno(Class<? extends Annotation> ann, int mods) {
		List<Field> fields = new LinkedList<Field>();
		for (Field f : getFields(Object.class, mods)) {
			if (f.isAnnotationPresent(ann))
				fields.add(f);
		}
		return fields.toArray(new Field[fields.size()]);
	}

	/**
	 * 获得所有的属性，包括私有字段。不包括 Object 的属性，不包括static,final和transient字段
	 * <br>
	 * 使用Map消除了重复的属性，子类属性优先返回
	 * @return 字段数组
	 */
	public Field[] getFields() {
		Class<?> cc = _clazz;
		Map<String, Field> map = new LinkedHashMap<String, Field>();
		while (cc != null && cc != Object.class) {
			Field[] fs = cc.getDeclaredFields();
			for (int i = 0; i < fs.length; i++) {
				if (!LE.isIgnoredField(fs[i]) && !map.containsKey(fs[i].getName()))
					map.put(fs[i].getName(), fs[i]);
			}
			cc = cc.getSuperclass();
		}
		return map.values().toArray(new Field[map.size()]);
	}

	/**
	 * 获得所有属性，可以指定限定父类<br>
	 * 排除重复字段
	 * @param limit
	 * @return
	 */
	public Field[] getFields(Class<?> limit) {
		Class<?> cc = _clazz;
		Map<String, Field> map = new LinkedHashMap<String, Field>();
		while (cc != null && cc != Object.class) {
			Field[] fs = cc.getDeclaredFields();
			for (int i = 0; i < fs.length; i++) {
				if (!map.containsKey(fs[i].getName()))
					map.put(fs[i].getName(), fs[i]);
			}
			cc = cc.getSuperclass() == limit ? null : cc.getSuperclass();
		}
		return map.values().toArray(new Field[map.size()]);
	}

	/**
	 * 获取class指定Modifier的Field数组，忽略final static transient<b>
	 * 
	 * @param limit
	 *          查找限定类
	 * @param mods
	 * 		    Modifier的值，可以使用"|"复合 . 如 Modifier.PUBLIC | Modifier.PROTECTED | Modifier.PRIVATE
	 * @return
	 */
	public Field[] getFields(Class<?> limit, int mods) {
		Class<?> cc = _clazz;
		Map<String, Field> map = new LinkedHashMap<String, Field>();
		while (cc != null && cc != Object.class) {
			Field[] fs = cc.getDeclaredFields();
			for (int i = 0; i < fs.length; i++) {
				if (!map.containsKey(fs[i].getName()) && fs[i].getModifiers() == mods)
					map.put(fs[i].getName(), fs[i]);
			}
			cc = cc.getSuperclass() == limit ? null : cc.getSuperclass();
		}

		return map.values().toArray(new Field[map.size()]);
	}

	/**
	 * 向父类递归查找某一个运行时注解
	 *
	 * @param <A>
	 *            注解类型参数
	 * @param annType
	 *            注解类型
	 * @return 注解
	 */
	public <A extends Annotation> A getAnnotation(Class<A> annType) {
		Class<?> cc = _clazz;
		A ann;
		do {
			ann = cc.getAnnotation(annType);
			cc = cc.getSuperclass();
		} while (null == ann && cc != Object.class);
		return ann;
	}

	/**
	 * 获取本类型所有的方法，包括私有方法。不包括 Object 的方法
	 */
	public Method[] getMethods() {
		Class<?> cc = _clazz;
		ArrayEx<Method> array = ArrayEx.of(Method.class);
		while (cc != null && cc != Object.class) {
			Method[] ms = cc.isInterface() ? cc.getMethods() : cc.getDeclaredMethods();
			for (int i = 0; i < ms.length; i++) {
				array.add(ms[i]);
			}
			cc = cc.getSuperclass();
		}
		return array.toArray(new Method[array.size]);
	}

	/**
	 * 获取当前对象，所有的方法，包括私有方法。递归查找至自己某一个父类为止 。
	 * <p>
	 * 并且这个按照名称，消除重复的方法。子类方法优先
	 *
	 * @param limit
	 *            截至的父类
	 * @return 方法数组
	 */
	public Method[] getMethods(Class<?> limit) {
		Class<?> cc = _clazz;
		ArrayEx<Method> array = ArrayEx.of(Method.class);
		while (cc != null && cc != Object.class) {
			Method[] ms = cc.isInterface() ? cc.getMethods() : cc.getDeclaredMethods();
			for (int i = 0; i < ms.length; i++) {
				array.add(ms[i]);
			}
			cc = cc.getSuperclass() == limit ? null : cc.getSuperclass();
		}
		return array.toArray(new Method[array.size]);
	}

	/**
	 * 依据标注获取方法数组
	 * @param annoType
	 * 			方法的标注类型
	 * @param mods
	 * 			限定标记
	 * @return 方法数组
	 */
	public Method[] getMethodsByAnno(Class<? extends Annotation> annoType, int mods) {
		List<Method> ms = new LinkedList<Method>();
		for (Method m : getMethods(Object.class, mods)) {
			if (m.getAnnotation(annoType) != null) {
				ms.add(m);
			}
		}
		return ms.toArray(new Method[ms.size()]);
	}

	/**
	 * 获取限定父类以及限定的mods的方法数组
	 * @param limit
	 *            截至的父类 
	 * @param mods 
	 * 		指定的Modifier，可以复合，如：<br> 
	 * 		Modifier.PUBLIC | Modifier.PRIVATE
	 * @return 方法数组
	 */
	public Method[] getMethods(Class<?> limit, int mods) {
		Class<?> cc = _clazz;
		ArrayEx<Method> array = ArrayEx.of(Method.class);
		while (cc != null && cc != Object.class) {
			Method[] ms = cc.isInterface() ? cc.getMethods() : cc.getDeclaredMethods();
			for (int i = 0; i < ms.length; i++) {
				if (mods != 0 && ms[i].getModifiers() == mods) {
					array.add(ms[i]);
				} else {
					array.add(ms[i]);
				}
			}
			cc = cc.getSuperclass() == limit ? null : cc.getSuperclass();
		}
		return array.toArray(new Method[array.size()]);
	}

	/**
	 * @return 对象类型
	 */
	public Class<T> getClassType() {
		return _clazz;
	}

	/**
	 * @return 获取经过提炼的对象类型数组。从对象自身的类型到 Object，中间的继承关系中最有特点的几个类型
	 */
	public Class<?>[] getExtractTypes() {
		return typeExtractor.extract(this);
	}

	/**
	 * @return 获得外覆类，如果没有外覆类，则返回自身的类型
	 */
	public Class<?> getWrapperType() {
		Class<?> clss = _clazz;
		if (_clazz.isPrimitive()) {
			clss = (Class<?>) primitiveWrapperMap.get(_clazz);
		}
		return clss;
	}

	/**
	 * @return 如果当前类为内部类，则返回其外部类。否则返回 null
	 */
	public Class<?> getOuterType() {
		if (Modifier.isStatic(_clazz.getModifiers()))
			return null;
		String name = _clazz.getName();
		int pos = name.lastIndexOf('$');
		if (pos == -1)
			return null;
		name = name.substring(0, pos);
		return ClassUtils.loadClass(name);
	}

	/**
	 * 获取对象构建器
	 *
	 * @param args
	 *            构造函数参数
	 * @return 当前对象的构建方式。
	 *
	 * @throws CreatingException
	 *             当没有发现合适的 Creating 时抛出
	 *
	 * @see Creating
	 */
	public Creating<T> getCreating(Object... args) throws CreatingException {
		CreatingContext<T> bc = CreatingUtils.eval(_clazz, args);
		if (null == bc)
			throw new CreatingException(_clazz, args);

		return bc.getCreating();
	}

	/**
	 * 获取对象构建器
	 *
	 * @param argTypes
	 *            构造函数参数类型数组
	 * @return 当前对象构建方式
	 *
	 * @throws net.street.common.creating.CreatingException
	 *             当没有发现合适的 Creating 时抛出
	 */
	public Creating<T> getCreatingByArgTypes(Class<?>... argTypes) throws CreatingException {
		CreatingContext<T> bc = CreatingUtils.evalByArgTypes(_clazz, argTypes);
		if (null == bc)
			throw new CreatingException(_clazz, argTypes);
		return bc.getCreating();
	}

	/**
	 * 根据构造函数参数，创建一个对象。
	 *
	 * @param args
	 *            构造函数参数
	 * @return 新对象
	 */
	public T create(Object... args) {
		return CreatingUtils.eval(_clazz, args).create();
	}

	/**
	 * 在个类实例方法的所有方法中查找指定名称和参数签名的方法(目前实现是排除Object的方法)<br/>
	 * 参数匹配比较宽泛，允许继承的方法签名
	 * @param name
	 *            方法名
	 * @param paramTypes
	 *            参数类型列表
	 * @return 要查找的方法 
	 * @throws NoSuchMethodException
	 */
	public Method findMethod(String name, Class<?>... paramTypes) throws NoSuchMethodException {
		//动态匹配模式
		Method[] methods = getMethods();
		for (Method m : methods) {
			if (m.getName().equals(name))
				if (LE.matchParamTypes(m.getParameterTypes(), paramTypes) == MatchType.YES)
					return m;
		}
		throw new NoSuchMethodException(String.format("Fail to find Method %s->%s with params:\n%s", _clazz.getName(),
				name, LE.toString(paramTypes, "NULL")));
	}

	/**
	 * 根据名称和参数个数，查找一组方法 (目前实现是排除Object的方法)
	 *
	 * @param name
	 *            方法名
	 * @param mods
	 *            指定方法的Modifier
	 * @param paramTypes
	 *            参数类型列表
	 *                        
	 * @return 方法数组
	 */
	public Method findMethod(String name, int mods, Class<?>... paramTypes) throws NoSuchMethodException {
		if (paramTypes == null)
			paramTypes = new Class<?>[0];
		Method[] ms = getMethods();
		for (Method m : ms) {
			if (m.getName().equals(name) && m.getModifiers() == mods) {
				if (LE.matchParamTypes(m.getParameterTypes(), paramTypes) == MatchType.YES)
					return m;
			}
		}
		throw new NoSuchMethodException(String.format(
				"Fail to find Method %s->%s with modifier mods[%s] with params:\n%s", _clazz.getName(), name, mods,
				LE.toString(paramTypes, "NULL")));
	}

	/**
	 * 根据返回值类型，以及参数类型，查找第一个匹配的方法 (目前实现是排除Object的方法)
	 * 注意：本方法用的是类型严格匹配
	 * @param returnType
	 *            返回值类型
	 * @param paramTypes
	 *            参数个数
	 * @return 方法
	 * @throws NoSuchMethodException
	 */
	public Method findMethod(Class<?> returnType, Class<?>... paramTypes) throws NoSuchMethodException {
		for (Method m : getMethods()) {
			if (returnType == m.getReturnType())
				if (paramTypes.length == m.getParameterTypes().length) {
					if (LE.matchParamTypes(m.getParameterTypes(), paramTypes) == MatchType.YES)
						return m;
				}
		}
		throw new NoSuchMethodException(String.format(
				"Can not find method in [%s] with return type '%s' and arguemtns \n'%s'!", _clazz.getName(),
				returnType.getName(), LE.toString(paramTypes, "NULL")));
	}

	/**
	 * 判断当前对象是否为一个类型。精确匹配，即使是父类和接口，也不相等
	 *
	 * @param type
	 *            类型
	 * @return 是否相等
	 */
	public boolean is(Class<?> type) {
		return null != type && _clazz == type;
	}

	/**
	 * <p>
	 * Checks if one <code>Class</code> can be assigned to a variable of another
	 * <code>Class</code>.
	 * </p>
	 *
	 * <p>
	 * Unlike the {@link Class#isAssignableFrom(java.lang.Class)} method, this
	 * method takes into account widenings of primitive classes and
	 * <code>null</code>s.
	 * </p>
	 *
	 * <p>
	 * Primitive widenings allow an int to be assigned to a long, float or
	 * double. This method returns the correct result for these cases.
	 * </p>
	 *
	 * <p>
	 * <code>Null</code> may be assigned to any reference type. This method will
	 * return <code>true</code> if <code>null</code> is passed in and the
	 * targetType is non-primitive.
	 * </p>
	 *
	 * @param target
	 *            类型或接口名
	 * @return 当前对象是否为一个类型的子类，或者一个接口的实现类
	 */
	public boolean isOf(Class<?> target) {

		if (target == null)
			return false;
		// have to validate for null, as isAssignableFrom doesn't
		if (_clazz == null) {
			return !(target.isPrimitive());
		}
		if (_clazz.equals(target)) {
			return true;
		}
		if (!_clazz.isPrimitive()) {
			return target.isAssignableFrom(_clazz);
		}
		if (!target.isPrimitive()) {
			return false;
		}
		// 剩下全是的情况就全是基本类型了
		if (Integer.TYPE.equals(_clazz)) {
			return Long.TYPE.equals(target) || Float.TYPE.equals(target) || Double.TYPE.equals(target);
		}
		if (Long.TYPE.equals(_clazz)) {
			return Float.TYPE.equals(target) || Double.TYPE.equals(target);
		}
		if (Boolean.TYPE.equals(_clazz)) {
			return false;
		}
		if (Double.TYPE.equals(_clazz)) {
			return false;
		}
		if (Float.TYPE.equals(_clazz)) {
			return Double.TYPE.equals(target);
		}
		if (Character.TYPE.equals(_clazz)) {
			return Integer.TYPE.equals(target) || Long.TYPE.equals(target) || Float.TYPE.equals(target)
					|| Double.TYPE.equals(target);
		}
		if (Short.TYPE.equals(_clazz)) {
			return Integer.TYPE.equals(target) || Long.TYPE.equals(target) || Float.TYPE.equals(target)
					|| Double.TYPE.equals(target);
		}
		if (Byte.TYPE.equals(_clazz)) {
			return Short.TYPE.equals(target) || Integer.TYPE.equals(target) || Long.TYPE.equals(target)
					|| Float.TYPE.equals(target) || Double.TYPE.equals(target);
		}
		// should never get here
		return false;
	}

	/**
	 * @return 当前对象是否为字符串
	 */
	public boolean isString() {
		return is(String.class);
	}

	/**
	 * @return 当前对象是否为CharSequence的子类
	 */
	public boolean isStringLike() {
		return CharSequence.class.isAssignableFrom(_clazz);
	}

	/**
	 * @return 当前对象是否为字符
	 */
	public boolean isChar() {
		return is(char.class) || is(Character.class);
	}

	/**
	 * @return 当前对象是否为枚举
	 */
	public boolean isEnum() {
		return _clazz.isEnum();
	}

	/**
	 * @return 当前对象是否为布尔
	 */
	public boolean isBoolean() {
		return is(boolean.class) || is(Boolean.class);
	}

	/**
	 * @return 当前对象是否为浮点
	 */
	public boolean isFloat() {
		return is(float.class) || is(Float.class);
	}

	/**
	 * @return 当前对象是否为双精度浮点
	 */
	public boolean isDouble() {
		return is(double.class) || is(Double.class);
	}

	/**
	 * @return 当前对象是否为整型
	 */
	public boolean isInt() {
		return is(int.class) || is(Integer.class);
	}

	/**
	 * @return 当前对象是否为整数（包括 int, long, short, byte）
	 */
	public boolean isIntLike() {
		return isInt() || isLong() || isShort() || isByte() || is(BigDecimal.class);
	}

	/**
	 * @return 当前对象是否为小数 (float, dobule)
	 */
	public boolean isDecimal() {
		return isFloat() || isDouble();
	}

	/**
	 * @return 当前对象是否为长整型
	 */
	public boolean isLong() {
		return is(long.class) || is(Long.class);
	}

	/**
	 * @return 当前对象是否为短整型
	 */
	public boolean isShort() {
		return is(short.class) || is(Short.class);
	}

	/**
	 * @return 当前对象是否为字节型
	 */
	public boolean isByte() {
		return is(byte.class) || is(Byte.class);
	}

	/**
	 * @param type
	 *            类型
	 * @return 否为一个对象的外覆类
	 */
	public boolean isWrapperOf(Class<?> type) {

		return CE.of(type).getWrapperType() == _clazz;
	}

	/**
	 * @return 当前对象是否为原生的数字类型 （即不包括 boolean 和 char）
	 */
	public boolean isPrimitiveNumber() {
		return isInt() || isLong() || isFloat() || isDouble() || isByte() || isShort();
	}

	/**
	 * @return 当前对象是否为数字
	 */
	public boolean isNumber() {
		return Number.class.isAssignableFrom(_clazz) || _clazz.isPrimitive() && !is(boolean.class) && !is(char.class);
	}

	/** 
	 * @return 当前对象是否是日期
	 */
	public boolean isDate() {
		return is(java.sql.Date.class) || is(java.util.Date.class);
	}

	/** 
	 * @return 当前对象是否为时间
	 */
	public boolean isTime() {
		return is(java.sql.Time.class);
	}

	/** 
	 * @return 当前对象是否为时间戳
	 */
	public boolean isTimestamp() {
		return is(java.sql.Timestamp.class);
	}

	/**
	* @return 当前对象是否在表示日期
	*/
	public boolean isDateLike() {
		return java.util.Date.class.isAssignableFrom(_clazz) || java.sql.Date.class.isAssignableFrom(_clazz);
	}

	/**
	 * @return 当前对象是否在表示时间
	 */
	public boolean isTimeLike() {
		return Calendar.class.isAssignableFrom(_clazz) || java.sql.Timestamp.class.isAssignableFrom(_clazz)
				|| java.sql.Time.class.isAssignableFrom(_clazz);
	}

	/**
	 * @return 当前对象是否在表示日期或时间
	 */
	public boolean isDateTimeLike() {
		return Calendar.class.isAssignableFrom(_clazz) || java.util.Date.class.isAssignableFrom(_clazz)
				|| java.sql.Date.class.isAssignableFrom(_clazz) || java.sql.Time.class.isAssignableFrom(_clazz);
	}

	/** 
	 * @return 当前对象是否为一个集合类
	 */
	public boolean isCollection() {
		return Collection.class.isAssignableFrom(_clazz);
	}

	/**
	 * 获取对象的可被copy的属性名称数组(该属性包括读和写方法)
	 *
	 * @return Array of property names, or null if an error occurred.
	 */
	//    public String[] getCopyablePropertyNames() {
	//    	 try {
	//             BeanInfo info = Introspector.getBeanInfo(_clazz);
	//             PropertyDescriptor[] properties = info.getPropertyDescriptors();
	//             List<String> ret = new ArrayList<String>();
	//
	//             for (int i = 0; i < properties.length; i++) {
	//            	 PropertyDescriptor pd = properties[i];
	//            	 if (pd.getReadMethod()!=null&&pd.getWriteMethod()!=null){
	//            		 ret.add(properties[i].getName());
	//            	 }
	//             }
	//             return ret.toArray(new String[ret.size()]);
	//         } catch (IntrospectionException e) {
	//             return null;
	//         }
	//    }

	//    public Object invokeTry(Object src, String property, Object... value) {
	//    	CE ce_src = null;
	//    	if (src.getClass() == _clazz) 
	//    		ce_src = this; 
	//    	else
	//    		ce_src = CE.of(src.getClass());
	//    	
	//        Class<?>[] param_cls =  LE.evalToTypes(value);
	//        String mn;
	//        int mods  = Modifier.PUBLIC | Modifier.PROTECTED;
	//        try {
	//        	mn = "get"+ StringUtils.capitalize(property);
	//            //try object.getProperty(...)
	//            Method method = ce_src.findMethod(mn, mods, param_cls);
	//            return method.invoke(src, value);
	//        } catch (Exception e1) {
	//            try {
	//                //try object.isProperty(...)
	//            	mn = "is" + StringUtils.capitalize(property);
	//                Method method = ce_src.findMethod(mn, mods, param_cls);
	//                return method.invoke(src, value);
	//            } catch (Exception e2) {
	//                try {
	//                    // try object.property(...)
	//                    Method method = ce_src.findMethod(property, mods, param_cls);
	//                    return method.invoke(src, value);
	//                } catch (Exception e3) {
	//                    try {
	//                        //try object.property
	//                        Field field = ce_src.findField(property);
	//        				field.setAccessible(true);
	//                        return field.get(src);
	//                    } catch (Exception e4) {
	//                        // 哥实在是尽力了
	//                        throw new FailToInvokingException(e4,
	//                                "Invoke method [ %s ] on class[ %s ] error.",
	//                                property,
	//                                src.getClass());
	//                    }
	//                }
	//            }
	//        }
	//    }

	public Class<?> unWrapper() {
		return wrapperPrimitiveMap.get(_clazz);
	}

	@Override
	public String toString() {
		return _clazz.getName();
	}

	/**
	 * Maps primitive <code>Class</code>es to their corresponding wrapper
	 * <code>Class</code>.
	 */
	private static Map<Class<?>, Class<?>> primitiveWrapperMap = new HashMap<Class<?>, Class<?>>();
	static {
		primitiveWrapperMap.put(Boolean.TYPE, Boolean.class);
		primitiveWrapperMap.put(Byte.TYPE, Byte.class);
		primitiveWrapperMap.put(Character.TYPE, Character.class);
		primitiveWrapperMap.put(Short.TYPE, Short.class);
		primitiveWrapperMap.put(Integer.TYPE, Integer.class);
		primitiveWrapperMap.put(Long.TYPE, Long.class);
		primitiveWrapperMap.put(Double.TYPE, Double.class);
		primitiveWrapperMap.put(Float.TYPE, Float.class);
		primitiveWrapperMap.put(Void.TYPE, Void.TYPE);
	}

	private static final Map<Class<?>, Class<?>> wrapperPrimitiveMap = new HashMap<Class<?>, Class<?>>();
	static {
		wrapperPrimitiveMap.put(Short.class, short.class);
		wrapperPrimitiveMap.put(Integer.class, int.class);
		wrapperPrimitiveMap.put(Long.class, long.class);
		wrapperPrimitiveMap.put(Double.class, double.class);
		wrapperPrimitiveMap.put(Float.class, float.class);
		wrapperPrimitiveMap.put(Byte.class, byte.class);
		wrapperPrimitiveMap.put(Character.class, char.class);
		wrapperPrimitiveMap.put(Boolean.class, boolean.class);
	}
}

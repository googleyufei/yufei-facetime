package com.facetime.core.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 处理class操作功能类。尽可能的不使用反射。
 *
 * @author dzb2k9
 */
@SuppressWarnings({ "rawtypes" })
public abstract class ClassUtils {
	/**
	 * 将classpth外部的文件(jar)在运行时或者路径添加到classPath中<BR></>
	 *
	 * @see #addUrlToClassPath(java.net.URL, java.net.URLClassLoader)
	 */
	public static void addFileToClassPath(File path) {
		addFileToClassPath(path, (URLClassLoader) getClassLoader());
	}

	/**
	 * Adds additional file or path to classpath during runtime.
	 *
	 * @see #addUrlToClassPath(java.net.URL, java.net.URLClassLoader)
	 */
	public static void addFileToClassPath(File path, URLClassLoader classLoader) {
		try {
			addUrlToClassPath(path.toURL(), classLoader);
		} catch (MalformedURLException muex) {
			throw new IllegalArgumentException("Unable to convert path to URL: '" + path + "'.", muex);
		}
	}

	/**
	 * 将classpth外部的文件(jar)在运行时或者路径添加到classPath中<BR></>
	 * <p/>
	 * Adds additional file or path to classpath during runtime.
	 *
	 * @see #addUrlToClassPath(java.net.URL, java.net.URLClassLoader)
	 */
	public static void addFileToClassPath(String path) {
		addFileToClassPath(path, (URLClassLoader) getClassLoader());
	}

	/**
	 * 将classpth外部的文件(jar)在运行时或者路径添加到classPath中<BR></>
	 * Adds additional file or path to classpath during runtime.
	 *
	 * @see #addUrlToClassPath(java.net.URL, java.net.URLClassLoader)
	 */
	public static void addFileToClassPath(String path, URLClassLoader classLoader) {
		addFileToClassPath(new File(path), classLoader);
	}

	/**
	 * Adds the content pointed by the URL to the classpath during runtime.
	 *
	 * @see #addUrlToClassPath(java.net.URL, java.net.URLClassLoader)
	 */
	public static void addUrlToClassPath(URL url) {
		addUrlToClassPath(url, (URLClassLoader) getClassLoader());
	}

	/**
	 * Adds the content pointed by the URL to the classpath during runtime. Uses
	 * reflection since <code>addURL</code> method of
	 * <code>URLClassLoader</code> is protected.
	 */
	public static void addUrlToClassPath(URL url, URLClassLoader classLoader) {
		try {
			LE.invoke(classLoader, "addURL", url);
		} catch (Exception ex) {
			throw new IllegalArgumentException("Unable to extend classpath with URL: '" + url + "'.", ex);
		}
	}

	/**
	 * Defines a class from byte array into the system class loader.
	 *
	 * @see #defineClass(String, byte[], ClassLoader)
	 */
	public static Class defineClass(byte[] classData) {
		return defineClass(null, classData, getClassLoader());
	}

	/**
	 * Defines a class from byte array into the specified class loader.
	 *
	 * @see #defineClass(String, byte[], ClassLoader)
	 */
	public static Class defineClass(byte[] classData, ClassLoader classLoader) {
		return defineClass(null, classData, classLoader);
	}

	/**
	 * Defines a class from byte array into the system class loader.
	 *
	 * @see #defineClass(String, byte[], ClassLoader)
	 */
	public static Class defineClass(String className, byte[] classData) {
		return defineClass(className, classData, getClassLoader());
	}

	/**
	 * Defines a class from byte array into the specified class loader.
	 *
	 * @see #defineClass(String, byte[], ClassLoader)
	 */
	public static Class defineClass(String className, byte[] classData, ClassLoader classLoader) {
		try {
			return (Class) LE.invoke(classLoader, "defineClass", className, classData, 0, classData.length);
		} catch (Throwable th) {
			throw new RuntimeException("Unable to define class '" + className + "'.", th);
		}
	}

	/**
	 * 在全部classpath中查找className的类
	 *
	 * @param className
	 * @return
	 */
	public static Class findClass(String className) {
		return findClass(className, getFullClassPath(getClassLoader()), null);
	}

	/**
	 * @param className
	 * @param parent
	 * @return
	 */
	public static Class findClass(String className, ClassLoader parent) {
		return findClass(className, getFullClassPath(getClassLoader()), parent);
	}

	/**
	 * Finds and loads class on classpath even if it was already loaded.
	 */
	public static Class findClass(String className, URL[] classPath, ClassLoader parent) {
		URLClassLoader loader = parent != null ? new URLClassLoader(classPath, parent) : new URLClassLoader(classPath);
		try {
			return (Class) LE.invoke(loader, "findClass", className);
		} catch (Throwable e) {
			throw new RuntimeException("Unable to find class '" + className + "'.", e);
		}
	}

	/**
	 * Opens a class of the specified name for reading.
	 * @see #getResourceAsStream(String, Class)
	 */
	public static InputStream getClassAsStream(Class clazz) {
		return getResourceAsStream(getClassFileName(clazz), clazz);
	}

	/**
	 * Opens a class of the specified name for reading.
	 * @see #getResourceAsStream(String, Class)
	 */
	public static InputStream getClassAsStream(String className) {
		return getResourceAsStream(getClassFileName(className), ClassUtils.class);
	}

	/**
	 * Resolves class file getName from class getName by replacing dot's with '/'
	 * separator and adding class extension at the end.
	 */
	public static String getClassFileName(Class clazz) {
		if (clazz.isArray())
			clazz = clazz.getComponentType();
		return getClassFileName(clazz.getName());
	}

	/**
	 * Resolves class file getName from class getName by replacing dot's with '/'
	 * separator.
	 */
	public static String getClassFileName(String className) {
		return className.replace('.', '/') + ".class";
	}

	/**
	 * 获取当前线程缺省的ClassLoader
	 *
	 * @return classloader in current thread.
	 */
	public static ClassLoader getClassLoader() {

		ClassLoader loader = null;
		try {
			loader = Thread.currentThread().getContextClassLoader();
		} catch (Throwable ex) {
		}

		if (loader == null)
			loader = ClassUtils.class.getClassLoader();
		return loader;
	}

	/**
	 * Javassist needs the class getName to be as it appears in source code, even
	 * for arrays. Invoking getName() on a Class creating representing an array
	 * returns the internal format (i.e, "[...;" or something). This returns it
	 * as it would appear in Java code.
	 */
	public static String getClassNameForJava(Class inputClass) {
		if (inputClass.isArray())
			return getClassNameForJava(inputClass.getComponentType()) + "[]";

		return inputClass.getName();
	}

	/**
	 * Returns complete class path from all available
	 * <code>URLClassLoaders</code> starting from class loader that has loaded
	 * the specified class.
	 */
	public static URL[] getFullClassPath(Class clazz) {
		return getFullClassPath(clazz.getClassLoader());
	}

	/**
	 * Returns complete class path from all available
	 * <code>URLClassLoader</code>s.
	 */
	public static URL[] getFullClassPath(ClassLoader classLoader) {
		List<URL> list = new ArrayList<URL>();
		while (classLoader != null) {
			if (classLoader instanceof URLClassLoader) {
				URL[] urls = ((URLClassLoader) classLoader).getURLs();
				list.addAll(Arrays.asList(urls));
			}
			classLoader = classLoader.getParent();
		}

		URL[] result = new URL[list.size()];
		return list.toArray(result);
	}

	/**
	 * <p>
	 * Gets a <code>List</code> of all interfaces implemented by the given class
	 * and its superclasses.
	 * </p>
	 * <p/>
	 * <p>
	 * The order is determined by looking through each interface in turn as
	 * declared in the source file and following its hierarchy up. Then each
	 * superclass is considered in the same way. Later duplicates are ignored,
	 * so the order is maintained.
	 * </p>
	 *
	 * @param clazz the class to look up, may be <code>null</code>
	 * @return the <code>List</code> of interfaces in order, <code>null</code>
	 *         if null input
	 */
	public static List<Class<?>> getInterfaces(Class<?> clazz) {
		if (clazz == null)
			return new ArrayList<Class<?>>(0);
		List<Class<?>> list = new ArrayList<Class<?>>();
		while (clazz != null) {
			Class<?>[] interfaces = clazz.getInterfaces();
			for (Class<?> interface1 : interfaces) {
				if (list.contains(interface1) == false)
					list.add(interface1);
				List<Class<?>> superInterfaces = getInterfaces(interface1);
				for (Class<?> intface : superInterfaces) {
					if (list.contains(intface) == false)
						list.add(intface);
				}
			}
			clazz = clazz.getSuperclass();
		}
		return list;
	}

	/**
	 * <p>
	 * Gets the package getName of a <code>Class</code>.
	 * </p>
	 *
	 * @param clazz the class to get the package getName for, may be
	 *              <code>null</code>.
	 * @return the package getName or an empty string
	 */
	public static String getPackageName(Class<?> clazz) {
		if (clazz == null)
			return StringPool.EMPTY;
		return getPackageName(clazz.getName());
	}

	/**
	 * 由类名得到包名
	 *
	 * @param className the className to get the package getName for, may be
	 *                  <code>null</code>
	 * @return the package getName or an empty string
	 */
	public static String getPackageName(String className) {
		if (className == null)
			return StringPool.EMPTY;
		int i = className.lastIndexOf('.');
		if (i == -1)
			return StringPool.EMPTY;
		return className.substring(0, i);
	}

	/**
	 * Return the qualified getName of the given method, consisting of fully
	 * qualified interface/class getName + "." + method getName.
	 *
	 * @param method the method
	 * @return the qualified getName of the method
	 */
	public static String getQualifiedMethodName(Method method) {
		assert method != null : "Method must not be null";
		return method.getDeclaringClass().getName() + "." + method.getName();
	}

	/**
	 * Opens a resource of the specified name for reading.
	 * @see #getResourceAsStream(String, Class)
	 */
	public static InputStream getResourceAsStream(String resourceName) {
		return getResourceAsStream(resourceName, ClassUtils.class);
	}

	/**
	 * Opens a resource of the specified name for reading.
	 * @see #getResourceUrl(String, Class)
	 */
	public static InputStream getResourceAsStream(String resourceName, Class callingClass) {
		URL url = getResourceUrl(resourceName, callingClass);
		if (url != null)
			try {
				return url.openStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
		return null;
	}

	/**
	 * Retrieves resource as file.
	 * @see #getResourceFile(String)
	 */
	public static File getResourceFile(String resourceName) {
		return getResourceFile(resourceName, null);
	}

	// ---------------------------------------------------------------- get resource

	/**
	 * Retrieves resource as file. Resource is retrieved as {@link #getResourceUrl(String, Class) URL},
	 * than it is converted to URI so it can be used by File constructor.
	 */
	public static File getResourceFile(String resourceName, Class callingClass) {
		try {
			return new File(getResourceUrl(resourceName, callingClass).toURI());
		} catch (URISyntaxException usex) {
			return null;
		}
	}

	/**
	 * Retrieves given resource as URL.
	 * @see #getResourceUrl(String, Class)
	 */
	public static URL getResourceUrl(String resourceName) {
		return getResourceUrl(resourceName, null);
	}

	// ---------------------------------------------------------------- get resource file

	/**
	 * Retrieves given resource as URL.
	 * <p>
	 * Resource will be loaded using class loaders in the following order:
	 * <ul>
	 * <li>{@link Thread#getContextClassLoader() Thread.currentThread().getContextClassLoader()}
	 * <li>{@link Class#getClassLoader() ClassLoaderUtils.class.getClassLoader()}
	 * <li>if <code>callingClass</code> is provided: {@link Class#getClassLoader() callingClass.getClassLoader()}
	 * </ul>
	 */
	public static URL getResourceUrl(String resourceName, Class callingClass) {
		URL url = getClassLoader().getResource(resourceName);
		if (url == null && callingClass != null) {
			ClassLoader cl = callingClass.getClassLoader();
			if (cl != null)
				url = cl.getResource(resourceName);
		}
		return url;
	}

	/**
	 * <p>
	 * Gets the class getName minus the package getName from a <code>Class</code>.
	 * </p>
	 *
	 * @param clazz the class to get the short getName for.
	 * @return the class getName without the package getName or an empty string
	 */
	public static String getShortClassName(Class<?> clazz) {
		if (clazz == null)
			return StringPool.EMPTY;
		return getShortClassName(clazz.getName());
	}

	/**
	 * <p>
	 * Gets the class getName minus the package getName from a String.
	 * </p>
	 * <p/>
	 * <p>
	 * The string passed in is assumed to be a class getName - it is not checked.
	 * </p>
	 *
	 * @param className the className to get the short getName for
	 * @return the class getName of the class without the package getName or an empty
	 *         string
	 */
	public static String getShortClassName(String className) {
		if (className == null)
			return StringPool.EMPTY;
		if (className.length() == 0)
			return StringPool.EMPTY;
		char[] chars = className.toCharArray();
		int last_dot = 0;
		for (int i = 0; i < chars.length; i++)
			if (chars[i] == '.')
				last_dot = i + 1;
			else if (chars[i] == '$')
				chars[i] = '.';
		return new String(chars, last_dot, chars.length - last_dot);
	}

	/**
	 * <p>
	 * Gets a <code>List</code> of superclasses for the given class.
	 * </p>
	 *
	 * @param clazz the class to look up, may be <code>null</code>
	 * @return the <code>List</code> of superclasses in order going up from this
	 *         one <code>null</code> if null input
	 */
	public static List<Class<?>> getSuperclasses(Class<?> clazz) {
		if (clazz == null)
			return null;
		List<Class<?>> classes = new ArrayList<Class<?>>();
		Class<?> superclass = clazz.getSuperclass();
		while (superclass != null) {
			classes.add(superclass);
			superclass = superclass.getSuperclass();
		}
		return classes;
	}

	/**
	 * Loads a class with a given getName dynamically.
	 *
	 * @see #loadClass(String, Class)
	 */
	public static Class loadClass(String className) {
		return loadClass(className, null);
	}

	/**
	 * Loads a class with a given getName dynamically, more reliable then
	 * <code>Class.forName</code>.
	 * <p/>
	 * Class will be loaded using class loaders in the following order:
	 * <ul>
	 * <li>{@link Thread#getContextClassLoader()
	 * Thread.currentThread().getContextClassLoader()}
	 * <li>the basic {@link Class#forName(java.lang.String)}
	 * <li>{@link Class#getClassLoader()
	 * ClassLoaderUtils.class.getClassLoader()}
	 * <li>if <code>callingClass</code> is provided:
	 * {@link Class#getClassLoader() callingClass.getClassLoader()}
	 * </ul>
	 */
	public static Class loadClass(String className, Class calling) {
		try {
			return getClassLoader().loadClass(className);
		} catch (ClassNotFoundException cnfex1) {
			try {
				return Class.forName(className);
			} catch (ClassNotFoundException cnfex2) {
				if (calling != null)
					try {
						return calling.getClassLoader().loadClass(className);
					} catch (ClassNotFoundException e) {
					}
				return null;
			}
		}
	}
}

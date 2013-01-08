package com.facetime.core.resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.facetime.core.resource.impl.AbstractResource;
import com.facetime.core.utils.ClassUtils;

/**
 * Resource implementation for class path resources. Uses either the thread
 * context class loader, a given ClassLoader or a given Class for loading
 * resources.
 * 
 * <p>
 * Supports resolution as <code>java.io.File</code> if the class path
 * org.streets.commons.resource resides in the file system, but not for
 * resources in a JAR. Always supports resolution as URL.
 * 
 * @author Juergen Hoeller
 * @since 28.12.2003
 * @see java.lang.Thread#getContextClassLoader()
 * @see java.lang.ClassLoader#getResourceAsStream(String)
 * @see java.lang.Class#getResourceAsStream(String)
 */
public class ClasspathResource extends AbstractResource {

	// Guarded by this
	private URL url;
	// Guarded by this
	private boolean urlResolved;

	private ClassLoader classLoader;

	/**
	 * Create a new ClassPathResource for ClassLoader usage. A leading slash
	 * will be removed, as the ClassLoader org.streets.commons.resource access
	 * methods will not accept it.
	 * <p>
	 * The thread context class loader will be used for loading the
	 * org.streets.commons.resource.
	 * 
	 * @param path
	 *            the absolute path within the class path
	 * @see java.lang.ClassLoader#getResourceAsStream(String)
	 */
	public ClasspathResource(String path) {
		this(path, (ClassLoader) null);
	}

	/**
	 * Create a new ClassPathResource for ClassLoader usage. A leading slash
	 * will be removed, as the ClassLoader org.streets.commons.resource access
	 * methods will not accept it.
	 * 
	 * @param path
	 *            the absolute path within the classpath
	 * @param classLoader
	 *            the class loader to load the org.streets.commons.resource
	 *            with, or <code>null</code> for the thread context class loader
	 * @see java.lang.ClassLoader#getResourceAsStream(String)
	 */
	public ClasspathResource(String path, ClassLoader classLoader) {
		super(path);
		this.classLoader = (classLoader != null ? classLoader : ClassUtils.getClassLoader());
	}

	/**
	 * This implementation opens an InputStream for the given class path
	 * org.streets.commons.resource.
	 * 
	 * @see java.lang.ClassLoader#getResourceAsStream(String)
	 * @see java.lang.Class#getResourceAsStream(String)
	 */
	public InputStream getInputStream() throws IOException {
		InputStream is = this.classLoader.getResourceAsStream(path());
		if (is == null) {
			is = openInputStream();
		}
		return is;
	}

	/**
	 * This implementation returns a URL for the underlying class path
	 * org.streets.commons.resource.
	 * 
	 * @see java.lang.ClassLoader#getResource(String)
	 */

	public URL toURL() {
		if (!urlResolved) {
			url = classLoader.getResource(path());
			urlResolved = true;
		}
		return url;
	}

	@Override
	protected Resource newResource(String path) {
		return new ClasspathResource(path, classLoader);
	}

	/**
	 * This implementation compares the underlying class path locations.
	 */
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (obj.getClass() != getClass())
			return false;
		ClasspathResource other = (ClasspathResource) obj;
		return other.classLoader == classLoader && other.path().equals(path());
	}

	/**
	 * This implementation returns the hash code of the underlying class path
	 * location.
	 */
	@Override
	public int hashCode() {
		return 227 ^ path().hashCode();
	}

	@Override
	public String toString() {
		return "classpath:" + path();
	}

	public long lastModified() {
		return 0;
	}

	public static void main(String[] args) {
		ClasspathResource cpr = new ClasspathResource("org/testng/Assert.class");
		System.out.println("path = " + cpr.path());
		System.out.println("url = " + cpr.toURL());
		System.out.println(cpr.exists());
		System.out.println("file = " + cpr.name());
		System.out.println("folder = " + cpr.folder());
		try {
			InputStream is = cpr.getInputStream();
			System.out.println(is);
		} catch (IOException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

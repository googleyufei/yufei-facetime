package com.facetime.core.resource.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

import com.facetime.core.collection.CollectionFactory;
import com.facetime.core.collection.Stack;
import com.facetime.core.file.IOUtils;
import com.facetime.core.resource.ClasspathURLConverter;
import com.facetime.core.resource.FilepathResource;
import com.facetime.core.resource.Resource;
import com.facetime.core.resource.ResourceScanner;

public class DefaultResourceScanner implements ResourceScanner {
	private static final String CLASS_SUFFIX = ".class";

	private final ClasspathURLConverter<URL> converter;

	// This matches normal class files but not inner class files (which contain
	// a '$'.

	private final Pattern CLASS_NAME_PATTERN = Pattern.compile(
			"^\\p{javaJavaIdentifierStart}[\\p{javaJavaIdentifierPart}&&[^\\$]]*\\.class$", Pattern.CASE_INSENSITIVE);

	private final Pattern FOLDER_NAME_PATTERN = Pattern.compile(
			"^\\p{javaJavaIdentifierStart}[\\p{javaJavaIdentifierPart}]*$", Pattern.CASE_INSENSITIVE);

	static class Queued {
		final URL packageURL;
		final String packagePath;

		public Queued(final URL packageURL, final String packagePath) {
			this.packageURL = packageURL;
			this.packagePath = packagePath;
		}
	}

	public DefaultResourceScanner() {
		this.converter = new ClasspathURLConverterImpl();
	}

	public DefaultResourceScanner(ClasspathURLConverter<URL> converter) {
		this.converter = converter;
	}

	/**
	 * Starts resource scanning.
	 */
	public synchronized Collection<Resource> scanFile(File root, boolean recursive, String pattern) {
		Collection<Resource> result = new ArrayList<Resource>();
		File[] allFiles = root.listFiles();
		if (allFiles == null) {
			return result;
		}
		for (File file : allFiles) {
			boolean isFolder = file.isDirectory();
			if (isFolder && recursive) {
				Collection<Resource> res = scanFile(file, recursive, pattern);
				result.addAll(res);
			} else {
				result.add(new FilepathResource(file));
			}
		}
		return result;
	}

	/**
	 * Synchronization should not be necessary, but perhaps the underlying
	 * ClassLoader's are sensitive to threading.
	 */
	public synchronized Collection<String> scanClassNames(String packageName) {
		String packagePath = packageName.replace('.', '/') + "/";

		try {

			return findClassesWithinPath(packagePath, "class");

		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	private Collection<String> findClassesWithinPath(String packagePath, String suffix) throws IOException {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		if (loader == null) {
			loader = DefaultResourceScanner.class.getClassLoader();
		}

		Collection<String> result = CollectionFactory.newList();
		Enumeration<URL> urls = loader.getResources(packagePath);

		//Edit By Jimmy : 从一个packagePath里拿到其里面所有的类，顶多只需要一个，否则就重复添加了
		if (urls.hasMoreElements()) {
			URL url = urls.nextElement();

			URL converted = converter.convert(url);

			scanURL(packagePath, result, converted, suffix);
		}

		return result;
	}

	private void scanURL(String packagePath, Collection<String> resources, URL url, String suffix) throws IOException {
		URLConnection connection = url.openConnection();

		JarFile jarFile;

		if (connection instanceof JarURLConnection) {
			jarFile = ((JarURLConnection) connection).getJarFile();
		} else {
			jarFile = getAlternativeJarFile(url);
		}

		if (jarFile != null) {
			scanJarFile(packagePath, resources, jarFile);
		} else if (supportsDirStream(url)) {
			Stack<Queued> queue = CollectionFactory.newStack();

			queue.push(new Queued(url, packagePath));

			while (!queue.isEmpty()) {
				Queued queued = queue.pop();

				scanDirStream(queued.packagePath, queued.packageURL, resources, queue);
			}
		} else {
			// Try scanning file system.
			String packageName = packagePath.replace("/", ".");
			if (packageName.endsWith(".")) {
				packageName = packageName.substring(0, packageName.length() - 1);
			}
			scanDir(packageName, new File(url.getFile()), resources);
		}

	}

	/**
	 * Check whether container supports opening a stream on a dir/package to get
	 * a list of its contents.
	 * 
	 * @param packageURL
	 * @return
	 */
	private boolean supportsDirStream(URL packageURL) {
		InputStream is = null;
		try {
			is = packageURL.openStream();
			return true;
		} catch (FileNotFoundException ex) {
			return false;
		} catch (IOException e) {
			return false;
		} finally {
			IOUtils.close(is);
		}
	}

	private void scanDirStream(String packagePath, URL packageURL, Collection<String> componentClassNames,
			Stack<Queued> queue) throws IOException {

		InputStream is;

		try {
			is = new BufferedInputStream(packageURL.openStream());
		} catch (FileNotFoundException ex) {
			// This can happen for certain application servers (JBoss 4.0.5 for
			// example), that
			// export part of the exploded WAR for deployment, but leave part
			// (WEB-INF/classes)
			// unexploded.

			return;
		}

		Reader reader = new InputStreamReader(is);
		LineNumberReader lineReader = new LineNumberReader(reader);

		String packageName = null;

		try {
			while (true) {
				String line = lineReader.readLine();

				if (line == null)
					break;

				if (CLASS_NAME_PATTERN.matcher(line).matches()) {
					if (packageName == null)
						packageName = packagePath.replace('/', '.');

					// packagePath ends with '/', packageName ends with '.'

					String fullClassName = packageName + line.substring(0, line.length() - CLASS_SUFFIX.length());

					componentClassNames.add(fullClassName);

					continue;
				}

				// This should match just directories. It may also match files
				// that have no extension;
				// when we read those, none of the lines should look like class
				// files.

				if (FOLDER_NAME_PATTERN.matcher(line).matches()) {
					URL newURL = new URL(packageURL.toExternalForm() + line + "/");
					String newPackagePath = packagePath + line + "/";

					queue.push(new Queued(newURL, newPackagePath));
				}
			}

			lineReader.close();
			lineReader = null;
		} finally {
			IOUtils.close(lineReader);
		}

	}

	private void scanJarFile(String packagePath, Collection<String> componentClassNames, JarFile jarFile) {
		Enumeration<JarEntry> e = jarFile.entries();

		while (e.hasMoreElements()) {
			String name = e.nextElement().getName();

			if (!name.startsWith(packagePath))
				continue;

			if (!name.endsWith(CLASS_SUFFIX))
				continue;

			if (name.contains("$"))
				continue;

			// Strip off .class and convert the slashes back to periods.

			String className = name.substring(0, name.length() - CLASS_SUFFIX.length()).replace("/", ".");

			componentClassNames.add(className);
		}
	}

	/**
	 * Scan a dir for classes. Will recursively look in the supplied directory
	 * and all sub directories.
	 * 
	 * @param packageName
	 *            Name of package that this directory corresponds to.
	 * @param dir
	 *            Dir to scanFile for clases.
	 * @param componentClassNames
	 *            List of class names that have been found.
	 */
	private void scanDir(String packageName, File dir, Collection<String> componentClassNames) {
		if (dir.exists() && dir.isDirectory()) {
			for (File file : dir.listFiles()) {
				String fileName = file.getName();
				if (file.isDirectory()) {
					scanDir(packageName + "." + fileName, file, componentClassNames);
				} else if (fileName.endsWith(CLASS_SUFFIX)) {
					String className = packageName + "."
							+ fileName.substring(0, fileName.length() - CLASS_SUFFIX.length());
					componentClassNames.add(className);
				}
			}
		}
	}

	/**
	 * For URLs to JARs that do not use JarURLConnection - allowed by the
	 * servlet spec - attempt to produce a JarFile object all the same. Known
	 * servlet engines that function like this include Weblogic and OC4J. This
	 * is not a full solution, since an unpacked WAR or EAR will not have JAR
	 * "files" as such.
	 * 
	 * @param url
	 *            URL of jar
	 * @return JarFile or null
	 * @throws java.io.IOException
	 *             If error occurs creating jar file
	 */
	private JarFile getAlternativeJarFile(URL url) throws IOException {
		String urlFile = url.getFile();
		// Trim off any suffix - which is prefixed by "!/" on Weblogic
		int separatorIndex = urlFile.indexOf("!/");

		// OK, didn't find that. Try the less safe "!", used on OC4J
		if (separatorIndex == -1) {
			separatorIndex = urlFile.indexOf('!');
		}
		if (separatorIndex != -1) {
			String jarFileUrl = urlFile.substring(0, separatorIndex);
			// And trim off any "file:" prefix.
			if (jarFileUrl.startsWith("file:")) {
				jarFileUrl = jarFileUrl.substring("file:".length());
			}
			return new JarFile(jarFileUrl);
		}
		return null;
	}

}

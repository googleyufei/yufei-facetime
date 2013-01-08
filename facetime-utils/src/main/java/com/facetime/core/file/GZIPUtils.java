package com.facetime.core.file;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import com.facetime.core.utils.PathUtils;
import com.facetime.core.utils.StringPool;
import com.facetime.core.utils.StringUtils;

/**
 * A collection of utility methods for working on GZIPed data.
 */
public class GZIPUtils {

	private static final int EXPECTED_COMPRESSION_RATIO = 5;
	private static final int BUF_SIZE = 4096;

	/**
	 * Adds a folder to the zip recursively using its getName as relative zip path.
	 */
	public static void addDirToZip(ZipOutputStream out, File dir) throws IOException {
		String path = PathUtils.getName(dir.getAbsolutePath());
		addDirToZip(out, dir, path);
	}

	/**
	 * Adds a folder to the zip recursively.
	 */
	public static void addDirToZip(ZipOutputStream out, File dir, String relativePath) throws IOException {
		boolean noRelativePath = StringUtils.isEmpty(relativePath);
		if (noRelativePath == false)
			addFileToZip(out, dir, relativePath);
		final File[] children = dir.listFiles();
		if (children != null)
			for (File child : children) {
				final String childRelativePath = (noRelativePath ? StringPool.EMPTY : relativePath + '/')
						+ child.getName();
				if (child.isDirectory())
					addDirToZip(out, child, childRelativePath);
				else
					addFileToZip(out, child, childRelativePath);
			}
	}

	public static void addDirToZip(ZipOutputStream out, String dirName) throws IOException {
		String path = PathUtils.getName(dirName);
		addDirToZip(out, new File(dirName), path);
	}

	public static void addDirToZip(ZipOutputStream out, String dirName, String relativePath) throws IOException {
		addDirToZip(out, new File(dirName), relativePath);
	}

	/*
	 * Adds a new file entry to the ZIP output stream.
	 */
	public static void addFileToZip(ZipOutputStream zos, File file, String relativeName) throws IOException {
		addFileToZip(zos, file, relativeName, null);
	}

	public static void addFileToZip(ZipOutputStream zos, File file, String relativeName, String comment)
			throws IOException {
		while (relativeName.length() != 0 && relativeName.charAt(0) == '/')
			relativeName = relativeName.substring(1);

		boolean isDir = file.isDirectory();
		if (isDir && !StringUtils.endsWithChar(relativeName, '/'))
			relativeName += "/";

		long size = isDir ? 0 : file.length();
		ZipEntry e = new ZipEntry(relativeName);
		e.setTime(file.lastModified());
		e.setComment(comment);
		if (size == 0) {
			e.setMethod(ZipEntry.STORED);
			e.setSize(0);
			e.setCrc(0);
		}
		zos.putNextEntry(e);
		if (!isDir) {
			InputStream is = new BufferedInputStream(new FileInputStream(file));
			try {
				IOUtils.copy(is, zos);
			} finally {
				IOUtils.close(is);
			}
		}
		zos.closeEntry();
	}

	public static void addFileToZip(ZipOutputStream zos, String fileName, String relativeName) throws IOException {
		addFileToZip(zos, new File(fileName), relativeName, null);
	}

	public static void addFileToZip(ZipOutputStream zos, String fileName, String relativeName, String comment)
			throws IOException {
		addFileToZip(zos, new File(fileName), relativeName, comment);
	}

	/**
	 * Closes zip file safely.
	 */
	public static void close(ZipFile zipFile) {
		if (zipFile != null)
			try {
				zipFile.close();
			} catch (IOException ioex) {
				// ignore
			}
	}

	public static ZipOutputStream createSingleEntryOutputStream(File entryFile) throws IOException {
		String entryName = entryFile.getName();
		String ext = PathUtils.getExtension(entryName);
		if (StringUtils.isNotEmpty(ext))
			entryName = entryName.substring(0, entryName.length() - ext.length() - 1/* 减1是减去扩展名分隔符*/);
		return createSingleEntryOutputStream(entryName, entryFile);
	}

	public static ZipOutputStream createSingleEntryOutputStream(String entryFileName) throws IOException {
		return createSingleEntryOutputStream(new File(entryFileName));
	}

	// ---------------------------------------------------------------- zip

	/**
	 * CreatingUtils an <code>ZipOutputStream</zip> to zip file with single entry.
	 */
	public static ZipOutputStream createSingleEntryOutputStream(String entryName, File entryFile) throws IOException {
		String zipFileName = entryFile.getAbsolutePath();

		FileOutputStream fos = new FileOutputStream(new File(zipFileName));
		ZipOutputStream zos = new ZipOutputStream(fos);
		ZipEntry ze = new ZipEntry(entryName);
		try {
			zos.putNextEntry(ze);
		} catch (IOException ioex) {
			IOUtils.close(fos);
			throw ioex;
		}
		return zos;
	}

	public static ZipOutputStream createSingleEntryOutputStream(String entryName, String entryFileName)
			throws IOException {
		return createSingleEntryOutputStream(entryName, new File(entryFileName));
	}

	/**
	 * CreatingUtils and opens zip output stream of a zip file. If zip file exist it will be recreated.
	 */
	public static ZipOutputStream createZip(File zip) throws FileNotFoundException {
		return new ZipOutputStream(new FileOutputStream(zip));
	}

	/**
	 * @see #createZip(java.io.File)
	 */
	public static ZipOutputStream createZip(String zipFile) throws FileNotFoundException {
		return createZip(new File(zipFile));
	}

	/**
	 * Returns an gzipped copy of the input array.
	 */
	public static final byte[] gzip(byte[] in) {
		try {
			// compress using GZIPOutputStream
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream(in.length / EXPECTED_COMPRESSION_RATIO);

			GZIPOutputStream outStream = new GZIPOutputStream(byteOut);

			try {
				outStream.write(in);
			} catch (Exception e) {
			}

			try {
				outStream.close();
			} catch (IOException e) {
			}

			return byteOut.toByteArray();

		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Returns an gunzipped copy of the input array.
	 *
	 * @throws IOException if the input cannot be properly decompressed
	 */
	public static final byte[] ungzip(byte[] in) throws IOException {
		// decompress using GZIPInputStream
		ByteArrayOutputStream outStream = new ByteArrayOutputStream(EXPECTED_COMPRESSION_RATIO * in.length);

		GZIPInputStream inStream = new GZIPInputStream(new ByteArrayInputStream(in));

		byte[] buf = new byte[BUF_SIZE];
		while (true) {
			int size = inStream.read(buf);
			if (size <= 0)
				break;
			outStream.write(buf, 0, size);
		}
		outStream.close();

		return outStream.toByteArray();
	}

	/**
	 * Returns an gunzipped copy of the input array. If the gzipped input has
	 * been truncated or corrupted, a best-effort attempt is made to unzip as
	 * much as possible. If no data can be extracted <code>null</code> is
	 * returned.
	 */
	public static final byte[] ungzipBestEffort(byte[] in) {
		return ungzipBestEffort(in, Integer.MAX_VALUE);
	}

	/**
	 * Returns an gunzipped copy of the input array, truncated to
	 * <code>sizeLimit</code> bytes, if necessary. If the gzipped input has
	 * been truncated or corrupted, a best-effort attempt is made to unzip as
	 * much as possible. If no data can be extracted <code>null</code> is
	 * returned.
	 */
	public static final byte[] ungzipBestEffort(byte[] in, int sizeLimit) {
		try {
			// decompress using GZIPInputStream
			ByteArrayOutputStream outStream = new ByteArrayOutputStream(EXPECTED_COMPRESSION_RATIO * in.length);

			GZIPInputStream inStream = new GZIPInputStream(new ByteArrayInputStream(in));

			byte[] buf = new byte[BUF_SIZE];
			int written = 0;
			while (true)
				try {
					int size = inStream.read(buf);
					if (size <= 0)
						break;
					if (written + size > sizeLimit) {
						outStream.write(buf, 0, sizeLimit - written);
						break;
					}
					outStream.write(buf, 0, size);
					written += size;
				} catch (Exception e) {
					break;
				}
			try {
				outStream.close();
			} catch (IOException e) {
			}

			return outStream.toByteArray();

		} catch (IOException e) {
			return null;
		}
	}

	// ---------------------------------------------------------------- close

	/**
	 * Extracts zip file content to the target directory.
	 *
	 * @param zipFile zip file
	 * @param destDir destination directory
	 */
	public static void unzip(File zipFile, File destDir) throws IOException {
		ZipFile zip = new ZipFile(zipFile);
		Enumeration en = zip.entries();

		while (en.hasMoreElements()) {
			ZipEntry entry = (ZipEntry) en.nextElement();
			File file = destDir != null ? new File(destDir, entry.getName()) : new File(entry.getName());
			if (entry.isDirectory()) {
				if (!file.mkdirs())
					if (file.isDirectory() == false)
						throw new IOException("Error creating directory: " + file);
			} else {
				File parent = file.getParentFile();
				if (parent != null && !parent.exists())
					if (!parent.mkdirs())
						if (file.isDirectory() == false)
							throw new IOException("Error creating directory: " + parent);

				InputStream in = zip.getInputStream(entry);
				OutputStream out = null;
				try {
					out = new FileOutputStream(file);
					IOUtils.copy(in, out);
				} finally {
					IOUtils.close(out);
					IOUtils.close(in);
				}
			}
		}
	}

	/**
	 * Extracts zip file content to the target directory.
	 */
	public static void unzip(String zipFile, String destDir) throws IOException {
		unzip(new File(zipFile), new File(destDir));
	}

}

package com.facetime.core.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.facetime.core.security.Base64;

/**
 * 串行化功能类<br>
 * <p>Assists with the serialization process and performs additional functionality based
 * on serialization.</p>
 * <p>
 * <ul>
 * <li>Deep clone using serialization
 * <li>Serialize managing finally and IOException
 * <li>Deserialize managing finally and IOException
 * </ul>
 *
 * <p>This class throws exceptions for invalid <code>null</code> inputs.
 * Each method documents its behaviour in more detail.</p>
 *
 */
public class SerializeUtils {

	/**
	 * <p>SerializationUtils instances should NOT be constructed in standard programming.
	 * Instead, the class should be used as <code>SerializationUtils.clone(object)</code>.</p>
	 *
	 * <p>This constructor is public to permit tools that require a JavaBean creating
	 * to operate.</p>
	 * @since 2.0
	 */
	public SerializeUtils() {
		super();
	}

	/**
	 * <p>Deep clone an <code>Object</code> using serialization.</p>
	 *
	 * <p>This is many times slower than writing clone methods by hand
	 * on all objects in your object graph. However, for complex object
	 * graphs, or for those that don't support deep cloning this can
	 * be a simple alternative implementation. Of course all the objects
	 * must be <code>Serializable</code>.</p>
	 *
	 * @param object  the <code>Serializable</code> object to clone
	 * @return the cloned object
	 * @throws RuntimeException (runtime) if the serialization fails
	 */
	public static Object clone(Serializable object) {
		return deserialize(serialize(object));
	}

	// Serialize
	//-----------------------------------------------------------------------

	/**
	 * Decode binary data from String using base64.
	 *
	 * @see #encodeBytes(byte[])
	 */
	public final static byte[] decodeBytes(String str) throws IOException {

		return Base64.decode(str);
	}

	/**
	 * Decode Object from a String by decoding with base64 then deserializing.
	 *
	 * @see #encodeObject(java.lang.Object)
	 */
	public final static Object decodeObject(String str) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bytes = new ByteArrayInputStream(decodeBytes(str));
		ObjectInputStream stream = new ObjectInputStream(bytes);
		Object result = stream.readObject();
		stream.close();

		return result;
	}

	/**
	 * <p>Deserializes a single <code>Object</code> from an array of bytes.</p>
	 *
	 * @param objectData  the serialized object, must not be null
	 * @return the deserialized object
	 * @throws IllegalArgumentException if <code>objectData</code> is <code>null</code>
	 * @throws RuntimeException (runtime) if the serialization fails
	 */
	public static Object deserialize(byte[] objectData) {
		if (objectData == null)
			throw new IllegalArgumentException("The byte[] must not be null");
		ByteArrayInputStream bais = new ByteArrayInputStream(objectData);
		return deserialize(bais);
	}

	// Deserialize
	//-----------------------------------------------------------------------
	/**
	 * <p>Deserializes an <code>Object</code> from the specified stream.</p>
	 *
	 * <p>The stream will be closed once the object is written. This
	 * avoids the need for a finally clause, and maybe also exception
	 * handling, in the application code.</p>
	 *
	 * <p>The stream passed in is not buffered internally within this method.
	 * This is the responsibility of your application if desired.</p>
	 *
	 * @param inputStream  the serialized object input stream, must not be null
	 * @return the deserialized object
	 * @throws IllegalArgumentException if <code>inputStream</code> is <code>null</code>
	 * @throws RuntimeException (runtime) if the serialization fails
	 */
	public static Object deserialize(InputStream inputStream) {
		if (inputStream == null)
			throw new IllegalArgumentException("The InputStream must not be null");
		ObjectInputStream in = null;
		try {
			// stream closed in the finally
			in = new ObjectInputStream(inputStream);
			return in.readObject();

		} catch (ClassNotFoundException ex) {
			throw new RuntimeException(ex);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException ex) {
				// ignore close exception
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T deserializeFromString(String value) {
		if (null == value)
			return null;

		byte[] value_bytes_decoded = Base64.decode(value);
		if (null == value_bytes_decoded)
			throw new RuntimeException("Errors occurred during deserialization");

		ByteArrayInputStream bytes_is = new ByteArrayInputStream(value_bytes_decoded);
		GZIPInputStream gzip_is = null;
		ObjectInputStream object_is = null;
		try {
			gzip_is = new GZIPInputStream(bytes_is);
			object_is = new ObjectInputStream(gzip_is);
			return (T) object_is.readObject();
		} catch (IOException e) {
			throw new RuntimeException("IO errors occurred during deserialization", e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Class not found error occurred during deserialization", e);
		}
	}

	/**
	 * Encode binary data into String using base64.
	 *
	 * @see #decodeBytes(java.lang.String)
	 */
	public final static String encodeBytes(byte[] data) throws IOException {
		return Base64.encodeToString(data, true);
	}

	/**
	 * Encode an Object to String by serializing it and Encoding using base64.
	 *
	 * @see #decodeObject(java.lang.String)
	 */
	public final static String encodeObject(Object o) throws IOException {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		ObjectOutputStream stream = new ObjectOutputStream(bytes);
		stream.writeObject(o);
		stream.close();
		bytes.flush();

		return encodeBytes(bytes.toByteArray());
	}

	/**
	 * <p>Serializes an <code>Object</code> to a byte array for
	 * storage/serialization.</p>
	 *
	 * @param obj  the object to serialize to bytes
	 * @return a byte[] with the converted Serializable
	 * @throws RuntimeException (runtime) if the serialization fails
	 */
	public static byte[] serialize(Serializable obj) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
		serialize(obj, baos);
		return baos.toByteArray();
	}

	/**
	 * <p>Serializes an <code>Object</code> to the specified stream.</p>
	 *
	 * <p>The stream will be closed once the object is written.
	 * This avoids the need for a finally clause, and maybe also exception
	 * handling, in the application code.</p>
	 *
	 * <p>The stream passed in is not buffered internally within this method.
	 * This is the responsibility of your application if desired.</p>
	 *
	 * @param obj  the object to serialize to bytes, may be null
	 * @param outputStream  the stream to write to, must not be null
	 * @throws IllegalArgumentException if <code>outputStream</code> is <code>null</code>
	 * @throws RuntimeException (runtime) if the serialization fails
	 */
	public static void serialize(Serializable obj, OutputStream outputStream) {
		if (outputStream == null)
			throw new IllegalArgumentException("The OutputStream must not be null");
		ObjectOutputStream out = null;
		try {
			// stream closed in the finally
			out = new ObjectOutputStream(outputStream);
			out.writeObject(obj);

		} catch (IOException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (IOException ex) {
				// ignore close exception
			}
		}
	}

	public static String serializeToString(Serializable value) {
		if (null == value)
			throw new IllegalArgumentException("symbol can't be null.");

		ByteArrayOutputStream byte_os = new ByteArrayOutputStream();
		GZIPOutputStream gzip_os = null;
		ObjectOutputStream object_os = null;
		try {
			gzip_os = new GZIPOutputStream(byte_os);
			object_os = new ObjectOutputStream(gzip_os);
			object_os.writeObject(value);
			object_os.flush();
			gzip_os.flush();
			gzip_os.finish();
		} catch (IOException e) {
			throw new RuntimeException("IO errors occurred during deserialization", e);
		}

		byte[] value_bytes_decoded = byte_os.toByteArray();

		return Base64.encodeToString(value_bytes_decoded, false);
	}

}

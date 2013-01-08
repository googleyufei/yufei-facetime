// Copyright 2006, 2008 The Apache Software Foundation
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

package com.facetime.core.resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;

/**
 * Represents a org.streets.commons.resource on the server that may be used for
 * server side processing, or may be exposed to the client side. Generally, this
 * represents an abstraction on top of files on the class path and files stored
 * in the web application context.
 * <p/>
 * Resources are often used as map keys; they should be immutable and should
 * implement hashCode() and equals().
 */
public interface Resource {
	/**
     * "/"分隔符之后的部分
	 * Returns the file portion of the Resource path, everything that follows
	 * the final forward slash.
	 */
	String name();
	/**
     * "/"符号的之前部分
	 * Returns the portion of the path up to the last forward slash; this is the
	 * directory or folder portion of the Resource.
	 */
	String folder();
	/**
     * folder+name的组合部分
	 * Return the path (the combination of folder and getName).
	 */
	String path();
	/**
	 * Returns true if the org.streets.commons.resource exists; if a stream to
	 * the content of the file may be openned.
	 * 
	 * @return true if the org.streets.commons.resource exists, false if it does
	 *         not
	 */
	boolean exists();
	/**
	 * Opens a stream to the content of the org.streets.commons.resource, or
	 * returns null if the org.streets.commons.resource does not exist.
	 * 
	 * @return an open, buffered stream to the content, if available
	 */
	InputStream openInputStream() throws IOException;

	/**
	 * 最后修改时间
	 */
	long lastModified();
	/**
	 * Returns the URL for the org.streets.commons.resource, or null if it does
	 * not exist.
	 */
	URL toURL();
	/**
	 * Returns a localized getVersion of the org.streets.commons.resource. May
	 * return null if no such org.streets.commons.resource exists.
	 */
	Resource forLocale(Locale locale);
	/**
	 * Returns a new Resource with the extension changed (or, if the
	 * org.streets.commons.resource does not have an extension, the extension is
	 * added). The new Resource may not exist (that is, {@link #toURL()} may
	 * return null.
	 * 
	 * @param extension
	 *            to apply to the org.streets.commons.resource, such as "html"
	 *            or "properties"
	 * @return the new org.streets.commons.resource
	 */
	Resource forExtension(String extension);
}

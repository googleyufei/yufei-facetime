// Copyright 2006, 2008, 2010 The Apache Software Foundation
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

package com.facetime.core.resource.impl;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;

import com.facetime.core.resource.LocalizedNameGenerator;
import com.facetime.core.resource.Resource;
import com.facetime.core.utils.PathUtils;
import com.facetime.core.utils.StringUtils;

/**
 * Abstract implementation of {@link net.street.common.resource.Resource}. Subclasses must implement the
 * abstract methods {@link net.street.common.resource.Resource#toURL()} and
 * {@link #newResource(String)} as well as toString(), hashCode() and equals().
 */
public abstract class AbstractResource implements Resource {
	
	private final String path;

	protected AbstractResource(String path) {
		assert path != null;
		this.path = PathUtils.cleanPath(path);
	}

	public final String path() {
		return path;
	}

	public final String name() {
		int slashx = path.lastIndexOf('/');
		return path.substring(slashx + 1);
	}

	public final String folder() {
		int slashx = path.lastIndexOf('/');
		return (slashx < 0) ? "" : path.substring(0, slashx);
	}

	public final Resource forLocale(Locale locale) {
		for (String path : new LocalizedNameGenerator(this.path, locale)) {
			Resource potential = createResource(path);
			if (potential.exists())
				return potential;
		}
		return null;
	}

	public final Resource forExtension(String extension) {
		assert StringUtils.isNotBlank(extension);
		int dotx = path.lastIndexOf('.');

		if (dotx < 0)
			return createResource(path + "." + extension);

		return createResource(path.substring(0, dotx + 1) + extension);
	}

	/**
	 * CreatingUtils a new org.streets.commons.resource, unless the path matches the
	 * current Resource's path (in which case, this org.streets.commons.resource
	 * is returned).
	 */
	private Resource createResource(String path) {
		if (this.path.equals(path)) return this;
		return newResource(path);
	}

	/**
	 * Simple validate for whether {@link #toURL()} returns null or not.
	 */
	public boolean exists() {
		return toURL() != null;
	}

	/**
	 * Obtains the URL for the Resource and opens the stream, wrapped by a
	 * BufferedInputStream.
	 */
	public InputStream openInputStream() throws IOException {
		URL url = toURL();

		if (url == null)
			return null;

		return new BufferedInputStream(url.openStream());
	}

	public String toString() {
		return path();
	}

	/**
	 * Factory method provided by subclasses.
	 */
	protected abstract Resource newResource(String path);
}

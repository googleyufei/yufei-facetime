// Copyright (c) 2003-2010, Jodd Team (jodd.org). All Rights Reserved.

package com.facetime.core.resource;


/**
 * Interface for listening to disk file changes.
 * @see ResourceMonitor
 */
public interface ResourceChangeListener {

	/**
	 * Invoked when one of the monitored files is created, deleted or modified.
	 */
	void onChange(Resource file);

}

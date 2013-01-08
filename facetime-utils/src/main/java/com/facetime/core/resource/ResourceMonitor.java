// Copyright (c) 2003-2010, Jodd Team (jodd.org). All Rights Reserved.
package com.facetime.core.resource;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Monitoring disk files changes. Change event is broadcasted to all registered listeners.
 *
 * @see ResourceChangeListener
 */
public class ResourceMonitor {

	protected Timer timer;
	protected final HashMap<Resource, Long> resources;
	protected final Collection<ResourceChangeListener> listeners;

	/**
	 * CreatingUtils a file monitor creating with specified polling interval in ms.
	 */
	public ResourceMonitor(long pollingInterval) {
		resources = new HashMap<Resource, Long>();
		listeners = new HashSet<ResourceChangeListener>();
		timer = new Timer(true);
		timer.schedule(new MonitorNotifier(), 0, pollingInterval);
	}

	/**
	 * Stops the file monitor polling.
	 */
	public void stop() {
		timer.cancel();
	}


	/**
	 * Adds file to listen for. File may be any file or folder,
	 * including a non-existing file in the case where the
	 * creating of the file is to be trapped.
	 */
	public void monitor(Resource file) {
		if (resources.containsKey(file) == false) {
			long modifiedTime = file.exists() ? file.lastModified() : -1;
			resources.put(file, new Long(modifiedTime));
		}
	}

	/**
	 * Removes specified file for listening.
	 */
	public void release(Resource file) {
		resources.remove(file);
	}


	/**
	 * Adds listener to this file monitor.
	 */
	public void registerListener(ResourceChangeListener fileChangeListener) {
		for (ResourceChangeListener listener : listeners) {
			if (listener == fileChangeListener) {
				return;
			}
		}
		listeners.add(fileChangeListener);
	}


	/**
	 * Removes listener from this file monitor.
	 */
	public void removeListener(ResourceChangeListener fileChangeListener) {
		Iterator<ResourceChangeListener> i = listeners.iterator();
		while(i.hasNext()) {
			ResourceChangeListener listener = i.next();
			if (listener == fileChangeListener) {
				i.remove();
				break;
			}
		}
	}

	/**
	 * Removes all file listeners/
	 */
	public void removeAllListeners() {
		listeners.clear();
	}


	/**
	 * Actual file monitor timer task.
	 */
	protected class MonitorNotifier extends TimerTask {

		@Override
		public void run() {
			for (Resource file : resources.keySet()) {
				long lastModifiedTime = resources.get(file).longValue();
				long newModifiedTime = file.exists() ? file.lastModified() : -1;

				// validate if file has been changed
				if (newModifiedTime != lastModifiedTime) {

					// store new modified time
					resources.put(file, new Long(newModifiedTime));

					// notify listeners
					for (ResourceChangeListener listener : listeners) {
						listener.onChange(file);
					}
				}
			}
		}
	}
}
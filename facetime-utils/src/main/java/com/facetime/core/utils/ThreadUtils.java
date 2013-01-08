/*
 * Copyright (C) 2010 SUNRico Inc.
 * ------------------------------------------------------------------------------
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Visit  http://www.streets.com for more infomation.
 *
 * ----------------------------------------------------------------------------------
 */

package com.facetime.core.utils;

/**
 * Thread utilities.
 */
public class ThreadUtils {

	public static void join(Thread thread) {
		try {
			thread.join();
		} catch (InterruptedException inex) {
			// ignore
		}
	}

	public static void join(Thread thread, long millis) {
		try {
			thread.join(millis);
		} catch (InterruptedException inex) {
			// ignore
		}
	}

	// ---------------------------------------------------------------- synchronization

	public static void join(Thread thread, long millis, int nanos) {
		try {
			thread.join(millis, nanos);
		} catch (InterruptedException inex) {
			// ignore
		}
	}

	/**
	 * Notifies an object for synchronization purposes.
	 */
	public static void notify(Object obj) {
		synchronized (obj) {
			obj.notify();
		}
	}

	/**
	 * Notifies an object for synchronization purposes.
	 */
	public static void notifyAll(Object obj) {
		synchronized (obj) {
			obj.notifyAll();
		}
	}

	/**
	 * Puts a thread to sleep forever.
	 */
	public static void sleep() {
		try {
			Thread.sleep(Long.MAX_VALUE);
		} catch (InterruptedException iex) {
			// ignore
		}
	}

	// ---------------------------------------------------------------- joinBy

	/**
	 * Puts a thread to sleep, without throwing an InterruptedException.
	 *
	 * @param ms     the length of time to sleep in milliseconds
	 */
	public static void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException iex) {
			// ignore
		}
	}

	/**
	 * Waits for a object for synchronization purposes.
	 */
	public static void wait(Object obj) {
		synchronized (obj) {
			try {
				obj.wait();
			} catch (InterruptedException inex) {
				//ignore
			}
		}
	}

	/**
	 * Waits for a object or a timeout for synchronization purposes.
	 */
	public static void wait(Object obj, long timeout) {
		synchronized (obj) {
			try {
				obj.wait(timeout);
			} catch (InterruptedException inex) {
				// ignore
			}
		}
	}

}
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

package com.facetime.core.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Provides simple mutual exclusion.
 * <p>
 * Interesting, the previous implementation based on Leslie Lamport's
 * "Fast Mutal Exclusion" algorithm was not working, probably due wrong
 * implementation.
 * <p>
 * Object (i.e. resource) that uses MutexLock must be accessed only between
 * {@link #lock()} and {@link #unlock()}.
 */
public class MutexLock implements Lock {

	private Thread owner;

	/**
	 * Blocks execution and acquires a lock. If already inside of critical block,
	 * it simply returns.
	 */
	public synchronized void lock() {
		Thread currentThread = Thread.currentThread();
		if (owner == currentThread) {
			return;
		}
		while (owner != null) {
			try {
				wait();
			} catch (InterruptedException iex) {
				notify();
			}
		}
		owner = currentThread;
	}

    public void lockInterruptibly() throws InterruptedException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
	 * Acquires a lock. If lock already acquired, returns <code>false</code>,
	 */
	public synchronized boolean tryLock() {
		Thread currentThread = Thread.currentThread();
		if (owner == currentThread) {
			return true;
		}
		if (owner != null) {
			return false;
		}
		owner = currentThread;
		return true;
	}

    public synchronized boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
	 * Releases a lock.
	 */
	public synchronized void unlock() {
		owner = null;
		notify();
	}

    public Condition newCondition() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}

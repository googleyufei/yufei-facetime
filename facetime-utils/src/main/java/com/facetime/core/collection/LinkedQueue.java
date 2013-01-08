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

package com.facetime.core.collection;

import java.util.LinkedList;

/**
 * Simple Queue (FIFO) based on LinkedList.
 */
public class LinkedQueue<E> {

	private LinkedList<E> list = new LinkedList<E>();

	/**
	 * Puts object in queue.
	 */
	public void put(E o) {
		list.addLast(o);
	}

	/**
	 * Returns an element (object) from queue.
	 *
	 * @return element from queue or <code>null</code> if queue is empty
	 */
	public E get() {
		if (list.isEmpty()) {
			return null;
		}
		return list.removeFirst();
	}

	/**
	 * Returns all elements from the queue and clears it.
	 */
	public Object[] getAll() {
		Object[] res = new Object[list.size()];
		for (int i = 0; i < res.length; i++) {
			res[i] = list.get(i);
		}
		list.clear();
		return res;
	}


	/**
	 * Peeks an element in the queue. Returned elements is not removed from the queue.
	 */
	public E peek() {
		return list.getFirst();
	}

	/**
	 * Returns <code>true</code> if queue is empty, otherwise <code>false</code>
	 */
	public boolean isEmpty() {
		return list.isEmpty();
	}

	/**
	 * Returns queue size.
	 */
	public int size() {
		return list.size();
	}
}

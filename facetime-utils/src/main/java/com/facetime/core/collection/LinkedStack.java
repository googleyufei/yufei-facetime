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
 * Simple FastStack (LIFO) class.
 */
public class LinkedStack<E> implements Stack<E> {

	private LinkedList<E> list = new LinkedList<E>();

	/**
	 * FastStack push.
	 */
	public void push(E o) {
		list.addLast(o);
	}

	/**
	 * FastStack pop.
	 *
	 * @return poped object from stack
	 */
	public E pop() {
		if (list.isEmpty()) {
			return null;
		}
		return list.removeLast();
	}


	public Object[] popAll() {
		Object[] res = new Object[list.size()];
		for (int i = 0; i < res.length; i++) {
			res[i] = list.get(i);
		}
		list.clear();
		return res;
	}

	/**
	 * Peek element from stack.
	 *
	 * @return peeked object
	 */
	public E peek() {
		return list.getLast();
	}


	/**
	 * Is stack empty?
	 *
	 * @return true if stack is empty
	 */
	public boolean isEmpty() {
		return list.isEmpty();
	}

	/**
	 * Returns stack size.
	 *
	 * @return	stack size
	 */
	public int size() {
		return list.size();
	}

    /**
     * 获取当前栈内快照
     * @return
     */
    public Object[] snapshot() {
        Object[] res = new Object[list.size()];
        for (int i = 0; i < res.length; i++) {
            res[i] = list.get(i);
        }
        return res;
    }

}

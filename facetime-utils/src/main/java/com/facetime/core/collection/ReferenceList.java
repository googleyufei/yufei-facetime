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

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

import com.facetime.core.utils.LE;

/**
 * Created by IntelliJ IDEA.
 * User: dzb
 * Date: 10-12-13
 * Time: 下午4:38
 * To change this template use File | Settings | File Templates.
 * TODO : 引用形式的Iterator还没有实现
 */

public class ReferenceList<E> extends AbstractList<E> {

	protected CopyOnWriteArrayList delegate;
	protected final ReferenceType referenceType;

	public ReferenceList(ReferenceType referenceType) {
		this.referenceType = referenceType;
		this.delegate = new CopyOnWriteArrayList();
	}

	@Override
	public void add(int index, E element) {
		Object ref = referenceValue(element);
		delegate.add(ref);
	}

	@Override
	public E get(int index) {
		Object ref = delegate.get(index);
		return dereferenceValue(ref);
	}

	@Override
	public Iterator<E> iterator() {
		throw LE.makeThrow("Not implement yet");
	}

	@Override
	public E remove(int index) {
		Object ref = delegate.get(index);
		E r = dereferenceValue(ref);
		delegate.remove(ref);
		return r;
	}

	@Override
	public boolean remove(Object element) {
		Object ref = referenceValue((E) element);
		return delegate.remove(ref);
	}

	@Override
	public E set(int index, E element) {
		Object ref = referenceValue(element);
		ref = delegate.set(index, ref);
		return dereferenceValue(ref);

	}

	@Override
	public int size() {
		return delegate.size();
	}

	/**
	 * Converts a reference to a symbol.
	 */
	E dereferenceValue(Object refer) {
		if (refer == null)
			return null;
		Object value = referenceType == ReferenceType.STRONG ? refer : ((Reference) refer).get();
		if (value == null)
			delegate.remove(refer); // old symbol was garbage collected
		return (E) value;
	}

	/**
	 * Creates a reference for a key.
	 */
	Object referenceValue(E v) {
		switch (referenceType) {
		case STRONG:
			return v;
		case SOFT:
			return new SoftReference(v);
		case WEAK:
			return new WeakReference(v);
		default:
			throw new AssertionError();
		}
	}
}

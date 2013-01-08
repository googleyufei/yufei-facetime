package com.facetime.core.utils;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 可以将Enumeration用作Iterator的适配器<br>
 * 
 * Utility method for converting Enumeration to an Iterator
 * class.  If you attempt to remove() an Object from the iterator, it will
 * throw an UnsupportedOperationException. Added for use by TagLib so
 * Enumeration can be supported
 */

public class IteratorAdapter<T> implements Iterator<T> {

	private final java.util.Enumeration<T> enumrtn;

	public IteratorAdapter(java.util.Enumeration<T> enumrtn) {
		this.enumrtn = enumrtn;
	}

	public boolean hasNext() {
		return enumrtn.hasMoreElements();
	}

	public T next() {
		if (!enumrtn.hasMoreElements())
			throw new NoSuchElementException("IteratorAdaptor.next() has no more elements");
		return enumrtn.nextElement();
	}

	public void remove() {
		throw new UnsupportedOperationException("Method IteratorAdaptor.remove() not implemented");
	}
}

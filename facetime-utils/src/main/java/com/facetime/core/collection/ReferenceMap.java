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
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.facetime.core.utils.StringPool;

/**
 * Concurrent hash map that wraps keys and/or values in SOFT or WEAK references.
 * Does not support <code>null</code> keys or values. Uses identity equality for
 * weak and soft keys.
 *
 * @author crazybob@google.com (Bob Lee)
 * @author fry@google.com (Charles Fry)
 * @author najgor
 */
@SuppressWarnings("unchecked")
public class ReferenceMap<K, V> extends AbstractMap<K, V> implements ConcurrentMap<K, V> {

	protected ConcurrentMap<Object, Object> delegate;

	protected final ReferenceType keyReferenceType;
	protected final ReferenceType valueReferenceType;

	private volatile Set<Map.Entry<K, V>> entrySet;

	// ---------------------------------------------------------------- map implementations

	/**
	 * Concurrent hash map that wraps keys and/or values based on specified
	 * reference types.
	 *
	 * @param keyReferenceType   key reference type
	 * @param valueReferenceType symbol reference type
	 */
	public ReferenceMap(ReferenceType keyReferenceType, ReferenceType valueReferenceType) {
		if (keyReferenceType == null || valueReferenceType == null)
			throw new IllegalArgumentException("References types can not be null.");
		if (keyReferenceType == ReferenceType.PHANTOM || valueReferenceType == ReferenceType.PHANTOM)
			throw new IllegalArgumentException("Phantom references not supported.");
		this.delegate = new ConcurrentHashMap<Object, Object>();
		this.keyReferenceType = keyReferenceType;
		this.valueReferenceType = valueReferenceType;
	}

	/**
	 * Returns <code>true</code> if the specified symbol reference has been garbage
	 * collected. The symbol behind the reference is also passed in, rather than
	 * queried inside this method, to ensure that the return statement of this
	 * method will still hold true after it has returned (that is, a symbol
	 * reference exists outside of this method which will prevent that symbol from
	 * being garbage collected).
	 *
	 * @param valueReference the symbol reference to be tested
	 * @param value          the object referenced by <code>valueReference</code>
	 * @return <code>true</code> if <code>valueReference</code> is non-null and <code>symbol</code> is <code>null</code>
	 */
	private static boolean isExpired(Object valueReference, Object value) {
		return valueReference != null && value == null;
	}

	/**
	 * Tests weak and soft references for identity equality. Compares references
	 * to other references and wrappers. If o is a reference, this returns true if
	 * r == o or if r and o reference the same non-null object. If o is a wrapper,
	 * this returns true if r's referent is identical to the wrapped object.
	 */
	private static boolean referenceEquals(Reference r, Object o) {
		if (o instanceof InternalReference) { // compare reference to reference.
			if (o == r)
				return true;
			Object referent = ((Reference) o).get(); // do they reference identical values? used in conditional puts.
			return referent != null && referent == r.get();
		}
		return ((ReferenceAwareWrapper) o).unwrap() == r.get(); // is the wrapped object identical to the referent? used in lookups.
	}

	@Override
	public void clear() {
		delegate.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		Object referenceAwareKey = makeKeyReferenceAware(key);
		return delegate.containsKey(referenceAwareKey);
	}

	@Override
	public boolean containsValue(Object value) {
		for (Object valueReference : delegate.values())
			if (value.equals(dereferenceValue(valueReference)))
				return true;
		return false;
	}

	@Override
	public Set<Map.Entry<K, V>> entrySet() {
		if (entrySet == null)
			entrySet = new EntrySet();
		return entrySet;
	}

	@Override
	public V get(final Object key) {
		Object valueReference = delegate.get(makeKeyReferenceAware(key));
		return dereferenceValue(valueReference);
	}

	@Override
	public boolean isEmpty() {
		return delegate.isEmpty();
	}

	@Override
	public V put(K key, V value) {
		return execute(PutStrategy.PUT, key, value);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> t) {
		for (Map.Entry<? extends K, ? extends V> entry : t.entrySet())
			put(entry.getKey(), entry.getValue());
	}

	@Override
	public V putIfAbsent(K key, V value) {
		return execute(PutStrategy.PUT_IF_ABSENT, key, value);
	}

	@Override
	public V remove(Object key) {
		Object referenceAwareKey = makeKeyReferenceAware(key);
		Object valueReference = delegate.remove(referenceAwareKey);
		return dereferenceValue(valueReference);
	}

	@Override
	public boolean remove(Object key, Object value) {
		return delegate.remove(makeKeyReferenceAware(key), makeValueReferenceAware(value));
	}

	// ---------------------------------------------------------------- conversions

	@Override
	public V replace(K key, V value) {
		return execute(PutStrategy.REPLACE, key, value);
	}

	@Override
	public boolean replace(K key, V oldValue, V newValue) {
		Object keyReference = referenceKey(key);
		Object referenceAwareOldValue = makeValueReferenceAware(oldValue);
		return delegate.replace(keyReference, referenceAwareOldValue, referenceValue(keyReference, newValue));
	}

	@Override
	public int size() {
		return delegate.size();
	}

	/**
	 * Dereferences an entry. Returns <code>null</code> if the key or symbol has been gc'ed.
	 */
	Entry dereferenceEntry(Map.Entry<Object, Object> entry) {
		K key = dereferenceKey(entry.getKey());
		V value = dereferenceValue(entry.getValue());
		return key == null || value == null ? null : new Entry(key, value);
	}

	/**
	 * Converts a reference to a key.
	 */
	K dereferenceKey(Object o) {
		return (K) dereference(keyReferenceType, o);
	}

	/**
	 * Converts a reference to a symbol.
	 */
	V dereferenceValue(Object o) {
		if (o == null)
			return null;
		Object value = dereference(valueReferenceType, o);
		if (o instanceof InternalReference) {
			InternalReference reference = (InternalReference) o;
			if (value == null)
				reference.finalizeReferent(); // old symbol was garbage collected
		}
		return (V) value;
	}

	/**
	 * Creates a reference for a key.
	 */
	Object referenceKey(K key) {
		switch (keyReferenceType) {
		case STRONG:
			return key;
		case SOFT:
			return new SoftKeyReference(key);
		case WEAK:
			return new WeakKeyReference(key);
		default:
			throw new AssertionError();
		}
	}

	/**
	 * CreatingUtils a reference for a symbol.
	 */
	Object referenceValue(Object keyReference, Object value) {
		switch (valueReferenceType) {
		case STRONG:
			return value;
		case SOFT:
			return new SoftValueReference(keyReference, value);
		case WEAK:
			return new WeakValueReference(keyReference, value);
		default:
			throw new AssertionError();
		}
	}

	// ---------------------------------------------------------------- inner classes

	/**
	 * Returns the refererent for reference given its reference type.
	 */
	private Object dereference(ReferenceType referenceType, Object reference) {
		return referenceType == ReferenceType.STRONG ? reference : ((Reference) reference).get();
	}

	private V execute(Strategy strategy, K key, V value) {
		Object keyReference = referenceKey(key);
		return (V) strategy.execute(this, keyReference, referenceValue(keyReference, value));
	}

	/**
	 * Wraps key so it can be compared to a referenced key for equality.
	 */
	private Object makeKeyReferenceAware(Object o) {
		return keyReferenceType == ReferenceType.STRONG ? o : new KeyReferenceAwareWrapper(o);
	}

	/**
	 * Wraps symbol so it can be compared to a referenced symbol for equality.
	 */
	private Object makeValueReferenceAware(Object o) {
		return valueReferenceType == ReferenceType.STRONG ? o : new ReferenceAwareWrapper(o);
	}

	protected interface Strategy {
		public Object execute(ReferenceMap map, Object keyReference, Object valueReference);
	}

	class Entry implements Map.Entry<K, V> {
		final K key;
		V value;

		Entry(K key, V value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public boolean equals(Object o) {
			if (!(o instanceof ReferenceMap.Entry))
				return false;

			Entry entry = (Entry) o;
			return key.equals(entry.key) && value.equals(entry.value);
		}

		@Override
		public K getKey() {
			return this.key;
		}

		@Override
		public V getValue() {
			return this.value;
		}

		@Override
		public int hashCode() {
			return key.hashCode() * 31 + value.hashCode();
		}

		@Override
		public V setValue(V newValue) {
			value = newValue;
			return put(key, newValue);
		}

		@Override
		public String toString() {
			return key + StringPool.EQUALS + value;
		}
	}

	static class FinalizableReferenceQueue extends ReferenceQueue<Object> {

		static final ReferenceQueue<Object> instance = createAndStart();

		private FinalizableReferenceQueue() {
		}

		/**
		 * Gets creating.
		 */
		public static ReferenceQueue<Object> getInstance() {
			return instance;
		}

		static FinalizableReferenceQueue createAndStart() {
			FinalizableReferenceQueue queue = new FinalizableReferenceQueue();
			queue.start();
			return queue;
		}

		void cleanUp(Reference reference) {
			try {
				((InternalReference) reference).finalizeReferent();
			} catch (Throwable t) {
				throw new IllegalStateException("Unable to clean up after reference.", t);
			}
		}

		void start() {
			Thread thread = new Thread("FinalizableReferenceQueue") {
				@Override
				@SuppressWarnings({ "InfiniteLoopStatement" })
				public void run() {
					while (true)
						try {
							cleanUp(remove());
						} catch (InterruptedException iex) { /* ignore */
						}
				}
			};
			thread.setDaemon(true);
			thread.start();
		}
	}

	/**
	 * Marker interface to differentiate external and internal references. Also
	 * duplicates FinalizableReference and Reference.get for internal use.
	 */
	interface InternalReference {
		/**
		 * Invoked on a background thread after the referent has been garbage
		 * collected.
		 */
		void finalizeReferent();

		Object get();
	}

	/**
	 * Used for keys. Overrides hash code to use identity hash code.
	 */
	static class KeyReferenceAwareWrapper extends ReferenceAwareWrapper {
		KeyReferenceAwareWrapper(Object wrapped) {
			super(wrapped);
		}

		@Override
		public int hashCode() {
			return System.identityHashCode(wrapped);
		}
	}

	/**
	 * Big hack. Used to compare keys and values to referenced keys and values
	 * without creating more references.
	 */
	static class ReferenceAwareWrapper {
		final Object wrapped;

		ReferenceAwareWrapper(Object wrapped) {
			this.wrapped = wrapped;
		}

		@Override
		public boolean equals(Object obj) {
			return this.equals(obj);
		}

		@Override
		public int hashCode() {
			return wrapped.hashCode();
		}

		Object unwrap() {
			return wrapped;
		}
	}

	// ---------------------------------------------------------------- put strategy

	class SoftKeyReference extends SoftReference<Object> implements InternalReference {
		final int hashCode;

		SoftKeyReference(Object key) {
			super(key, FinalizableReferenceQueue.getInstance());
			this.hashCode = System.identityHashCode(key);
		}

		@Override
		public boolean equals(Object o) {
			return referenceEquals(this, o);
		}

		@Override
		public void finalizeReferent() {
			delegate.remove(this);
		}

		@Override
		public int hashCode() {
			return this.hashCode;
		}
	}

	class SoftValueReference extends SoftReference<Object> implements InternalReference {
		final Object keyReference;

		SoftValueReference(Object keyReference, Object value) {
			super(value, FinalizableReferenceQueue.getInstance());
			this.keyReference = keyReference;
		}

		@Override
		public boolean equals(Object obj) {
			return referenceEquals(this, obj);
		}

		@Override
		public void finalizeReferent() {
			delegate.remove(keyReference, this);
		}
	}

	// ---------------------------------------------------------------- map entry set

	class WeakKeyReference extends WeakReference<Object> implements InternalReference {
		final int hashCode;

		WeakKeyReference(Object key) {
			super(key, FinalizableReferenceQueue.getInstance());
			this.hashCode = System.identityHashCode(key);
		}

		@Override
		public boolean equals(Object o) {
			return referenceEquals(this, o);
		}

		@Override
		public void finalizeReferent() {
			delegate.remove(this);
		}

		@Override
		public int hashCode() {
			return this.hashCode;
		}
	}

	class WeakValueReference extends WeakReference<Object> implements InternalReference {
		final Object keyReference;

		WeakValueReference(Object keyReference, Object value) {
			super(value, FinalizableReferenceQueue.getInstance());
			this.keyReference = keyReference;
		}

		@Override
		public boolean equals(Object obj) {
			return referenceEquals(this, obj);
		}

		@Override
		public void finalizeReferent() {
			delegate.remove(keyReference, this);
		}
	}

	private class EntrySet extends AbstractSet<Map.Entry<K, V>> {

		@Override
		public void clear() {
			delegate.clear();
		}

		@Override
		public boolean contains(Object o) {
			if (!(o instanceof Map.Entry))
				return false;
			Map.Entry<K, V> e = (Map.Entry<K, V>) o;
			V v = ReferenceMap.this.get(e.getKey());
			return v != null && v.equals(e.getValue());
		}

		@Override
		public Iterator<Map.Entry<K, V>> iterator() {
			return new ReferenceIterator();
		}

		@Override
		public boolean remove(Object o) {
			if (!(o instanceof Map.Entry))
				return false;
			Map.Entry<K, V> e = (Map.Entry<K, V>) o;
			return ReferenceMap.this.remove(e.getKey(), e.getValue());
		}

		@Override
		public int size() {
			return delegate.size();
		}
	}

	private enum PutStrategy implements Strategy {
		PUT {
			@Override
			public Object execute(ReferenceMap map, Object keyReference, Object valueReference) {
				return map.dereferenceValue(map.delegate.put(keyReference, valueReference));
			}
		},

		REPLACE {
			@Override
			public Object execute(ReferenceMap map, Object keyReference, Object valueReference) {
				// ensure that the existing symbol is not collected
				do {
					Object existingValueReference;
					Object existingValue;
					do {
						existingValueReference = map.delegate.get(keyReference);
						existingValue = map.dereferenceValue(existingValueReference);
					} while (isExpired(existingValueReference, existingValue));

					if (existingValueReference == null)
						return Boolean.valueOf(false); // nothing to replace

					if (map.delegate.replace(keyReference, existingValueReference, valueReference))
						return existingValue; // existingValue did not expire since we still have a reference to it
				} while (true);
			}
		},

		PUT_IF_ABSENT {
			@Override
			public Object execute(ReferenceMap map, Object keyReference, Object valueReference) {
				Object existingValueReference;
				Object existingValue;
				do {
					existingValueReference = map.delegate.putIfAbsent(keyReference, valueReference);
					existingValue = map.dereferenceValue(existingValueReference);
				} while (isExpired(existingValueReference, existingValue));
				return existingValue;
			}
		},
	}

	private class ReferenceIterator implements Iterator<Map.Entry<K, V>> {
		private Iterator<Map.Entry<Object, Object>> i = delegate.entrySet().iterator();
		private Map.Entry<K, V> nextEntry;
		private Map.Entry<K, V> lastReturned;

		private ReferenceIterator() {
			advanceToNext();
		}

		@Override
		public boolean hasNext() {
			return nextEntry != null;
		}

		@Override
		public Map.Entry<K, V> next() {
			if (nextEntry == null)
				throw new NoSuchElementException();
			lastReturned = nextEntry;
			advanceToNext();
			return lastReturned;
		}

		@Override
		public void remove() {
			ReferenceMap.this.remove(lastReturned.getKey());
		}

		private void advanceToNext() {
			while (i.hasNext()) {
				Map.Entry<K, V> entry = dereferenceEntry(i.next());
				if (entry != null) {
					nextEntry = entry;
					return;
				}
			}
			nextEntry = null;
		}
	}

}

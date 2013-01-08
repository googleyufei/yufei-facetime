package com.facetime.core.collection;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import com.facetime.core.utils.MathUtils;

/**
 * 真的很快的HashMap实现/做多次散列<br></>
 * 存储的数据在100w下，优势越明显（2倍的速度优势于HashMap），100上需要调节阀值<p><</>
 *
 * An unordered map. This implementation is a cuckoo hash map using 3 hashes, random walking, and a small stash for problematic
 * keys. Null keys are not allowed. Null values are allowed. No allocation is done except when growing the table size. <br>
 * <br>
 * This map performs very fast get, containsKey, and remove (typically O(1), worst case O(log(n))). Put may be a bit slower,
 * depending on hash collisions. Load factors greater than 0.91 greatly increase the chances the map will have to rehash to the
 * next higher POT size.
 *
 * @author dzb2k9 (第二作者)
 * @author Nathan Sweet
 */
public class FastMap<K, V> implements Map<K, V> {

	private static final int PRIME2 = 0xb4b82e39;
	private static final int PRIME3 = 0xced1c241;

	public int size;

	K[] keyTable;
	V[] valueTable;
	int capacity, stashSize;

	private float loadFactor;
	private int hashShift, mask, threshold;
	private int stashCapacity;
	private int pushIterations;

	private Entries entries;
	private Values values;
	private Keys keys;

	/**
	 * Creates a new map with an initial capacity of 32 and a load factor of 0.8. This map will hold 25 items before growing the
	 * backing table.
	 */
	public FastMap() {
		this(32, 0.8f);
	}

	/**
	 * Creates a new map with a load factor of 0.8. This map will hold initialCapacity * 0.8 items before growing the backing
	 * table.
	 */
	public FastMap(int initialCapacity) {
		this(initialCapacity, 0.8f);
	}

	/**
	 * Creates a new map with the specified initial capacity and load factor. This map will hold initialCapacity * loadFactor items
	 * before growing the backing table.
	 */
	public FastMap(int initialCapacity, float loadFactor) {
		if (initialCapacity < 0)
			throw new IllegalArgumentException("initialCapacity must be >= 0: " + initialCapacity);
		if (capacity > 1 << 30)
			throw new IllegalArgumentException("initialCapacity is too large: " + initialCapacity);
		capacity = MathUtils.nextPowerOfTwo(initialCapacity);

		if (loadFactor <= 0)
			throw new IllegalArgumentException("loadFactor must be > 0: " + loadFactor);
		this.loadFactor = loadFactor;

		threshold = (int) (capacity * loadFactor);
		mask = capacity - 1;
		hashShift = 31 - Integer.numberOfTrailingZeros(capacity);
		stashCapacity = Math.max(3, (int) Math.ceil(Math.log(capacity)) + 1);
		pushIterations = Math.max(Math.min(capacity, 32), (int) Math.sqrt(capacity) / 4);

		keyTable = (K[]) new Object[capacity + stashCapacity];
		valueTable = (V[]) new Object[keyTable.length];
	}

	public FastMap(Map<? extends K, ? extends V> map) {
		this(map.size());
		putAll(map);
	}

	@Override
	public void clear() {
		K[] keyTable = this.keyTable;
		V[] valueTable = this.valueTable;
		for (int i = capacity + stashSize; i-- > 0;) {
			keyTable[i] = null;
			valueTable[i] = null;
		}
		size = 0;
		stashSize = 0;
	}

	@Override
	public boolean containsKey(Object key) {
		int hashCode = key.hashCode();
		int index = hashCode & mask;
		if (!key.equals(keyTable[index])) {
			index = hash2(hashCode);
			if (!key.equals(keyTable[index])) {
				index = hash3(hashCode);
				if (!key.equals(keyTable[index]))
					return containsKeyStash((K) key);
			}
		}
		return true;
	}

	@Override
	public boolean containsValue(Object value) {
		return containsValue(value, false);
	}

	/**
	 * Returns true if the specified symbol is in the map. Note this traverses the entire map and compares every symbol, which may be
	 * an expensive operation.
	 */
	public boolean containsValue(Object value, boolean identity) {
		V[] valueTable = this.valueTable;
		if (value == null) {
			K[] keyTable = this.keyTable;
			for (int i = capacity + stashSize; i-- > 0;)
				if (keyTable[i] != null && valueTable[i] == null)
					return true;
		} else if (identity) {
			for (int i = capacity + stashSize; i-- > 0;)
				if (valueTable[i] == value)
					return true;
		} else
			for (int i = capacity + stashSize; i-- > 0;)
				if (value.equals(valueTable[i]))
					return true;
		return false;
	}

	/**
	 * Increases the size of the backing array to acommodate the specified number of additional items. Useful before adding many
	 * items to avoid multiple backing array resizes.
	 */
	public void ensureCapacity(int additionalCapacity) {
		int sizeNeeded = size + additionalCapacity;
		if (sizeNeeded >= threshold)
			resize(MathUtils.nextPowerOfTwo((int) (sizeNeeded / loadFactor)));
	}

	/**
	 * Returns an iterator for the entries in the map. Remove is supported. Note that the same iterator creating is returned each
	 * time this method is called. Use the {@link Entries} constructor for nested or multithreaded iteration.
	 */
	public Entries<K, V> entries() {
		if (entries == null)
			entries = new Entries(this);
		else
			entries.reset();
		return entries;
	}

	@Override
	public Set<Map.Entry<K, V>> entrySet() {
		Set<Map.Entry<K, V>> set = new HashSet<Entry<K, V>>(size);
		for (Entry2<K, V> entry2 : entries())
			set.add(entry2);
		return set;
	}

	@Override
	public V get(Object key) {
		int hashCode = key.hashCode();
		int index = hashCode & mask;
		if (!key.equals(keyTable[index])) {
			index = hash2(hashCode);
			if (!key.equals(keyTable[index])) {
				index = hash3(hashCode);
				if (!key.equals(keyTable[index]))
					return getStash((K) key);
			}
		}
		return valueTable[index];
	}

	@Override
	public boolean isEmpty() {
		return size() > 0;
	}

	/**
	 * Returns an iterator for the keys in the map. Remove is supported. Note that the same iterator creating is returned each time
	 * this method is called. Use the {@link Entries} constructor for nested or multithreaded iteration.
	 */
	public Keys<K> keys() {
		if (keys == null)
			keys = new Keys(this);
		else
			keys.reset();
		return keys;
	}

	@Override
	public Set<K> keySet() {
		Set<K> set = new HashSet<K>(keyTable.length);
		Keys<K> keys = keys();
		while (keys.hasNext())
			set.add(keys.next());
		return set;
	}

	@Override
	public V put(K key, V value) {
		if (key == null)
			throw new IllegalArgumentException("key cannot be null.");
		// Check for existing keys.
		int hashCode = key.hashCode();
		int index1 = hashCode & mask;
		K key1 = keyTable[index1];
		if (key.equals(key1)) {
			V oldValue = valueTable[index1];
			valueTable[index1] = value;
			return oldValue;
		}

		int index2 = hash2(hashCode);
		K key2 = keyTable[index2];
		if (key.equals(key2)) {
			V oldValue = valueTable[index2];
			valueTable[index2] = value;
			return oldValue;
		}

		int index3 = hash3(hashCode);
		K key3 = keyTable[index3];
		if (key.equals(key3)) {
			V oldValue = valueTable[index3];
			valueTable[index3] = value;
			return oldValue;
		}

		// Check for empty buckets.
		if (key1 == null) {
			keyTable[index1] = key;
			valueTable[index1] = value;
			if (size++ >= threshold)
				resize(capacity << 1);
			return null;
		}

		if (key2 == null) {
			keyTable[index2] = key;
			valueTable[index2] = value;
			if (size++ >= threshold)
				resize(capacity << 1);
			return null;
		}

		if (key3 == null) {
			keyTable[index3] = key;
			valueTable[index3] = value;
			if (size++ >= threshold)
				resize(capacity << 1);
			return null;
		}

		push(key, value, index1, key1, index2, key2, index3, key3);
		return null;
	}

	public void putAll(FastMap<K, V> map) {
		for (Entry2<K, V> entry : map.entries())
			put(entry.key, entry.value);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> map) {
		for (Map.Entry entry : map.entrySet())
			put((K) entry.getKey(), (V) entry.getValue());
	}

	@Override
	public V remove(Object key) {
		int hashCode = key.hashCode();
		int index = hashCode & mask;
		if (key.equals(keyTable[index])) {
			keyTable[index] = null;
			V oldValue = valueTable[index];
			valueTable[index] = null;
			size--;
			return oldValue;
		}

		index = hash2(hashCode);
		if (key.equals(keyTable[index])) {
			keyTable[index] = null;
			V oldValue = valueTable[index];
			valueTable[index] = null;
			size--;
			return oldValue;
		}

		index = hash3(hashCode);
		if (key.equals(keyTable[index])) {
			keyTable[index] = null;
			V oldValue = valueTable[index];
			valueTable[index] = null;
			size--;
			return oldValue;
		}
		return removeStash((K) key);
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public String toString() {
		if (size == 0)
			return "[]";
		StringBuilder buffer = new StringBuilder(32);
		buffer.append('[');
		K[] keyTable = this.keyTable;
		V[] valueTable = this.valueTable;
		int i = keyTable.length;
		while (i-- > 0) {
			K key = keyTable[i];
			if (key == null)
				continue;
			buffer.append(key);
			buffer.append('=');
			buffer.append(valueTable[i]);
			break;
		}
		while (i-- > 0) {
			K key = keyTable[i];
			if (key == null)
				continue;
			buffer.append(", ");
			buffer.append(key);
			buffer.append('=');
			buffer.append(valueTable[i]);
		}
		buffer.append(']');
		return buffer.toString();
	}

	/**
	 * Returns an iterator for the values in the map. Remove is supported. Note that the same iterator creating is returned each
	 * time this method is called. Use the {@link Entries} constructor for nested or multithreaded iteration.
	 */
	@Override
	public Values<V> values() {
		if (values == null)
			values = new Values(this);
		else
			values.reset();
		return values;
	}

	V removeStash(K key) {
		K[] keyTable = this.keyTable;
		for (int i = capacity, n = i + stashSize; i < n; i++)
			if (key.equals(keyTable[i])) {
				V oldValue = valueTable[i];
				removeStashIndex(i);
				size--;
				return oldValue;
			}
		return null;
	}

	void removeStashIndex(int index) {
		// If the removed location was not last, move the last tuple to the removed location.
		stashSize--;
		int lastIndex = capacity + stashSize;
		if (index < lastIndex) {
			keyTable[index] = keyTable[lastIndex];
			valueTable[index] = valueTable[lastIndex];
			valueTable[lastIndex] = null;
		} else
			valueTable[index] = null;
	}

	private boolean containsKeyStash(K key) {
		K[] keyTable = this.keyTable;
		for (int i = capacity, n = i + stashSize; i < n; i++)
			if (key.equals(keyTable[i]))
				return true;
		return false;
	}

	private V getStash(K key) {
		K[] keyTable = this.keyTable;
		for (int i = capacity, n = i + stashSize; i < n; i++)
			if (key.equals(keyTable[i]))
				return valueTable[i];
		return null;
	}

	private int hash2(long h) {
		h *= PRIME2;
		return (int) ((h ^ h >>> hashShift) & mask);
	}

	private int hash3(long h) {
		h *= PRIME3;
		return (int) ((h ^ h >>> hashShift) & mask);
	}

	private void push(K insertKey, V insertValue, int index1, K key1, int index2, K key2, int index3, K key3) {
		K[] keyTable = this.keyTable;
		V[] valueTable = this.valueTable;
		int mask = this.mask;

		// Push keys until an empty bucket is found.
		K evictedKey;
		V evictedValue;
		int i = 0, pushIterations = this.pushIterations;
		do {
			// Replace the key and symbol for one of the hashes.
			switch (MathUtils.random(2)) {
			case 0:
				evictedKey = key1;
				evictedValue = valueTable[index1];
				keyTable[index1] = insertKey;
				valueTable[index1] = insertValue;
				break;
			case 1:
				evictedKey = key2;
				evictedValue = valueTable[index2];
				keyTable[index2] = insertKey;
				valueTable[index2] = insertValue;
				break;
			default:
				evictedKey = key3;
				evictedValue = valueTable[index3];
				keyTable[index3] = insertKey;
				valueTable[index3] = insertValue;
				break;
			}

			// If the evicted key hashes to an empty bucket, put it there and stop.
			int hashCode = evictedKey.hashCode();
			index1 = hashCode & mask;
			key1 = keyTable[index1];
			if (key1 == null) {
				keyTable[index1] = evictedKey;
				valueTable[index1] = evictedValue;
				if (size++ >= threshold)
					resize(capacity << 1);
				return;
			}

			index2 = hash2(hashCode);
			key2 = keyTable[index2];
			if (key2 == null) {
				keyTable[index2] = evictedKey;
				valueTable[index2] = evictedValue;
				if (size++ >= threshold)
					resize(capacity << 1);
				return;
			}

			index3 = hash3(hashCode);
			key3 = keyTable[index3];
			if (key3 == null) {
				keyTable[index3] = evictedKey;
				valueTable[index3] = evictedValue;
				if (size++ >= threshold)
					resize(capacity << 1);
				return;
			}

			if (++i == pushIterations)
				break;

			insertKey = evictedKey;
			insertValue = evictedValue;
		} while (true);

		putStash(evictedKey, evictedValue);
	}

	/**
	 * Skips checks for existing keys.
	 */
	private void putResize(K key, V value) {
		// Check for empty buckets.
		int hashCode = key.hashCode();
		int index1 = hashCode & mask;
		K key1 = keyTable[index1];
		if (key1 == null) {
			keyTable[index1] = key;
			valueTable[index1] = value;
			if (size++ >= threshold)
				resize(capacity << 1);
			return;
		}

		int index2 = hash2(hashCode);
		K key2 = keyTable[index2];
		if (key2 == null) {
			keyTable[index2] = key;
			valueTable[index2] = value;
			if (size++ >= threshold)
				resize(capacity << 1);
			return;
		}

		int index3 = hash3(hashCode);
		K key3 = keyTable[index3];
		if (key3 == null) {
			keyTable[index3] = key;
			valueTable[index3] = value;
			if (size++ >= threshold)
				resize(capacity << 1);
			return;
		}

		push(key, value, index1, key1, index2, key2, index3, key3);
	}

	private void putStash(K key, V value) {
		if (stashSize == stashCapacity) {
			// Too many pushes occurred and the stash is full, increase the table size.
			resize(capacity << 1);
			put(key, value);
			return;
		}
		// Update key in the stash.
		K[] keyTable = this.keyTable;
		for (int i = capacity, n = i + stashSize; i < n; i++)
			if (key.equals(keyTable[i])) {
				valueTable[i] = value;
				return;
			}
		// Store key in the stash.
		int index = capacity + stashSize;
		keyTable[index] = key;
		valueTable[index] = value;
		stashSize++;
	}

	private void resize(int newSize) {
		int oldEndIndex = capacity + stashSize;

		capacity = newSize;
		threshold = (int) (newSize * loadFactor);
		mask = newSize - 1;
		hashShift = 31 - Integer.numberOfTrailingZeros(newSize);
		stashCapacity = Math.max(3, (int) Math.ceil(Math.log(newSize)));
		pushIterations = Math.max(Math.min(capacity, 32), (int) Math.sqrt(capacity) / 4);

		K[] oldKeyTable = keyTable;
		V[] oldValueTable = valueTable;

		keyTable = (K[]) new Object[newSize + stashCapacity];
		valueTable = (V[]) new Object[newSize + stashCapacity];

		size = 0;
		stashSize = 0;
		for (int i = 0; i < oldEndIndex; i++) {
			K key = oldKeyTable[i];
			if (key != null)
				putResize(key, oldValueTable[i]);
		}
	}

	public static class Entries<K, V> extends MapIterator<K, V> implements Iterable<Entry2<K, V>>,
			Iterator<Entry2<K, V>> {

		private Entry2<K, V> entry = new Entry2();

		public Entries(FastMap<K, V> map) {
			super(map);
		}

		@Override
		public boolean hasNext() {
			return hasNext;
		}

		@Override
		public Iterator<Entry2<K, V>> iterator() {
			return this;
		}

		/**
		 * Note the same entry creating is returned each time this method is called.
		 */
		@Override
		public Entry2<K, V> next() {
			if (!hasNext)
				throw new NoSuchElementException();
			K[] keyTable = map.keyTable;
			entry.key = keyTable[nextIndex];
			entry.value = map.valueTable[nextIndex];
			currentIndex = nextIndex;
			findNextIndex();
			return entry;
		}
	}

	//现在让我们来实现这个破Entry
	public static class Entry2<K, V> implements Map.Entry<K, V> {

		public K key;
		public V value;

		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return value;
		}

		@Override
		public V setValue(V value) {
			return this.value = value;
		}

		@Override
		public String toString() {
			return key + "=" + value;
		}
	}

	static public class Keys<K> extends MapIterator<K, Object> implements Iterable<K>, Iterator<K> {
		public Keys(FastMap<K, ?> map) {
			super((FastMap<K, Object>) map);
		}

		@Override
		public boolean hasNext() {
			return hasNext;
		}

		@Override
		public Iterator<K> iterator() {
			return this;
		}

		@Override
		public K next() {
			K key = map.keyTable[nextIndex];
			currentIndex = nextIndex;
			findNextIndex();
			return key;
		}

		/**
		 * Returns a new array containing the remaining keys.
		 */
		public ArrayEx<K> toArray() {
			ArrayEx array = new ArrayEx(map.size, true);
			while (hasNext)
				array.add(next());
			return array;
		}
	}

	public static class Values<V> extends MapIterator<Object, V> implements Iterable<V>, Iterator<V>, Collection<V> {
		public Values(FastMap<?, V> map) {
			super((FastMap<Object, V>) map);
		}

		@Override
		public boolean add(V o) {
			return false;
		}

		@Override
		public boolean addAll(Collection<? extends V> c) {
			return false;
		}

		@Override
		public void clear() {
		}

		@Override
		public boolean contains(Object object) {
			return map.containsValue(object); //To change body of implemented methods use File | Settings | File Templates.
		}

		@Override
		public boolean containsAll(Collection<?> c) {
			return false;
		}

		@Override
		public boolean hasNext() {
			return hasNext;
		}

		@Override
		public boolean isEmpty() {
			return map.isEmpty(); //To change body of implemented methods use File | Settings | File Templates.
		}

		@Override
		public Iterator<V> iterator() {
			return this;
		}

		@Override
		public V next() {
			V value = map.valueTable[nextIndex];
			currentIndex = nextIndex;
			findNextIndex();
			return value;
		}

		@Override
		public boolean remove(Object o) {
			return false;
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			return false;
		}

		@Override
		public boolean retainAll(Collection<?> c) {
			return false;
		}

		@Override
		public int size() {
			return map.size(); //To change body of implemented methods use File | Settings | File Templates.
		}

		@Override
		public Object[] toArray() {
			Object[] result = new Object[map.size];
			int i = 0;
			while (hasNext) {
				result[i] = next();
				i++;
			}
			return result;
		}

		@Override
		public <T> T[] toArray(T[] a) {
			if (a.length < map.size)
				a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), map.size);
			int i = 0;
			while (hasNext) {
				a[i] = (T) next();
				i++;
			}
			if (a.length > map.size)
				a[map.size] = null;
			return a;
		}

		/**
		 * Returns a new array containing the remaining values.
		 */
		public ArrayEx<V> toArray2() {
			ArrayEx array = new ArrayEx(map.size, true);
			while (hasNext)
				array.add(next());
			return array;
		}
	}

	private static class MapIterator<K, V> {

		public boolean hasNext;
		final FastMap<K, V> map;
		int nextIndex, currentIndex;

		public MapIterator(FastMap<K, V> map) {
			this.map = map;
			reset();
		}

		public void remove() {
			if (currentIndex < 0)
				throw new IllegalStateException("next must be called before remove.");
			if (currentIndex >= map.capacity)
				map.removeStashIndex(currentIndex);
			else {
				map.keyTable[currentIndex] = null;
				map.valueTable[currentIndex] = null;
			}
			currentIndex = -1;
			map.size--;
		}

		public void reset() {
			currentIndex = -1;
			nextIndex = -1;
			findNextIndex();
		}

		void findNextIndex() {
			hasNext = false;
			K[] keyTable = map.keyTable;
			for (int n = map.capacity + map.stashSize; ++nextIndex < n;)
				if (keyTable[nextIndex] != null) {
					hasNext = true;
					break;
				}
		}
	}
}

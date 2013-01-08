package com.facetime.core.collection;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import com.facetime.core.utils.LE;
import com.facetime.core.utils.MathUtils;

/**
 * 实现了Stack和List接口的数组<br></>
 * 当这个数组被构建为无顺序的（即ordered=false的时候）那么实现在进行数据删除的时候有效的回避了避免了内存拷贝System.arraycopy()<br></>
 * 把最后一个元素移动到移除元素的位置<br></>
 * 对数组的排序可以使用{@link ArraySorting} 类
 *
 *
 * A resizable, ordered or unordered array of objects. If unordered, this class avoids a memory copy when removing elements (the
 * last element is moved to the removed element's position).
 *
 * @author dzb2k9 (实现了Stack和List接口)
 * @author Nathan Sweet
 */
public class ArrayEx<E> implements Iterable<E>, Stack<E>, List<E> {
	/**
	 *  直接对底层数组进行存取 ，如果如果泛型类型不是Object类型<br></>
	 *  那么唯一可以改变这个类型的地方就是使用{@link ArrayEx#ArrayEx(int, boolean, Class<E>)}进行构建的时候了
	 */
	public E[] items;
	public int size;
	public boolean ordered;

	private ArrayIterator<E> iterator;

	/**
	 * Creates an ordered array with a capacity of 16.
	 */
	public ArrayEx() {
		this(16, true);
	}

	/**
	 * Creates a new array containing the elements in the specific array. The new array will be ordered if the specific array is
	 * ordered. The capacity is set to the number of elements, so any subsequent elements added will cause the backing array to be
	 * grown.
	 */
	public ArrayEx(ArrayEx array) {
		this(array.size, array.ordered, (Class<E>) array.items.getClass().getComponentType());
		size = array.size;
		System.arraycopy(array.items, 0, items, 0, size);
	}

	/**
	 * Creates an ordered array with the specified capacity.
	 */
	public ArrayEx(int capacity) {
		this(capacity, true);
	}

	/**
	 * @param ordered If false, methods that remove elements may change the order of other elements in the array, which avoids a
	 *           memory copy.
	 * @param capacity Any elements added beyond this will cause the backing array to be grown.
	 */
	public ArrayEx(int capacity, boolean ordered) {
		this.ordered = ordered;
		items = (E[]) new Object[capacity];
	}

	/**
	 * Creates a new array with {@link #items} of the specified type.
	 * @param ordered If false, methods that remove elements may change the order of other elements in the array, which avoids a
	 *           memory copy.
	 * @param capacity Any elements added beyond this will cause the backing array to be grown.
	 */
	public ArrayEx(int capacity, boolean ordered, Class<E> arrayType) {
		this.ordered = ordered;
		items = (E[]) Array.newInstance(arrayType, capacity);
	}

	public static void main(String[] args) {
		ArrayEx<Character> array = ArrayEx.of(Character.class);
		array.add('a');
		array.add('b');
		System.out.println(array.get(0));
	}

	public static <T> ArrayEx<T> of(Class<T> clazzOfT) {
		ArrayEx<T> array2 = new ArrayEx<T>(16, true, clazzOfT);
		return array2;
	}

	public static <T> ArrayEx<T> of(T[] array) {
		ArrayEx<T> array2 = new ArrayEx<T>(array.length, true, (Class<T>) array.getClass().getComponentType());
		array2.addAll(array);
		return array2;
	}

	/**
	 * append
	 * @param value
	 * @return
	 */
	@Override
	public boolean add(E value) {
		E[] items = this.items;
		if (size == items.length)
			items = resize(Math.max(8, (int) (size * 1.75f)));
		items[size++] = value;
		return true;
	}

	/**
	 * inert
	 * @param index
	 * @param element
	 */
	@Override
	public void add(int index, E element) {
		E[] items = this.items;
		if (size == items.length)
			items = resize(Math.max(8, (int) (size * 1.75f)));
		if (ordered)
			System.arraycopy(items, index, items, index + 1, size - index);
		else
			items[size] = items[index];
		size++;
		items[index] = element;
	}

	public ArrayEx addAll(ArrayEx<? extends E> array) {
		addAll(array, 0, array.size);
		return this;
	}

	public ArrayEx addAll(ArrayEx<? extends E> array, int offset, int length) {
		if (offset + length > array.size)
			throw new IllegalArgumentException("offset + length must be <= size: " + offset + " + " + length + " <= "
					+ array.size);
		addAll(array.items, offset, length);
		return this;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		int o_size = size;
		E[] array = (E[]) c.toArray(); //暴力转换？？
		addAll(array, 0, array.length);
		return size > o_size;
	}

	public ArrayEx addAll(E[] array) {
		addAll(array, 0, array.length);
		return this;
	}

	public ArrayEx<E> addAll(E[] array, int offset, int length) {
		E[] items = this.items;
		int sizeNeeded = size + length - offset;
		if (sizeNeeded >= items.length)
			items = resize(Math.max(8, (int) (sizeNeeded * 1.75f)));
		System.arraycopy(array, offset, items, size, length);
		size += length;
		return this;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		int o_size = size;
		E[] array = (E[]) c.toArray(); //暴力转换？？
		addAll(array, index, array.length);
		return size > o_size;
	}

	public ArrayEx addValue(E value) {
		E[] items = this.items;
		if (size == items.length)
			items = resize(Math.max(8, (int) (size * 1.75f)));
		items[size++] = value;
		return this;
	}

	@Override
	public void clear() {
		Object[] items = this.items;
		for (int i = 0, n = size; i < n; i++)
			items[i] = null;
		size = 0;
	}

	/**
	 * @param value
	 * @param identity 是否使用==进行比较
	 * @return
	 */
	public boolean contains(E value, boolean identity) {
		Object[] items = this.items;
		int i = size - 1;
		if (identity || value == null) {
			while (i >= 0)
				if (items[i--] == value)
					return true;
		} else
			while (i >= 0)
				if (value.equals(items[i--]))
					return true;
		return false;
	}

	/**
	 * 对对象使用equals
	 * @param o
	 * @return
	 */
	@Override
	public boolean contains(Object o) {
		return contains((E) o, false);
	}

	@Override
	public boolean containsAll(Collection<?> collection) {
		boolean b = true;
		for (Object o : collection) {
			b = contains((E) o, false);
			if (!b)
				break;
		}
		return b;
	}

	/**
	 * Increases the size of the backing array to acommodate the specified number of additional items. Useful before adding many
	 * items to avoid multiple backing array resizes.
	 * @return {@link #items}
	 */
	public E[] ensureCapacity(int additionalCapacity) {
		int sizeNeeded = size + additionalCapacity;
		if (sizeNeeded >= items.length)
			resize(Math.max(8, sizeNeeded));
		return items;
	}

	@Override
	public E get(int index) {
		if (index >= size)
			throw new IndexOutOfBoundsException(String.valueOf(index));
		return items[index];
	}

	public int indexOf(E value, boolean identity) {
		Object[] items = this.items;
		if (identity || value == null) {
			for (int i = 0, n = size; i < n; i++)
				if (items[i] == value)
					return i;
		} else
			for (int i = 0, n = size; i < n; i++)
				if (value.equals(items[i]))
					return i;
		return -1;
	}

	@Override
	public int indexOf(Object o) {
		return indexOf((E) o, false);
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Returns an iterator for the items in the array. Remove is supported. Note that the same iterator creating is returned each
	 * time this method is called. Use the {@link ArrayIterator} constructor for nested or multithreaded iteration.
	 */
	@Override
	public Iterator<E> iterator() {
		if (iterator == null)
			iterator = new ArrayIterator(this);
		iterator.index = 0;
		return iterator;
	}

	public int lastIndexOf(E value, boolean identity) {
		Object[] items = this.items;
		if (identity || value == null) {
			for (int i = size - 1; i >= 0; i--)
				if (items[i] == value)
					return i;
		} else
			for (int i = size - 1; i >= 0; i--)
				if (value.equals(items[i]))
					return i;
		return -1;
	}

	@Override
	public int lastIndexOf(Object o) {
		return lastIndexOf((E) o, false);
	}

	@Override
	public ListIterator<E> listIterator() {
		//TODO : 少用 没实现
		throw LE.notImplementYet();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		//TODO : 少用 没实现
		throw LE.notImplementYet();
	}

	/**
	 * Returns the last item.
	 */
	@Override
	public E peek() {
		return items[size - 1];
	}

	/**
	 * 弹出最后一个元素 同时清除
	 */
	@Override
	public E pop() {
		--size;
		E item = items[size];
		items[size] = null;
		return item;
	}

	@Override
	public Object[] popAll() {
		Object[] res = new Object[size];
		System.arraycopy(items, 0, res, 0, size);
		size = 0;
		for (int i = 0; i < items.length; i++)
			items[i] = null;
		return res;
	}

	@Override
	public void push(E o) {
		E[] items = this.items;
		if (size == items.length)
			items = resize(Math.max(8, (int) (size * 1.75f)));
		items[size++] = o;
	}

	public boolean remove(E value, boolean identity) {
		Object[] items = this.items;
		if (identity || value == null) {
			for (int i = 0, n = size; i < n; i++)
				if (items[i] == value) {
					remove(i);
					return true;
				}
		} else
			for (int i = 0, n = size; i < n; i++)
				if (value.equals(items[i])) {
					remove(i);
					return true;
				}
		return false;
	}

	/**
	 * 依据索引删除
	 * @param index
	 * @return
	 */
	@Override
	public E remove(int index) {
		Object[] items = this.items;
		if (index >= size)
			throw new IndexOutOfBoundsException(String.valueOf(index));
		E value = (E) items[index];
		size--;
		if (ordered)
			System.arraycopy(items, index + 1, items, index, size - index);
		else
			items[index] = items[size];
		items[size] = null;
		return value;
	}

	@Override
	public boolean remove(Object o) {
		return remove((E) o, false);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean modified = false;
		Iterator<?> e = iterator();
		while (e.hasNext())
			if (c.contains(e.next())) {
				e.remove();
				modified = true;
			}
		return modified;
	}

	/**
	 *
	 * @param index 索引
	 * @return 返回自身
	 */
	public ArrayEx removeValue(int index) {
		remove(index);
		return this;
	}

	/**
	 * 返回自身
	 * @param o
	 * @return
	 */
	public ArrayEx removeValue(Object o) {
		remove((E) o, false);
		return this;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		boolean modified = false;
		Iterator<E> e = iterator();
		while (e.hasNext())
			if (!c.contains(e.next())) {
				e.remove();
				modified = true;
			}
		return modified;
	}

	public void reverse() {
		for (int i = 0, lastIndex = size - 1, n = size / 2; i < n; i++) {
			int ii = lastIndex - i;
			E temp = items[i];
			items[i] = items[ii];
			items[ii] = temp;
		}
	}

	@Override
	public E set(int index, E value) {
		if (index >= size)
			throw new IndexOutOfBoundsException(String.valueOf(index));
		items[index] = value;
		return items[index];
	}

	/**
	 * Reduces the size of the backing array to the size of the actual items. This is useful to release memory when many items have
	 * been removed, or if it is known that more items will not be added.
	 */
	public void shrink() {
		resize(size);
	}

	public void shuffle() {
		for (int i = size - 1; i >= 0; i--) {
			int ii = MathUtils.random(i);
			E temp = items[i];
			items[i] = items[ii];
			items[ii] = temp;
		}
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public Object[] snapshot() {
		return toArray();
	}

	/**
	 * Sorts this array. The array elements must implement {@link Comparable}. This method is not thread safe (uses
	 * {@link ArraySorting#instance()}).
	 */
	public void sort() {
		ArraySorting.instance().sort(items, 0, size);
	}

	/**
	 * Sorts the array. This method is not thread safe (uses {@link ArraySorting#instance()}).
	 */
	public void sort(Comparator<E> comparator) {
		ArraySorting.instance().sort(items, comparator, 0, size);
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		int len = toIndex - fromIndex + 1;
		ArrayEx arry = new ArrayEx(len, ordered);
		System.arraycopy(this.items, fromIndex, arry.items, 0, len);
		arry.size = len;
		return arry;
	}

	@Override
	public E[] toArray() {
		E[] result = (E[]) Array.newInstance(items.getClass().getComponentType(), size);
		System.arraycopy(items, 0, result, 0, size);
		return result;
	}

	@Override
	public <T> T[] toArray(T[] a) {
		if (a.length < size)
			a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
		Object[] items = this.items;
		for (int i = 0, n = size; i < n; i++)
			a[i] = (T) items[i];
		return a;
	}

	@Override
	public String toString() {
		if (size == 0)
			return "[]";
		Object[] items = this.items;
		StringBuilder buffer = new StringBuilder(32);
		buffer.append('[');
		buffer.append(items[0]);
		for (int i = 1; i < size; i++) {
			buffer.append(", ");
			buffer.append(items[i]);
		}
		buffer.append(']');
		return buffer.toString();
	}

	protected E[] resize(int newSize) {
		E[] items = this.items;
		E[] newItems = (E[]) java.lang.reflect.Array.newInstance(items.getClass().getComponentType(), newSize);
		System.arraycopy(items, 0, newItems, 0, Math.min(items.length, newItems.length));
		this.items = newItems;
		return newItems;
	}

	public static class ArrayIterator<T> implements Iterator<T> {
		private final ArrayEx<T> array;
		int index;

		public ArrayIterator(ArrayEx<T> array) {
			this.array = array;
		}

		@Override
		public boolean hasNext() {
			return index < array.size;
		}

		@Override
		public T next() {
			if (index >= array.size)
				throw new NoSuchElementException(String.valueOf(index));
			return array.items[index++];
		}

		@Override
		public void remove() {
			index--;
			array.remove(index);
		}
	}
}

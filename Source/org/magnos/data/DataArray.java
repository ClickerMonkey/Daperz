/* 
 * NOTICE OF LICENSE
 * 
 * This source file is subject to the Open Software License (OSL 3.0) that is 
 * bundled with this package in the file LICENSE.txt. It is also available 
 * through the world-wide-web at http://opensource.org/licenses/osl-3.0.php
 * If you did not receive a copy of the license and are unable to obtain it 
 * through the world-wide-web, please send an email to pdiffenderfer@gmail.com 
 * so we can send you a copy immediately. If you use any of this software please
 * notify me via my website or email, your feedback is much appreciated. 
 * 
 * @copyright   Copyright (c) 2011 Magnos Software (http://www.magnos.org)
 * @license     http://opensource.org/licenses/osl-3.0.php
 * 				Open Software License (OSL 3.0)
 */

package org.magnos.data;

/**
 * An array of data elements which are the same type and size. An array can be
 * either lazy or not. A lazy array does not allocate an array to store the data
 * elements, but instead creates a new element for each get and doesn't cache
 * the element in the set. An array that is not lazy will have an internal cache
 * of items
 * 
 * @author Philip Diffenderfer
 *
 * @param <T>
 * 		The type of element stored in the array.
 */
public class DataArray<T extends Data> extends AbstractData 
{
	
	// The root element to use to create copies.
	private final T root;
	
	// The array of elements (or null if lazy).
	private final Data[] elements;
	
	// The number of elements in the array.
	private final int count;
	
	// Whether or not this array is lazy. A lazy array will read data everytime 
	// a get is invoked, and will write data everytime a set is invoked. A non- 
	// lazy array will only call write and read to each data item when write or 
	// read is explicitly called on the array, which also requires the data items 
	// to be stored in memory at all times. To save space use a lazy array (true),
	// for a slightly more efficient array choose a non-lazy array (false).
	private final boolean lazy;
	
	
	/**
	 * Instantiates a new DataArray.
	 * 
	 * @param root
	 * 		The data to use to create other data objects of similar type and
	 * 		having the same characteristics.
	 * @param count
	 * 		The number of data elements to store in the array.
	 * @param lazy
	 * 		Whether or not this array is lazy. A lazy array will read data 
	 * 		everytime a get is invoked, and will write data everytime a set
	 * 		is invoked. A non-lazy array will only call write and read to each
	 *		data item when write or read is explicitly called on the array, 
	 *		which also requires the data items to be stored in memory at all
	 *		times. To save space use a lazy array (true), for a slightly more 
	 *		efficient array choose a non-lazy array (false).
	 */
	public DataArray(T root, int count, boolean lazy) 
	{
		super(count * root.getSize());
		this.root = root;
		this.count = count;
		this.lazy = lazy;
		this.elements = (lazy ? null : new Data[count]);
	}
	
	/**
	 * Gets the data at the given index. If the index is outside the bounds of
	 * the array an IndexOutOfBoundsException is thrown.
	 * 
	 * @param index
	 * 		The index of the data in the array.
	 * @return
	 * 		The element at the given index. This element may be null if this
	 * 		array is not lazy and it has not been read from a store yet.
	 */
	@SuppressWarnings("unchecked")
	public T get(int index) 
	{
		if (index < 0 || index >= count) {
			throw new IndexOutOfBoundsException();
		}
		Data data = null;
		if (lazy) {
			data = root.copy();
			data.read(offset(index) + getActualLocation(), getStore());
		}
		else {
			data = elements[index];
		}
		return (T)data;
	}

	/**
	 * Updates the data element at the given index and returns it. A data 
	 * element which is updated is one that was recently read from the store. 
	 * If the index is outside the bounds of the array an 
	 * IndexOutOfBoundsException is thrown.
	 * 
	 * @param index
	 * 		The index of the data in the array.
	 * @return
	 * 		The updated element. This will return a non-null element.
	 */
	public T update(int index) 
	{
		T current = get(index);
		if (!lazy) {
			if (elements[index] == null) {
				elements[index] = root.copy();	
			}
			elements[index].read(offset(index) + getActualLocation(), getStore());
		}
		return current;
	}
	
	/**
	 * Sets the data at the given index. If the index is outside the bounds of
	 * the array an IndexOutOfBoundsException is thrown. If data is null the
	 * element at the given index will be set to null in the internal array
	 * if this array is not null.
	 * 
	 * @param index
	 * 		The index of the data in the array.
	 * @param data
	 * 		The data to set.
	 */
	public void set(int index, T data) 
	{
		if (index < 0 || index >= count) {
			throw new IndexOutOfBoundsException();
		}
		if (lazy) {
			if (data != null) {
				data.write(offset(index) + getActualLocation(), getStore());	
			}
		}
		else {
			elements[index] = data;
		}
	}
	
	/**
	 * Returns the number of elements in the array.
	 * 
	 * @return
	 * 		The number of elements in the array.
	 */
	public int getLength() 
	{
		return count;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onRead(int location, Store store) 
	{
		// Only read all of the elements in if its not lazy.
		if (!lazy) {
			for (int i = 0; i < count; i++) {
				if (elements[i] == null) {
					elements[i] = root.copy();
				}
				elements[i].read(offset(i) + location, store);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onWrite(int location, Store store) 
	{
		// Only write all of the elements out if its not lazy.
		if (!lazy) {
			for (int i = 0; i < count; i++) {
				if (elements[i] != null) {
					elements[i].write(offset(i) + location, store);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Data copy() 
	{	
		DataArray<T> copy = new DataArray<T>(root, count, lazy);
		copy.setLocation(getLocation());
		copy.setStore(getStore());
		copy.setParent(getParent());
		for (int i = 0; i < count; i++) {
			if (elements[i] != null) {
				copy.elements[i] = elements[i].copy();
			}
		}
		return copy;
	}
	
	/**
	 * Returns the offset of the element given its index.
	 * 
	 * @param index
	 * 		The index of the element in the array.
	 * @return
	 * 		The offset in bytes.
	 */
	private int offset(int index) 
	{
		return (index * root.getSize());
	}
	
	/**
	 * Creates a new DataArray given a root element to which elements in the
	 * created array should be copied from.
	 * 
	 * @param <E>
	 * 		The type of element in the array.
	 * @param root
	 * 		The element to use for copying.
	 * @param count
	 * 		The number of data elements in the array.
	 * @param lazy
	 * 		Whether or not this array is lazy. A lazy array will read data 
	 * 		everytime a get is invoked, and will write data everytime a set
	 * 		is invoked. A non-lazy array will only call write and read to each
	 *		data item when write or read is explicitly called on the array, 
	 *		which also requires the data items to be stored in memory at all
	 *		times. To save space use a lazy array (true), for a slightly more 
	 *		efficient array choose a non-lazy array (false).
	 * @return
	 * 		The reference to the newly instantiated DataArray.
	 */
	public static <E extends Data> DataArray<E> create(E root, int count, boolean lazy) 
	{
		return new DataArray<E>(root, count, lazy);
	}
	
	/**
	 * Creates a new DataArray given an element type to which elements in the
	 * created array should be instantiated form. If the given type does not
	 * have a default constructor (one without arguments) a RuntimeException
	 * will be thrown.
	 * 
	 * @param <E>
	 * 		The type of element in the array.
	 * @param type
	 * 		The class of the data element.
	 * @param count
	 * 		The number of data elements in the array.
	 * @param lazy
	 * 		Whether or not this array is lazy. A lazy array will read data 
	 * 		everytime a get is invoked, and will write data everytime a set
	 * 		is invoked. A non-lazy array will only call write and read to each
	 *		data item when write or read is explicitly called on the array, 
	 *		which also requires the data items to be stored in memory at all
	 *		times. To save space use a lazy array (true), for a slightly more 
	 *		efficient array choose a non-lazy array (false).
	 * @return
	 * 		The reference to the newly instantiated DataArray.
	 */
	public static <E extends Data> DataArray<E> create(Class<E> type, int count, boolean lazy)
	{
		try {
			return new DataArray<E>(type.newInstance(), count, lazy);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}

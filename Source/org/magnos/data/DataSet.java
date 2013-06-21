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

import java.util.ArrayList;
import java.util.List;

/**
 * A set of data pieces.
 * 
 * @author Philip Diffenderfer
 *
 */
public class DataSet extends AbstractData 
{

	// The current and actual size of the set in bytes.
	private int dataSize = 0;
	
	// The data added to the set.
	private List<Data> dataList = new ArrayList<Data>();

	
	/**
	 * Instantiates a new DataSet.
	 * 
	 * @param size
	 * 		The fixed size of the DataSet in bytes.
	 */
	public DataSet(int size) 
	{
		super(size);
	}
	
	/**
	 * Instantiates a new DataSet.
	 * 
	 * @param set
	 * 		A DataSet to copy data from.
	 */
	public DataSet(DataSet set) 
	{
		super(set.getSize());
		setLocation(set.getLocation());
		setStore(set.getStore());
		for (Data d : set.dataList) {
			add(d);
		}
	}
	
	/**
	 * Adds a piece of data to this set. If the set cannot hold the given data
	 * a RuntimeException will be thrown.
	 * 
	 * @param datas
	 * 		The data to add to the set.
	 * @throws RuntimeException
	 * 		The data was to large to add to this set.
	 */
	public void add(Data ... datas) 
	{
		for (Data d : datas) {
			if (d.getSize() + dataSize > getSize()) {
				throw new RuntimeException();
			}
			d.setLocation(dataSize);
			d.setStore(getStore());
			d.setParent(this);
			dataSize += d.getSize();
			dataList.add(d);
		}
	}
	
	/**
	 * Gets the data element at the given index.
	 * 
	 * @param <T>
	 * 		The element retrieved from the list is cast to this type.
	 * @param index
	 * 		The index of the data in this set.
	 * @return
	 * 		The data at the given index.
	 * @throws IndexOutOfBoundsException
	 * 		No data exists at the given index.
	 */
	@SuppressWarnings("unchecked")
	public <T extends Data> T get(int index) 
	{
		return (T)dataList.get(index);
	}
	
	/**
	 * Returns the current or actual size of the DataSet.
	 * 
	 * @return
	 * 		The current or actual size of the DataSet in bytes.
	 */
	public int getSetSize() 
	{
		return dataSize;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onRead(int location, Store store) 
	{
		for (Data d : dataList) {
			d.read(location, store);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onWrite(int location, Store store) 
	{
		for (Data d : dataList) {
			d.write(location, store);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Data copy() 
	{
		DataSet set = new DataSet(getSize());
		set.setStore(getStore());
		set.setLocation(getLocation());
		set.setParent(getParent());
		for (Data d : dataList) {
			set.add(d);
		}
		return set;
	}
	
	/**
	 * Creates a DataSet with the given array of data.
	 * 
	 * @param data
	 * 		The data to add to the DataSet.
	 * @return
	 * 		The reference to a newly instantiated DataSet which contains the
	 * 		given data.
	 */
	public static DataSet create(Data ... data) 
	{
		int total = 0;
		for (Data d : data) {
			total += d.getSize();
		}
		DataSet set = new DataSet(total);
		for (Data d : data) {
			set.add(d);
		}
		return set;
	}

}

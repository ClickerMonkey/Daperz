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

package org.magnos.data.var;

import org.magnos.data.Bits;
import org.magnos.data.Data;
import org.magnos.data.Store;

/**
 * A Var with a boolean value.
 * 
 * @author Philip Diffenderfer
 *
 */
public class BooleanVar extends AbstractVar<Boolean> 
{
	
	// The size of the var in bytes.
	public static final int SIZE = 1;

	// The value of the var.
	private boolean value;

	/**
	 * Instantiates a new BooleanVar. 
	 */
	public BooleanVar() 
	{
		this(null, 0, false);
	}

	/**
	 * Instantiates a new BooleanVar.
	 *  
	 * @param value
	 * 		The initial value.
	 */
	public BooleanVar(boolean value) 
	{
		this(null, 0, value);
	}
	
	/**
	 * Instantiates a new BooleanVar.
	 * 
	 * @param store
	 * 		The intial store.
	 * @param location
	 * 		The intial location.
	 */
	public BooleanVar(Store store, int location) 
	{
		this(store, location, false);
	}
	
	/**
	 * Instantiates a new BooleanVar.
	 * 
	 * @param store
	 * 		The initial store.
	 * @param location
	 * 		The initial location.
	 * @param value
	 * 		The initial value.
	 */
	public BooleanVar(Store store, int location, boolean value) 
	{
		super(SIZE);
		this.setStore(store);
		this.setLocation(location);
		this.set(value);
	}
	
	/**
	 * Returns the current value of the var.
	 * 
	 * @return
	 * 		The current value.
	 */
	public boolean get() 
	{
		return value;
	}
	
	/**
	 * Sets the value of this var, but does not write it to the store.
	 * 
	 * @param value
	 * 		The new value.
	 */
	public void set(boolean value) 
	{
		this.value = value;
	}
	
	/**
	 * Sets the value of this var and writes it to the store.
	 * 
	 * @param value
	 * 		The new value.
	 */
	public void put(boolean value) 
	{
		this.value = value;
		this.write();
	}
	
	/**
	 * Returns the value of this var by first reading it from the store.
	 * 
	 * @return
	 * 		The value of this var.
	 */
	public boolean take() 
	{
		this.read();
		return value;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean getValue() 
	{
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValue(Boolean value) 
	{
		this.value = value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onRead(int location, Store store) 
	{
		value = Bits.getBoolean(store.get(location, SIZE)[0]);
	}
	
	/**
	 * {@inheritDoc} 
	 */
	@Override
	public void onWrite(int location, Store store) 
	{
		store.put(location, new byte[] {Bits.getBooleanBytes(value)});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Data copy() 
	{
		return new BooleanVar(getStore(), getLocation(), value);
	}

}

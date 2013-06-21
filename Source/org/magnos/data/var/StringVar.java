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

import org.magnos.data.Data;
import org.magnos.data.Store;

/**
 * A Var with a boolean value.
 * 
 * @author Philip Diffenderfer
 *
 */
public class StringVar extends AbstractVar<String> 
{
	
	// The value of the var.
	private final byte[] value;

	/**
	 * Instantiates a new BooleanVar. 
	 */
	public StringVar(int length) 
	{
		this(length, null, 0, null);
	}

	/**
	 * Instantiates a new BooleanVar.
	 *  
	 * @param value
	 * 		The initial value.
	 */
	public StringVar(int length, String value) 
	{
		this(length, null, 0, value);
	}
	
	/**
	 * Instantiates a new BooleanVar.
	 * 
	 * @param store
	 * 		The intial store.
	 * @param location
	 * 		The intial location.
	 */
	public StringVar(int length, Store store, int location) 
	{
		this(length, store, location, null);
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
	public StringVar(int length, Store store, int location, String value) 
	{
		super(length);
		this.value = new byte[getSize()];
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
	public String get() 
	{
		return new String(value).trim();
	}
	
	/**
	 * Sets the value of this var, but does not write it to the store.
	 * 
	 * @param value
	 * 		The new value.
	 */
	public void set(String value) 
	{
		int max = Math.min(getSize(), (value == null ? 0 : value.length()));
		for (int i = 0; i < max; i++){
			this.value[i] = (byte)value.charAt(i);
		}
		for (int i = max; i < getSize(); i++) {
			this.value[i] = (byte)0x00;
		}
	}
	
	/**
	 * Sets the value of this var and writes it to the store.
	 * 
	 * @param value
	 * 		The new value.
	 */
	public void put(String value) 
	{
		this.set(value);
		this.write();
	}
	
	/**
	 * Returns the value of this var by first reading it from the store.
	 * 
	 * @return
	 * 		The value of this var.
	 */
	public String take() 
	{
		this.read();
		return get();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getValue() 
	{
		return get();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValue(String value) 
	{
		this.set(value);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onRead(int location, Store store) 
	{
		store.get(location, value);
	}
	
	/**
	 * {@inheritDoc} 
	 */
	@Override
	public void onWrite(int location, Store store) 
	{
		store.put(location, value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Data copy() 
	{
		return new StringVar(getSize(), getStore(), getLocation(), get());
	}

}

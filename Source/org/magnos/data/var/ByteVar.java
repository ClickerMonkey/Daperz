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
 * A Var with a byte value.
 * 
 * @author Philip Diffenderfer
 *
 */
public class ByteVar extends AbstractVar<Byte> 
{
	
	// The size of the var in bytes.
	public static final int SIZE = 1;

	// The value of the var.
	private byte value;

	/**
	 * Instantiates a new ByteVar. 
	 */
	public ByteVar() 
	{
		this(null, 0, (byte)0);
	}

	/**
	 * Instantiates a new ByteVar.
	 *  
	 * @param value
	 * 		The initial value.
	 */
	public ByteVar(byte value) 
	{
		this(null, 0, value);
	}
	
	/**
	 * Instantiates a new ByteVar.
	 * 
	 * @param store
	 * 		The intial store.
	 * @param location
	 * 		The intial location.
	 */
	public ByteVar(Store store, int location) 
	{
		this(store, location, (byte)0);
	}
	
	/**
	 * Instantiates a new ByteVar.
	 * 
	 * @param store
	 * 		The initial store.
	 * @param location
	 * 		The initial location.
	 * @param value
	 * 		The initial value.
	 */
	public ByteVar(Store store, int location, byte value) 
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
	public byte get() 
	{
		return value;
	}
	
	/**
	 * Adds the given value to this var and returns the result.
	 * 
	 * @param x
	 * 		The value to add to this var.
	 * @return
	 * 		The result of the operation.
	 */
	public byte add(byte x) 
	{
		return (value += x);
	}
	
	/**
	 * Sets the var to the max between this var and the value.
	 * 
	 * @param x
	 * 		The value to take the max of with this var.
	 * @return
	 * 		The result of the operation.
	 */
	public byte max(byte x) 
	{
		return (value = (byte)Math.max(value, x));
	}

	/**
	 * Sets the var to the min between this var and the value.
	 * 
	 * @param x
	 * 		The value to take the min of with this var.
	 * @return
	 * 		The result of the operation.
	 */
	public byte min(byte x) 
	{
		return (value = (byte)Math.min(value, x));
	}

	/**
	 * Multiplies the given value to this var and returns the result.
	 * 
	 * @param x
	 * 		The value to multiply by this var.
	 * @return
	 * 		The result of the operation.
	 */
	public byte mul(byte x) 
	{
		return (value *= x);
	}
	
	/**
	 * Sets the value of this var, but does not write it to the store.
	 * 
	 * @param value
	 * 		The new value.
	 */
	public void set(byte value) 
	{
		this.value = value;
	}
	
	/**
	 * Sets the value of this var and writes it to the store.
	 * 
	 * @param value
	 * 		The new value.
	 */
	public void put(byte value) 
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
	public byte take() 
	{
		this.read();
		return value;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Byte getValue() 
	{
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValue(Byte value) 
	{
		this.value = value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onRead(int location, Store store) 
	{
		value = store.get(location, SIZE)[0];
	}
	
	/**
	 * {@inheritDoc} 
	 */
	@Override
	public void onWrite(int location, Store store) 
	{
		store.put(location, new byte[] {value});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Data copy() 
	{
		return new ByteVar(getStore(), getLocation(), value);
	}

}

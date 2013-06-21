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

package org.magnos.data.store;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.magnos.data.Bits;
import org.magnos.data.StoreAccess;


/**
 * A store which is kept entirely in memory and is not persisted to any medium.
 * 
 * @author Philip Diffenderfer
 *
 */
public class MemoryStore extends AbstractStore
{
	
	// The initial capacity of the store. Also updated when the store is resized.
	private int capacity;
	
	// The buffer which holds all of the stores data.
	private ByteBuffer buffer;

	
	/**
	 * Instantiates a MemoryStore.
	 * 
	 * @param name
	 * 		The unique name of the Store.
	 */
	public MemoryStore(String name) 
	{
		this(name, 0);
	}
	
	/**
	 * Instantiates a MemoryStore.
	 *  
	 * @param name
	 * 		The unique name of the Store.
	 * @param capacity
	 * 		The requested capacity of the store.
	 */
	public MemoryStore(String name, int capacity) 
	{
		super(name);
		this.capacity = capacity;
	}
	
	/**
	 * Instantiates a MemoryStore.
	 *  
	 * @param name
	 * 		The unique name of the Store.
	 * @param access
	 * 		The requested access to the store.
	 * @param capacity
	 * 		The requested capacity of the store.
	 */
	public MemoryStore(String name, StoreAccess access, int capacity) 
	{
		super(name);
		this.capacity = capacity;
		this.create(access, capacity);
	}

	/**
	 * Returns the buffer which contains all this stores data.
	 * 
	 * @return
	 * 		The reference to the underlying buffer.
	 */
	public ByteBuffer getBuffer() 
	{
		return buffer;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int storeOpen(StoreAccess access) throws IOException 
	{
		return storeResize(capacity);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void storeLoad() throws IOException 
	{
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void storeFlush() throws IOException 
	{
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void storeClose() throws IOException 
	{
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void storeDelete() 
	{
		Bits.free(buffer);
		buffer = null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean storeExists() 
	{
		return (buffer != null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int storeResize(int bytes) 
	{
		if (buffer == null) {
			buffer = ByteBuffer.allocateDirect(bytes);
		}
		else {
			buffer.position(0);
			buffer.limit(Math.min(buffer.capacity(), bytes));
			
			ByteBuffer newBuffer = ByteBuffer.allocateDirect(bytes);
			newBuffer.put(buffer);
			newBuffer.clear();
			
			Bits.free(buffer);
			
			buffer = newBuffer;
		}
		capacity = buffer.capacity();
		return capacity;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void storeGet(int location, byte[] bytes, int offset, int length) throws IOException 
	{
		buffer.position(location);
		buffer.get(bytes, offset, length);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void storePut(int location, byte[] bytes, int offset, int length) throws IOException 
	{
		buffer.position(location);
		buffer.put(bytes, offset, length);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void storeGet(int location, ByteBuffer b) throws IOException 
	{
		buffer.position(location);
		buffer.limit(location + b.remaining());
		b.put(buffer);
		buffer.limit(buffer.capacity());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void storePut(int location, ByteBuffer b) throws IOException 
	{
		buffer.position(location);
		buffer.put(b);
	}

}

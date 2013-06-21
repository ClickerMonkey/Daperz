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

import org.magnos.data.Store;
import org.magnos.data.StoreAccess;
import org.magnos.data.error.StoreClosedException;
import org.magnos.data.error.StoreIOException;


/**
 * An abstract Store. Implementations must adhere to the guidelines specified
 * in the documentation of the abstract methods.
 * 
 * @author Philip Diffenderfer
 *
 */
public abstract class AbstractStore implements Store 
{
	
	// Whether this store will automatically open if its closed.
	private boolean autoOpen = true;

	// Whether this store will automatically write data to any persisted medium
	// after every write occurs.
	private boolean autoFlush = false;
	
	// Whether this store will automatically load data from any persisted medium
	// before every read occurs.
	private boolean autoLoad = false;
	
	// The access of the store to any persisted medium.
	private StoreAccess access = StoreAccess.ReadWrite;
	
	// The unique name of this store. If this store is persisted to a file 
	// system this may be the file name.
	private final String name;
	
	// Whether the store is currently closed.
	private volatile boolean closed = true;
	
	// The current capacity of the store. This does not represent the desired 
	// capacity but the actual capacity of the store.
	private volatile int capacity = -1;
	
	
	/**
	 * Instantiates a new AbstractStore.
	 * 
	 * @param name
	 * 		The unique name of this store. If this store is persisted to a 
	 * 		file system this may be the file name.
	 */
	protected AbstractStore(String name) 
	{
		// Check name for non-null constraint.
		if (name == null) {
			throw new NullPointerException();
		}
		this.name = name;
	}
	
	
	/**
	 * Should open the store and return the capacity. If the requested access
	 * could not be granted an exception should be thrown. This will only be
	 * invoked if the store has been closed previously or has not been opened
	 * yet. If the store doesn't exist this should forcefully create it
	 * before opening it with the requested access.
	 * 
	 * @return
	 * 		The capacity of the store.
	 * @throws IOException
	 * 		An error occurred opening the store with the given access.
	 */
	protected abstract int storeOpen(StoreAccess access) throws IOException;
	
	/**
	 * Should load contents of the store into memory from a persisted medium if 
	 * the implementation supports it.
	 * 
	 * @throws IOException
	 * 		An error occurred loading the store.
	 */
	protected abstract void storeLoad() throws IOException;
	
	/**
	 * Should flush the contents of the store from memory to a persisted medium
	 * if the implementation supports it.
	 * 
	 * @throws IOException
	 * 		An error occurred flushing the store.
	 */
	protected abstract void storeFlush() throws IOException;
	
	/**
	 * Should close the store. This will only be invoked if the store has
	 * been successfully opened previously. If there is an error closing the
	 * store an exception should be thrown. This exception however may be 
	 * ignored due to the likeliness that the store is still closed even after
	 * an exception is thrown.
	 * 
	 * @throws IOException
	 * 		An error occurred closing the store.
	 */
	protected abstract void storeClose() throws IOException;
	
	/**
	 * Resizes the store to the given capacity. If the store could not be 
	 * resized to the requested capacity an exception should be thrown. This
	 * will only be called if the store was successfully opened previously.
	 * If the resize results in a larger store the end of the store should be
	 * padded with zeros (which the system will implicitly due most likely).
	 * 
	 * @param capacity
	 * 		The new capacity of the store.
	 * @throws IOException
	 * 		An error occured resizing the store.
	 */
	protected abstract int storeResize(int capacity) throws IOException;

	/**
	 * Returns whether the store exists in its probable medium. This could be
	 * called before or after the store has been opened or created.
	 * 
	 * @return
	 * 		True if the store already exists.
	 */
	protected abstract boolean storeExists();
	
	/**
	 * Deletes the medium to which the store has been persisting to.
	 */
	protected abstract void storeDelete();

	/**
	 * Gets data at the given location and stores it in the given byte array.
	 * 
	 * @param location
	 * 		The offset to get the data, in bytes, from the beginning of the store.
	 * @param bytes
	 * 		The array of bytes to place the data in.
	 * @param offset
	 * 		The offset in the byte array to place the data.
	 * @param length
	 * 		The number of bytes to copy from the store and place in the array.
	 * @throws IOException
	 * 		An error occurred reading from the store.
	 */
	protected abstract void storeGet(int location, byte[] bytes, int offset, int length) throws IOException;
	
	/**
	 * Gets data at the given location and stores it in the given ByteBuffer.
	 * 
	 * @param location
	 * 		The offset to get the data, in bytes, from the beginning of the store.
	 * @param buffer
	 * 		The buffer to place the data in. The buffer will be filled with data
	 * 		to its limit (so it has no remaining bytes).
	 * @throws IOException
	 * 		An error occurred reading from the store.
	 */
	protected abstract void storeGet(int location, ByteBuffer buffer) throws IOException;
	
	/**
	 * Puts data at the given location from the given byte array.
	 * 
	 * @param location
	 * 		The offset to put the data, in bytes, from the beginning of the store.
	 * @param bytes
	 * 		The array of bytes to take data from.
	 * @param offset
	 * 		The offset in the byte array to take the data.
	 * @param length
	 * 		The number of bytes to copy from the array and place in the store.
	 * @throws IOException
	 * 		An error occurred writing to the store.
	 */
	protected abstract void storePut(int location, byte[] bytes, int offset, int length) throws IOException;

	/**
	 * Puts data at the given location from the given ByteBuffer.
	 * 
	 * @param location
	 * 		The offset to put the data, in bytes, from the beginning of the store.
	 * @param buffer
	 * 		The buffer to take the data from. The buffer will be emptied of data
	 * 		to its limit (so it has no remaining bytes).
	 * @throws IOException
	 * 		An error occurred writing to the store.
	 */
	protected abstract void storePut(int location, ByteBuffer buffer) throws IOException;


	/**
	 * Validates an action by checking if the store is closed. If the store
	 * is closed and the store could not be automatically opened this will throw
	 * a StoreClosedException. 
	 */
	protected final void validate() throws StoreClosedException 
	{
		if (closed) {
			if (autoOpen) {
				// Open with previous or default access.
				open(access);
			}
			else {
				throw new StoreClosedException();
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getName() 
	{
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final int capacity() 
	{
		synchronized (this) {
			return capacity;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final StoreAccess getAccess() 
	{
		synchronized (this) {
			return access;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final StoreAccess setAccess(StoreAccess newAccess) 
	{
		synchronized (this) {
			// If its not open, just change default access
			if (closed) {
				access = newAccess;
			}
			// Its open, so close it and try to reopen it.
			else {
				close();
				access = open(newAccess);
			}
			return access;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean exists() 
	{
		synchronized (this) {
			return (!closed || storeExists());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void delete() 
	{
		synchronized (this) 
		{
			// Requires write permissions.
			access.tryWrite(this);

			// Ensure its closed, then delete it.
			close();
			storeDelete();	
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override	
	public final int capacity(int newCapacity) 
	{
		synchronized (this) 
		{
			// Open if necessary, or throw StoreClosedException
			validate();
			
			// Requires write permissions.
			access.tryWrite(this);
				
			// Store is open..
			if (capacity != newCapacity) {
				try {
					capacity = storeResize(newCapacity);
				}
				catch (IOException e) {
					throw new StoreIOException(e);
				}
			}	
			return capacity;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final StoreAccess create(StoreAccess initialAccess, int initialCapacity) throws StoreIOException 
	{
		synchronized (this) 
		{
			// Open with given access.
			if (open(initialAccess) != null) 
			{
				// Size to capacity.
				capacity(initialCapacity);	
			}
			
			return access;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final StoreAccess open(StoreAccess initialAccess) throws StoreIOException 
	{
		synchronized (this) 
		{
			// If access is null, no permissions can be granted!
			if (initialAccess == null) {
				return null;
			}
			
			// Only open if closed or requesting different access.
			if (closed || initialAccess != access) 
			{
				if (!closed) {
					close();
				}
				
				try {
					capacity = storeOpen(initialAccess);
					// Open succeeds, access granted.
					access = initialAccess;
					closed = false;
				}
				// An error occurred opening store.
				catch (IOException e) {
//					e.printStackTrace();
					// Try opening with lower access.
					access = open(initialAccess.next);
				}
				
				// Finally load data to memory if open is success.
				if (!closed) {
					load();	
				}
			}	
			
			// The accepted access to the store.
			return access;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void load() throws StoreIOException 
	{
		synchronized (this) 
		{
			// Open if necessary, or throw StoreClosedException
			validate();
			
			// Try loading data from persisted medium to memory.
			try {
				storeLoad();
			}
			catch (IOException e) {
				throw new StoreIOException(e);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void flush() throws StoreIOException 
	{
		synchronized (this) 
		{
			// Only flush if not closed and we can write
			if (!closed && access.canWrite) 
			{
				try {
					storeFlush();
				}
				catch (IOException e) {
					throw new StoreIOException(e);
				}	
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void close() throws StoreIOException 
	{
		synchronized (this) 
		{
			// Only close if not already closed.
			if (!closed) {
				// Always flush before close.
				flush();
				
				try {
					storeClose();
				}
				catch (IOException e) {
					throw new StoreIOException(e);
				}
				finally {
					// Assume its closed even when an exception is thrown.
					closed = true;
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean isClosed() 
	{
		synchronized (this) {
			return closed;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean isOpen() 
	{
		synchronized (this) {
			return !closed;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void get(int location, byte[] bytes, int offset, int length) 
	{
		synchronized (this) 
		{
			// Automatically loads data after a single write.
			if (autoLoad) {
				load();
			}
			else {
				// Open if necessary, or throw StoreClosedException
				validate();	
			}

			// Requires read permissions.
			access.tryRead(this);

			try {
				storeGet(location, bytes, offset, length);	
			}
			catch (IOException e) {
				throw new StoreIOException(e);
			}
		}	
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void get(int location, ByteBuffer buffer) 
	{
		synchronized (this) 
		{
			// Automatically loads data after a single write.
			if (autoLoad) {
				load();
			}
			else {
				// Open if necessary, or throw StoreClosedException
				validate();	
			}
			
			// Requires read permissions.
			access.tryRead(this);

			try {
				storeGet(location, buffer);	
			}
			catch (IOException e) {
				throw new StoreIOException(e);
			}
		}	
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void get(int location, byte[] bytes) 
	{
		get(location, bytes, 0, bytes.length);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final byte[] get(int location, int size) 
	{
		byte[] data = new byte[size];
		get(location, data, 0, size);
		return data;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void put(int location, byte[] bytes, int offset, int length) 
	{
		synchronized (this) 
		{
			// Open if necessary, or throw StoreClosedException
			validate();
			
			// Requires write permissions.
			access.tryWrite(this);
			
			try {
				storePut(location, bytes, offset, length);
			}
			catch (IOException e) {
				throw new StoreIOException(e);
			}
			
			// Automatically flushes data after a single write.
			if (autoFlush) {
				flush();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void put(int location, ByteBuffer buffer) 
	{
		synchronized (this) 
		{
			// Open if necessary, or throw StoreClosedException
			validate();
			
			// Requires write permissions.
			access.tryWrite(this);
			
			try {
				storePut(location, buffer);
			}
			catch (IOException e) {
				throw new StoreIOException(e);
			}
			
			// Automatically flushes data after a single write.
			if (autoFlush) {
				flush();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void put(int location, byte[] bytes) 
	{
		put(location, bytes, 0, bytes.length);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean isAutoOpen() 
	{
		return autoOpen;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setAutoOpen(boolean autoOpen) 
	{ 
		this.autoOpen = autoOpen;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean isAutoFlush() 
	{
		return autoFlush;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setAutoFlush(boolean autoFlush) 
	{
		this.autoFlush = autoFlush;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean isAutoLoad() 
	{
		return autoLoad;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setAutoLoad(boolean autoLoad) 
	{
		this.autoLoad = autoLoad;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final int hashCode() 
	{
		return name.hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean equals(Object o) 
	{
		if (o instanceof Store) {
			return ((Store)o).getName().equals(name);
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String toString() 
	{
		return name;
	}

}

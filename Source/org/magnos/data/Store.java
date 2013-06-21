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

import java.nio.ByteBuffer;

import org.magnos.data.error.StoreAccessException;
import org.magnos.data.error.StoreClosedException;
import org.magnos.data.error.StoreIOException;
import org.magnos.data.store.FileStore;
import org.magnos.data.store.MappedStore;
import org.magnos.data.store.MemoryStore;


/**
 * A place to store data as bytes and ByteBuffers. Stores can represent data
 * thats persisted in memory, on disk, or persisted to RAM and sync'd to the
 * file system (mapped). Granting access to opening and creating stores is 
 * implementation dependent.
 * 
 * @see FileStore
 * @see MappedStore
 * @see MemoryStore
 * 
 * @author Philip Diffenderfer
 *
 */
public interface Store 
{
	
	/**
	 * Returns the name of the store. This should be unique amongst stores to
	 * avoid concurrent access to the same data source. This may represent a
	 * file name if the store implementation persists its data to some medium.
	 * 
	 * @return
	 * 		The unique name of this store.
	 */
	public String getName();
	
	/**
	 * Creates the store by opening it with the given access and setting the 
	 * initial capacity. If the store already exists it will not modify any
	 * pre-existing data but simply change the capacity and access if possible.
	 * If the requested access cannot be granted the next best access will be 
	 * granted. If no access could be granted null will be returned.
	 * 
	 * @param initialAccess
	 * 		The requested access to the store.
	 * @param initialCapacity
	 * 		The new capacity of the store.
	 * @return
	 * 		The access that has been granted on the store, or null if no access
	 * 		could be granted to the store.
	 * @throws StoreIOException
	 * 		An error occurred in the implementation. See attached exception.
	 * @throws StoreAccessException
	 * 		The store does not have sufficient access to perform the operation.
	 */
	public StoreAccess create(StoreAccess initialAccess, int initialCapacity) throws StoreIOException, StoreAccessException;
	
	/**
	 * Explicitly opens this store for reading and writing. If this store has
	 * auto open set true then this is called when the user tries doing their 
	 * first read or write since the stores creation or a recent close. If the 
	 * requested access cannot be granted the next best access will be granted. 
	 * If no access could be granted null will be returned.
	 * 
	 * @param initialAccess
	 * 		The requested access to the store.
	 * @return
	 * 		The access that has been granted on the store, or null if no access
	 * 		could be granted to the store.
	 * @throws StoreIOException
	 * 		An error occurred in the implementation. See attached exception.
	 */
	public StoreAccess open(StoreAccess initialAccess) throws StoreIOException;
	
	/**
	 * Explicitly loads the data from the store into memory. This is mainly
	 * used for memory mapped files that may be modified by different threads
	 * and the current threads wants to see current data. If this store has auto
	 * load set true then this is called when the store is open, however it will
	 * not be called before every read.
	 * 
	 * @throws StoreIOException
	 * 		An error occurred in the implementation. See attached exception.
	 * @throws StoreClosedException
	 * 		The store is closed. If auto open is set to true this still may be
	 * 		thrown if there was an error opening the store.
	 */
	public void load() throws StoreIOException, StoreClosedException;
	
	/**
	 * Explicitly writes the data to the store. This is mainly used for memory
	 * mapped files that occasionally can have unwritten sections. If this store
	 * has auto write set trye then this is called whenever data is written to
	 * the store.
	 * 
	 * @throws StoreIOException
	 * 		An error occurred in the implementation. See attached exception.
	 * @throws StoreClosedException
	 * 		The store is closed. If auto open is set to true this still may be
	 * 		thrown if there was an error opening the store.
	 */
	public void flush() throws StoreIOException, StoreClosedException;
	
	/**
 	 * Closes this store if not closed already. A closed store will throw 
	 * exceptions when data is trying to be read from and written to.
	 * 
	 * @throws StoreIOException
	 * 		An error occurred in the implementation. See attached exception.
	 */
	public void close() throws StoreIOException;
	
	/**
	 * Removes this store from any persisted medium. If open, this store will be
	 * automatically closed before anything is removed. 
	 * 
	 * @throws StoreIOException
	 * 		An error occurred in the implementation. See attached exception.
	 * @throws StoreAccessException
	 * 		The store does not have sufficient access to perform the operation.
	 */
	public void delete() throws StoreIOException, StoreAccessException;
	
	/**
	 * Whether the store exists. If this store is based in memory it will always
	 * exist. If this store is persisted to some medium it will returned whether
	 * that medium exists (i.e. a file in the filesystem). This is independent
	 * on whether the store is open or closed.
	 * 
	 * @return
	 * 		True if the store exists in some medium, otherwise false.
	 */
	public boolean exists();
	
	/**
	 * Returns the size or capacity of the store in bytes. 
	 * 
	 * @return
	 * 		The store's size in bytes.
	 */
	public int capacity();
	
	/**
	 * Sets the stores size or capacity in bytes. If the given capacity is less
	 * than the current capacity the difference will be removed from the end of
	 * this store losing the last bytes. If the given capacity is greater than
	 * the current capacity than this will have no affect on the data currently
	 * in the store.
	 * 
	 * @param newCapacity
	 * 		The desired capacity of the store.
	 * @return
	 * 		The capacity of this store at the end of the method invokation.
	 * @throws StoreIOException
	 * 		An error occurred in the implementation. See attached exception.
	 * @throws StoreClosedException
	 * 		The store is closed. If auto open is set to true this still may be
	 * 		thrown if there was an error opening the store.
	 * @throws StoreAccessException
	 * 		The store does not have sufficient access to perform the operation.
	 */
	public int capacity(int newCapacity) throws StoreIOException, StoreClosedException, StoreAccessException;
	
	/**
	 * Returns the current access of this store to its persisted medium if any 
	 * exists. The default access if the store is opened implicitly is ReadWrite
	 * access. The current access may have less access then the requested access
	 * sent to create or open if the requested access could not be granted.
	 * 
	 * @return
	 * 		The current access to the persisted medium.
	 */
	public StoreAccess getAccess();
	
	/**
	 * Sets the access of this store to its persisted medium. If this store is
	 * closed the access will be changed without affecting it. If the store is
	 * open it will be closed and reopened with the new access, or an access
	 * which is grantable if the given access cannot be granted.
	 * 
	 * @param newAccess
	 * 		The requested access to the store. 
	 * @return
	 * 		The granted access on the store.
	 */
	public StoreAccess setAccess(StoreAccess newAccess);
	
	/**
	 * Whether the store is closed. A store is closed by default. Once opened a
	 * store can be closed if its invoked explicitly, or if the store has been 
	 * deleted.
	 * 
	 * @return
	 * 		True if the store is closed, otherwise false.
	 */
	public boolean isClosed();
	
	/**
	 * Whether the store is open. A store is closed by default. It can only be
	 * opened by explicitly calling open or create.
	 * 
	 * @return
	 * 		True if the store is open, otherwise false.
	 */
	public boolean isOpen();

	/**
	 * Whether the store automatically opens with the default or last access if
	 * some action needs to be done to the store. This is true by default. 
	 *  
	 * @return
	 * 		True if the store can automatically open, otherwise false.
	 */
	public boolean isAutoOpen();
	
	/**
	 * Sets whether the store automatically opens with the default or last 
	 * access if some actions need to be done to the store. This is true by 
	 * default.
	 * 
	 * @param autoOpen
	 * 		True if the store should automatically open, otherwise false.
	 */
	public void setAutoOpen(boolean autoOpen);
	
	/**
	 * Whether the store automatically flushes data immediately after its written
	 * to the store. This is false by defaut.
	 * 
	 * @return
	 * 		True if the store should automatically flush, otherwise false.
	 */
	public boolean isAutoFlush();
	
	/**
	 * Sets whether the store automatically flushes data immediately after its
	 * written to the store. This is false by default.
	 * 
	 * @param autoFlush
	 * 		True if the store should automatically flush, otherwise false.
	 */
	public void setAutoFlush(boolean autoFlush);
	
	/**
	 * Whether the store automatically loads data immediately before its read
	 * from the store. This is false by defaut.
	 * 
	 * @return
	 * 		True if the store should automatically load, otherwise false.
	 */
	public boolean isAutoLoad();
	
	/**
	 * Set whether the store automatically loads data immediately before its read
	 * from the store. This is false by defaut.
	 * 
	 * @param autoLoad
	 * 		True if the store should automatically load, otherwise false.
	 */
	public void setAutoLoad(boolean autoLoad);
	
	
	/**
	 * Writes the array of bytes to the store at the given location.
	 * 
	 * @param location
	 * 		The location in the store, the offset of bytes from the beginning.
	 * @param bytes
	 * 		The array of bytes to write.
	 * @throws StoreIOException
	 * 		An error occurred in the implementation. See attached exception.
	 * @throws StoreClosedException
	 * 		The store is closed. If auto open is set to true this still may be
	 * 		thrown if there was an error opening the store.
	 * @throws StoreAccessException
	 * 		The store does not have sufficient access to perform the operation.
	 */
	public void put(int location, byte[] bytes) throws StoreIOException, StoreClosedException, StoreAccessException;
	
	/**
	 * Writes a section in the array of bytes to the store at the given location.
	 * 
	 * @param location
	 * 		The location in the store, the offset of bytes from the beginning.
	 * @param bytes
	 * 		The array of bytes to write.
	 * @param offset
	 * 		The offset into the array of bytes.
	 * @param length
	 * 		The number of bytes to write starting at the offset in the array.
	 * @throws StoreIOException
	 * 		An error occurred in the implementation. See attached exception.
	 * @throws StoreClosedException
	 * 		The store is closed. If auto open is set to true this still may be
	 * 		thrown if there was an error opening the store.
	 * @throws StoreAccessException
	 * 		The store does not have sufficient access to perform the operation.
	 */
	public void put(int location, byte[] bytes, int offset, int length) throws StoreIOException, StoreClosedException, StoreAccessException;
	
	/**
	 * Writes a ByteBuffer to the store at the given location.
	 * 
	 * @param location
	 * 		The location in the store, the offset of bytes from the beginning.
	 * @param buffer
	 * 		The buffer of data to write. The bytes written to the store are the
	 * 		bytes that exist between the buffers position and limit.
	 * @throws StoreIOException
	 * 		An error occurred in the implementation. See attached exception.
	 * @throws StoreClosedException
	 * 		The store is closed. If auto open is set to true this still may be
	 * 		thrown if there was an error opening the store.
	 * @throws StoreAccessException
	 * 		The store does not have sufficient access to perform the operation.
	 */
	public void put(int location, ByteBuffer buffer) throws StoreIOException, StoreClosedException, StoreAccessException;
	

	/**
	 * Reads an array of bytes from the store at the given location.
	 * 
	 * @param location
	 * 		The location in the store, the offset of bytes from the beginning.
	 * @param bytes
	 * 		The array of bytes to read into.
	 * @throws StoreIOException
	 * 		An error occurred in the implementation. See attached exception.
	 * @throws StoreClosedException
	 * 		The store is closed. If auto open is set to true this still may be
	 * 		thrown if there was an error opening the store.
	 * @throws StoreAccessException
	 * 		The store does not have sufficient access to perform the operation.
	 */
	public void get(int location, byte[] bytes) throws StoreIOException, StoreClosedException, StoreAccessException;

	/**
	 * Reads from the store and puts it in a section in the array of bytes.
	 * 
	 * @param location
	 * 		The location in the store, the offset of bytes from the beginning.
	 * @param bytes
	 * 		The array of bytes to read into.
	 * @param offset
	 * 		The offset into the array of bytes.
	 * @param length
	 * 		The number of bytes to read starting at the offset in the array.
	 * @throws StoreIOException
	 * 		An error occurred in the implementation. See attached exception.
	 * @throws StoreClosedException
	 * 		The store is closed. If auto open is set to true this still may be
	 * 		thrown if there was an error opening the store.
	 * @throws StoreAccessException
	 * 		The store does not have sufficient access to perform the operation.
	 */
	public void get(int location, byte[] bytes, int offset, int length) throws StoreIOException, StoreClosedException, StoreAccessException;

	/**
	 * Reads a ByteBuffer to the store at the given location.
	 * 
	 * @param location
	 * 		The location in the store, the offset of bytes from the beginning.
	 * @param buffer
	 * 		The buffer of data to write. The bytes written to the store are the
	 * 		bytes that exist between the buffers position and limit.
	 * @throws StoreIOException
	 * 		An error occurred in the implementation. See attached exception.
	 * @throws StoreClosedException
	 * 		The store is closed. If auto open is set to true this still may be
	 * 		thrown if there was an error opening the store.
	 * @throws StoreAccessException
	 * 		The store does not have sufficient access to perform the operation.
	 */
	public void get(int location, ByteBuffer buffer) throws StoreIOException, StoreClosedException, StoreAccessException;


	/**
	 * Returns an array of bytes read from the store at the given location.
	 * 
	 * @param location
	 * 		The location in the store, the offset of bytes from the beginning.
	 * @param size
	 * 		The number of bytes to read and return.
	 * @return
	 * 		An array of bytes with the given size. 
	 * @throws StoreIOException
	 * 		An error occurred in the implementation. See attached exception.
	 * @throws StoreClosedException
	 * 		The store is closed. If auto open is set to true this still may be
	 * 		thrown if there was an error opening the store.
	 * @throws StoreAccessException
	 * 		The store does not have sufficient access to perform the operation.
	 */
	public byte[] get(int location, int size) throws StoreIOException, StoreClosedException, StoreAccessException;
	
}


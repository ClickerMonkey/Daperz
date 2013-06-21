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

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

import org.magnos.data.Bits;
import org.magnos.data.StoreAccess;


/**
 * A store which has a memory-mapped file as its persisted medium. All reads and
 * writes are not done immediately, only when the disk is idle or when flush or 
 * load are called explicitly. If this store has exclusive or read-write access
 * to the mapped file load does not need to be called before reads.
 *  
 * @author Philip Diffenderfer
 *
 */
public class MappedStore extends AbstractStore 
{

	// The file the store is persisted to.
	private File file;
	
	// The stream used to perform operations on the file.
	private RandomAccessFile stream;
	
	// The channel used to perform operations on the file.
	private FileChannel channel;
	
	// The memory mapped buffer of the files data.
	private MappedByteBuffer buffer;
	
	// The last map mode used by the store.
	private MapMode mapMode;

	
	/**
	 * Instantiates a new MappedStore while opening it with the given access and
	 * setting it the given capacity.
	 * 
	 * @param file
	 * 		The file to persist data to. If this file doesn't exist it will be
	 * 		created when the store is opened, sized, or written to.
	 * @param access
	 * 		The requested access to the store.
	 * @param capacity
	 * 		The requested capacity of the store.
	 */
	public MappedStore(File file, StoreAccess access, int capacity) 
	{
		this(file);
		this.create(access, capacity);
	}
	
	/**
	 * Instantiates a new MappedStore while opening it with the given access and
	 * setting it the given capacity.
	 * 
	 * @param filename
	 * 		The file to persist data to. If this file doesn't exist it will be
	 * 		created when the store is opened, sized, or written to.
	 * @param access
	 * 		The requested access to the store.
	 * @param capacity
	 * 		The requested capacity of the store.
	 */
	public MappedStore(String filename, StoreAccess access, int capacity) 
	{
		this(filename);
		this.create(access, capacity);
	}
	
	/**
	 * Instantiantes a new MappedStore.
	 * 
	 * @param file
	 * 		The file to persist data to. If this file doesn't exist it will be
	 * 		created when the store is opened, sized, or written to.
	 */
	public MappedStore(File file) 
	{
		super(file.getAbsolutePath());
		this.file = file;
	}

	/**
	 * Instantiantes a new MappedStore.
	 * 
	 * @param filename
	 * 		The file to persist data to. If this file doesn't exist it will be
	 * 		created when the store is opened, sized, or written to.
	 */
	public MappedStore(String filename) 
	{
		super(filename);
		this.file = new File(filename);
	}
	
	/**
	 * Returns the channel used to perform operations on the file.
	 * 
	 * @return
	 * 		The stores channel.
	 */
	public FileChannel getChannel() 
	{
		return channel;
	}

	/**
	 * Returns the file the store is persisted to.
	 * 
	 * @return
	 * 		The stores file.
	 */
	public File getFile() 
	{
		return file;
	}

	/**
	 * The stream used to perform operations on the file.
	 * 
	 * @return
	 * 		The stores data stream.
	 */
	public RandomAccessFile getStream() 
	{
		return stream;
	}
	
	/**
	 * Returns the memory mapped buffer of the files data.
	 * 
	 * @return
	 * 		The stores memory mapped buffer.
	 */
	public MappedByteBuffer getBuffer() 
	{
		return buffer;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int storeOpen(StoreAccess access) throws IOException 
	{
		// If the file doesn't exist, create it.
		if (!file.exists()) {
			file.createNewFile();
		}
		file.setReadable(access.canRead, access.canLock);
		file.setWritable(access.canWrite, access.canLock);
		
		// Open with desired access.
		String mode = (access.canWrite ? "rw" : "r");
		stream = new RandomAccessFile(file, mode);
		int capacity = (int)Math.min(stream.length(), Integer.MAX_VALUE);
		channel = stream.getChannel();
		
		// Map file to memory.
		mapMode = (access.canWrite ? MapMode.READ_WRITE : MapMode.READ_ONLY);
		buffer = channel.map(mapMode, 0, capacity);
		
		// If we're supposed to lock it, then do it.
		if (access.canLock) {
			try {
				channel.lock();
			}
			catch (Exception e) {
				storeClose();
				// Cannot acquire lock, access not granted!
				throw new IOException(e);
			}
		}
		return capacity;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void storeLoad() throws IOException 
	{
		buffer.load();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void storeFlush() throws IOException 
	{
		buffer.force();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void storeClose() throws IOException 
	{
		// Write out all meta-data first.
		channel.force(true);
		// Closing stream will also unlock file
		stream.close();
		stream = null;
		
		Bits.free(buffer);
		buffer = null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void storeDelete() 
	{
		if (!file.delete()) {
			file.deleteOnExit();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean storeExists() 
	{
		return file.isFile();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int storeResize(int capacity) throws IOException 
	{
		Bits.free(buffer);
		stream.setLength(capacity);
		buffer = channel.map(mapMode, 0, capacity);
		buffer.load();
		return (int)Math.min(stream.length(), Integer.MAX_VALUE);
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

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

import org.magnos.data.StoreAccess;


/**
 * A store which has a file as its persisted medium. All reads and writes are
 * made to the file immediately.
 * 
 * @author Philip Diffenderfer
 *
 */
public class FileStore extends AbstractStore 
{

	// The file the store is persisted to.
	private File file;
	
	// The stream used to perform operations on the file.
	private RandomAccessFile stream;
	
	/**
	 * Instantiates a new FileStore while opening it with the given access and
	 * setting it to the given capacity.
	 * 
	 * @param file
	 * 		The file to persist data to. If this file doesn't exist it will be
	 * 		created when the store is opened, sized, or written to.
	 * @param access
	 * 		The requested access to the store.
	 * @param capacity
	 * 		The requested capacity of the store.
	 */
	public FileStore(File file, StoreAccess access, int capacity) 
	{
		this(file);
		this.create(access, capacity);
	}
	
	/**
	 * Instantiates a new FileStore while opening it with the given access and
	 * setting it to the given capacity.
	 * 
	 * @param filename
	 * 		The file to persist data to. If this file doesn't exist it will be
	 * 		created when the store is opened, sized, or written to.
	 * @param access
	 * 		The requested access to the store.
	 * @param capacity
	 * 		The requested capacity of the store.
	 */
	public FileStore(String filename, StoreAccess access, int capacity) 
	{
		this(filename);
		this.create(access, capacity);
	}
	
	/**
	 * Instantiantes a new FileStore.
	 * 
	 * @param file
	 * 		The file to persist data to. If this file doesn't exist it will be
	 * 		created when the store is opened, sized, or written to.
	 */
	public FileStore(File file) 
	{
		super(file.getAbsolutePath());
		this.file = file;
	}
	
	/**
	 * Instantiantes a new FileStore.
	 * 
	 * @param filename
	 * 		The file to persist data to. If this file doesn't exist it will be
	 * 		created when the store is opened, sized, or written to.
	 */
	public FileStore(String filename) 
	{
		super(filename);
		this.file = new File(filename);
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
		
		// If we're supposed to lock it, then do it.
		if (access.canLock) {
			try {
				stream.getChannel().lock();
			}
			catch (Exception e) {
				storeClose();
				// Cannot acquire lock, access not granted!
				throw new IOException(e);
			}
		}
		return (int)Math.min(stream.length(), Integer.MAX_VALUE);
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
		// Write out all meta-data first.
		stream.getChannel().force(true);
		// Closing stream will also unlock file
		stream.close();
		stream = null;
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
		stream.setLength(capacity);
		return (int)Math.min(stream.length(), Integer.MAX_VALUE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void storeGet(int location, byte[] bytes, int offset, int length) throws IOException 
	{
		stream.seek(location);
		stream.read(bytes, offset, length);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void storePut(int location, byte[] bytes, int offset, int length) throws IOException 
	{
		stream.seek(location);
		stream.write(bytes, offset, length);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void storeGet(int location, ByteBuffer buffer) throws IOException 
	{
		stream.seek(location);
		stream.getChannel().read(buffer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void storePut(int location, ByteBuffer buffer) throws IOException 
	{
		stream.seek(location);
		stream.getChannel().write(buffer);
	}

}

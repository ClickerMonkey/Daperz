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

import static org.junit.Assert.*;

import org.junit.Test;
import org.magnos.data.StoreAccess;
import org.magnos.data.TestStore;
import org.magnos.data.error.StoreAccessException;
import org.magnos.data.store.FileStore;


public class TestFileStore extends TestStore 
{

	@Test
	public void testDefaults()
	{
		FileStore fs = new FileStore("testDefaults.dat"); 
		testDefaults(fs);
		fs.close();
	}
	
	@Test
	public void testOpen()
	{
		FileStore fs = new FileStore("testOpen.dat");
		testOpen(fs);
		fs.delete();
		
		assertFalse( fs.exists() );
	}
	
	@Test
	public void testCapacity()
	{
		FileStore fs = new FileStore("testCreate.dat", StoreAccess.ReadWrite, 20);
		testCapacity(fs);
		fs.delete();
		
		assertFalse( fs.exists() );
	}
	
	@Test
	public void testCreate()
	{
		FileStore fs = new FileStore("testCreate.dat");
		testCreate(fs);
		fs.delete();
		
		assertFalse( fs.exists() );
	}
	
	@Test
	public void testDelete()
	{
		FileStore fs = new FileStore("testDelete.dat", StoreAccess.ReadWrite, 20);
		testDelete(fs);
	}
	
	@Test
	public void testByteArray()
	{
		FileStore fs = new FileStore("testByteArray.dat", StoreAccess.ReadWrite, 20);
		testByteArray(fs);
		fs.delete();
		
		assertFalse( fs.exists() );
	}
	
	@Test(expected = StoreAccessException.class)
	public void testByteArrayAccess()
	{
		FileStore fs = new FileStore("testByteArrayAccess.dat", StoreAccess.ReadWrite, 20);
		try {
			fs.open(StoreAccess.ReadOnly);
			testByteArray(fs);
		}
		finally {
			fs.open(StoreAccess.ReadWrite);
			fs.delete();
		}
	}
	
	@Test
	public void testByteSection()
	{
		FileStore fs = new FileStore("testByteSection.dat", StoreAccess.ReadWrite, 20);
		testByteSection(fs);
		fs.delete();
		
		assertFalse( fs.exists() );
	}
	
	@Test(expected = StoreAccessException.class)
	public void testByteSectionAccess()
	{
		FileStore fs = new FileStore("testByteSectionAccess.dat", StoreAccess.ReadWrite, 20);
		try {
			fs.open(StoreAccess.ReadOnly);
			testByteSection(fs);	
		}
		finally {
			fs.open(StoreAccess.ReadWrite);
			fs.delete();
		}
	}
	
	@Test
	public void testByteBuffer()
	{
		FileStore fs = new FileStore("testByteBuffer.dat", StoreAccess.ReadWrite, 20);
		testByteBuffer(fs);
		fs.delete();
		
		assertFalse( fs.exists() );
	}
	
	@Test(expected = StoreAccessException.class)
	public void testByteBufferAccess()
	{
		FileStore fs = new FileStore("testByteBufferAccess.dat", StoreAccess.ReadWrite, 20);
		try {
			fs.open(StoreAccess.ReadOnly);
			testByteBuffer(fs);	
		}
		finally {
			fs.open(StoreAccess.ReadWrite);
			fs.delete();
		}
	}
	
	@Test
	public void testOpenAccess()
	{
		FileStore fs1 = new FileStore("testOpenAccess.dat");
		FileStore fs2 = new FileStore("testOpenAccess.dat");
		try {
			testOpenAccess(fs1, fs2);			
		}
		finally {
			fs2.close();
			fs1.open(StoreAccess.ReadWrite);
			fs1.delete();
		}
	}

//	@Test // TODO
	public void testSetAccessClosed()
	{
		FileStore fs1 = new FileStore("testSetAccessClosed.dat");
		FileStore fs2 = new FileStore("testSetAccessClosed.dat");
		try {
			testSetAccessClosed(fs1, fs2);	
		}
		finally {
			fs2.close();
			fs1.open(StoreAccess.ReadWrite);
			fs1.delete();	
			
			assertFalse( fs1.exists() );
		}	
	}
	
	@Test
	public void testSetAccess()
	{
		FileStore fs1 = new FileStore("testSetAccess.dat");
		FileStore fs2 = new FileStore("testSetAccess.dat");
		try {
			testSetAccess(fs1, fs2);	
		}
		finally {
			fs2.close();
			fs1.open(StoreAccess.ReadWrite);
			fs1.delete();
		}
	}
	
	
}

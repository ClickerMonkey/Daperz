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

import org.junit.Test;
import org.magnos.data.StoreAccess;
import org.magnos.data.TestStore;
import org.magnos.data.error.StoreAccessException;
import org.magnos.data.store.MemoryStore;


public class TestMemoryStore extends TestStore 
{

	@Test
	public void testDefaults()
	{
		MemoryStore ms = new MemoryStore("testDefaults.dat"); 
		testDefaults(ms);
		ms.close();
	}
	
	@Test
	public void testOpen()
	{
		MemoryStore ms = new MemoryStore("testOpen.dat");
		testOpen(ms);
		ms.delete();
	}
	
	@Test
	public void testCapacity()
	{
		MemoryStore ms = new MemoryStore("testCreate.dat", StoreAccess.ReadWrite, 20);
		testCapacity(ms);
		ms.delete();
	}
	
	@Test
	public void testCreate()
	{
		MemoryStore ms = new MemoryStore("testCreate.dat");
		testCreate(ms);
		ms.delete();
	}
	
	@Test
	public void testByteArray()
	{
		MemoryStore ms = new MemoryStore("testByteArray.dat", StoreAccess.ReadWrite, 20);
		testByteArray(ms);
		ms.delete();
	}
	
	@Test(expected = StoreAccessException.class)
	public void testByteArrayAccess()
	{
		MemoryStore ms = new MemoryStore("testByteArrayAccess.dat", StoreAccess.ReadOnly, 20);
		try {
			testByteArray(ms);	
		}
		finally {
			ms.open(StoreAccess.ReadWrite);
			ms.delete();
		}
	}
	
	@Test
	public void testByteSection()
	{
		MemoryStore ms = new MemoryStore("testByteSection.dat", StoreAccess.ReadWrite, 20);
		testByteSection(ms);
		ms.delete();
	}
	
	@Test(expected = StoreAccessException.class)
	public void testByteSectionAccess()
	{
		MemoryStore ms = new MemoryStore("testByteSectionAccess.dat", StoreAccess.ReadOnly, 20);
		try {
			testByteSection(ms);	
		}
		finally {
			ms.open(StoreAccess.ReadWrite);
			ms.delete();
		}
	}
	
	@Test
	public void testByteBuffer()
	{
		MemoryStore ms = new MemoryStore("testByteBuffer.dat", StoreAccess.ReadWrite, 20);
		testByteBuffer(ms);
		ms.delete();
	}
	
	@Test(expected = StoreAccessException.class)
	public void testByteBufferAccess()
	{
		MemoryStore ms = new MemoryStore("testByteBufferAccess.dat", StoreAccess.ReadOnly, 20);
		try {
			testByteBuffer(ms);	
		}
		finally {
			ms.open(StoreAccess.ReadWrite);
			ms.delete();
		}
	}
	
}

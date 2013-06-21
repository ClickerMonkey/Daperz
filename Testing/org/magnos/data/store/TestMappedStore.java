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
import org.magnos.data.store.MappedStore;


public class TestMappedStore extends TestStore 
{

	@Test
	public void testDefaults()
	{
		MappedStore ms = new MappedStore("testDefaults.dat"); 
		testDefaults(ms);
		ms.close();
	}

	@Test
	public void testOpen()
	{
		MappedStore ms = new MappedStore("testOpen.dat");
		testOpen(ms);
		ms.delete();
	}

	@Test
	public void testCapacity()
	{
		MappedStore ms = new MappedStore("testCreate.dat", StoreAccess.ReadWrite, 20);
		testCapacity(ms);
		ms.delete();
	}

	@Test
	public void testCreate()
	{
		MappedStore ms = new MappedStore("testCreate.dat");
		testCreate(ms);
		ms.delete();
	}

	@Test
	public void testDelete()
	{
		MappedStore ms = new MappedStore("testDelete.dat", StoreAccess.ReadWrite, 20);
		testDelete(ms);
	}

	@Test
	public void testByteArray()
	{
		MappedStore ms = new MappedStore("testByteArray.dat", StoreAccess.ReadWrite, 20);
		testByteArray(ms);
		ms.delete();
	}

	@Test(expected = StoreAccessException.class)
	public void testByteArrayAccess()
	{
		MappedStore ms = new MappedStore("testByteArrayAccess.dat", StoreAccess.ReadWrite, 20);
		try {
			ms.open(StoreAccess.ReadOnly);
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
		MappedStore ms = new MappedStore("testByteSection.dat", StoreAccess.ReadWrite, 20);
		testByteSection(ms);
		ms.delete();
	}

	@Test(expected = StoreAccessException.class)
	public void testByteSectionAccess()
	{
		MappedStore ms = new MappedStore("testByteSectionAccess.dat", StoreAccess.ReadWrite, 20);
		try {
			ms.open(StoreAccess.ReadOnly);
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
		MappedStore ms = new MappedStore("testByteBuffer.dat", StoreAccess.ReadWrite, 20);
		testByteBuffer(ms);
		ms.delete();
	}

	@Test(expected = StoreAccessException.class)
	public void testByteSectionBuffer()
	{
		MappedStore ms = new MappedStore("testByteBufferAccess.dat", StoreAccess.ReadWrite, 20);
		try {
			ms.open(StoreAccess.ReadOnly);
			testByteBuffer(ms);	
		}
		finally {
			ms.open(StoreAccess.ReadWrite);
			ms.delete();
		}
	}

	@Test
	public void testOpenAccess()
	{
		MappedStore ms1 = new MappedStore("testOpenAccess.dat");
		MappedStore ms2 = new MappedStore("testOpenAccess.dat");
		try {
			testOpenAccess(ms1, ms2);
		}
		finally {
			ms2.close();
			ms1.open(StoreAccess.ReadWrite);
			ms1.delete();
		}
	}

	@Test
	public void testSetAccessClosed()
	{
		MappedStore ms1 = new MappedStore("testSetAccessClosed.dat");
		MappedStore ms2 = new MappedStore("testSetAccessClosed.dat");
		try {
			testSetAccessClosed(ms1, ms2);	
		}
		finally {
			ms2.close();
			ms1.open(StoreAccess.ReadWrite);
			ms1.delete();	
		}	
	}

	@Test
	public void testSetAccess()
	{
		MappedStore ms1 = new MappedStore("testSetAccess.dat");
		MappedStore ms2 = new MappedStore("testSetAccess.dat");
		try {
			testSetAccess(ms1, ms2);	
		}
		finally {
			ms2.close();
			ms1.open(StoreAccess.ReadWrite);
			ms1.delete();
		}
	}

}

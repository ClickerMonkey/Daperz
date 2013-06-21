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

import static org.junit.Assert.*;

import java.nio.ByteBuffer;

import org.magnos.data.Store;
import org.magnos.data.StoreAccess;
import org.magnos.io.Buffers;
import org.magnos.test.BaseTest;


public class TestStore extends BaseTest 
{

	protected void testDefaults(Store s)
	{
		assertTrue( s.isClosed() );
		assertFalse( s.isOpen() );
		assertFalse( s.isAutoFlush() );
		assertFalse( s.isAutoLoad() );
		assertTrue( s.isAutoOpen() );
		assertNotNull( s.getName() );
	}
	
	protected void testOpen(Store s)
	{
		assertFalse( s.isOpen() );
		
		assertNotNull( s.open(StoreAccess.Exclusive) );
		
		assertTrue( s.isOpen() );
	}
	
	protected void testCapacity(Store s) 
	{
		assertEquals( 20, s.capacity() );
		assertEquals( 40, s.capacity(40) );
		assertEquals( 40, s.capacity() );
		assertEquals( 10, s.capacity(10) );
		assertEquals( 10, s.capacity() );
	}
	
	protected void testCreate(Store s) 
	{
		assertEquals( StoreAccess.ReadWrite, s.create(StoreAccess.ReadWrite, 40) );
		assertEquals( 40, s.capacity() );
		
		assertEquals( StoreAccess.Exclusive, s.create(StoreAccess.Exclusive, 30) );
		assertEquals( 30, s.capacity() );
	}
	
	protected void testDelete(Store s)
	{
		assertTrue( s.exists() );
		s.delete();
		assertFalse( s.exists() );
	}
	
	protected void testByteArray(Store s) 
	{
		assertTrue( s.capacity() >= 17 );
		
		byte[] data1 = "Hello World!".getBytes();
		s.put(5, data1);
		
		byte[] data2 = s.get(5, data1.length);
		
		assertArrayEquals( data1, data2 );
	}
	
	protected void testByteSection(Store s) 
	{
		assertTrue( s.capacity() >= 17 );

		byte[] data1 = "Hello World!".getBytes();
		
		s.put(0, data1, 6, 5);
		
		byte[] data2 = s.get(0, 5);
		
		assertArrayEquals( "World".getBytes(), data2 );
	}
	
	protected void testByteBuffer(Store s)
	{
		assertTrue( s.capacity() >= 17 );
		
		ByteBuffer buffer1 = Buffers.toBuffer("Hello World!");
		
		s.put(4, buffer1);
		buffer1.rewind();
		
		ByteBuffer buffer2 = ByteBuffer.allocate(buffer1.capacity());
		
		s.get(4, buffer2);
		buffer2.rewind();
		
		assertTrue( Buffers.equals(buffer1, buffer2) );
	}
	
	protected void testOpenAccess(Store s1, Store s2) 
	{
		s1.capacity(20);
		s1.close();
		
		assertEquals( StoreAccess.Exclusive, s1.open(StoreAccess.Exclusive) );
		assertEquals( StoreAccess.ReadOnly, s2.open(StoreAccess.ReadOnly) );
		assertEquals( StoreAccess.ReadWrite, s2.open(StoreAccess.ReadWrite) );
		assertEquals( StoreAccess.ReadWrite, s2.open(StoreAccess.Exclusive) );
		
		assertEquals( StoreAccess.ReadWrite, s1.open(StoreAccess.ReadWrite) );
		assertEquals( StoreAccess.Exclusive, s2.open(StoreAccess.Exclusive) );
		assertEquals( StoreAccess.ReadWrite, s2.open(StoreAccess.ReadWrite) );
		assertEquals( StoreAccess.ReadOnly, s2.open(StoreAccess.ReadOnly) );

		assertEquals( StoreAccess.ReadWrite, s1.open(StoreAccess.ReadWrite) );
		assertEquals( StoreAccess.ReadOnly, s2.open(StoreAccess.ReadOnly) );
		assertEquals( StoreAccess.ReadWrite, s2.open(StoreAccess.ReadWrite) );
		assertEquals( StoreAccess.Exclusive, s2.open(StoreAccess.Exclusive) );
	}
	
	protected void testSetAccessClosed(Store s1, Store s2)
	{
		assertEquals( StoreAccess.Exclusive, s1.create(StoreAccess.Exclusive, 20) );
		
		// s2 can set exclusive when its closed.
		s2.close();
		assertTrue( s2.isClosed() );
		
		assertEquals( StoreAccess.Exclusive, s2.setAccess(StoreAccess.Exclusive) );
		
		assertTrue( s2.isClosed() );
		assertEquals( StoreAccess.Exclusive, s2.getAccess() );
		
		// but when its implicitly opened it can't have exclusive access.
		s2.get(0, new byte[5]);
		// read-write access will be granted
		assertEquals( StoreAccess.ReadWrite, s2.getAccess() );
	}
	
	protected void testSetAccess(Store s1, Store s2) 
	{
		s1.capacity(20);
		s1.close();
		
		s1.open(StoreAccess.ReadOnly);
		s2.open(StoreAccess.ReadOnly);
		
		assertEquals( StoreAccess.ReadWrite, s1.open(StoreAccess.ReadWrite) );
		assertEquals( StoreAccess.Exclusive, s2.open(StoreAccess.Exclusive) );
		
		s2.close();

		assertEquals( StoreAccess.Exclusive, s1.open(StoreAccess.Exclusive) );
		
		assertEquals( StoreAccess.ReadWrite, s2.open(StoreAccess.Exclusive) );
	}
	
}

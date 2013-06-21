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

import org.junit.Test;
import org.magnos.data.DataSet;
import org.magnos.data.Store;
import org.magnos.data.StoreAccess;
import org.magnos.data.Var;
import org.magnos.data.store.MemoryStore;
import org.magnos.data.var.BooleanVar;
import org.magnos.data.var.ByteVar;
import org.magnos.data.var.FloatVar;
import org.magnos.data.var.IntVar;
import org.magnos.data.var.LongVar;
import org.magnos.data.var.ShortVar;
import org.magnos.test.BaseTest;


public class TestDataSet extends BaseTest 
{
	
	@Test
	public void testConstructorSize() 
	{
		DataSet set = new DataSet(12);
		
		assertNotNull( set );
		assertEquals( 12, set.getSize() );
	}
	
	@Test
	public void testAdd() 
	{
		DataSet set = new DataSet(12);
		assertEquals( 0, set.getSetSize() );
		
		set.add(new IntVar());
		assertEquals( 4, set.getSetSize() );
		
		set.add(new BooleanVar());
		assertEquals( 5, set.getSetSize() );
		
		set.add(new ByteVar());
		assertEquals( 6, set.getSetSize() );

		set.add(new FloatVar());
		assertEquals( 10, set.getSetSize() );

		set.add(new ShortVar());
		assertEquals( 12, set.getSetSize() );
	}
	
	@Test
	public void testConstructorSet() 
	{
		Store store = new MemoryStore("temporary", 36);
		store.open(StoreAccess.ReadWrite);
		
		DataSet set1 = new DataSet(12);
		set1.setLocation(24);
		set1.setStore(store);
		set1.add(new FloatVar(14.9f));
		set1.add(new LongVar(235132432423L));
		set1.write();
		
		DataSet set2 = new DataSet(set1);
		
		assertEquals( set1.getSize(), set2.getSize() );
		assertEquals( set1.getLocation(), set2.getLocation() );
		assertEquals( set1.getStore(), set2.getStore() );
		
		set2.read();
		
		FloatVar v1 = set2.get(0);
		LongVar v2 = set2.get(1);
		
		assertEquals( 14.9f, v1.get(), 0.00001 );
		assertEquals( 235132432423L, v2.get() );
	}
	
	@Test
	public void testCreate() 
	{
		DataSet set = DataSet.create(new FloatVar(23.f), new IntVar(56), new BooleanVar(true));

		assertEquals( FloatVar.SIZE + IntVar.SIZE + BooleanVar.SIZE, set.getSize() );
		
		Var<?> v0 = set.get(0);
		assertTrue( v0 instanceof FloatVar );
		
		Var<?> v1 = set.get(1);
		assertTrue( v1 instanceof IntVar );
		
		Var<?> v2 = set.get(2);
		assertTrue( v2 instanceof BooleanVar );
	}
	
	@Test(expected = RuntimeException.class)
	public void testAddOverflow() 
	{
		DataSet set = new DataSet(3);
		assertEquals( 0, set.getSetSize() );

		set.add(new ShortVar());
		assertEquals( 2, set.getSetSize() );
		
		set.add(new FloatVar());
	}
	
	@Test
	public void testCopy() 
	{
		Store store = new MemoryStore("temporary", 36);
		store.open(StoreAccess.ReadWrite);
		
		DataSet set1 = new DataSet(6);
		set1.setLocation(11);
		set1.setStore(store);
		set1.add(new IntVar());
		set1.add(new BooleanVar());
		set1.add(new ByteVar());
	
		DataSet set2 = (DataSet)set1.copy();
		assertEquals( 11, set2.getLocation() );
		assertEquals( store, set2.getStore() );
		assertTrue( set2.get(0) instanceof IntVar );
		assertTrue( set2.get(1) instanceof BooleanVar );
		assertTrue( set2.get(2) instanceof ByteVar );
	}

}

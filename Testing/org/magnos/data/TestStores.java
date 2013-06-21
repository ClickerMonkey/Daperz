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
import org.magnos.data.Stores;
import org.magnos.data.store.MemoryStore;
import org.magnos.test.BaseTest;


public class TestStores extends BaseTest 
{

	@Test
	public void testGet()
	{
		assertNull( Stores.get("temp0") );
		
		Stores.put(new MemoryStore("temp0", 32));
		
		assertNotNull( Stores.get("temp0") );
	}
	
	@Test
	public void testAlias() 
	{
		MemoryStore store = new MemoryStore("temp1", 32);
		
		assertNull( Stores.get("temp1") );
		assertNull( Stores.put(store, "alias1") );
		assertNull( Stores.get("temp1") );
		assertSame( store, Stores.get("alias1") );
	}
	
	@Test
	public void testRemove() 
	{
		MemoryStore store = new MemoryStore("temp2", 32);
		
		assertNull( Stores.remove("temp2") );
		assertNull( Stores.put(store) );
		assertSame( store, Stores.remove("temp2") );
	}
	
	@Test
	public void testRemoveAlias() 
	{
		MemoryStore store = new MemoryStore("temp3", 32);

		assertNull( Stores.remove("temp3") );
		assertNull( Stores.remove("alias3") );
		assertNull( Stores.put(store, "alias3") );
		assertNull( Stores.remove("temp3") );
		assertSame( store, Stores.remove("alias3") );
	}
	
}

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

package org.magnos.data.var;

import static org.junit.Assert.*;

import org.magnos.data.Store;
import org.magnos.data.StoreAccess;
import org.magnos.data.Var;
import org.magnos.data.store.MemoryStore;
import org.magnos.test.BaseTest;


public class TestVar extends BaseTest 
{

	protected <T> void testAccessors(Var<T> var, T defaultValue, T value) 
	{
		testAccessors(var, defaultValue, value, value);
	}

	protected <T> void testAccessors(Var<T> var, T defaultValue, T value, T expected) 
	{
		assertEquals( defaultValue, var.getValue() );
		var.setValue(value);
		assertEquals( expected, var.getValue() );
	}
	
	protected <T> void testPersist(Var<T> var1, Var<T> var2, T value)
	{
		testPersist(var1, var2, value, value);
	}
	
	protected <T> void testPersist(Var<T> var1, Var<T> var2, T value, T expected) 
	{
		Store store = new MemoryStore("tmp");
		store.create(StoreAccess.ReadWrite, var1.getSize());

		var1.setStore(store);
		var1.setLocation(0);
		var2.setStore(store);
		var2.setLocation(0);
		
		var1.putValue(value);
		
		assertEquals( expected, var2.takeValue() );
	}
	
}

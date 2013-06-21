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
import org.magnos.data.Bits;
import org.magnos.test.BaseTest;

public class TestBits extends BaseTest
{

	public static final byte TRUE = 1;
	public static final byte FALSE = 0;
	
	@Test
	public void testBoolean() 
	{
		assertEquals( true, Bits.getBoolean(TRUE) );
		assertEquals( false, Bits.getBoolean(FALSE) );

		assertEquals( TRUE, Bits.getBooleanBytes(true) );
		assertEquals( FALSE, Bits.getBooleanBytes(false) );

		assertEquals( true, Bits.getBoolean(Bits.getBooleanBytes(true)) );
		assertEquals( false, Bits.getBoolean(Bits.getBooleanBytes(false)) );
	}
	
	
	@Test
	public void testChar() 
	{
		for (char x = Character.MIN_VALUE; x < Character.MAX_VALUE; x++) {
			assertEquals( x, Bits.getChar(Bits.getCharBytes(x)) );
		}
	}
	
	
	@Test
	public void testShort() 
	{
		for (short x = Short.MIN_VALUE; x < Short.MAX_VALUE; x++) {
			assertEquals( x, Bits.getShort(Bits.getShortBytes(x)) );
		}
	}
	
	
	@Test
	public void testInt() 
	{
		for (int x = Short.MIN_VALUE; x < Short.MAX_VALUE; x++) {
			assertEquals( x, Bits.getInt(Bits.getIntBytes(x)) );
		}
		for (int x = Integer.MIN_VALUE; x < Integer.MAX_VALUE - 7919; x += 7919) {
			assertEquals( x, Bits.getInt(Bits.getIntBytes(x)) );
		}
	}
	
	
	@Test
	public void testLong()
	{
		for (int x = Short.MIN_VALUE; x < Short.MAX_VALUE; x++) {
			assertEquals( x, Bits.getLong(Bits.getLongBytes(x)) );
		}
		for (long x = Long.MIN_VALUE; x < Long.MAX_VALUE - 9.82451653e14; x += 9.82451653e14) {
			assertEquals( x, Bits.getLong(Bits.getLongBytes(x)) );
		}
	}
	
}

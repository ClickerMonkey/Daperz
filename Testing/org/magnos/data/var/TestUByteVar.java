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

import org.junit.Test;
import org.magnos.data.var.UByteVar;

public class TestUByteVar extends TestVar 
{

	@Test
	public void testAccessors()
	{
		testAccessors(new UByteVar(), (short)0, (short)23);
		testAccessors(new UByteVar(), (short)0, (short)0);
		testAccessors(new UByteVar(), (short)0, (short)255);
	}
	
	@Test
	public void testPersist()
	{
		testPersist(new UByteVar(), new UByteVar(), (short)0);
		testPersist(new UByteVar(), new UByteVar(), (short)23);
		testPersist(new UByteVar(), new UByteVar(), (short)255);
	}
	
}

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

package org.magnos.data.store.factory;


import org.magnos.data.Store;
import org.magnos.data.StoreFactory;
import org.magnos.data.store.MemoryStore;

/**
 * A factory for creating memory stores.
 * 
 * @author Philip Diffenderfer
 *
 */
public class MemoryStoreFactory implements StoreFactory 
{

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Store create(String name, int capacity) 
	{
		return new MemoryStore(name, capacity);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Store create(String name) 
	{
		return new MemoryStore(name);
	}

}

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

/**
 * A factory for creating stores given their name and their desired capacity.
 * 
 * @author Philip Diffenderfer
 *
 */
public interface StoreFactory 
{
	
	/**
	 * Creates a store given its name and desired capacity.
	 * 
	 * @param name
	 * 		The name of the store. If the store is persistable this may be the
	 * 		file name to the file to which the store is persisted. If the store
	 * 		is memory based then it is merely a reference.
	 * @param capacity
	 * 		The desired capacity of the store in bytes.
	 * @return
	 * 		The reference to a newly instantiated store with the given name.
	 */
	public Store create(String name, int capacity);
	
	/**
	 * Creates a store given its name.
	 * 
	 * @param name
	 * 		The name of the store. If the store is persistable this may be the
	 * 		file name to the file to which the store is persisted. If the store
	 * 		is memory based then it is merely a reference.
	 * @return
	 * 		The reference to a newly instantiated store with the given name.
	 */
	public Store create(String name);
	
}

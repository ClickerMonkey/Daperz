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
 * A fixed size variable that can be read and written to and from a store. The
 * value of the variable can be set or gotten lazily, such that the value 
 * is not read or written immediately, the last value is returned or updated.
 * The value of the variable can also be put or taken which will do a read
 * and write immediately from the store and then return the value.
 * 
 * @author Philip Diffenderfer
 *
 * @param <E>
 * 		The type of variable.
 */
public interface Var<E> extends Data 
{
	
	/**
	 * Returns the value of this var as it was the last time this variable
	 * was read from a store at the location (and maybe with an offset) or
	 * as it was set with the setValue method.
	 * 
	 * @return
	 * 		The last value read.
	 */
	public E getValue();
	
	/**
	 * Sets the value of this var without writing the value to a store.
	 * 
	 * @param newValue
	 * 		The new value to set.
	 */
	public void setValue(E newValue);
	
	/**
	 * Returns the value of this var by reading it from the store and updating
	 * the cached value. If there is a problem reading the value from the store
	 * this will throw a RuntimeException and not update the cached value.
	 * 
	 * @return
	 * 		The value read from the store.
	 */
	public E takeValue();
	
	/**
	 * Sets the value of this var and writes it to the store.
	 * 
	 * @param newValue
	 * 		The value to write to the store.
	 */
	public boolean putValue(E newValue);
	
}

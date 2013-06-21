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

import org.magnos.data.AbstractData;
import org.magnos.data.Var;

/**
 * An abstract implementation of Var.
 * 
 * @author Philip Diffenderfer
 *
 * @param <E>
 */
public abstract class AbstractVar<E> extends AbstractData implements Var<E>
{

	/**
	 * Instantiates a new AbstractVar.
	 * 
	 * @param size
	 * 		The fixed size of the Var in bytes.
	 */
	public AbstractVar(int size) 
	{
		super(size);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public E takeValue() 
	{
		read();
		return getValue();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean putValue(E newValue) 
	{
		setValue(newValue);
		write();
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean equals(Object o) 
	{
		if (o instanceof Var<?>) {
			return getValue().equals(((Var<?>)o).getValue());
		}
		return getValue().equals(o);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int hashCode() 
	{
		E value = getValue();
		if (value != null) {
			return value.hashCode();
		}
		return super.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String toString() 
	{
		E value = getValue();
		if (value != null) {
			return value.toString();
		}
		return super.toString();
	}

}

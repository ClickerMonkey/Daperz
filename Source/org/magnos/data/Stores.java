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

import java.util.concurrent.ConcurrentHashMap;

/**
 * A registry of stores mapped by their (hopefully) unique names.
 * 
 * @author Philip Diffenderfer
 *
 */
public class Stores 
{

	// The hashmap of stores.
	private static ConcurrentHashMap<String, Store> storeMap = 
		new ConcurrentHashMap<String, Store>();
	
	
	/**
	 * Gets the store with the given name from the registry.
	 * 
	 * @param name
	 * 		The name of the store to get from the registry.
	 * @return
	 * 		The store with the given name, or null if none existed.
	 */
	public static Store get(String name) 
	{
		return storeMap.get(name);
	}
	
	/**
	 * Adds the store to this registry. If the registry already contains a store
	 * with the same name it will be overriden and the previous store will be
	 * returned. 
	 * 
	 * @param store
	 * 		The store to add to the registry.
	 * @return
	 * 		The previous store under the same name, or null if none existed.
	 */
	public static Store put(Store store) 
	{
		return storeMap.put(store.getName(), store);
	}
	
	/**
	 * Adds the store to this registry under an alias. If the registry already 
	 * contains a store with the given name it will be overriden and the 
	 * previous tore will be returned. 
	 * 
	 * @param store
	 * 		The store to add to the registry.
	 * @param alias
	 * 		The alias to add the store under, opposed to its own name.
	 * @return
	 * 		The previous store under the same name, or null if none existed.
	 */
	public static Store put(Store store, String alias) 
	{
		return storeMap.put(alias, store);
	}
	
	/**
	 * Removes the store with the given name from this registry.
	 * 
	 * @param name
	 * 		The name of the store to remove.
	 * @return
	 * 		The store removed, or null if none existed.
	 */
	public static Store remove(String name) 
	{
		return storeMap.remove(name);
	}
	
	/**
	 * Removes the given store from this registry.
	 * 
	 * @param store
	 * 		The store to remove.
	 * @return
	 * 		The store removed, or null if none existed.
	 */
	public static Store remove(Store store) 
	{
		return storeMap.remove(store.getName());
	}
	
}

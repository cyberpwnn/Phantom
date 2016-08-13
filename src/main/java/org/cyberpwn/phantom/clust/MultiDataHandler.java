package org.cyberpwn.phantom.clust;

import org.cyberpwn.phantom.lang.GMap;

/**
 * An interface for handing multiple data clustered objects with the same
 * relative type. A Great example would be using this for playerdata
 * 
 * @author cyberpwn
 * @param <T>
 *            the Configurable object
 * @param <V>
 *            the bound object used to get the data
 */
public interface MultiDataHandler<T extends Configurable, V>
{
	/**
	 * Get the cached object from the identifier. If it is not cached, it will
	 * be loaded, then returned.
	 * 
	 * @param identifier
	 *            the identifier
	 * @return the object
	 */
	public T get(V identifier);
	
	/**
	 * Loads the object from the identifier
	 * 
	 * @param identifier
	 *            the identitiable object
	 */
	public void load(V identifier);
	
	/**
	 * Save the object based on the identifier
	 * 
	 * @param identifier
	 *            the itenditiable object
	 */
	public void save(V identifier);
	
	/**
	 * Called to Load the object from the identifier
	 * 
	 * @param identifier
	 *            the identitiable object
	 */
	public T onLoad(V identifier);
	
	/**
	 * Called to Save the object based on the identifier
	 * 
	 * @param identifier
	 *            the itenditiable object
	 */
	public void onSave(V identifier);
	
	/**
	 * Does this handler have a cached object bound to this identifier
	 * 
	 * @param identifier
	 *            the identifier
	 * @return true if it contains the object
	 */
	public boolean contains(V identifier);
	
	/**
	 * Get the cache
	 * 
	 * @return the cached data
	 */
	public GMap<V, T> getCache();
}

package org.phantomapi.clust;

import org.phantomapi.lang.GMap;

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
public interface AsyncMultiDataHandler<T, V>
{
	/**
	 * Checks if the config from the identifier is still loading on another
	 * thread
	 * 
	 * @param identifier
	 *            the identifier
	 * @return Returns true if the thread is still loading the config. Returns
	 *         false if the config is finished loading, or was never loading in
	 *         the first place.
	 */
	public boolean isLoading(V identifier);
	
	/**
	 * Checks if the config is safley loaded async and is ready to be
	 * read/written to.
	 * 
	 * @param identifier
	 *            the identidier
	 * @return Returns true if the config can be safley read to. Returns false
	 *         if the config has never been loaded, or if it is still loading
	 *         async
	 */
	public boolean isLoaded(V identifier);
	
	/**
	 * Directly checks the configurable object if it has been loaded or not
	 * 
	 * @param identifier
	 *            the identifier
	 * @return true if it is loaded. False if it has not yet been loaded or it
	 *         is still loading
	 */
	public boolean checkLoad(V identifier);
	
	/**
	 * Get the cached object from the identifier. If it is not cached, it will
	 * be loaded, then returned.
	 * 
	 * @param identifier
	 *            the identifier
	 * @return the object
	 */
	public T get(V identifier) throws ClustAsyncAlreadyLoadingException;
	
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
	public void save(V identifier) throws ClustAsyncAlreadyLoadingException;
	
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
	
	/**
	 * Saves everything in the cache to the respective onsave functions
	 */
	public void saveAll();
}

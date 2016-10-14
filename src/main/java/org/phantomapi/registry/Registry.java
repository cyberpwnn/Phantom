package org.phantomapi.registry;

/**
 * Register object holders
 * 
 * @author cyberpwn
 * @param <T>
 *            the type of object
 */
public interface Registry<T>
{
	/**
	 * Set the object to a key
	 * 
	 * @param s
	 *            the key
	 * @param t
	 *            the object
	 */
	public void set(String s, T t);
	
	/**
	 * Get the object from the given key.
	 * 
	 * @param s
	 *            the key
	 * @return the object at the given key, or null if it doesnt exist
	 */
	public T get(String s);
	
	/**
	 * Check if the given registry contains an object to the given key
	 * 
	 * @param s
	 *            the given key
	 * @return true if the given key contains an object
	 */
	public boolean contains(String s);
}

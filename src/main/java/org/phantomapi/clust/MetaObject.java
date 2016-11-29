package org.phantomapi.clust;

/**
 * Represents a custom object which can write and read data from and to itself
 * by using one or multiple keys and value types.
 * 
 * @author cyberpwn
 */
public interface MetaObject
{
	/**
	 * Write data to the data cluster with the given base key
	 * 
	 * @param cc
	 *            the data cluster
	 * @param key
	 *            the key
	 */
	public void write(DataCluster cc, String key);
	
	/**
	 * Read data from the data cluster with the given base key
	 * 
	 * @param cc
	 *            the data cluster
	 * @param key
	 *            the key
	 */
	public void read(DataCluster cc, String key);
}

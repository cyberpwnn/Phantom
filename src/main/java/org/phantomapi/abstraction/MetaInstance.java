package org.phantomapi.abstraction;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import org.phantomapi.clust.DataCluster;

/**
 * Represents a meta instance
 * 
 * @author cyberpwn
 */
public interface MetaInstance
{
	/**
	 * Get data
	 * 
	 * @return the data
	 */
	public DataCluster getData();
	
	/**
	 * Get the type
	 * 
	 * @return the type
	 */
	public String getType();
	
	/**
	 * Get the parent instance
	 * 
	 * @return the parent or null
	 */
	public MetaInstance getParent();
	
	/**
	 * Get the root path
	 * 
	 * @return the path
	 */
	public String getRoot();
	
	/**
	 * Get the id
	 * 
	 * @return the id
	 */
	public UUID getId();
	
	/**
	 * Load in data from a file
	 * 
	 * @param file
	 *            the file
	 * @throws IOException
	 *             shit happens
	 */
	public void load(File file) throws IOException;
	
	/**
	 * Save the data to a file
	 * 
	 * @param file
	 *            the file
	 * @throws IOException
	 *             shit happens
	 */
	public void save(File file) throws IOException;
}

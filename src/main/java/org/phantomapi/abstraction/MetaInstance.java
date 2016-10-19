package org.phantomapi.abstraction;

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
}

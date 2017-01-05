package org.phantomapi.ctn;

import org.phantomapi.clust.DataCluster;

/**
 * Represents a subroutine used for identifying a command's functionality
 * 
 * @author cyberpwn
 */
public interface Subroutine
{
	/**
	 * Handle the incoming subroutine data
	 * 
	 * @param data
	 *            the data
	 * @return the response data
	 */
	public DataCluster handleSubroutine(DataCluster data);
	
	/**
	 * Get the subroutine id
	 * 
	 * @return the id
	 */
	public String getSubroutineIdentifier();
	
	/**
	 * Get the subroutine name
	 * 
	 * @return the name
	 */
	public String getSubroutineName();
	
	/**
	 * Register this subroutine
	 */
	public void registerSubroutine();
	
	/**
	 * Unregister this subroutine
	 */
	public void unregisterSubroutine();
	
	/**
	 * Is this subroutine registered?
	 * 
	 * @return true if it is
	 */
	public boolean isSubroutineRegistered();
}

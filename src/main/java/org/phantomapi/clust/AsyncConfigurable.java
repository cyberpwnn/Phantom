package org.phantomapi.clust;

/**
 * Represents an object that is async and configurable
 * 
 * @author cyberpwn
 */
public interface AsyncConfigurable
{
	/**
	 * Checks if the config is still being loaded.
	 * 
	 * @return true if it is loaded, false if it is not, or still is being
	 *         loaded
	 */
	public boolean isLoaded();
}

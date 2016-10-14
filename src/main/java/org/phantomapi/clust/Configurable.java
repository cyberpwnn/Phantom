package org.phantomapi.clust;

/**
 * Make an object configurable
 * 
 * @author cyberpwn
 *
 */
public interface Configurable
{
	/**
	 * Called when a configuration needs to be structured. Place default values
	 * and paths here
	 */
	void onNewConfig();
	
	/**
	 * Called when the config has been read and the DataCluster object has been
	 * modified. Ofcourse, you could just read directly from the DataCluster
	 * object when needed instead of creating bload variables
	 */
	void onReadConfig();
	
	/**
	 * Get the DataCluster object for this object. Needed for writing and
	 * reading
	 * 
	 * @return the DataCluster object
	 */
	DataCluster getConfiguration();
	
	/**
	 * The code name is the file name EXCLUDING .yml. For example if you need
	 * the file name as "config.yml", ensure it is returned as "config".
	 * 
	 * @return the desired code name
	 */
	String getCodeName();
}

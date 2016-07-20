package org.cyberpwn.phantom.construct;

import org.cyberpwn.phantom.clust.Configurable;
import org.cyberpwn.phantom.clust.DataCluster;
import org.cyberpwn.phantom.clust.Keyed;
import org.cyberpwn.phantom.lang.GList;

//We define that this class is a PhantomPlugin, because its a plugin.
//We define that this class is configurable, so it can be configured.
public class Base extends PhantomPlugin implements Configurable
{
	//We create an empty data cluster object
	//This stores all of our configurable settings.
	private DataCluster cc;
	
	//This annotation defines that this field is CONFIGURABLE
	//This annotation binds this field to a key
	//This field MUST BE PUBLIC
	//This field CAN NOT BE STATIC
	//This field MUST BE PRIMITIVE (String or List<T> also)
	//When the config is loaded, this will be used to form the config and defaults
	//This value is also modified when the data is read. No need to set them yourself
	//This feature works with the onNewConfig. You can use them both.
	@Keyed("path.to.double")
	public Double someDouble = 0.32321;
	
	//Another one
	@Keyed("path.to.url")
	public String someUrl = "http://google.com/";
	
	//And Another one
	@Keyed("path.to.some-list")
	public GList<String> dummyList = new GList<String>().qadd("placeholder").qadd("values");
	
	//And Another one
	@Keyed("path.to.boolz")
	public Boolean someBool = false;
	
	//Called when this plugin is enabled
	public void enable()
	{
		//As soon as we load in, we load the cluster (this class)
		//This will call onNewConfig, and grab the defaults
		//If there is a value that already exists, it will be loaded in
		//Defaults that don't exist in the file will be added
		//If the file doesn't exist, it will be created.
		loadCluster(this);
	}
	
	//Called when this plugin is disabled
	public void disable()
	{
		//No need to save the cluster, as it was read in.
		//Unless you need to explicitly modify something, no need.
	}

	//This is called when a new config has begun to load
	//Here, we define the DEFAULT VALUES with keys.
	//This will be used for new configs and new entries.
	public void onNewConfig()
	{
		//This sets path.to.value to an INTEGER 42
		//It would look like this in a config:
		
		/*
		 * path:
		 *   to: 
		 *     value: 42
		 */
		cc.set("path.to.value", 42);
		
		//Same here. all primitive types are compatible
		//You can also set List<T> types as well
		cc.set("path.to.string", "a string value");
	}

	//This is called when we have defined the defaults of the config
	//And now they have been read and injected into the DataCluster object.
	public void onReadConfig()
	{
		//This is an example to get the String value
		cc.getString("path.to.string");
	}

	//Needed for configuration magic
	public DataCluster getConfiguration()
	{
		//We need to return our data cluster
		//This allows the behind the scenes magic to happen for loading and saving
		//Its also a free getter...
		return cc;
	}

	//The Code name represents the file name
	//DONT ADD A FILE EXTENTION. DONT PUT .yml!
	//DONT ADD DIRECTORIES. HANDLED WHEN LOADING
	public String getCodeName()
	{
		return "config";
	}
}

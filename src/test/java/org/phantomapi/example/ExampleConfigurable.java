package org.phantomapi.example;

import org.phantomapi.clust.Configurable;
import org.phantomapi.clust.DataCluster;

public class ExampleConfigurable implements Configurable
{
	//Define the cluster
	private DataCluster cc;
	
	public ExampleConfigurable()
	{
		//Create a new cluster for this instance
		cc = new DataCluster();
	}
	
	@Override
	public void onNewConfig()
	{
		//Here is where we structure DEFAULTS for the config
		cc.set("something.enabled", true, "Optional\nMultiline\nComment");
		cc.set("something.name", "Configurable Name");
	}

	@Override
	public void onReadConfig()
	{
		//Called when the dataCluster has been updated with the remote
	}

	@Override
	public DataCluster getConfiguration()
	{
		//This is needed for any requests to handle this configuration object
		return cc;
	}

	@Override
	public String getCodeName()
	{
		//This is the name of the file,
		//NOT THE LOCATION
		//NOT THE EXTENSION
		return "config";
	}
	
}

package org.cyberpwn.phantom.clust;

import java.io.File;
import java.io.IOException;

public class ConfigurationHandler
{
	public static void read(File base, Configurable c) throws IOException
	{
		File config = new File(base, c.getCodeName() + ".yml");
		
		if(!config.getParentFile().exists())
		{
			config.getParentFile().mkdirs();
		}
		
		if(!config.exists())
		{
			config.createNewFile();
		}
		
		if(config.isDirectory())
		{
			throw new IOException("Cannot read config (it's a folder)");
		}
		
		c.onNewConfig();
		new YAMLDataInput().load(c.getConfiguration(), config);
		new YAMLDataOutput().save(c.getConfiguration(), config);
		c.onReadConfig();
	}
}

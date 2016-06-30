package org.cyberpwn.phantom.clust;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class YAMLDataOutput extends DataOutput
{
	@Override
	public void save(DataCluster cluster, File file) throws IOException
	{
		super.save(cluster, file);
		
		FileConfiguration fc = new YamlConfiguration();
		
		try
		{
			fc.load(file);
		}
		
		catch(InvalidConfigurationException e)
		{
			file.delete();
			file.createNewFile();
		}
		
		for(String i : cluster.getData().keySet())
		{
			fc.set(i, cluster.getAbstract(i));
		}
		
		fc.save(file);
	}
}

package org.cyberpwn.phantom.clust;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * 
 * @author cyberpwn
 *
 */
public class YAMLDataInput extends DataInput
{
	@Override
	public void load(DataCluster cluster, File file) throws IOException
	{
		super.load(cluster, file);
		
		FileConfiguration fc = new YamlConfiguration();
		
		try
		{
			fc.load(file);
		}
		
		catch(InvalidConfigurationException e)
		{
			throw new IOException("Invalid configuration: " + e.getMessage());
		}
		
		for(String i : fc.getKeys(true))
		{
			if(fc.isBoolean(i))
			{
				cluster.set(i, fc.getBoolean(i));
			}
			
			else if(fc.isInt(i))
			{
				cluster.set(i, fc.getInt(i));
			}
			
			else if(fc.isLong(i))
			{
				cluster.set(i, fc.getLong(i));
			}
			
			else if(fc.isDouble(i))
			{
				cluster.set(i, fc.getDouble(i));
			}
			
			else if(fc.isList(i))
			{
				cluster.set(i, fc.getStringList(i));
			}
			
			else if(fc.isString(i))
			{
				cluster.set(i, fc.getString(i));
			}
		}
	}
}

package org.phantomapi.hive;

import java.io.File;
import java.io.IOException;
import org.phantomapi.clust.ConfigurableObject;
import org.phantomapi.clust.ConfigurationHandler;

/**
 * Represents a hyve
 * 
 * @author cyberpwn
 */
public class BaseHyve extends ConfigurableObject implements Hyve
{
	protected HyveType type;
	protected File path;
	
	/**
	 * Create a base hyve
	 * 
	 * @param type
	 *            the hyve type
	 * @param id
	 *            the id
	 * @param path
	 *            the path
	 */
	public BaseHyve(HyveType type, String id, File path)
	{
		super(id);
		
		this.type = type;
		this.path = path;
	}
	
	@Override
	public HyveType getType()
	{
		return type;
	}
	
	@Override
	public void save()
	{
		try
		{
			ConfigurationHandler.fastWrite(path, this);
		}
		
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void load()
	{
		try
		{
			ConfigurationHandler.fastRead(path, this);
		}
		
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void drop()
	{
		new File(path, getCodeName()).delete();
	}
}

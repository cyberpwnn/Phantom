package org.phantomapi.hive;

import java.io.File;
import java.io.IOException;
import org.phantomapi.clust.ConfigurableObject;
import org.phantomapi.clust.ConfigurationHandler;

public class BaseHyve extends ConfigurableObject implements Hyve
{
	protected HyveType type;
	protected File path;
	
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
			ConfigurationHandler.save(path, this);
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
			ConfigurationHandler.read(path, this);
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

package org.phantomapi.abstraction;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.clust.YAMLDataInput;
import org.phantomapi.clust.YAMLDataOutput;
import org.phantomapi.lang.GList;

/**
 * Represents an instance
 * 
 * @author cyberpwn
 */
public class Instance implements MetaInstance
{
	protected final DataCluster data;
	protected final String type;
	protected final MetaInstance parent;
	protected final UUID id;
	
	/**
	 * Create an instance
	 * 
	 * @param type
	 *            the type
	 * @param parent
	 *            the parent instance or null
	 */
	public Instance(String type, MetaInstance parent)
	{
		this.type = type;
		this.parent = parent;
		id = UUID.randomUUID();
		
		if(parent != null)
		{
			data = parent.getData();
			data.set(parent.getRoot() + "." + parent.getType() + "-type", type);
		}
		
		else
		{
			data = new DataCluster();
		}
		
		data.set(getRoot() + ".hash", id.toString());
	}
	
	@Override
	public DataCluster getData()
	{
		return data;
	}
	
	@Override
	public String getType()
	{
		return type;
	}
	
	@Override
	public MetaInstance getParent()
	{
		return parent;
	}
	
	@Override
	public UUID getId()
	{
		return id;
	}
	
	@Override
	public String getRoot()
	{
		MetaInstance m = this;
		GList<String> ma = new GList<String>();
		
		while(m != null)
		{
			ma.add(m.getType());
			m = m.getParent();
		}
		
		return ma.reverse().toString(".");
	}
	
	@Override
	public void load(File file) throws IOException
	{
		new YAMLDataInput().load(getData(), file);
	}
	
	@Override
	public void save(File file) throws IOException
	{
		new YAMLDataOutput().save(getData(), file);
	}
}

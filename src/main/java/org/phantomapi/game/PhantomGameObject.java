package org.phantomapi.game;

import java.io.IOException;
import java.util.UUID;
import org.phantomapi.clust.ConfigurableObject;
import org.phantomapi.clust.ConfigurationHandler;
import org.phantomapi.clust.DataCluster;

/**
 * Game objects for storing data
 * 
 * @author cyberpwn
 */
public class PhantomGameObject extends ConfigurableObject implements GameObject
{
	protected final String type;
	
	/**
	 * Create a game object
	 * 
	 * @param type
	 *            the object type
	 */
	public PhantomGameObject(String type)
	{
		super(type);
		
		this.type = type;
		getConfiguration().set("gameobject-id", UUID.randomUUID().toString());
	}
	
	@Override
	public String getCodeName()
	{
		return type + "-" + getId();
	}
	
	public String getType()
	{
		return type;
	}
	
	public String getId()
	{
		return getConfiguration().getString("gameobject-id");
	}
	
	@Override
	public byte[] toData() throws IOException
	{
		ConfigurationHandler.fromFields(this);
		
		return getConfiguration().compress();
	}
	
	@Override
	public void fromData(byte[] data) throws IOException
	{
		getConfiguration().setData(new DataCluster(data).getData());
		ConfigurationHandler.toFields(this);
	}
}

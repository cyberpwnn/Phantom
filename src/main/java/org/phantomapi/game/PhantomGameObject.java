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
	protected final Game game;
	
	/**
	 * Create a game object
	 * 
	 * @param type
	 *            the object type
	 */
	public PhantomGameObject(Game game, String type)
	{
		super(type);
		
		this.game = game;
		this.type = type;
		getConfiguration().set("gameobject-id", UUID.randomUUID().toString());
		game.getState().registerGameObject(getId(), this);
	}
	
	@Override
	public String getCodeName()
	{
		return type + "-" + getId();
	}
	
	@Override
	public String getType()
	{
		return type;
	}
	
	@Override
	public Game getGame()
	{
		return game;
	}
	
	@Override
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

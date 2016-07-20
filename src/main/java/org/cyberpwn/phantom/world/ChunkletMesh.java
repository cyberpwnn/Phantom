package org.cyberpwn.phantom.world;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.cyberpwn.phantom.lang.GList;
import org.cyberpwn.phantom.lang.GMap;

/**
 * A bunch of chunklets for fast reads
 * 
 * @author cyberpwn
 *
 */
public class ChunkletMesh
{
	private World world;
	private GMap<Integer, GList<Chunklet>> chunklets;
	private GList<Chunklet> all;
	
	public ChunkletMesh(World world)
	{
		this.world = world;
		this.chunklets = new GMap<Integer, GList<Chunklet>>();
		this.all = new GList<Chunklet>();
	}
	
	public void rebuildReferences()
	{
		for(Integer i : chunklets.k())
		{
			all.add(chunklets.get(i));
		}
	}
	
	public GList<Chunklet> getChunklets()
	{
		return all;
	}
	
	public boolean contains(Location l)
	{
		for(Chunklet i : all)
		{
			if(i.contains(l))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public boolean contains(Player p)
	{
		return contains(p.getLocation());
	}
	
	public boolean contains(Entity e)
	{
		return contains(e.getLocation());
	}
	
	public GList<Player> getPlayers()
	{
		GList<Player> players = new GList<Player>();
		
		for(Chunklet i : all)
		{
			players.add(i.getPlayers());
		}
		
		return players;
	}
	
	public GList<Entity> getEntities()
	{
		GList<Entity> entities = new GList<Entity>();
		
		for(Chunklet i : all)
		{
			entities.add(i.getEntities());
		}
		
		return entities;
	}
	
	public void add(Chunklet c)
	{
		if(c.getWorld().equals(world))
		{
			if(!chunklets.containsKey(c.getX()))
			{
				chunklets.put(c.getX(), new GList<Chunklet>());
			}
			
			if(!chunklets.get(c.getX()).contains(c))
			{
				chunklets.get(c.getX()).add(c);
				rebuildReferences();
			}
		}
	}
}

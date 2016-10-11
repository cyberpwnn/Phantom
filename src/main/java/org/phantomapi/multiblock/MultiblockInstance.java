package org.phantomapi.multiblock;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.phantomapi.lang.GList;
import org.phantomapi.lang.GLocation;
import org.phantomapi.lang.GMap;
import org.phantomapi.lang.GSet;
import org.phantomapi.lang.GVector;
import org.phantomapi.util.Chunks;

/**
 * Multiblock instance
 * 
 * @author cyberpwn
 */
public class MultiblockInstance implements Multiblock
{
	private static final long serialVersionUID = 1L;
	
	private GMap<GVector, GLocation> mapping;
	private int id;
	private String type;
	
	/**
	 * Create a multiblock instance
	 * 
	 * @param id
	 *            the id
	 * @param type
	 *            the type
	 * @param mapping
	 *            the mapping
	 */
	public MultiblockInstance(int id, String type, GMap<Vector, Location> mapping)
	{
		this.id = id;
		this.type = type;
		this.mapping = new GMap<GVector, GLocation>();
		
		for(Vector i : mapping.k())
		{
			this.mapping.put(new GVector(i), new GLocation(mapping.get(i)));
		}
	}
	
	@Override
	public GList<Chunk> getChunks()
	{
		GSet<Chunk> set = new GSet<Chunk>();
		
		for(GVector i : mapping.k())
		{
			set.add(mapping.get(i).toLocation().getChunk());
		}
		
		return new GList<Chunk>(set);
	}
	
	@Override
	public GMap<Vector, Location> getMapping()
	{
		GMap<Vector, Location> map = new GMap<Vector, Location>();
		
		for(GVector i : mapping.k())
		{
			map.put(i.toVector(), mapping.get(i).toLocation());
		}
		
		return map;
	}
	
	@Override
	public GList<Location> getLocations()
	{
		return getMapping().v();
	}
	
	@Override
	public String getType()
	{
		return type;
	}
	
	@Override
	public boolean contains(Location location)
	{
		return getLocations().contains(location);
	}
	
	@Override
	public int size()
	{
		return mapping.size();
	}
	
	@Override
	public int getId()
	{
		return id;
	}
	
	@Override
	public void unload()
	{
		Chunks.unload(getChunks());
	}
	
	@Override
	public void load()
	{
		Chunks.load(getChunks());
	}
	
	@Override
	public World getWorld()
	{
		return getChunks().get(0).getWorld();
	}
	
	@Override
	public boolean update()
	{
		MultiblockStructure s = MB.getStructure(this);
		
		if(s.match(getMapping().get(s.getSchematic().k().pickRandom())) == null)
		{
			return false;
		}
		
		return true;
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + (mapping == null ? 0 : mapping.hashCode());
		result = prime * result + (type == null ? 0 : type.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
		{
			return true;
		}
		if(obj == null)
		{
			return false;
		}
		if(getClass() != obj.getClass())
		{
			return false;
		}
		MultiblockInstance other = (MultiblockInstance) obj;
		if(id != other.id)
		{
			return false;
		}
		if(mapping == null)
		{
			if(other.mapping != null)
			{
				return false;
			}
		}
		else if(!mapping.equals(other.mapping))
		{
			return false;
		}
		if(type == null)
		{
			if(other.type != null)
			{
				return false;
			}
		}
		else if(!type.equals(other.type))
		{
			return false;
		}
		return true;
	}
}

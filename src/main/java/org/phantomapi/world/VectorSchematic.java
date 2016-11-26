package org.phantomapi.world;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.lang.GList;
import org.phantomapi.lang.GMap;

/**
 * Vector schematics
 * 
 * @author cyberpwn
 */
public class VectorSchematic
{
	private final GMap<Vector, VariableBlock> schematic;
	
	/**
	 * Create a vector schematic
	 */
	public VectorSchematic()
	{
		schematic = new GMap<Vector, VariableBlock>();
	}
	
	/**
	 * Does the vector schematic's variable blocks contain the given material
	 * block?
	 * 
	 * @param mb
	 *            the material block
	 * @return true if it does
	 */
	public boolean contains(MaterialBlock mb)
	{
		for(Vector i : schematic.k())
		{
			if(schematic.get(i).is(mb))
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Does this vector schematic contain multiple of the given material blocks
	 * 
	 * @param mb
	 *            the materialblock
	 * @return true if it does
	 */
	public boolean containsMultiple(MaterialBlock mb)
	{
		return find(mb).size() > 1;
	}
	
	/**
	 * Find all vector references that match the given material block
	 * 
	 * @param mb
	 *            the materialblock
	 * @return the vector matches
	 */
	public GList<Vector> find(MaterialBlock mb)
	{
		GList<Vector> vectors = new GList<Vector>();
		
		for(Vector i : schematic.k())
		{
			if(schematic.get(i).is(mb))
			{
				vectors.add(i);
			}
		}
		
		return vectors;
	}
	
	/**
	 * Match the location as part of a multiblock structure
	 * 
	 * @param location
	 *            the location
	 * @return the mapping or null if no match
	 */
	public GMap<Vector, Location> match(Location location)
	{
		MaterialBlock mb = new MaterialBlock(location);
		
		for(Vector i : find(mb))
		{
			Vector shift = i;
			Location base = location.clone().subtract(shift);
			GMap<Vector, Location> map = new GMap<Vector, Location>();
			Boolean found = true;
			
			for(Vector j : schematic.k())
			{
				Location attempt = base.clone().add(j);
				MaterialBlock mbx = new MaterialBlock(attempt);
				
				if(schematic.get(j).is(mbx))
				{
					map.put(j, attempt);
				}
				
				else
				{
					found = false;
					break;
				}
			}
			
			if(found)
			{
				return map;
			}
		}
		
		return null;
	}
	
	/**
	 * Get the schematic
	 * 
	 * @return the schematic
	 */
	public GMap<Vector, VariableBlock> getSchematic()
	{
		return schematic;
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((schematic == null) ? 0 : schematic.hashCode());
		return result;
	}
	
	public DataCluster toConfiguration()
	{
		DataCluster cc = new DataCluster();
		
		for(Vector i : schematic.k())
		{
			cc.set(i.getBlockX() + "-" + i.getBlockY() + "-" + i.getBlockZ(), schematic.get(i).toString());
		}
		
		return cc;
	}
	
	public void fromConfiguration(DataCluster cc)
	{
		schematic.clear();
		
		for(String i : cc.getRoots())
		{
			Vector v = new Vector(Integer.valueOf(i.split("-")[0]), Integer.valueOf(i.split("-")[1]), Integer.valueOf(i.split("-")[2]));
			VariableBlock vb = new VariableBlock(cc.getString(i));
			schematic.put(v, vb);
		}
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
		VectorSchematic other = (VectorSchematic) obj;
		if(schematic == null)
		{
			if(other.schematic != null)
			{
				return false;
			}
		}
		else if(!schematic.equals(other.schematic))
		{
			return false;
		}
		return true;
	}
}

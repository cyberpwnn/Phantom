package org.cyberpwn.phantom.world;

import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;
import org.cyberpwn.phantom.lang.GList;

/**
 * Schematics
 * 
 * @author cyberpwn
 *
 */
public class Schematic
{
	private Dimension dimension;
	private MaterialBlock[][][] schematic;
	
	/**
	 * Make a schematic out of a dimension
	 * 
	 * @param dimension
	 *            the dimension
	 */
	public Schematic(Dimension dimension)
	{
		this.dimension = dimension;
		this.schematic = new MaterialBlock[dimension.getWidth()][dimension.getHeight()][dimension.getDepth()];
		
		for(int i = 0; i < dimension.getWidth(); i++)
		{
			for(int j = 0; j < dimension.getHeight(); j++)
			{
				for(int k = 0; k < dimension.getDepth(); k++)
				{
					set(i, j, k, Material.AIR, (byte) 0);
				}
			}
		}
	}
	
	/**
	 * Get a list of all material blocks within this schematic
	 * 
	 * @return the list
	 */
	public GList<MaterialBlock> toList()
	{
		GList<MaterialBlock> md = new GList<MaterialBlock>();
		
		for(int i = 0; i < dimension.getWidth(); i++)
		{
			for(int j = 0; j < dimension.getHeight(); j++)
			{
				for(int k = 0; k < dimension.getDepth(); k++)
				{
					md.add(schematic[i][j][k]);
				}
			}
		}
		
		return md;
	}
	
	/**
	 * Iterate through the blocks
	 * 
	 * @return
	 */
	public Iterator<MaterialBlock> iterator()
	{
		return toList().iterator();
	}
	
	/**
	 * Apply data from this schematic to the world at a given location.
	 * 
	 * @param location
	 *            the location
	 */
	@SuppressWarnings("deprecation")
	public void apply(Location location)
	{
		for(int i = 0; i < dimension.getWidth(); i++)
		{
			for(int j = 0; j < dimension.getHeight(); j++)
			{
				for(int k = 0; k < dimension.getDepth(); k++)
				{
					if(!schematic[i][j][k].getMaterial().equals(location.clone().add(i, j, j).getBlock().getType()) || schematic[i][j][k].getData() != location.clone().add(i, j, j).getBlock().getData())
					{
						schematic[i][j][k].apply(location.clone().add(i, j, j));
					}
				}
			}
		}
	}
	
	/**
	 * Read data into the schematic from the given location.
	 * 
	 * @param location
	 *            the location
	 */
	public void read(Location location)
	{
		for(int i = 0; i < dimension.getWidth(); i++)
		{
			for(int j = 0; j < dimension.getHeight(); j++)
			{
				for(int k = 0; k < dimension.getDepth(); k++)
				{
					MaterialBlock mb = new MaterialBlock(location.clone().add(new Vector(i, j, k)));
					schematic[i][j][k] = mb;
				}
			}
		}
	}
	
	/**
	 * Clear the schematic
	 * 
	 * @param mb
	 *            give a type of block to clear it with
	 */
	public void clear(MaterialBlock mb)
	{
		for(int i = 0; i < dimension.getWidth(); i++)
		{
			for(int j = 0; j < dimension.getHeight(); j++)
			{
				for(int k = 0; k < dimension.getDepth(); k++)
				{
					schematic[i][j][k] = mb;
				}
			}
		}
	}
	
	/**
	 * Set data to the schematic. Make sure the xyz coords are within 0,0,0, and
	 * your dimension supplied width,height,depth. These arent actual locations
	 * in a world.
	 * 
	 * @param x
	 *            the x (RELATIVE)
	 * @param y
	 *            the y (RELATIVE)
	 * @param z
	 *            the z (RELATIVE)
	 * @param material
	 *            the type
	 * @param data
	 *            the metadata (0)
	 */
	public void set(int x, int y, int z, Material material, Byte data)
	{
		schematic[x][y][z] = new MaterialBlock();
		schematic[x][y][z].setMaterial(material);
		schematic[x][y][z].setData(data);
	}
	
	public Dimension getDimension()
	{
		return dimension;
	}
	
	public MaterialBlock[][][] getSchematic()
	{
		return schematic;
	}
}

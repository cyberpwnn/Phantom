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
	
	public Iterator<MaterialBlock> iterator()
	{
		return toList().iterator();
	}
	
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

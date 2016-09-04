package org.phantomapi.world;

import java.util.Iterator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;
import org.phantomapi.lang.GList;
import org.phantomapi.util.A;

/**
 * Schematics
 * 
 * @author cyberpwn
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
	 * Set all faces to a block
	 * 
	 * @param mb
	 *            the type
	 */
	public void setFaces(MaterialBlock mb)
	{
		for(Direction i : Direction.values())
		{
			setFace(mb, i);
		}
	}
	
	/**
	 * Set the outer face of the block
	 * 
	 * @param mb
	 *            the block type
	 * @param d
	 *            the face
	 */
	public void setFace(MaterialBlock mb, Direction d)
	{
		if(d.equals(Direction.U))
		{
			for(int i = 0; i < mx(); i++)
			{
				for(int j = 0; j < mz(); j++)
				{
					set(i, my(), j, mb.getMaterial(), mb.getData());
				}
			}
		}
		
		else if(d.equals(Direction.D))
		{
			for(int i = 0; i < mx(); i++)
			{
				for(int j = 0; j < mz(); j++)
				{
					set(i, 0, j, mb.getMaterial(), mb.getData());
				}
			}
		}
		
		else if(d.equals(Direction.N))
		{
			for(int i = 0; i < mx(); i++)
			{
				for(int j = 0; j < my(); j++)
				{
					set(i, j, 0, mb.getMaterial(), mb.getData());
				}
			}
		}
		
		else if(d.equals(Direction.S))
		{
			for(int i = 0; i < mx(); i++)
			{
				for(int j = 0; j < my(); j++)
				{
					set(i, j, mz(), mb.getMaterial(), mb.getData());
				}
			}
		}
		
		else if(d.equals(Direction.E))
		{
			for(int i = 0; i < mz(); i++)
			{
				for(int j = 0; j < my(); j++)
				{
					set(mx(), j, i, mb.getMaterial(), mb.getData());
				}
			}
		}
		
		else if(d.equals(Direction.W))
		{
			for(int i = 0; i < mz(); i++)
			{
				for(int j = 0; j < my(); j++)
				{
					set(0, j, i, mb.getMaterial(), mb.getData());
				}
			}
		}
	}
	
	/**
	 * Distort a schematic with a distortion
	 * 
	 * @param d
	 *            the distortion instance
	 */
	public void distort(Distortion d)
	{
		d.onDistort(this);
	}
	
	/**
	 * Get the max x
	 * 
	 * @return x
	 */
	public int mx()
	{
		return dimension.getWidth() - 1;
	}
	
	/**
	 * Get the max y
	 * 
	 * @return the y
	 */
	public int my()
	{
		return dimension.getHeight() - 1;
	}
	
	/**
	 * Get the max z
	 * 
	 * @return z
	 */
	public int mz()
	{
		return dimension.getDepth() - 1;
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
	 * Fill the schematic with material blocks
	 * 
	 * @param mb
	 *            the data
	 */
	public void fill(MaterialBlock mb)
	{
		clear(mb);
	}
	
	/**
	 * Strip down the y axis
	 * 
	 * @param mb
	 *            the type
	 * @param x
	 *            the x
	 * @param z
	 *            the z
	 */
	public void setStripY(MaterialBlock mb, int x, int z)
	{
		if(x < dimension.getWidth() && z < dimension.getDepth() && x >= 0 && z >= 0)
		{
			for(int i = 0; i < dimension.getHeight(); i++)
			{
				set(x, i, z, mb.getMaterial(), mb.getData());
			}
		}
	}
	
	/**
	 * Strip down the x axis
	 * 
	 * @param mb
	 *            the type
	 * @param y
	 *            the y
	 * @param z
	 *            the z
	 */
	public void setStripX(MaterialBlock mb, int z, int y)
	{
		if(y < dimension.getHeight() && z < dimension.getDepth() && y >= 0 && z >= 0)
		{
			for(int i = 0; i < dimension.getWidth(); i++)
			{
				set(i, y, z, mb.getMaterial(), mb.getData());
			}
		}
	}
	
	/**
	 * Strip down the z axis
	 * 
	 * @param mb
	 *            the type
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public void setStripZ(MaterialBlock mb, int x, int y)
	{
		if(x < dimension.getWidth() && y < dimension.getHeight() && x >= 0 && y >= 0)
		{
			for(int i = 0; i < dimension.getDepth(); i++)
			{
				set(x, y, i, mb.getMaterial(), mb.getData());
			}
		}
	}
	
	/**
	 * Replace all blocks from a type to another
	 * 
	 * @param from
	 *            this
	 * @param to
	 *            that
	 */
	public void replace(MaterialBlock from, MaterialBlock to)
	{
		for(int i = 0; i < dimension.getWidth(); i++)
		{
			for(int j = 0; j < dimension.getHeight(); j++)
			{
				for(int k = 0; k < dimension.getDepth(); k++)
				{
					if(schematic[i][j][k] != null && schematic[i][j][k].equals(from))
					{
						schematic[i][j][k] = to;
					}
				}
			}
		}
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
		new A()
		{
			@Override
			public void async()
			{
				for(int i = 0; i < dimension.getWidth(); i++)
				{
					for(int j = 0; j < dimension.getHeight(); j++)
					{
						for(int k = 0; k < dimension.getDepth(); k++)
						{
							if(!schematic[i][j][k].getMaterial().equals(location.clone().add(i, j, k).getBlock().getType()) || schematic[i][j][k].getData() != location.clone().add(i, j, k).getBlock().getData())
							{
								schematic[i][j][k].apply(location.clone().add(i, j, k));
							}
						}
					}
				}
			}
		};
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
	 * set the schematic
	 * 
	 * @param mbx
	 *            the iterator
	 */
	public void set(SchematicIterator mbx)
	{
		for(int i = 0; i < dimension.getWidth(); i++)
		{
			for(int j = 0; j < dimension.getHeight(); j++)
			{
				for(int k = 0; k < dimension.getDepth(); k++)
				{
					mbx.run(schematic[i][j][k], i, j, k);
					schematic[i][j][k] = mbx.getB();
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

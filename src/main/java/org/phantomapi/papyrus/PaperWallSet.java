package org.phantomapi.papyrus;

import java.util.Iterator;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.map.MapView;
import org.phantomapi.lang.GMap;
import org.phantomapi.world.Cuboid;
import org.phantomapi.world.CuboidException;
import org.phantomapi.world.DimensionFace;

/**
 * A Paper wall set for a wall of maps
 * 
 * @author cyberpwn
 */
public class PaperWallSet
{
	private Cuboid c;
	
	/**
	 * Create a paper wall set from a cuboid
	 * 
	 * @param c
	 *            the cuboid (W.getSelection(Player p)
	 * @throws CuboidException
	 *             if the cuboid is not a pane, either X or Z has to be one
	 *             block thick
	 */
	public PaperWallSet(Cuboid c) throws CuboidException
	{
		this.c = c;
		
		if(c.getDimension().getPane() == null)
		{
			throw new CuboidException("Invalid dimension for cuboid. Must be 'thin'");
		}
		
		if(c.getDimension().getPane().equals(DimensionFace.Y))
		{
			throw new CuboidException("Invalid dimension for cuboid. Must hold a wall of maps.");
		}
		
		Iterator<Block> it = c.iterator();
		
		while(it.hasNext())
		{
			if(!it.next().getType().equals(Material.MAP))
			{
				throw new CuboidException("Invalid wall. Must be all maps.");
			}
		}
	}
	
	/**
	 * Get the blocks in the map view format
	 * 
	 * @return the blocks[][]
	 */
	public Block[][] getBlocks()
	{
		Block[][] b = new Block[(c.getDimension().getWidth() < 2 ? c.getDimension().getDepth() : c.getDimension().getWidth())][c.getDimension().getHeight()];
		
		for(int i = 0; i < (c.getDimension().getWidth() < 2 ? c.getDimension().getDepth() : c.getDimension().getWidth()); i++)
		{
			for(int j = 0; j < c.getDimension().getHeight(); j++)
			{
				Block block = null;
				
				if(c.getDimension().getPane().equals(DimensionFace.X))
				{
					block = c.getRelativeBlock(0, j, i);
				}
				
				else
				{
					block = c.getRelativeBlock(i, j, 0);
				}
				
				b[i][j] = block;
			}
		}
		
		return b;
	}
	
	/**
	 * Get the width of the map
	 * 
	 * @return the width of the map
	 */
	public int getWidth()
	{
		return c.getDimension().getWidth() < 2 ? c.getDimension().getDepth() : c.getDimension().getWidth();
	}
	
	/**
	 * Get the height of the map
	 * 
	 * @return the height
	 */
	public int getHeight()
	{
		return c.getDimension().getHeight();
	}
	
	/**
	 * Get the blocks and map views with them
	 * 
	 * @return the blocks and map views
	 */
	public GMap<Block, MapView> getBlockMaps()
	{
		GMap<Block, MapView> maps = new GMap<Block, MapView>();
		
		for(int i = 0; i < (c.getDimension().getWidth() < 2 ? c.getDimension().getDepth() : c.getDimension().getWidth()); i++)
		{
			for(int j = 0; j < c.getDimension().getHeight(); j++)
			{
				Block block = null;
				
				if(c.getDimension().getPane().equals(DimensionFace.X))
				{
					block = c.getRelativeBlock(0, j, i);
				}
				
				else
				{
					block = c.getRelativeBlock(i, j, 0);
				}
				
				MapView mv = ((MapView) block.getState());
				maps.put(block, mv);
			}
		}
		
		return maps;
	}
}

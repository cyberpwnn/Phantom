package org.phantomapi.world;

import java.util.Iterator;
import org.bukkit.Chunk;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.phantomapi.builder.Brush;
import org.phantomapi.world.Cuboid.CuboidDirection;

/**
 * The World queue with enhancements
 * 
 * @author cyberpwn
 */
public class WQ extends PhantomWorldQueue
{
	/**
	 * Color a block to the given location
	 * 
	 * @param location
	 *            the location
	 * @param color
	 *            the color
	 */
	@SuppressWarnings("deprecation")
	public void color(Location location, DyeColor color)
	{
		if(location.getBlock().getType().equals(Material.BANNER) || location.getBlock().getType().equals(Material.WALL_BANNER) || location.getBlock().getType().equals(Material.STANDING_BANNER))
		{
			Banner banner = (Banner) location.getBlock().getState();
			banner.setBaseColor(color);
			banner.update();
			
			return;
		}
		
		new MaterialBlock(location.getBlock().getType(), color.getDyeData()).apply(location.getBlock().getLocation());
	}
	
	/**
	 * Set an entire chunk to a given biome
	 * 
	 * @param chunk
	 *            the chunk
	 * @param biome
	 *            the biome
	 */
	public void setBiome(Chunk chunk, Biome biome)
	{
		for(int i = 0; i < 16; i++)
		{
			for(int j = 0; j < 16; j++)
			{
				setBiome(chunk.getBlock(i, 0, j).getLocation(), biome);
			}
		}
	}
	
	public void clearTile(Location l)
	{
		set(l, new MaterialBlock(l));
	}
	
	public void outline(Cuboid c, Brush b)
	{
		set(c.getFace(CuboidDirection.Up), b);
		set(c.getFace(CuboidDirection.Down), b);
		set(c.getFace(CuboidDirection.North), b);
		set(c.getFace(CuboidDirection.South), b);
		set(c.getFace(CuboidDirection.East), b);
		set(c.getFace(CuboidDirection.West), b);
	}
	
	public void outline(Cuboid c, MaterialBlock b)
	{
		set(c.getFace(CuboidDirection.Up), b);
		set(c.getFace(CuboidDirection.Down), b);
		set(c.getFace(CuboidDirection.North), b);
		set(c.getFace(CuboidDirection.South), b);
		set(c.getFace(CuboidDirection.East), b);
		set(c.getFace(CuboidDirection.West), b);
	}
	
	public void walls(Cuboid c, Brush b)
	{
		set(c.getFace(CuboidDirection.North), b);
		set(c.getFace(CuboidDirection.South), b);
		set(c.getFace(CuboidDirection.East), b);
		set(c.getFace(CuboidDirection.West), b);
	}
	
	public void walls(Cuboid c, MaterialBlock b)
	{
		set(c.getFace(CuboidDirection.North), b);
		set(c.getFace(CuboidDirection.South), b);
		set(c.getFace(CuboidDirection.East), b);
		set(c.getFace(CuboidDirection.West), b);
	}
	
	public void set(Cuboid c, MaterialBlock mb)
	{
		VariableBlock v = new VariableBlock(mb);
		set(c, new Brush()
		{
			@Override
			public VariableBlock brush(Location location)
			{
				return v;
			}
		});
	}
	
	public void set(Cuboid c, Brush brush)
	{
		Iterator<Block> it = c.iterator();
		
		while(it.hasNext())
		{
			Block b = it.next();
			set(b.getLocation(), brush.brush(b.getLocation()).random());
		}
	}
}

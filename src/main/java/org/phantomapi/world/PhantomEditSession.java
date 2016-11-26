package org.phantomapi.world;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import com.boydti.fawe.util.EditSessionBuilder;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.world.biome.BaseBiome;

/**
 * Represents a fast queue using fawe
 * 
 * @author cyberpwn
 */
public class PhantomEditSession
{
	private EditSession es;
	
	/**
	 * Create a session
	 * 
	 * @param world
	 */
	public PhantomEditSession(PhantomWorld world)
	{
		es = new EditSessionBuilder(world.getName()).fastmode(true).limitUnlimited().autoQueue(false).build();
	}
	
	public void makePyramid(Location center, MaterialBlock mb, int height, boolean filled)
	{
		try
		{
			es.makePyramid(W.toEditVector(center), mb.toPattern(), height, filled);
		}
		
		catch(MaxChangedBlocksException e)
		{
			
		}
	}
	
	public void makePyramid(Location center, VariableBlock mb, int height, boolean filled)
	{
		try
		{
			es.makePyramid(W.toEditVector(center), mb.toPattern(), height, filled);
		}
		
		catch(MaxChangedBlocksException e)
		{
			
		}
	}
	
	public void makeSphere(Location center, MaterialBlock mb, double radiusX, double radiusY, double radiusZ, boolean filled)
	{
		try
		{
			es.makeSphere(W.toEditVector(center), mb.toPattern(), radiusX, radiusY, radiusZ, filled);
		}
		
		catch(MaxChangedBlocksException e)
		{
			
		}
	}
	
	public void makeSphere(Location center, VariableBlock mb, double radiusX, double radiusY, double radiusZ, boolean filled)
	{
		try
		{
			es.makeSphere(W.toEditVector(center), mb.toPattern(), radiusX, radiusY, radiusZ, filled);
		}
		
		catch(MaxChangedBlocksException e)
		{
			
		}
	}
	
	public void makeSphere(Location center, MaterialBlock mb, double radius, boolean filled)
	{
		try
		{
			es.makeSphere(W.toEditVector(center), mb.toPattern(), radius, filled);
		}
		
		catch(MaxChangedBlocksException e)
		{
			
		}
	}
	
	public void makeSphere(Location center, VariableBlock mb, double radius, boolean filled)
	{
		try
		{
			es.makeSphere(W.toEditVector(center), mb.toPattern(), radius, filled);
		}
		
		catch(MaxChangedBlocksException e)
		{
			
		}
	}
	
	public void makeCylinder(Location center, MaterialBlock mb, double radius, int height, boolean filled)
	{
		try
		{
			es.makeCylinder(W.toEditVector(center), mb.toPattern(), radius, height, filled);
		}
		
		catch(MaxChangedBlocksException e)
		{
			
		}
	}
	
	public void makeCylinder(Location center, VariableBlock mb, double radius, int height, boolean filled)
	{
		try
		{
			es.makeCylinder(W.toEditVector(center), mb.toPattern(), radius, height, filled);
		}
		
		catch(MaxChangedBlocksException e)
		{
			
		}
	}
	
	public Biome getBiome(int x, int z)
	{
		return Biome.values()[es.getBiome(new Vector2D(x, z)).getId()];
	}
	
	public void setBiome(int x, int z, Biome biome)
	{
		es.setBiome(new Vector2D(x, z), new BaseBiome(biome.ordinal()));
	}
	
	public int getHighestBlock(int x, int z)
	{
		return es.getHighestTerrainBlock(x, z, 0, 255);
	}
	
	@SuppressWarnings("deprecation")
	public void set(Location location, MaterialBlock block)
	{
		es.setBlock(location.getBlockX(), location.getBlockY(), location.getBlockZ(), new BaseBlock(block.getMaterial().getId(), block.getData()));
	}
	
	public void set(Location location, Material material, Byte data)
	{
		set(location, new MaterialBlock(material, data));
	}
	
	public void set(Location location, Material material, Integer data)
	{
		set(location, new MaterialBlock(material, data.byteValue()));
	}
	
	public void set(Location location, Material material)
	{
		set(location, material, (byte) 0);
	}
	
	@SuppressWarnings("deprecation")
	public MaterialBlock get(Location location)
	{
		BaseBlock bb = es.getBlock(new Vector(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
		
		return new MaterialBlock(Material.getMaterial(bb.getId()), (byte) bb.getData());
	}
	
	public void flush()
	{
		es.flushQueue();
	}
}

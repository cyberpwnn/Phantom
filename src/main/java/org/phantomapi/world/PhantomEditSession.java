package org.phantomapi.world;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import com.boydti.fawe.util.EditSessionBuilder;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;

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
	public PhantomEditSession(World world)
	{
		es = new EditSessionBuilder(world.getName()).fastmode(true).limitUnlimited().autoQueue(false).build();
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

package org.phantomapi.skyblock;

import java.util.List;
import java.util.UUID;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

/**
 * A structure for a wrapper island for skyblock
 * 
 * @author cyberpwn
 */
public interface SkyblockIsland
{
	/**
	 * Get the distance
	 * 
	 * @return the distance
	 */
	public int getDistance();
	
	/**
	 * Get the date created
	 * 
	 * @return the date created
	 */
	public long getCreatedDate();
	
	/**
	 * Get the center of the island
	 * 
	 * @return the center
	 */
	public Location getCenter();
	
	/**
	 * Get the world this island resides in
	 * 
	 * @return the world
	 */
	public World getWorld();
	
	/**
	 * Does the island contain the given entity
	 * 
	 * @param e
	 *            the entity
	 * @return true if it does
	 */
	public boolean contains(Entity e);
	
	/**
	 * Does the island contain the given location
	 * 
	 * @param l
	 *            the location
	 * @return true if it does
	 */
	public boolean contains(Location l);
	
	/**
	 * Does the island contain the given chunk
	 * 
	 * @param c
	 *            the chunk
	 * @return true if it does
	 */
	public boolean contains(Chunk c);
	
	/**
	 * Does the island contain the given block
	 * 
	 * @param b
	 *            the block
	 * @return true if it does
	 */
	public boolean contains(Block b);
	
	/**
	 * Get the owner uuid
	 * 
	 * @return the owner
	 */
	public UUID getOwner();
	
	/**
	 * Get members of island
	 * 
	 * @return the members
	 */
	public List<UUID> getMembers();
	
	/**
	 * Returns the underlying skyblock island
	 * 
	 * @return requires reflection to interact
	 */
	public Object getRawIsland();
}

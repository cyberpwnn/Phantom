package org.phantomapi.world;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.phantomapi.Phantom;
import org.phantomapi.lang.GList;

/**
 * Lock all chunks unloaded
 * 
 * @author cyberpwn
 */
public class WorldLock implements Listener
{
	private World world;
	private Boolean locked;
	
	/**
	 * Create a world lock
	 * 
	 * @param world
	 *            the world
	 */
	public WorldLock(World world)
	{
		this.world = world;
		locked = false;
	}
	
	/**
	 * Check the world lock status
	 * 
	 * @return true if the world is locked
	 */
	public boolean isLocked()
	{
		return locked;
	}
	
	/**
	 * Lock the world
	 */
	@SuppressWarnings("deprecation")
	public void lock()
	{
		Phantom.instance().registerListener(this);
		locked = true;
		
		for(Chunk i : new GList<Chunk>(world.getLoadedChunks()))
		{
			i.unload(true, true);
		}
	}
	
	/**
	 * Release the world from it's locked state
	 */
	public void release()
	{
		Phantom.instance().unRegisterListener(this);
		locked = false;
		
		for(Player i : world.getPlayers())
		{
			for(Chunk j : W.chunkRadius(i.getLocation().getChunk(), 1 + (Bukkit.getViewDistance() / 2)))
			{
				j.load();
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
	public void on(ChunkUnloadEvent e)
	{
		if(locked && e.getWorld().equals(world))
		{
			e.setCancelled(true);
		}
	}
}

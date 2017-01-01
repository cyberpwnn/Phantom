package org.phantomapi.ext;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.UUID;
import org.bukkit.Chunk;
import org.bukkit.Location;

/**
 * Skyblock connector
 * 
 * @author cyberpwn
 */
public class SkyblockPluginConnector extends PluginConnector
{
	/**
	 * Init plugin connector
	 */
	public SkyblockPluginConnector()
	{
		super("ASkyBlock");
	}
	
	/**
	 * Get the api class
	 * 
	 * @return the api class
	 * @throws ClassNotFoundException
	 */
	public Class<?> getAPIClass() throws ClassNotFoundException
	{
		return Class.forName("com.wasteofplastic.askyblock.ASkyBlockAPI");
	}
	
	/**
	 * Get the api instance
	 * 
	 * @return the api instance
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public Object getAPI() throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
	{
		Class<?> c = getAPIClass();
		return c.getMethod("getInstance").invoke(null);
	}
	
	/**
	 * Get the island at the given location
	 * 
	 * @param c
	 *            the location
	 * @return the island
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public Object getIsland(Chunk c) throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
	{
		return getIsland(c.getBlock(0, 0, 0).getLocation());
	}
	
	/**
	 * Get the island at the given location
	 * 
	 * @param location
	 *            the location
	 * @return the island
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public Object getIsland(Location location) throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
	{
		Object api = getAPI();
		Class<?> apic = getAPIClass();
		
		return apic.getMethod("getIslandAt", Location.class).invoke(api, location);
	}
	
	/**
	 * Get the island distance
	 * 
	 * @param island
	 *            the island
	 * @return the island distance
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public int getDistance(Object island) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
	{
		return (int) island.getClass().getMethod("getIslandDistance").invoke(island);
	}
	
	/**
	 * Get the center of the island
	 * 
	 * @param island
	 *            the island
	 * @return the center location
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public Location getCenter(Object island) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
	{
		return (Location) island.getClass().getMethod("getCenter").invoke(island);
	}
	
	/**
	 * Does the island contain the given location
	 * 
	 * @param island
	 *            the island
	 * @param c
	 *            the chunk
	 * @return true if it does
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public boolean islandContains(Object island, Chunk c) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
	{
		return islandContains(island, c.getBlock(0, 0, 0).getLocation());
	}
	
	/**
	 * Does the island contain the given location
	 * 
	 * @param island
	 *            the island
	 * @param location
	 *            the location
	 * @return true if it does
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public boolean islandContains(Object island, Location location) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
	{
		return (boolean) island.getClass().getMethod("inIslandSpace", Location.class).invoke(island, location);
	}
	
	/**
	 * Get the ms time when the island was created
	 * 
	 * @param island
	 *            the island
	 * @return the created time
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public long getCreatedDate(Object island) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
	{
		return (long) island.getClass().getMethod("getCreatedDate", long.class).invoke(island);
	}
	
	/**
	 * Get the island owner uuid
	 * 
	 * @param island
	 *            the island
	 * @return the owner uuid
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public UUID getOwner(Object island) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
	{
		return (UUID) island.getClass().getMethod("getOwner").invoke(island);
	}
	
	/**
	 * Get all members by uuid in the given island
	 * 
	 * @param island
	 *            the island
	 * @return the list of uuids
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	@SuppressWarnings("unchecked")
	public List<UUID> getMembers(Object island) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
	{
		return (List<UUID>) island.getClass().getMethod("getMembers").invoke(island);
	}
}

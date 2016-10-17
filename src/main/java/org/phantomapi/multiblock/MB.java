package org.phantomapi.multiblock;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.phantomapi.Phantom;
import org.phantomapi.lang.GList;

/**
 * Multiblock utilities (for working with multiblocks)
 * 
 * @author cyberpwn
 */
public class MB
{
	/**
	 * Get the structure for the given multiblock type
	 * 
	 * @param type
	 *            the type of the structure
	 * @return the multiblock structure
	 */
	public static MultiblockStructure getStructure(String type)
	{
		return Phantom.instance().getMultiblockRegistryController().getStructures().get(type);
	}
	
	/**
	 * Get the structure used for the given multiblock
	 * 
	 * @param instance
	 *            the instance
	 * @return the structure
	 */
	public static MultiblockStructure getStructure(Multiblock instance)
	{
		return getStructure(instance.getType());
	}
	
	/**
	 * Get all loaded multiblock instances
	 * 
	 * @return the instances
	 */
	public static GList<Multiblock> getInstances()
	{
		return Phantom.instance().getMultiblockRegistryController().getInstances().v();
	}
	
	/**
	 * Get all loaded multiblock instances
	 * 
	 * @param type
	 *            the multiblock type
	 * @return the instances
	 */
	public static GList<Multiblock> getInstances(String type)
	{
		GList<Multiblock> instances = getInstances().copy();
		
		for(Multiblock i : instances.copy())
		{
			if(!i.getType().equals(type))
			{
				instances.remove(i);
			}
		}
		
		return instances;
	}
	
	/**
	 * Get the multiblock instance for the given holder
	 * 
	 * @param v
	 *            the holder
	 * @return the multiblock or null
	 */
	public static Multiblock getInstance(int v)
	{
		return Phantom.instance().getMultiblockRegistryController().getInstances().get(v);
	}
	
	/**
	 * Get the multiblock instance for the given holder
	 * 
	 * @param v
	 *            the holder
	 * @return the multiblock or null
	 */
	public static Multiblock getInstance(Chunk v)
	{
		return getInstance(Phantom.instance().getMultiblockRegistryController().getInstanceReference().get(v));
	}
	
	/**
	 * Get the multiblock instance for the given holder
	 * 
	 * @param v
	 *            the holder
	 * @return the multiblock or null
	 */
	public static Multiblock getInstance(Block v)
	{
		return getInstance(v.getLocation());
	}
	
	/**
	 * Get the multiblock instance for the given holder
	 * 
	 * @param v
	 *            the holder
	 * @return the multiblock or null
	 */
	public static Multiblock getInstance(Location v)
	{
		for(Multiblock i : getInstances())
		{
			if(i.contains(v))
			{
				return i;
			}
		}
		
		return null;
	}
}

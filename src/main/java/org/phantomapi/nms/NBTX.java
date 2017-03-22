package org.phantomapi.nms;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.phantomapi.nbt.NBTTagCompound;
import org.phantomapi.nbt.NBTUtils;

/**
 * NBT Utilities
 * 
 * @author cyberpwn
 */
public class NBTX
{
	/**
	 * Get the entity nbt data
	 * 
	 * @param e
	 *            the entity
	 * @return the data
	 */
	public static NBTTagCompound getEntityNBT(Entity e)
	{
		return NBTUtils.getEntityNBTData(e);
	}
	
	/**
	 * Set the entity data
	 * 
	 * @param e
	 *            the entity
	 * @param n
	 *            the nbt
	 */
	public static void setEntityNBT(Entity e, NBTTagCompound n)
	{
		NBTUtils.setEntityNBTData(e, n);
	}
	
	/**
	 * Get tile entity nbt
	 * 
	 * @param b
	 *            the block
	 * @return the tile nbt data
	 */
	public static NBTTagCompound getTileNBT(Block b)
	{
		return NBTUtils.getTileEntityNBTData(b);
	}
	
	/**
	 * Set Tile nbt data
	 * 
	 * @param b
	 *            the block
	 * @param n
	 *            the tile data
	 */
	public static void setTileNBT(Block b, NBTTagCompound n)
	{
		NBTUtils.setTileEntityNBTData(b, n);
	}
	
	/**
	 * Get item nbt data
	 * 
	 * @param b
	 *            the item
	 * @return the nbt data
	 */
	public static NBTTagCompound getItemNBT(ItemStack b)
	{
		return NBTUtils.getItemStackTag(b);
	}
	
	/**
	 * Set item nbt data
	 * 
	 * @param b
	 *            the item
	 * @param n
	 *            the nbt data
	 */
	public static void setItemNBT(ItemStack b, NBTTagCompound n)
	{
		NBTUtils.setItemStackTag(b, n);
	}
}

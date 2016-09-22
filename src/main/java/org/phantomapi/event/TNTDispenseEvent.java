package org.phantomapi.event;

import org.bukkit.block.Block;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.ItemStack;

/**
 * TNT Was dispensed from a dispenser
 * 
 * @author cyberpwn
 */
public class TNTDispenseEvent extends CancellablePhantomEvent
{
	private final ItemStack item;
	private final Block dispenser;
	private final TNTPrimed tntEntity;
	
	public TNTDispenseEvent(ItemStack item, Block dispenser, TNTPrimed tntEntity)
	{
		super();
		
		this.item = item;
		this.tntEntity = tntEntity;
		this.dispenser = dispenser;
	}
	
	public TNTPrimed getTNT()
	{
		return tntEntity;
	}
	
	public ItemStack getItem()
	{
		return item;
	}
	
	public Block getDispenser()
	{
		return dispenser;
	}
}

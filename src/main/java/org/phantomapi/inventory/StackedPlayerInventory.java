package org.phantomapi.inventory;

import org.bukkit.Material;
import org.bukkit.inventory.PlayerInventory;
import org.phantomapi.lang.GMap;

/**
 * Player inventory modification
 * 
 * @author cyberpwn
 */
public class StackedPlayerInventory extends PhantomPlayerInventory implements StackedPhantomInventory
{
	public GMap<Integer, Stack> inventoryContents;
	
	public StackedPlayerInventory(PlayerInventory i)
	{
		super(i);
		
		this.inventoryContents = new GMap<Integer, Stack>();
	}
	
	public GMap<Integer, Stack> getStacks()
	{
		thrash();
		
		for(int i = 0; i < 36; i++)
		{
			inventoryContents.put(i, new Stack(getItem(i)));
		}
		
		return inventoryContents;
	}
	
	public void setStacks(GMap<Integer, Stack> stacks)
	{
		inventoryContents = stacks;
		thrash();
	}
	
	public void setStack(int slot, Stack stack)
	{
		inventoryContents.put(slot, stack);
	}
	
	public Stack getStack(int slot)
	{
		if(getItem(slot) != null)
		{
			return new Stack(getItem(slot).clone());
		}
		
		else
		{
			return new Stack(Material.AIR);
		}
	}
	
	public void thrash()
	{
		for(int i : inventoryContents.k())
		{
			if(!new Stack(getItem(i)).equals(inventoryContents.get(i)))
			{
				setItem(i, inventoryContents.get(i).toItemStack());
			}
		}
		
		inventoryContents.clear();
	}
}

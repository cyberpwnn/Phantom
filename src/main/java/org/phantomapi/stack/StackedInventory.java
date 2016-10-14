package org.phantomapi.stack;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.phantomapi.inventory.PhantomInventory;
import org.phantomapi.lang.GMap;

public class StackedInventory extends PhantomInventory implements StackedPhantomInventory
{
	public GMap<Integer, Stack> inventoryContents;
	
	public StackedInventory(Inventory i)
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
	
	public GMap<Integer, Stack> getNStacks()
	{
		GMap<Integer, Stack> inventoryContents = new GMap<Integer, Stack>();
		
		for(int i = 0; i < 36; i++)
		{
			inventoryContents.put(i, new Stack(getItem(i)));
		}
		
		return inventoryContents;
	}
	
	public void setStacks(GMap<Integer, Stack> stacks)
	{
		thrash();
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
			setItem(i, inventoryContents.get(i).toItemStack());
		}
		
		inventoryContents.clear();
	}
}

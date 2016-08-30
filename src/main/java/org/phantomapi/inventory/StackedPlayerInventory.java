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
	public GMap<Integer, Stack> armorContents;
	
	public StackedPlayerInventory(PlayerInventory i)
	{
		super(i);
		
		this.inventoryContents = new GMap<Integer, Stack>();
		this.armorContents = new GMap<Integer, Stack>();
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
	
	public GMap<Integer, Stack> getArmorStacks()
	{
		thrash();
		
		armorContents.put(0, new Stack(getHelmet()));
		armorContents.put(1, new Stack(getChestplate()));
		armorContents.put(2, new Stack(getLeggings()));
		armorContents.put(3, new Stack(getBoots()));
		
		return armorContents;
	}
	
	public void setStacks(GMap<Integer, Stack> stacks)
	{
		inventoryContents = stacks;
		thrash();
	}
	
	public Stack getHelmetStack()
	{
		return new Stack(getHelmet());
	}
	
	public Stack getChestplateStack()
	{
		return new Stack(getChestplate());
	}
	
	public Stack getLeggingsStack()
	{
		return new Stack(getLeggings());
	}
	
	public Stack getBootsStack()
	{
		return new Stack(getBoots());
	}
	
	public void setHelmetStack(Stack stack)
	{
		armorContents.put(0, stack);
	}
	
	public void setChestplateStack(Stack stack)
	{
		armorContents.put(1, stack);
	}
	
	public void setLeggingsStack(Stack stack)
	{
		armorContents.put(2, stack);
	}
	
	public void setBootsStack(Stack stack)
	{
		armorContents.put(3, stack);
	}
	
	public void setArmorStacks(GMap<Integer, Stack> stacks)
	{
		armorContents = stacks;
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
		
		if(armorContents.containsKey(0))
		{
			setHelmet(armorContents.get(0).toItemStack());
		}
		
		if(armorContents.containsKey(1))
		{
			setChestplate(armorContents.get(1).toItemStack());
		}
		
		if(armorContents.containsKey(2))
		{
			setLeggings(armorContents.get(2).toItemStack());
		}
		
		if(armorContents.containsKey(3))
		{
			setBoots(armorContents.get(3).toItemStack());
		}
		
		inventoryContents.clear();
	}
}

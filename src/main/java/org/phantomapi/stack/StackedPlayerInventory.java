package org.phantomapi.stack;

import java.io.IOException;
import org.bukkit.Material;
import org.bukkit.inventory.PlayerInventory;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.clust.JSONObject;
import org.phantomapi.inventory.PhantomPlayerInventory;
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
		
		inventoryContents = new GMap<Integer, Stack>();
		armorContents = new GMap<Integer, Stack>();
		
		pull();
	}
	
	@Override
	public GMap<Integer, Stack> getStacks()
	{
		thrash();
		
		inventoryContents.clear();
		
		for(int i = 0; i < 36; i++)
		{
			inventoryContents.put(i, new Stack(getItem(i)));
		}
		
		return inventoryContents;
	}
	
	public GMap<Integer, Stack> getArmorStacks()
	{
		thrash();
		armorContents.clear();
		
		armorContents.put(0, new Stack(getHelmet()));
		armorContents.put(1, new Stack(getChestplate()));
		armorContents.put(2, new Stack(getLeggings()));
		armorContents.put(3, new Stack(getBoots()));
		
		return armorContents;
	}
	
	@Override
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
	
	@Override
	public void setStack(int slot, Stack stack)
	{
		inventoryContents.put(slot, stack);
	}
	
	@Override
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
	
	@Override
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
	}
	
	@Override
	public byte[] toData() throws IOException
	{
		DataCluster cc = new DataCluster();
		
		for(Integer i : inventoryContents.k())
		{
			cc.set("i-" + i, new DataCluster(inventoryContents.get(i).toData()).toJSON().toString());
		}
		
		for(Integer i : armorContents.k())
		{
			cc.set("a-" + i, new DataCluster(armorContents.get(i).toData()).toJSON().toString());
		}
		
		return cc.compress();
	}
	
	@Override
	public void fromData(byte[] data) throws IOException
	{
		DataCluster cc = new DataCluster(data);
		inventoryContents.clear();
		armorContents.clear();
		
		for(String i : cc.keys())
		{
			String s = i.split("-")[0];
			Integer l = Integer.valueOf(i.split("-")[1]);
			
			Stack v = new Stack(Material.GLASS);
			v.fromData(new DataCluster(new JSONObject(cc.getString(i))).compress());
			
			if(s.equals("i"))
			{
				inventoryContents.put(l, v);
			}
			
			else if(s.equals("a"))
			{
				armorContents.put(l, v);
			}
		}
	}
	
	@Override
	public void pull()
	{
		armorContents.put(0, new Stack(getHelmet()));
		armorContents.put(1, new Stack(getChestplate()));
		armorContents.put(2, new Stack(getLeggings()));
		armorContents.put(3, new Stack(getBoots()));
		
		inventoryContents.clear();
		
		for(int i = 0; i < 36; i++)
		{
			inventoryContents.put(i, new Stack(getItem(i)));
		}
	}
}

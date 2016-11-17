package org.phantomapi.stack;

import java.io.IOException;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.clust.JSONObject;
import org.phantomapi.inventory.PhantomInventory;
import org.phantomapi.lang.GMap;

public class StackedInventory extends PhantomInventory implements StackedPhantomInventory
{
	public GMap<Integer, Stack> inventoryContents;
	private Inventory inventory;
	
	public StackedInventory(Inventory i)
	{
		super(i);
		
		inventory = i;
		inventoryContents = new GMap<Integer, Stack>();
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
	
	public GMap<Integer, Stack> getNStacks()
	{
		GMap<Integer, Stack> inventoryContents = new GMap<Integer, Stack>();
		
		for(int i = 0; i < 36; i++)
		{
			inventoryContents.put(i, new Stack(getItem(i)));
		}
		
		return inventoryContents;
	}
	
	@Override
	public void setStacks(GMap<Integer, Stack> stacks)
	{
		thrash();
		inventoryContents = stacks;
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
			setItem(i, inventoryContents.get(i).toItemStack());
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
		
		return cc.compress();
	}
	
	@Override
	public void fromData(byte[] data) throws IOException
	{
		DataCluster cc = new DataCluster(data);
		inventoryContents.clear();
		
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
		}
	}
	
	@Override
	public void pull()
	{
		inventoryContents.clear();
		
		for(int i = 0; i < inventory.getSize(); i++)
		{
			inventoryContents.put(i, new Stack(getItem(i)));
		}
	}
}

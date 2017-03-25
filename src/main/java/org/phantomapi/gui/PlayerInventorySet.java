package org.phantomapi.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerInventorySet
{
	private Player p;
	private ItemStack[] contents;
	
	public PlayerInventorySet(Player p)
	{
		this.p = p;
		contents = new ItemStack[p.getInventory().getContents().length];
	}
	
	public void pop()
	{
		ItemStack[] ic = p.getInventory().getContents();
		
		for(int i = 0; i < ic.length; i++)
		{
			if(ic[i] == null)
			{
				contents[i] = null;
			}
			
			else
			{
				contents[i] = ic[i].clone();
			}
		}
		
		p.getInventory().clear();
	}
	
	public void push()
	{
		p.getInventory().clear();
		p.getInventory().setContents(contents);
		contents = new ItemStack[p.getInventory().getContents().length];
	}
}

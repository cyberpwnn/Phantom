package org.cyberpwn.phantom.gui;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.cyberpwn.phantom.lang.GList;

public interface Element
{
	public Element addText(String text);
	
	public Material getType();
	
	public Element setType(Material type);
	
	public Byte getMetadata();
	
	public Element setMetadata(Byte metadata);
	
	public String getTitle();
	
	public Element setTitle(String title);
	
	public GList<String> getText();
	
	public Element setText(GList<String> text);
	
	public Short getDurability();
	
	public Element setDurability(Short durability);
	
	public Slot getSlot();
	
	public Element setSlot(Slot slot);
	
	public Integer getCount();
	
	public Element setCount(Integer count);
	
	public Element copy();
	
	public Element setStack(ItemStack stack);
	
	public ItemStack getStack();
	
	public void onClick(Player p, Click c, Window w);
	
	public UUID getId();
}

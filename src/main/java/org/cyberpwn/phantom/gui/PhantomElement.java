package org.cyberpwn.phantom.gui;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.cyberpwn.phantom.lang.GList;

public class PhantomElement implements Element
{
	private Material type;
	private Byte metadata;
	private String title;
	private GList<String> text;
	private Short durability;
	private Integer count;
	private Slot slot;
	private ItemStack stack;
	private final UUID id;
	
	@SuppressWarnings("deprecation")
	public PhantomElement(ItemStack stack, Slot slot)
	{
		this.stack = stack;
		this.type = stack.getType();
		this.slot = slot;
		this.metadata = stack.getData().getData();
		this.title = stack.hasItemMeta() ? stack.getItemMeta().getDisplayName() : stack.getType().toString();
		this.text = stack.hasItemMeta() ? new GList<String>(stack.getItemMeta().getLore()) : new GList<String>();
		this.durability = stack.getDurability();
		this.count = stack.getAmount();
		this.id = UUID.randomUUID();
	}
	
	@SuppressWarnings("deprecation")
	public PhantomElement(Material type, Byte metadata, Slot slot, String title, GList<String> text, Short durability, Integer count)
	{
		this.type = type;
		this.slot = slot;
		this.metadata = metadata;
		this.title = title;
		this.text = text;
		this.durability = durability;
		this.count = count;
		
		ItemStack stack = new ItemStack(type, count, durability, metadata);
		ItemMeta im = stack.getItemMeta();
		im.setDisplayName(title);
		im.setLore(text);
		
		this.stack = stack;
		
		this.id = UUID.randomUUID();
	}
	
	public PhantomElement(Material type, Slot slot, String title)
	{
		this(type, (byte) 0, slot, title, new GList<String>(), (short) 0, 1);
	}
	
	public PhantomElement(Material type, Byte metadata, Slot slot, String title)
	{
		this(type, metadata, slot, title, new GList<String>(), (short) 0, 1);
	}
	
	public PhantomElement(Material type, Slot slot, String title, Integer count)
	{
		this(type, (byte) 0, slot, title, new GList<String>(), (short) 0, count);
	}
	
	public PhantomElement(Material type, Slot slot, String title, Short durability)
	{
		this(type, (byte) 0, slot, title, new GList<String>(), durability, 1);
	}
	
	public PhantomElement(Material type, Byte metadata, Slot slot, String title, Integer count)
	{
		this(type, metadata, slot, title, new GList<String>(), (short) 0, count);
	}
	
	public PhantomElement(Material type, Byte metadata, Slot slot, String title, Short durability)
	{
		this(type, metadata, slot, title, new GList<String>(), durability, 1);
	}
	
	public PhantomElement(Material type, Byte metadata, Slot slot, String title, Short durability, Integer count)
	{
		this(type, metadata, slot, title, new GList<String>(), durability, count);
	}
	
	public Element addText(String text)
	{
		this.text.add(text);
		
		return this;
	}
	
	public Material getType()
	{
		return type;
	}
	
	public Element setType(Material type)
	{
		this.type = type;
		
		return this;
	}
	
	public Byte getMetadata()
	{
		return metadata;
	}
	
	public Element setMetadata(Byte metadata)
	{
		this.metadata = metadata;
		
		return this;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public Element setTitle(String title)
	{
		this.title = title;
		
		return this;
	}
	
	public GList<String> getText()
	{
		return text;
	}
	
	public Element setText(GList<String> text)
	{
		this.text = text;
		
		return this;
	}
	
	public Short getDurability()
	{
		return durability;
	}
	
	public Element setDurability(Short durability)
	{
		this.durability = durability;
		
		return this;
	}
	
	public Integer getCount()
	{
		return count;
	}
	
	public Element setCount(Integer count)
	{
		this.count = count;
		
		return this;
	}
	
	public Slot getSlot()
	{
		return slot;
	}
	
	public Element setSlot(Slot slot)
	{
		this.slot = slot;
		
		return this;
	}
	
	@SuppressWarnings("deprecation")
	public ItemStack getStack()
	{
		ItemStack stack = new ItemStack(type, count, durability, metadata);
		ItemMeta im = stack.getItemMeta();
		im.setDisplayName(title);
		im.setLore(text);
		
		setStack(stack);
		
		return this.stack;
	}
	
	public Element setStack(ItemStack stack)
	{
		this.stack = stack;
		
		return this;
	}
	
	public Element copy()
	{
		return new PhantomElement(type, metadata, slot, title, text, durability, count);
	}
	
	public UUID getId()
	{
		return id;
	}
	
	@Override
	public void onClick(Player p, Click c, Window w)
	{
		
	}
	
	public boolean equals(Object object)
	{
		if(object instanceof Element)
		{
			return ((Element)object).getId().equals(getId());
		}
		
		return false;
	}
}

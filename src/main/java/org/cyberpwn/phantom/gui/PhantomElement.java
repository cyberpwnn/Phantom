package org.cyberpwn.phantom.gui;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.cyberpwn.phantom.lang.GList;

/**
 * Element implementation
 * 
 * @author cyberpwn
 */
public abstract class PhantomElement implements Element
{
	protected Material type;
	protected Byte metadata;
	protected String title;
	protected GList<String> text;
	protected Short durability;
	protected Integer count;
	protected Slot slot;
	protected ItemStack stack;
	protected final UUID id;
	
	/**
	 * Create an element from an item stack and slot
	 * 
	 * @param stack
	 *            the item stack
	 * @param slot
	 *            the slot
	 */
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
	
	/**
	 * Create a phantom element from all data
	 * 
	 * @param type
	 *            the material type
	 * @param metadata
	 *            the metadata (sub item id)
	 * @param slot
	 *            the slot
	 * @param title
	 *            the title (custom item name)
	 * @param text
	 *            the text (lore of this item)
	 * @param durability
	 *            the durability
	 * @param count
	 *            the count
	 */
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
		ItemMeta im = stack.hasItemMeta() ? stack.getItemMeta() : Bukkit.getItemFactory().getItemMeta(stack.getType());
		
		if(im != null)
		{
			im.setDisplayName(title);
			im.setLore(text);
			stack.setItemMeta(im);
		}
		
		this.stack = stack;
		this.id = UUID.randomUUID();
	}
	
	/**
	 * Create a phantom element from all data
	 * 
	 * @param type
	 *            the material type
	 * @param slot
	 *            the slot
	 * @param title
	 *            the title (custom item name)
	 */
	public PhantomElement(Material type, Slot slot, String title)
	{
		this(type, (byte) 0, slot, title, new GList<String>(), (short) 0, 1);
	}
	
	/**
	 * Create a phantom element from all data
	 * 
	 * @param type
	 *            the material type
	 * @param metadata
	 *            the metadata (sub item id)
	 * @param slot
	 *            the slot
	 * @param title
	 *            the title (custom item name)
	 */
	public PhantomElement(Material type, Byte metadata, Slot slot, String title)
	{
		this(type, metadata, slot, title, new GList<String>(), (short) 0, 1);
	}
	
	/**
	 * Create a phantom element from all data
	 * 
	 * @param type
	 *            the material type
	 * @param slot
	 *            the slot
	 * @param title
	 *            the title (custom item name)
	 * @param count
	 *            the count
	 */
	public PhantomElement(Material type, Slot slot, String title, Integer count)
	{
		this(type, (byte) 0, slot, title, new GList<String>(), (short) 0, count);
	}
	
	/**
	 * Create a phantom element from all data
	 * 
	 * @param type
	 *            the material type
	 * @param slot
	 *            the slot
	 * @param title
	 *            the title (custom item name)
	 * @param durability
	 *            the durability
	 */
	public PhantomElement(Material type, Slot slot, String title, Short durability)
	{
		this(type, (byte) 0, slot, title, new GList<String>(), durability, 1);
	}
	
	/**
	 * Create a phantom element from all data
	 * 
	 * @param type
	 *            the material type
	 * @param metadata
	 *            the metadata (sub item id)
	 * @param slot
	 *            the slot
	 * @param title
	 *            the title (custom item name)
	 * @param count
	 *            the count
	 */
	public PhantomElement(Material type, Byte metadata, Slot slot, String title, Integer count)
	{
		this(type, metadata, slot, title, new GList<String>(), (short) 0, count);
	}
	
	/**
	 * Create a phantom element from all data
	 * 
	 * @param type
	 *            the material type
	 * @param metadata
	 *            the metadata (sub item id)
	 * @param slot
	 *            the slot
	 * @param title
	 *            the title (custom item name)
	 * @param durability
	 *            the durability
	 */
	public PhantomElement(Material type, Byte metadata, Slot slot, String title, Short durability)
	{
		this(type, metadata, slot, title, new GList<String>(), durability, 1);
	}
	
	/**
	 * Create a phantom element from all data
	 * 
	 * @param type
	 *            the material type
	 * @param metadata
	 *            the metadata (sub item id)
	 * @param slot
	 *            the slot
	 * @param title
	 *            the title (custom item name)
	 * @param durability
	 *            the durability
	 * @param count
	 *            the count
	 */
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
		ItemMeta im = Bukkit.getItemFactory().getItemMeta(type);
		
		if(im != null)
		{
			im.setDisplayName(title);
			im.setLore(text);
			stack.setItemMeta(im);
		}
		
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
		return new PhantomElement(type, metadata, slot, title, text, durability, count)
		{
			public void onClick(Player p, Click c, Window w)
			{
				
			}
		};
	}
	
	public UUID getId()
	{
		return id;
	}
	
	@Override
	public abstract void onClick(Player p, Click c, Window w);
	
	public boolean equals(Object object)
	{
		if(object instanceof Element)
		{
			return ((Element) object).getId().equals(getId());
		}
		
		return false;
	}
}

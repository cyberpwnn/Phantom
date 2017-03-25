package org.phantomapi.gui;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.phantomapi.lang.GList;

/**
 * Element implementation
 * 
 * @author cyberpwn
 */
public class PhantomElement implements Element
{
	protected Material type;
	protected Byte metadata;
	protected String title;
	protected GList<String> text;
	protected Short durability;
	protected Integer count;
	protected Slot slot;
	protected ItemStack stack;
	protected boolean glowing;
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
		type = stack.getType();
		this.slot = slot;
		metadata = stack.getData().getData();
		title = stack.hasItemMeta() ? stack.getItemMeta().getDisplayName() : stack.getType().toString();
		text = stack.hasItemMeta() ? new GList<String>(stack.getItemMeta().getLore()) : new GList<String>();
		durability = stack.getDurability();
		count = stack.getAmount();
		id = UUID.randomUUID();
		glowing = false;
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
		this.slot = slot.copy();
		this.metadata = metadata;
		this.title = title;
		this.text = text;
		this.durability = durability;
		this.count = count;
		glowing = false;
		
		ItemStack stack = new ItemStack(type, count, durability, metadata);
		ItemMeta im = stack.hasItemMeta() ? stack.getItemMeta() : Bukkit.getItemFactory().getItemMeta(stack.getType());
		
		if(im != null)
		{
			im.setDisplayName(title);
			im.setLore(text);
			stack.setItemMeta(im);
		}
		
		this.stack = stack;
		id = UUID.randomUUID();
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
	
	@Override
	public Element addText(String text)
	{
		this.text.add(text);
		
		return this;
	}
	
	@Override
	public Material getType()
	{
		return type;
	}
	
	@Override
	public Element setType(Material type)
	{
		this.type = type;
		
		return this;
	}
	
	@Override
	public Byte getMetadata()
	{
		return metadata;
	}
	
	@Override
	public Element setMetadata(Byte metadata)
	{
		this.metadata = metadata;
		
		return this;
	}
	
	@Override
	public String getTitle()
	{
		return title;
	}
	
	@Override
	public Element setTitle(String title)
	{
		this.title = title;
		
		return this;
	}
	
	@Override
	public GList<String> getText()
	{
		return text;
	}
	
	@Override
	public Element setText(GList<String> text)
	{
		this.text = text;
		
		return this;
	}
	
	@Override
	public Short getDurability()
	{
		return durability;
	}
	
	@Override
	public Element setDurability(Short durability)
	{
		this.durability = durability;
		
		return this;
	}
	
	@Override
	public Integer getCount()
	{
		return count;
	}
	
	@Override
	public Element setCount(Integer count)
	{
		this.count = count;
		
		return this;
	}
	
	@Override
	public Slot getSlot()
	{
		return slot;
	}
	
	@Override
	public Element setSlot(Slot slot)
	{
		this.slot = slot;
		
		return this;
	}
	
	@Override
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
		
		if(isGlowing())
		{
			stack.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
		}
		
		setStack(stack);
		
		return this.stack;
	}
	
	@Override
	public Element setStack(ItemStack stack)
	{
		this.stack = stack;
		
		return this;
	}
	
	@Override
	public Element copy()
	{
		return new PhantomElement(type, metadata, slot, title, text, durability, count)
		{
			@Override
			public void onClick(Player p, Click c, Window w)
			{
				
			}
		};
	}
	
	@Override
	public UUID getId()
	{
		return id;
	}
	
	@Override
	public void onClick(Player p, Click c, Window w)
	{
		
	}
	
	@Override
	public boolean equals(Object object)
	{
		if(object instanceof Element)
		{
			return ((Element) object).getId().equals(getId());
		}
		
		return false;
	}
	
	@Override
	public void setGlowing(boolean glow)
	{
		glowing = glow;
	}
	
	@Override
	public boolean isGlowing()
	{
		return glowing;
	}
}

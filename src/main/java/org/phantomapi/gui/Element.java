package org.phantomapi.gui;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.phantomapi.lang.GList;

/**
 * A GUI Element. It represents an itemstack with changable information in an
 * item within a window
 * 
 * @author cyberpwn
 *
 */
public interface Element
{
	/**
	 * Add Text to the element (lore)
	 * 
	 * @param text
	 *            the desired text
	 * @return the element
	 */
	public Element addText(String text);
	
	/**
	 * Get the type of this item
	 * 
	 * @return the Material type
	 */
	public Material getType();
	
	/**
	 * Set the material type
	 * 
	 * @param type
	 *            the type
	 * @return the element
	 */
	public Element setType(Material type);
	
	/**
	 * Get the meta (sub item id) for this item stack
	 * 
	 * @return the byte
	 */
	public Byte getMetadata();
	
	/**
	 * Set the metadata (sub item id)
	 * 
	 * @param metadata
	 *            the byte
	 * @return this element
	 */
	public Element setMetadata(Byte metadata);
	
	/**
	 * Get the title of this element (custom item name)
	 * 
	 * @return the title
	 */
	public String getTitle();
	
	/**
	 * Set the title of this element (custom item name)
	 * 
	 * @param title
	 *            the title
	 * @return this element
	 */
	public Element setTitle(String title);
	
	/**
	 * Get all text (lore) on this element
	 * 
	 * @return text
	 */
	public GList<String> getText();
	
	/**
	 * Set the text for this item
	 * 
	 * @param text
	 *            the text (lore)
	 * @return this
	 */
	public Element setText(GList<String> text);
	
	/**
	 * Get the durability for this item
	 * 
	 * @return the durability
	 */
	public Short getDurability();
	
	/**
	 * Set the durability for this item
	 * 
	 * @param durability
	 *            the durability
	 * @return this
	 */
	public Element setDurability(Short durability);
	
	/**
	 * Get the slot object for this element
	 * 
	 * @return the slot
	 */
	public Slot getSlot();
	
	/**
	 * Set the slot of this item (move it)
	 * 
	 * @param slot
	 *            the slot
	 * @return this
	 */
	public Element setSlot(Slot slot);
	
	/**
	 * Get the count of this item (the item amount)
	 * 
	 * @return the count
	 */
	public Integer getCount();
	
	/**
	 * Set the count of this item (the amount)
	 * 
	 * @param count
	 *            the count
	 * @return this
	 */
	public Element setCount(Integer count);
	
	/**
	 * Clone this item (different id, making them different)
	 * 
	 * @return the new item
	 */
	public Element copy();
	
	/**
	 * Set the item stack of this element. All the data will be filled in
	 * automatically
	 * 
	 * @param stack
	 *            the item stack
	 * @return this
	 */
	public Element setStack(ItemStack stack);
	
	/**
	 * Get the item stack for this element. It is created from the given data in
	 * this element
	 * 
	 * @return the item stack
	 */
	public ItemStack getStack();
	
	/**
	 * Fired when a player clicks an element in the inventory.
	 * 
	 * @param p
	 *            the player
	 * @param c
	 *            the click type
	 * @param w
	 *            the window this element resides in
	 */
	public void onClick(Player p, Click c, Window w);
	
	/**
	 * Get the ID of this element
	 * 
	 * @return the id
	 */
	public UUID getId();
}

package org.phantomapi.util;

import java.util.List;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.phantomapi.lang.GList;
import org.phantomapi.world.MaterialBlock;

/**
 * Itemstack utilities
 * 
 * @author cyberpwn
 */
public class Items
{
	/**
	 * Is the item an item (not null or air)
	 * 
	 * @param is
	 *            the item
	 * @return true if it is
	 */
	public static boolean is(ItemStack is)
	{
		return is != null && !is.getType().equals(Material.AIR);
	}
	
	/**
	 * Is the item a certain material
	 * 
	 * @param is
	 *            the item
	 * @param material
	 *            the material
	 * @return true if it is
	 */
	public static boolean is(ItemStack is, Material material)
	{
		return is(is) && is.getType().equals(material);
	}
	
	/**
	 * Is the item a certain material and metadata
	 * 
	 * @param is
	 *            the item
	 * @param mb
	 *            the materialblock
	 * @return true if it is
	 */
	@SuppressWarnings("deprecation")
	public static boolean is(ItemStack is, MaterialBlock mb)
	{
		return is(is, mb.getMaterial()) && is.getData().getData() == mb.getData();
	}
	
	/**
	 * Is the item a given material and data
	 * 
	 * @param is
	 *            the item
	 * @param material
	 *            the material
	 * @param data
	 *            the data
	 * @return true if it is
	 */
	public static boolean is(ItemStack is, Material material, byte data)
	{
		return is(is, new MaterialBlock(material, data));
	}
	
	/**
	 * Is the item a given material and data
	 * 
	 * @param is
	 *            the item
	 * @param material
	 *            the material
	 * @param data
	 *            the data
	 * @return true if it is
	 */
	public static boolean is(ItemStack is, Material material, int data)
	{
		return is(is, material, (byte) data);
	}
	
	/**
	 * Does the item have meta
	 * 
	 * @param is
	 *            the item
	 * @return true if it does
	 */
	public static boolean hasMeta(ItemStack is)
	{
		return is(is) && is.hasItemMeta();
	}
	
	/**
	 * Does the item have a custom name
	 * 
	 * @param is
	 *            the item
	 * @return true if it has a name
	 */
	public static boolean hasName(ItemStack is)
	{
		return hasMeta(is) && is.getItemMeta().hasDisplayName();
	}
	
	/**
	 * Does the item have any lore?
	 * 
	 * @param is
	 *            the item
	 * @return true if it does
	 */
	public static boolean hasLore(ItemStack is)
	{
		return hasMeta(is) && is.getItemMeta().hasLore();
	}
	
	/**
	 * Does the item have the given name (color matters)
	 * 
	 * @param is
	 *            the item
	 * @param name
	 *            the name
	 * @return true if it has the name
	 */
	public static boolean hasName(ItemStack is, String name)
	{
		return hasName(is) && is.getItemMeta().getDisplayName().equals(name);
	}
	
	/**
	 * Does the item have the exact lore
	 * 
	 * @param is
	 *            the item
	 * @param lores
	 *            the lore
	 * @return true if it does
	 */
	public static boolean hasLore(ItemStack is, List<String> lores)
	{
		return hasLore(is) && new GList<String>(lores).equals(new GList<String>(is.getItemMeta().getLore()));
	}
	
	/**
	 * Does the item have the given enchantment
	 * 
	 * @param is
	 *            the item
	 * @param e
	 *            the enchantment
	 * @return true if it does
	 */
	public static boolean hasEnchantment(ItemStack is, Enchantment e)
	{
		return is(is) && is.getEnchantments().containsKey(e);
	}
	
	/**
	 * Does the item have the enchantment at the given level
	 * 
	 * @param is
	 *            the item
	 * @param e
	 *            the enchantment
	 * @param level
	 *            the level
	 * @return true if it does
	 */
	public static boolean hasEnchantment(ItemStack is, Enchantment e, int level)
	{
		return hasEnchantment(is, e) && is.getEnchantmentLevel(e) == level;
	}
	
	/**
	 * Does the item have any enchantments
	 * 
	 * @param is
	 *            the item
	 * @return true if it does
	 */
	public static boolean hasEnchantments(ItemStack is)
	{
		return !is.getEnchantments().isEmpty();
	}
	
	/**
	 * Get a materialblock representation of this item
	 * 
	 * @param is
	 *            the item
	 * @return the materialblock or null if the item is null
	 */
	@SuppressWarnings("deprecation")
	public static MaterialBlock toMaterialBlock(ItemStack is)
	{
		if(is != null)
		{
			return new MaterialBlock(is.getType(), is.getData().getData());
		}
		
		return null;
	}
	
	/**
	 * Can the item a be stacked onto the item b (following max stack size)
	 * 
	 * @param a
	 *            the item a
	 * @param b
	 *            the item b
	 * @return true if they can be merged
	 */
	@SuppressWarnings("deprecation")
	public static boolean isMergable(ItemStack a, ItemStack b)
	{
		if(is(a) && is(b))
		{
			if(!a.getType().equals(b.getType()))
			{
				return false;
			}
			
			if(a.getData().getData() != b.getData().getData())
			{
				return false;
			}
			
			if(a.hasItemMeta() != b.hasItemMeta())
			{
				return false;
			}
			
			if(a.getDurability() != b.getDurability())
			{
				return false;
			}
			
			if(a.hasItemMeta())
			{
				if(!a.getItemMeta().getDisplayName().equals(b.getItemMeta().getDisplayName()))
				{
					return false;
				}
				
				if(!new GList<String>(a.getItemMeta().getLore()).equals(new GList<String>(b.getItemMeta().getLore())))
				{
					return false;
				}
			}
			
			if(a.getMaxStackSize() < a.getAmount() + b.getAmount())
			{
				return false;
			}
			
			return true;
		}
		
		return false;
	}
}

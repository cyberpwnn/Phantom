package org.phantomapi.inventory;

import org.bukkit.enchantments.Enchantment;
import org.phantomapi.lang.GList;

/**
 * Enchantment set of data
 * 
 * @author cyberpwn
 */
public class EnchantmentSet
{
	private GList<EnchantmentLevel> levels;
	
	/**
	 * Create a new set
	 */
	public EnchantmentSet()
	{
		this.levels = new GList<EnchantmentLevel>();
	}
	
	/**
	 * Get a copy of the levels
	 * 
	 * @return the copied list of levels
	 */
	public GList<EnchantmentLevel> getEnchantments()
	{
		return levels.copy();
	}
	
	/**
	 * Does it contain an enchantment level?
	 * 
	 * @param enchantment
	 *            the enchantment level
	 * @return true if it does
	 */
	public boolean contains(EnchantmentLevel enchantment)
	{
		return levels.contains(enchantment);
	}
	
	/**
	 * Does it contain a specific enchantment at a level?
	 * 
	 * @param enchantment
	 *            the enchantment
	 * @param level
	 *            the level
	 * @return true if it does
	 */
	public boolean contains(Enchantment enchantment, int level)
	{
		return levels.contains(new EnchantmentLevel(enchantment, level));
	}
	
	/**
	 * Does it contain an enchantment of any level?
	 * 
	 * @param enchantment
	 *            the enchantment
	 * @return true if it does
	 */
	public boolean contains(Enchantment enchantment)
	{
		for(EnchantmentLevel i : levels)
		{
			if(i.getEnchantment().getName().equals(enchantment.getName()))
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Get the enchantment level of this enchantment type
	 * 
	 * @param enchantment
	 *            the enchantment
	 * @return the enchantment level, or null if no enchantment type exists
	 */
	public EnchantmentLevel getEnchantmentLevel(Enchantment enchantment)
	{
		for(EnchantmentLevel i : levels)
		{
			if(i.getEnchantment().getName().equals(enchantment.getName()))
			{
				return i;
			}
		}
		
		return null;
	}
	
	/**
	 * Get the level of a given enchantment
	 * 
	 * @param enchantment
	 *            the given enchantment
	 * @return the level, or null if no enchantment exists
	 */
	public Integer getLevel(Enchantment enchantment)
	{
		EnchantmentLevel el = getEnchantmentLevel(enchantment);
		
		if(el != null)
		{
			return el.getLevel();
		}
		
		return null;
	}
	
	/**
	 * Add an enchantment level to this
	 * 
	 * @param enchantment
	 *            the enchantment level
	 * @return returns true if it changed the set
	 */
	public boolean addEnchantment(EnchantmentLevel enchantment)
	{
		if(contains(enchantment))
		{
			return false;
		}
		
		if(contains(enchantment.getEnchantment()))
		{
			getEnchantmentLevel(enchantment.getEnchantment()).setLevel(enchantment.getLevel());
			
			return true;
		}
		
		levels.add(enchantment);
		
		return true;
	}
	
	/**
	 * Add an enchantment and level to this
	 * 
	 * @param enchantment
	 *            the enchantment
	 * @param level
	 *            the level
	 * @return returns true if it changed the set
	 */
	public boolean addEnchantment(Enchantment enchantment, int level)
	{
		return addEnchantment(new EnchantmentLevel(enchantment, level));
	}
	
	/**
	 * Remove an enchantment level from this set
	 * 
	 * @param enchantment
	 *            the enchantment level
	 */
	public void removeEnchantment(EnchantmentLevel enchantment)
	{
		levels.remove(enchantment);
	}
	
	/**
	 * Remove an enchantment and level from this set
	 * 
	 * @param enchantment
	 *            the enchantment
	 * @param level
	 *            the level
	 */
	public void removeEnchantment(Enchantment enchantment, int level)
	{
		removeEnchantment(new EnchantmentLevel(enchantment, level));
	}
	
	/**
	 * Remove an enchantment of any level from this set
	 * 
	 * @param enchantment
	 *            the enchantment
	 */
	public void removeEnchantment(Enchantment enchantment)
	{
		for(EnchantmentLevel i : levels.copy())
		{
			if(i.getEnchantment().getName().equals(enchantment.getName()))
			{
				removeEnchantment(i);
				
				return;
			}
		}
	}
	
	/**
	 * Removes all enchantments from this set
	 */
	public void removeAllEnchantments()
	{
		levels.clear();
	}
}

package org.phantomapi.inventory;

import org.bukkit.enchantments.Enchantment;

public class EnchantmentLevel
{
	private Enchantment enchantment;
	private Integer level;
	
	public EnchantmentLevel(Enchantment enchantment, Integer level)
	{
		this.enchantment = enchantment;
		this.level = level;
	}
	
	public EnchantmentLevel(Enchantment enchantment)
	{
		this(enchantment, 1);
	}

	public Enchantment getEnchantment()
	{
		return enchantment;
	}

	public void setEnchantment(Enchantment enchantment)
	{
		this.enchantment = enchantment;
	}

	public Integer getLevel()
	{
		return level;
	}

	public void setLevel(Integer level)
	{
		this.level = level;
	}
	
	public boolean equals(Object o)
	{
		if(o instanceof EnchantmentLevel)
		{
			EnchantmentLevel e = (EnchantmentLevel) o;
			
			return e.getEnchantment().getName().equals(getEnchantment().getName()) && e.getLevel() == getLevel();
		}
		
		return false;
	}
}

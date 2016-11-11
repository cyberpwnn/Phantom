package org.phantomapi.inventory;

import java.io.IOException;
import org.bukkit.enchantments.Enchantment;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.clust.DataEntity;

public class EnchantmentLevel implements DataEntity
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
	
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof EnchantmentLevel)
		{
			EnchantmentLevel e = (EnchantmentLevel) o;
			
			return e.getEnchantment().getName().equals(getEnchantment().getName()) && e.getLevel() == getLevel();
		}
		
		return false;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public byte[] toData() throws IOException
	{
		DataCluster cc = new DataCluster();
		
		cc.set("e", enchantment.getId());
		cc.set("l", level);
		
		return cc.compress();
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void fromData(byte[] data) throws IOException
	{
		DataCluster cc = new DataCluster(data);
		
		enchantment = Enchantment.getById(cc.getInt("e"));
		level = cc.getInt("l");
	}
}

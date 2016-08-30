package org.phantomapi.inventory;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;
import org.phantomapi.lang.GList;
import org.phantomapi.world.MaterialBlock;

/**
 * An itemstack wrapper
 * 
 * @author cyberpwn
 */
public class Stack
{
	private MaterialBlock materialBlock;
	private String name;
	private GList<String> lore;
	private Short durability;
	private EnchantmentSet enchantmentSet;
	private GList<ItemFlag> flags;
	private Integer amount;
	private PotionData potionData;
	
	/**
	 * Create an item stack
	 * 
	 * @param materialBlock
	 * @param name
	 * @param lore
	 * @param durability
	 * @param enchantmentSet
	 * @param flags
	 * @param amount
	 */
	public Stack(MaterialBlock materialBlock, String name, GList<String> lore, Short durability, EnchantmentSet enchantmentSet, GList<ItemFlag> flags, int amount)
	{
		this.materialBlock = materialBlock;
		this.name = name;
		this.lore = lore;
		this.durability = durability;
		this.enchantmentSet = enchantmentSet;
		this.flags = flags;
		this.amount = amount;
		this.potionData = null;
	}
	
	/**
	 * Create an item stack
	 * 
	 * @param materialBlock
	 * @param name
	 * @param lore
	 * @param durability
	 * @param enchantmentSet
	 */
	public Stack(MaterialBlock materialBlock, String name, GList<String> lore, Short durability, EnchantmentSet enchantmentSet)
	{
		this(materialBlock, name, lore, durability, enchantmentSet, new GList<ItemFlag>(), 1);
	}
	
	/**
	 * Create an item stack
	 * 
	 * @param materialBlock
	 * @param name
	 * @param lore
	 * @param enchantmentSet
	 */
	public Stack(MaterialBlock materialBlock, String name, GList<String> lore, EnchantmentSet enchantmentSet)
	{
		this(materialBlock, name, lore, (short) 0, enchantmentSet);
	}
	
	/**
	 * Create an item stack
	 * 
	 * @param materialBlock
	 * @param name
	 * @param lore
	 * @param durability
	 */
	public Stack(MaterialBlock materialBlock, String name, GList<String> lore, Short durability)
	{
		this(materialBlock, name, lore, durability, new EnchantmentSet());
	}
	
	/**
	 * Create an item stack
	 * 
	 * @param materialBlock
	 * @param name
	 * @param lore
	 */
	public Stack(MaterialBlock materialBlock, String name, GList<String> lore)
	{
		this(materialBlock, name, lore, new EnchantmentSet());
	}
	
	/**
	 * Create an item stack
	 * 
	 * @param materialBlock
	 */
	public Stack(MaterialBlock materialBlock)
	{
		this(materialBlock, null, new GList<String>());
	}
	
	/**
	 * Create an item stack
	 * 
	 * @param material
	 * @param data
	 */
	public Stack(Material material, byte data)
	{
		this(new MaterialBlock(material, data));
	}
	
	/**
	 * Create an item stack
	 * 
	 * @param material
	 */
	public Stack(Material material)
	{
		this(new MaterialBlock(material));
	}
	
	/**
	 * Create a stack object from an itemstack
	 * 
	 * @param is
	 *            the itemstack
	 */
	@SuppressWarnings("deprecation")
	public Stack(ItemStack is)
	{
		if(is == null)
		{
			this.materialBlock = new MaterialBlock(Material.AIR);
			this.name = null;
			this.lore = new GList<String>();
			this.durability = (short) 0;
			this.enchantmentSet = new EnchantmentSet();
			this.flags = new GList<ItemFlag>();
			this.amount = 1;
		}
		
		else
		{
			this.materialBlock = new MaterialBlock(is.getType(), is.getData().getData());
			this.durability = is.getDurability();
			this.enchantmentSet = new EnchantmentSet();
			this.amount = is.getAmount();
			
			if(is.hasItemMeta())
			{
				ItemMeta im = is.getItemMeta();
				this.name = im.getDisplayName();
				this.lore = new GList<String>(im.getLore());
				this.flags = new GList<ItemFlag>(im.getItemFlags());
			}
			
			else
			{
				this.name = null;
				this.lore = new GList<String>();
				this.flags = new GList<ItemFlag>();
			}
			
			for(Enchantment i : is.getEnchantments().keySet())
			{
				enchantmentSet.addEnchantment(i, is.getEnchantments().get(i));
			}
			
			try
			{
				Potion p = Potion.fromItemStack(is);
				PotionData pd = new PotionData(p.isSplash(), p.getLevel(), p.getEffects());
				setPotionData(pd);
			}
			
			catch(Exception e)
			{
				
			}
		}
	}
	
	public boolean equals(Object o)
	{
		if(o == null)
		{
			return false;
		}
		
		if(o instanceof Stack)
		{
			Stack s = (Stack) o;
			
			try
			{
				if(s.getAmount() == getAmount() && s.getMaterialBlock().equals(getMaterialBlock()) && s.getLore().equals(getLore()) && s.getFlags().equals(getFlags()) && s.getDurability() == getDurability())
				{
					if((s.getName() != null && getName() != null && s.getName().equals(getName())) || (s.getName() == null && getName() == null))
					{
						return true;
					}
				}
			}
			
			catch(Exception e)
			{
				e.printStackTrace();
				return false;
			}
		}
		
		return false;
	}
	
	public Stack clone()
	{
		return new Stack(getMaterialBlock(), getName(), getLore(), getDurability(), getEnchantmentSet(), getFlags(), getAmount());
	}
	
	/**
	 * Create an item stack from this stack object
	 * 
	 * @return the item stack
	 */
	public ItemStack toItemStack()
	{
		@SuppressWarnings("deprecation")
		ItemStack is = new ItemStack(getMaterialBlock().getMaterial(), amount, durability, getMaterialBlock().getData());
		is.setDurability(durability);
		
		if(name != null || !lore.isEmpty() || !flags.isEmpty())
		{
			try
			{
				ItemMeta im = is.getItemMeta();
				
				if(name != null)
				{
					im.setDisplayName(name);
				}
				
				im.addItemFlags(flags.toArray(new ItemFlag[flags.size()]));
				im.setLore(lore);
				is.setItemMeta(im);
			}
			
			catch(Exception e)
			{
				
			}
		}
		
		for(EnchantmentLevel i : getEnchantmentSet().getEnchantments())
		{
			is.addUnsafeEnchantment(i.getEnchantment(), i.getLevel());
		}
		
		if(is.getType().equals(Material.POTION) && potionData != null)
		{
			try
			{
				Potion p = Potion.fromItemStack(is);
				p.setLevel(potionData.getLevel());
				p.getEffects().clear();
				p.getEffects().addAll(potionData.getEffects());
				p.setSplash(potionData.isSplash());
				p.apply(is);
			}
			
			catch(Exception e)
			{
				
			}
		}
		
		return is;
	}
	
	public void setType(Material material)
	{
		materialBlock.setMaterial(material);
	}
	
	public Material getType()
	{
		return materialBlock.getMaterial();
	}
	
	public void setData(byte data)
	{
		materialBlock.setData(data);
	}
	
	public byte getData()
	{
		return materialBlock.getData();
	}
	
	public void clearLore()
	{
		lore.clear();
	}
	
	public void clearName()
	{
		name = null;
	}
	
	public int getMaxStackSize()
	{
		return getType().getMaxStackSize();
	}
	
	public MaterialBlock getMaterialBlock()
	{
		return materialBlock;
	}
	
	public void setMaterialBlock(MaterialBlock materialBlock)
	{
		this.materialBlock = materialBlock;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public GList<String> getLore()
	{
		return lore;
	}
	
	public void setLore(GList<String> lore)
	{
		this.lore = lore;
	}
	
	public Short getDurability()
	{
		return durability;
	}
	
	public void setDurability(Short durability)
	{
		this.durability = durability;
	}
	
	public EnchantmentSet getEnchantmentSet()
	{
		return enchantmentSet;
	}
	
	public void setEnchantmentSet(EnchantmentSet enchantmentSet)
	{
		this.enchantmentSet = enchantmentSet;
	}
	
	public GList<ItemFlag> getFlags()
	{
		return flags;
	}
	
	public void setFlags(GList<ItemFlag> flags)
	{
		this.flags = flags;
	}
	
	public Integer getAmount()
	{
		return amount;
	}
	
	public void setAmount(Integer amount)
	{
		this.amount = amount;
	}
	
	public PotionData getPotionData()
	{
		return potionData;
	}
	
	public void setPotionData(PotionData potionData)
	{
		this.potionData = potionData;
	}
}

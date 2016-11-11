package org.phantomapi.stack;

import java.io.IOException;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.clust.DataEntity;
import org.phantomapi.clust.JSONObject;
import org.phantomapi.inventory.EnchantmentLevel;
import org.phantomapi.inventory.EnchantmentSet;
import org.phantomapi.inventory.PotionData;
import org.phantomapi.lang.GList;
import org.phantomapi.util.ExceptionUtil;
import org.phantomapi.world.MaterialBlock;
import org.phantomapi.world.W;

/**
 * An itemstack wrapper
 * 
 * @author cyberpwn
 */
@SuppressWarnings("deprecation")
public class Stack implements DataEntity
{
	private MaterialBlock materialBlock;
	private String name;
	private GList<String> lore;
	private Short durability;
	private EnchantmentSet enchantmentSet;
	private GList<ItemFlag> flags;
	private Integer amount;
	private PotionData potionData;
	private Color dyeColor;
	
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
		potionData = null;
		dyeColor = null;
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
	public Stack(ItemStack is)
	{
		if(is == null)
		{
			materialBlock = new MaterialBlock(Material.AIR);
			name = null;
			lore = new GList<String>();
			durability = (short) 0;
			enchantmentSet = new EnchantmentSet();
			flags = new GList<ItemFlag>();
			amount = 1;
		}
		
		else
		{
			materialBlock = new MaterialBlock(is.getType(), is.getData().getData());
			durability = is.getDurability();
			enchantmentSet = new EnchantmentSet();
			amount = is.getAmount();
			
			if(is.hasItemMeta())
			{
				ItemMeta im = is.getItemMeta();
				name = im.getDisplayName();
				lore = new GList<String>(im.getLore());
				flags = new GList<ItemFlag>(im.getItemFlags());
			}
			
			else
			{
				name = null;
				lore = new GList<String>();
				flags = new GList<ItemFlag>();
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
			
			if(is.getType().equals(Material.LEATHER_BOOTS) || is.getType().equals(Material.LEATHER_CHESTPLATE) || is.getType().equals(Material.LEATHER_HELMET) || is.getType().equals(Material.LEATHER_LEGGINGS))
			{
				LeatherArmorMeta lam = (LeatherArmorMeta) is.getItemMeta();
				dyeColor = lam.getColor();
			}
		}
	}
	
	@Override
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
				ExceptionUtil.print(e);
				return false;
			}
		}
		
		return false;
	}
	
	@Override
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
		
		if(is.getType().equals(Material.LEATHER_BOOTS) || is.getType().equals(Material.LEATHER_CHESTPLATE) || is.getType().equals(Material.LEATHER_HELMET) || is.getType().equals(Material.LEATHER_LEGGINGS))
		{
			LeatherArmorMeta lam = (LeatherArmorMeta) is.getItemMeta();
			lam.setColor(dyeColor);
			is.setItemMeta(lam);
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
	
	public Color getDyeColor()
	{
		return dyeColor;
	}
	
	public void setDyeColor(Color dyeColor)
	{
		this.dyeColor = dyeColor;
	}
	
	/*
	 * MaterialBlock materialBlock;
	 * String name;
	 * GList<String> lore;
	 * Short durability;
	 * EnchantmentSet enchantmentSet;
	 * GList<ItemFlag> flags;
	 * Integer amount;
	 * PotionData potionData;
	 * Color dyeColor;
	 */
	
	@Override
	public byte[] toData() throws IOException
	{
		DataCluster cc = new DataCluster();
		cc.set("m", materialBlock.getMaterial().toString() + ":" + materialBlock.getData());
		cc.set("e", new DataCluster(enchantmentSet.toData()).toJSON().toString());
		cc.set("a", amount);
		
		if(potionData != null)
		{
			cc.set("p", new DataCluster(potionData.toData()).toJSON().toString());
		}
		
		if(durability != 0)
		{
			cc.set("d", (int) durability);
		}
		
		if(name != null)
		{
			cc.set("n", name);
		}
		
		if(!lore.isEmpty())
		{
			cc.set("l", lore);
		}
		
		if(!flags.isEmpty())
		{
			GList<String> f = new GList<String>();
			
			for(ItemFlag i : flags)
			{
				f.add(i.toString());
			}
			
			cc.set("f", f);
		}
		
		return cc.compress();
	}
	
	@Override
	public void fromData(byte[] data) throws IOException
	{
		DataCluster cc = new DataCluster(data);
		EnchantmentSet es = new EnchantmentSet();
		MaterialBlock mb = W.getMaterialBlock(cc.getString("m"));
		
		es.fromData(new DataCluster(new JSONObject(cc.getString("e"))).compress());
		
		setType(mb.getMaterial());
		setData(mb.getData());
		setAmount(cc.getInt("a"));
		setEnchantmentSet(es);
		
		if(cc.contains("p"))
		{
			PotionData pd = new PotionData(false, 1, new GList<PotionEffect>());
			pd.fromData(new DataCluster(new JSONObject(cc.getString("p"))).compress());
		}
		
		if(cc.contains("d"))
		{
			setDurability(cc.getInt("d").shortValue());
		}
		
		if(cc.contains("n"))
		{
			setName(cc.getString("n"));
		}
		
		if(cc.contains("l"))
		{
			setLore(new GList<String>(cc.getStringList("l")));
		}
		
		if(cc.contains("f"))
		{
			GList<ItemFlag> iff = new GList<ItemFlag>();
			
			for(String i : cc.getStringList("f"))
			{
				iff.add(ItemFlag.valueOf(i));
			}
			
			setFlags(iff);
		}
	}
}

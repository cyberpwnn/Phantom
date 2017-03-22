package org.phantomapi.nbt;

import org.bukkit.inventory.ItemStack;

public final class VillagerNBTOffer
{
	
	private ItemStack _buyA;
	private ItemStack _buyB;
	private ItemStack _sell;
	private int _maxUses;
	private int _uses;
	
	public VillagerNBTOffer(ItemStack buyA, ItemStack buyB, ItemStack sell)
	{
		this(buyA, buyB, sell, 7);
	}
	
	public VillagerNBTOffer(ItemStack buyA, ItemStack buyB, ItemStack sell, int maxUses)
	{
		this(buyA, buyB, sell, maxUses, 0);
	}
	
	public VillagerNBTOffer(ItemStack buyA, ItemStack buyB, ItemStack sell, int maxUses, int uses)
	{
		_buyA = buyA;
		_buyB = buyB;
		_sell = sell;
		_maxUses = maxUses;
		_uses = uses;
	}
	
	VillagerNBTOffer(NBTTagCompound offer)
	{
		_buyA = NBTUtils.itemStackFromNBTData(offer.getCompound("buy"));
		if(offer.hasKey("buyB"))
		{
			_buyB = NBTUtils.itemStackFromNBTData(offer.getCompound("buyB"));
		}
		else
		{
			_buyB = null;
		}
		_sell = NBTUtils.itemStackFromNBTData(offer.getCompound("sell"));
		_maxUses = offer.getInt("maxUses");
		_uses = offer.getInt("uses");
	}
	
	NBTTagCompound getCompound()
	{
		NBTTagCompound offer = new NBTTagCompound();
		offer.setCompound("buy", NBTUtils.itemStackToNBTData(_buyA));
		if(_buyB != null)
		{
			offer.setCompound("buyB", NBTUtils.itemStackToNBTData(_buyB));
		}
		offer.setCompound("sell", NBTUtils.itemStackToNBTData(_sell));
		offer.setInt("maxUses", _maxUses);
		offer.setInt("uses", _uses);
		return offer;
	}
	
	public ItemStack getBuyA()
	{
		return _buyA;
	}
	
	public ItemStack getBuyB()
	{
		return _buyB;
	}
	
	public ItemStack getSell()
	{
		return _sell;
	}
	
	public int getMaxUses()
	{
		return _maxUses;
	}
	
	public int getUses()
	{
		return _uses;
	}
}

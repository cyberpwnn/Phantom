package org.phantomapi.nbt;

import org.bukkit.block.Block;
import org.bukkit.potion.PotionEffectType;

public final class BeaconNBTWrapper extends TileNBTWrapper
{
	
	public BeaconNBTWrapper(Block block)
	{
		super(block);
	}
	
	@SuppressWarnings("deprecation")
	public void setPrimary(PotionEffectType effect)
	{
		if(effect == null)
		{
			_data.setInt("Primary", 0);
		}
		else
		{
			_data.setInt("Primary", effect.getId());
		}
	}
	
	@SuppressWarnings("deprecation")
	public void setSecondary(PotionEffectType effect)
	{
		if(effect == null)
		{
			_data.setInt("Secondary", 0);
		}
		else
		{
			_data.setInt("Secondary", effect.getId());
		}
	}
	
}

package org.phantomapi.nbt;

import org.bukkit.entity.Player;

public final class HorseVariantVariable extends NBTGenericVariable
{
	
	public HorseVariantVariable()
	{
		super("Variant");
	}
	
	@Override
	boolean set(NBTTagCompound data, String value, Player player)
	{
		String[] pieces = value.split("\\s+", 2);
		if(pieces.length == 2)
		{
			int markings, color;
			try
			{
				markings = Integer.parseInt(pieces[0]);
				color = Integer.parseInt(pieces[1]);
			}
			catch(NumberFormatException e)
			{
				return false;
			}
			if(markings >= 0 && markings <= 4 && color >= 0 && color <= 6)
			{
				data.setInt(_nbtKey, color | markings << 8);
				return true;
			}
		}
		return false;
	}
	
	@Override
	String get(NBTTagCompound data)
	{
		if(data.hasKey(_nbtKey))
		{
			int variant = data.getInt(_nbtKey);
			return ((variant >> 8) & 0xFF) + " " + (variant & 0xFF);
		}
		return null;
	}
	
	@Override
	String getFormat()
	{
		return "Two integers, the fist one controls the horse markings (0 to 4), the second one controls the color (0 to 6), e.g. '4 1'.";
	}
	
}

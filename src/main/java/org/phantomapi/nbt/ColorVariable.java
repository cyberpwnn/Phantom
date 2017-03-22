package org.phantomapi.nbt;

import org.bukkit.Color;
import org.bukkit.entity.Player;

public class ColorVariable extends NBTGenericVariable
{
	
	public ColorVariable(String nbtKey)
	{
		super(nbtKey);
	}
	
	@Override
	boolean set(NBTTagCompound data, String value, Player player)
	{
		if(!value.startsWith("#"))
		{
			value = "#" + value;
		}
		try
		{
			java.awt.Color color = java.awt.Color.decode(value);
			int c = Color.fromRGB(color.getRed(), color.getGreen(), color.getBlue()).asRGB();
			data.setInt(_nbtKey, c);
			return true;
		}
		catch(NumberFormatException e)
		{
			return false;
		}
	}
	
	@Override
	String get(NBTTagCompound data)
	{
		Color color = Color.fromRGB(data.getInt(_nbtKey));
		String r = Integer.toHexString(color.getRed());
		String g = Integer.toHexString(color.getGreen());
		String b = Integer.toHexString(color.getBlue());
		return "#" + (r.length() == 1 ? "0" + r : r) + (g.length() == 1 ? "0" + g : g) + (b.length() == 1 ? "0" + b : b);
	}
	
	@Override
	String getFormat()
	{
		return "RGB format, #FFFFFF (e.g. #FF0000 for red).";
	}
	
}

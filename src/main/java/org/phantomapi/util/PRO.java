package org.phantomapi.util;

import org.bukkit.util.Vector;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

public class PRO
{
	public static ProtocolManager getLibrary()
	{
		return ProtocolLibrary.getProtocolManager();
	}
	
	public static Vector toVector(int val)
	{
		return new Vector(val >> 38, (val >> 26) & 0xFFF, val << 38 >> 38);
	}
	
	public static int toPosition(Vector v)
	{
		return (((int) v.getX() & 0x3FFFFFF) << 38) | (((int) v.getY() & 0xFFF) << 26) | ((int) v.getZ() & 0x3FFFFFF);
	}
}

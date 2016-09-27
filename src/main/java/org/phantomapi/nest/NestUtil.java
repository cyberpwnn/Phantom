package org.phantomapi.nest;

import java.io.File;
import org.bukkit.Chunk;

public class NestUtil
{
	public static File getFile(Chunk chunk)
	{
		return new File(new File(chunk.getWorld().getWorldFolder(), "nest"), "n." + chunk.getX() + "." + chunk.getZ() + ".nst");
	}
}

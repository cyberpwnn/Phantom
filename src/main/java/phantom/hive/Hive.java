package phantom.hive;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.phantomapi.Phantom;
import org.phantomapi.service.HiveSVC;

/**
 * Hive api for storing data to blocks, chunks, worlds and entities
 *
 * @author cyberpwn
 */
public class Hive
{
	public HiveWorld getHive(World w)
	{
		return Phantom.getService(HiveSVC.class).getWorld(w);
	}

	public HiveChunk getHive(Chunk c)
	{
		return getHive(c.getWorld()).getHiveChunk(c.getX(), c.getZ());
	}

	public HiveBlock getHive(Block b)
	{
		return getHive(b.getChunk()).getBlock(b.getLocation());
	}
}

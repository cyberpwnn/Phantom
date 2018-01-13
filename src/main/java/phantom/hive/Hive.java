package phantom.hive;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.phantomapi.Phantom;
import org.phantomapi.service.HiveSVC;

/**
 * Hive api for storing data to blocks, chunks, worlds and entities
 *
 * @author cyberpwn
 */
public class Hive
{
	/**
	 * Get a hive world
	 *
	 * @param w
	 *            the world
	 * @return the hive world
	 */
	public static IHive getHive(World w)
	{
		return Phantom.getService(HiveSVC.class).getWorld(w);
	}

	/**
	 * Get the hive chunk
	 *
	 * @param c
	 *            the chunk
	 * @return the hive chunk
	 */
	public static IHive getHive(Chunk c)
	{
		return ((HiveWorld) getHive(c.getWorld())).getHiveChunk(c.getX(), c.getZ());
	}

	/**
	 * Get the hive chunk
	 *
	 * @param c
	 *            the chunk
	 * @return the hive chunk
	 */
	public static IHive getHive(Entity c)
	{
		return ((HiveWorld) getHive(c.getWorld())).getHiveEntity(c.getWorld(), c.getUniqueId(), c.getEntityId());
	}

	/**
	 * Get the hive block
	 *
	 * @param b
	 *            the block
	 * @return the hive block
	 */
	public static IHive getHive(Block b)
	{
		return ((HiveChunk) getHive(b.getChunk())).getBlock(b.getLocation());
	}
}

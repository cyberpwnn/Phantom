package phantom.hive;

import java.io.File;

import org.bukkit.Location;
import org.bukkit.World;

import phantom.lang.GMap;
import phantom.lang.GTriset;

public class HiveChunk extends HiveObject
{
	private File dataFolder;
	private int x;
	private int z;
	private World world;
	private GMap<GTriset<Integer, Integer, Integer>, HiveBlock> blocks;

	public HiveChunk(World world, int x, int z)
	{
		this.dataFolder = new File(new File(world.getWorldFolder(), "hive"), "chunk." + x + "." + z);
		dataFolder.mkdirs();
		this.x = x;
		this.z = z;
		this.world = world;
		readEntries(dataFolder);
		blocks = new GMap<GTriset<Integer, Integer, Integer>, HiveBlock>();
	}

	public HiveBlock getBlock(int x, int y, int z)
	{
		GTriset<Integer, Integer, Integer> coords = new GTriset<Integer, Integer, Integer>(x, y, z);

		if(!blocks.containsKey(coords))
		{
			blocks.put(coords, new HiveBlock(new Location(world, x, y, z)));
		}

		return blocks.get(coords);
	}

	public HiveBlock getBlock(Location l)
	{
		return getBlock(l.getBlockX(), l.getBlockY(), l.getBlockZ());
	}

	@Override
	public File getDataFolder()
	{
		return dataFolder;
	}

	public int getX()
	{
		return x;
	}

	public int getZ()
	{
		return z;
	}

	public World getWorld()
	{
		return world;
	}
}

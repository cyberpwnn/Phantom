package phantom.hive;

import java.io.File;

import org.bukkit.World;

import phantom.lang.GBiset;
import phantom.lang.GMap;

public class HiveWorld extends HiveObject
{
	private File dataFolder;
	private World world;
	private GMap<GBiset<Integer, Integer>, HiveChunk> chunks;

	public HiveWorld(World world)
	{
		this.dataFolder = new File(world.getWorldFolder(), "hive");
		dataFolder.mkdirs();
		this.world = world;
		readEntries(dataFolder);
		chunks = new GMap<GBiset<Integer, Integer>, HiveChunk>();
	}

	public HiveChunk getHiveChunk(int x, int z)
	{
		GBiset<Integer, Integer> coords = new GBiset<Integer, Integer>(x, z);

		if(!chunks.containsKey(coords))
		{
			chunks.put(coords, new HiveChunk(world, x, z));
		}

		return chunks.get(coords);
	}

	@Override
	public File getDataFolder()
	{
		return dataFolder;
	}

	public World getWorld()
	{
		return world;
	}
}

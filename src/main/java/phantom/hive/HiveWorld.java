package phantom.hive;

import java.io.File;
import java.util.UUID;

import org.bukkit.World;

import phantom.lang.GBiset;
import phantom.lang.GMap;
import phantom.lang.GTriset;

public class HiveWorld extends HiveObject
{
	private File dataFolder;
	private World world;
	private GMap<GBiset<Integer, Integer>, HiveChunk> chunks;
	private GMap<GTriset<World, UUID, Integer>, HiveEntity> entities;

	public HiveWorld(World world)
	{
		this.dataFolder = new File(world.getWorldFolder(), "hive");
		dataFolder.mkdirs();
		this.world = world;
		readEntries(dataFolder);
		chunks = new GMap<GBiset<Integer, Integer>, HiveChunk>();
		entities = new GMap<GTriset<World, UUID, Integer>, HiveEntity>();
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

	public HiveEntity getHiveEntity(World w, UUID u, int e)
	{
		GTriset<World, UUID, Integer> coords = new GTriset<World, UUID, Integer>(w, u, e);

		if(!entities.containsKey(coords))
		{
			entities.put(coords, new HiveEntity(w, u, e));
		}

		return entities.get(coords);
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

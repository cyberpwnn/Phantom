package phantom.hive;

import java.io.File;
import java.util.UUID;

import org.bukkit.World;

public class HiveEntity extends HiveObject
{
	private File dataFolder;
	private UUID uid;
	private int eid;
	private World world;

	public HiveEntity(World world, UUID uid, int eid)
	{
		this.dataFolder = new File(new File(world.getWorldFolder(), "hive"), "entity." + uid.toString() + "." + eid);
		dataFolder.mkdirs();
		this.uid = uid;
		this.eid = eid;
		this.world = world;
		readEntries(dataFolder);
	}

	@Override
	public File getDataFolder()
	{
		return dataFolder;
	}

	public UUID getUid()
	{
		return uid;
	}

	public int getEid()
	{
		return eid;
	}

	public World getWorld()
	{
		return world;
	}
}

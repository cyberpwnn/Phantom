package phantom.hive;

import java.io.File;

import org.bukkit.Location;

public class HiveBlock extends HiveObject
{
	private File dataFolder;
	private Location location;

	public HiveBlock(Location location)
	{
		this.dataFolder = new File(new File(new File(location.getWorld().getWorldFolder(), "hive"), "chunk." + location.getChunk().getX() + "." + location.getChunk().getZ()), "block." + location.getBlockX() + "." + location.getBlockY() + "." + location.getBlockZ());
		dataFolder.mkdirs();
		this.location = location;
		readEntries(dataFolder);
	}

	@Override
	public File getDataFolder()
	{
		return dataFolder;
	}

	public Location getLocation()
	{
		return location;
	}
}

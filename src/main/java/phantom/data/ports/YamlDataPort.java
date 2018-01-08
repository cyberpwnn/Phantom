package phantom.data.ports;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import phantom.data.cluster.DataCluster;

public class YamlDataPort implements IDataPort<FileConfiguration>
{
	@Override
	public FileConfiguration write(DataCluster data)
	{
		FileConfiguration fc = new YamlConfiguration();

		return fc;
	}

	@Override
	public DataCluster read(FileConfiguration source)
	{
		return null;
	}

}

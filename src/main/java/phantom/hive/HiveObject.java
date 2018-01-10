package phantom.hive;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.phantomapi.Phantom;

import phantom.data.cluster.DataCluster;
import phantom.data.ports.CompressedDataPort;
import phantom.lang.GMap;
import phantom.util.files.FS;
import phantom.util.metrics.Documented;

/**
 * Represents a hive object
 *
 * @author cyberpwn
 */
@Documented
public abstract class HiveObject implements IHive
{
	private GMap<String, DataCluster> data;

	/**
	 * Create a new hive object
	 */
	public HiveObject()
	{
		this.data = new GMap<String, DataCluster>();
	}

	protected void writeEntries(File folder)
	{
		for(String i : data.k())
		{
			File f = new File(folder, i + ".cdc");

			try
			{
				DataCluster cc = data.get(i).copy();
				ByteBuffer bb = cc.write(new CompressedDataPort());
				FS.writeFully(f, bb.array());
			}

			catch(IOException e)
			{
				Phantom.kick(e);
			}
		}
	}

	protected void readEntries(File folder)
	{
		for(File i : folder.listFiles())
		{
			if(i.getName().endsWith(".cdc"))
			{
				try
				{
					ByteBuffer buf = ByteBuffer.wrap(FS.readFully(i));
					DataCluster cc = new DataCluster();
					cc.read(new CompressedDataPort(), buf);
					data.put(i.getName().replace(".cdc", ""), cc);
				}

				catch(IOException e)
				{
					Phantom.kick(e);
				}
			}
		}
	}

	@Override
	public void drop(String channel)
	{
		data.remove(channel);

		for(File i : getDataFolder().listFiles())
		{
			if(i.getName().equals(channel + ".cdc"))
			{
				i.delete();
			}
		}
	}

	public abstract File getDataFolder();

	@Override
	public DataCluster pull(String channel)
	{
		return data.containsKey(channel) ? data.get(channel).copy() : new DataCluster();
	}

	@Override
	public void push(DataCluster cc, String channel)
	{
		data.put(channel, cc.copy());
		writeEntries(getDataFolder());
	}
}

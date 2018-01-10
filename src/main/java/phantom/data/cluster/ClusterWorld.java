package phantom.data.cluster;

import java.io.IOException;

import org.bukkit.World;
import org.phantomapi.Phantom;

import phantom.util.data.StringUtil;

@Cluster(shortcode = "wd", type = World.class)
public class ClusterWorld extends PhantomCluster<World>
{
	public ClusterWorld(World value)
	{
		super(value);
	}

	@Override
	public void fromBytes(byte[] data) throws IOException
	{
		set(Phantom.getWorld(StringUtil.bytesToUTF(data)));
	}

	@Override
	public byte[] toBytes() throws IOException
	{
		return StringUtil.utfToBytes(get().getName());
	}

	@Override
	public String asString()
	{
		return get().getName();
	}

	@Override
	public void fromString(String s)
	{
		set(Phantom.getWorld(s));
	}
}

package phantom.data.cluster;

import java.io.UTFDataFormatException;

import org.bukkit.Location;

import phantom.util.data.StringUtil;

@Cluster(shortcode = "lc", type = Location.class)
public class ClusterLocation extends PhantomCluster<Location>
{
	public ClusterLocation(Location value)
	{
		super(value);
	}

	@Override
	public void fromBytes(byte[] data) throws UTFDataFormatException
	{
		set(StringUtil.locationFromString(StringUtil.bytesToUTF(data)));
	}

	@Override
	public byte[] toBytes() throws UTFDataFormatException
	{
		return StringUtil.utfToBytes(StringUtil.locationToString(get()));
	}

	@Override
	public String asString()
	{
		return StringUtil.locationToString(get());
	}

	@Override
	public void fromString(String s)
	{
		set(StringUtil.locationFromString(s));
	}
}

package phantom.data.cluster;

import java.io.IOException;

import org.bukkit.util.Vector;

import phantom.util.data.StringUtil;

@Cluster(shortcode = "vc", type = Vector.class)
public class ClusterVector extends PhantomCluster<Vector>
{
	public ClusterVector(Vector value)
	{
		super(value);
	}

	@Override
	public void fromBytes(byte[] data) throws IOException
	{
		set(StringUtil.vectorFromString(StringUtil.bytesToUTF(data)));
	}

	@Override
	public byte[] toBytes() throws IOException
	{
		return StringUtil.utfToBytes(StringUtil.vectorToString(get()));
	}

	@Override
	public String asString()
	{
		return StringUtil.vectorToString(get());
	}

	@Override
	public void fromString(String s)
	{
		set(StringUtil.vectorFromString(s));
	}
}

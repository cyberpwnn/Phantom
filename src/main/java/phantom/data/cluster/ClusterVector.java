package phantom.data.cluster;

import java.io.UTFDataFormatException;

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
	public void fromBytes(byte[] data) throws UTFDataFormatException
	{
		set(StringUtil.vectorFromString(StringUtil.bytesToUTF(data)));
	}

	@Override
	public byte[] toBytes() throws UTFDataFormatException
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

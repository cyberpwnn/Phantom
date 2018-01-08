package phantom.data.cluster;

import java.io.UTFDataFormatException;

import phantom.util.data.StringUtil;

@Cluster(shortcode = "s", type = String.class)
public class ClusterString extends PhantomCluster<String>
{
	public ClusterString(String value)
	{
		super(value);
	}

	@Override
	public void fromBytes(byte[] data) throws UTFDataFormatException
	{
		set(StringUtil.bytesToUTF(data));
	}

	@Override
	public byte[] toBytes() throws UTFDataFormatException
	{
		return StringUtil.utfToBytes(get());
	}

	@Override
	public String asString()
	{
		return get();
	}

	@Override
	public void fromString(String s)
	{
		set(s);
	}
}

package phantom.data.cluster;

import java.io.IOException;

import phantom.util.data.StringUtil;

@Cluster(shortcode = "s", type = String.class)
public class ClusterString extends PhantomCluster<String>
{
	public ClusterString(String value)
	{
		super(value);
	}

	@Override
	public void fromBytes(byte[] data) throws IOException
	{
		set(StringUtil.bytesToUTF(data));
	}

	@Override
	public byte[] toBytes() throws IOException
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

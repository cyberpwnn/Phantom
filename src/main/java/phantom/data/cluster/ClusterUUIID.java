package phantom.data.cluster;

import java.io.IOException;
import java.util.UUID;

import phantom.util.data.StringUtil;

@Cluster(shortcode = "u", type = UUID.class)
public class ClusterUUIID extends PhantomCluster<UUID>
{
	public ClusterUUIID(UUID value)
	{
		super(value);
	}

	@Override
	public void fromBytes(byte[] data) throws IOException
	{
		set(UUID.fromString(StringUtil.bytesToUTF(data)));
	}

	@Override
	public byte[] toBytes() throws IOException
	{
		return StringUtil.utfToBytes(get().toString());
	}

	@Override
	public String asString()
	{
		return get().toString();
	}

	@Override
	public void fromString(String s)
	{
		set(UUID.fromString(s));
	}
}

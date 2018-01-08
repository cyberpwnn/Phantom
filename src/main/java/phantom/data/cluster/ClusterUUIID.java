package phantom.data.cluster;

import java.io.UTFDataFormatException;
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
	public void fromBytes(byte[] data) throws UTFDataFormatException
	{
		set(UUID.fromString(StringUtil.bytesToUTF(data)));
	}

	@Override
	public byte[] toBytes() throws UTFDataFormatException
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

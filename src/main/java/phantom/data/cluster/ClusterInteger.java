package phantom.data.cluster;

@Cluster(shortcode = "i", type = Integer.class)
public class ClusterInteger extends PhantomCluster<Integer>
{
	public ClusterInteger(Integer value)
	{
		super(value);
	}

	@Override
	public void fromBytes(byte[] data)
	{
		set(((data[0] << 24) + (data[1] << 16) + (data[2] << 8) + (data[3] << 0)));
	}

	@Override
	public byte[] toBytes()
	{
		int v = get();
		byte[] buf = new byte[4];
		buf[0] = (byte) ((v >>> 24) & 0xFF);
		buf[1] = (byte) ((v >>> 16) & 0xFF);
		buf[2] = (byte) ((v >>> 8) & 0xFF);
		buf[3] = (byte) ((v >>> 0) & 0xFF);

		return buf;
	}

	@Override
	public String asString()
	{
		return get().toString();
	}

	@Override
	public void fromString(String s)
	{
		set(Integer.valueOf(s));
	}
}

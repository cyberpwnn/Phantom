package phantom.data.cluster;

@Cluster(Short.class)
public class ClusterShort extends PhantomCluster<Short>
{
	public ClusterShort(Short value)
	{
		super(value);
	}

	@Override
	public void fromBytes(byte[] data)
	{
		set((short) ((data[0] << 8) + (data[1] << 0)));
	}

	@Override
	public byte[] toBytes()
	{
		short v = get();
		byte[] buf = new byte[2];
		buf[0] = (byte) ((v >>> 8) & 0xFF);
		buf[1] = (byte) ((v >>> 0) & 0xFF);

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
		set(Short.valueOf(s));
	}
}

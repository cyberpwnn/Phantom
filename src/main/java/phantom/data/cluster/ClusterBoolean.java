package phantom.data.cluster;

@Cluster(shortcode = "b", type = Boolean.class)
public class ClusterBoolean extends PhantomCluster<Boolean>
{
	public ClusterBoolean(Boolean value)
	{
		super(value);
	}

	@Override
	public void fromBytes(byte[] data)
	{
		set(new Boolean(data[0] == 1));
	}

	@Override
	public byte[] toBytes()
	{
		return new byte[] {(byte) (get() ? 1 : 0)};
	}

	@Override
	public void fromString(String s)
	{
		set(Boolean.valueOf(s));
	}

	@Override
	public String asString()
	{
		return get().toString();
	}
}

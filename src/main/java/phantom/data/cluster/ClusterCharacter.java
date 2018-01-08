package phantom.data.cluster;

@Cluster(Character.class)
public class ClusterCharacter extends PhantomCluster<Character>
{
	public ClusterCharacter(Character value)
	{
		super(value);
	}

	@Override
	public void fromBytes(byte[] data)
	{
		set((char) ((data[0] << 8) + (data[1] << 0)));
	}

	@Override
	public byte[] toBytes()
	{
		byte[] buf = new byte[2];
		buf[0] = (byte) ((get().charValue() >>> 8) & 0xFF);
		buf[1] = (byte) ((get().charValue() >>> 0) & 0xFF);

		return buf;
	}

	@Override
	public String asString()
	{
		return get() + "";
	}

	@Override
	public void fromString(String s)
	{
		set(s.charAt(0));
	}
}

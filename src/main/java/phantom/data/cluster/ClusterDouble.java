package phantom.data.cluster;

@Cluster(shortcode = "d", type = Double.class)
public class ClusterDouble extends PhantomCluster<Double>
{
	public ClusterDouble(Double value)
	{
		super(value);
	}

	@Override
	public void fromBytes(byte[] data)
	{
		set(Double.longBitsToDouble((((long) data[0] << 56) + ((long) (data[1] & 255) << 48) + ((long) (data[2] & 255) << 40) + ((long) (data[3] & 255) << 32) + ((long) (data[4] & 255) << 24) + ((data[5] & 255) << 16) + ((data[6] & 255) << 8) + ((data[7] & 255) << 0))));
	}

	@Override
	public byte[] toBytes()
	{
		long v = Double.doubleToLongBits(get());
		byte[] writeBuffer = new byte[8];
		writeBuffer[0] = (byte) (v >>> 56);
		writeBuffer[1] = (byte) (v >>> 48);
		writeBuffer[2] = (byte) (v >>> 40);
		writeBuffer[3] = (byte) (v >>> 32);
		writeBuffer[4] = (byte) (v >>> 24);
		writeBuffer[5] = (byte) (v >>> 16);
		writeBuffer[6] = (byte) (v >>> 8);
		writeBuffer[7] = (byte) (v >>> 0);

		return writeBuffer;
	}

	@Override
	public String asString()
	{
		return get().toString();
	}

	@Override
	public void fromString(String s)
	{
		set(Double.valueOf(s));
	}
}

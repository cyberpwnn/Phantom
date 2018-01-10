package phantom.data.cluster;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import phantom.lang.GList;

@Cluster(shortcode = "sl", type = GList.class)
public class ClusterStringList extends PhantomCluster<GList<String>>
{
	public ClusterStringList(GList<String> value)
	{
		super(value);
	}

	@Override
	public void fromBytes(byte[] data) throws IOException
	{
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		DataInputStream din = new DataInputStream(in);
		int sz = din.readInt();
		set(new GList<String>());

		for(int i = 0; i < sz; i++)
		{
			get().add(din.readUTF());
		}

		din.close();
	}

	@Override
	public byte[] toBytes() throws IOException
	{
		ByteArrayOutputStream boas = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(boas);
		dos.writeInt(get().size());

		for(String i : get())
		{
			dos.writeUTF(i);
		}

		dos.flush();

		return boas.toByteArray();
	}

	@Override
	public String asString()
	{
		return get().toString("|,|");
	}

	@Override
	public void fromString(String s)
	{
		set(new GList<String>(s.split("|,|")));
	}
}

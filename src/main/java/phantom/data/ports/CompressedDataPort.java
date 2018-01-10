package phantom.data.ports;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.phantomapi.Phantom;
import org.phantomapi.service.ClusterSVC;

import phantom.data.IData;
import phantom.data.cluster.DataCluster;
import phantom.data.cluster.ICluster;

public class CompressedDataPort implements IDataPort<ByteBuffer>
{
	@Override
	public ByteBuffer write(DataCluster data) throws IOException
	{
		ByteArrayOutputStream boas = new ByteArrayOutputStream();
		GZIPOutputStream gzo = new GZIPOutputStream(boas);
		DataOutputStream dos = new DataOutputStream(gzo);

		dos.writeInt(data.k().size());

		for(String i : data.k())
		{
			dos.writeUTF(i);
			dos.writeUTF(data.getShortcodedKey(i));
			byte[] b = ((IData) data.getCluster(i)).toBytes();
			dos.writeInt(b.length);
			dos.write(b);
		}

		dos.flush();
		dos.close();

		return ByteBuffer.wrap(boas.toByteArray());
	}

	@Override
	public DataCluster read(ByteBuffer source) throws IOException
	{
		DataCluster cc = new DataCluster();
		ByteArrayInputStream bin = new ByteArrayInputStream(source.array());
		GZIPInputStream gzi = new GZIPInputStream(bin);
		DataInputStream dos = new DataInputStream(gzi);
		ClusterSVC c = Phantom.getService(ClusterSVC.class);

		int totalElements = dos.readInt();

		for(int i = 0; i < totalElements; i++)
		{
			String key = dos.readUTF();
			String shortCode = dos.readUTF();
			byte[] b = new byte[dos.readInt()];
			dos.read(b, 0, b.length);
			Class<? extends ICluster<?>> clusterType = c.getClusterType(shortCode);
			Class<?> type = c.getType(shortCode);
			ICluster<?> cluster;

			try
			{
				cluster = clusterType.getConstructor(type).newInstance(type.cast(null));
				((IData) cluster).fromBytes(b);
			}

			catch(IOException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e)
			{
				Phantom.kick(e);
				continue;
			}

			cc.setCluster(key, cluster);
		}

		dos.close();

		return cc;
	}

}

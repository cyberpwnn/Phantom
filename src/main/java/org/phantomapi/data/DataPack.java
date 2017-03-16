package org.phantomapi.data;

import java.io.IOException;
import org.phantomapi.lang.GList;

public class DataPack implements DataHandle
{
	GList<DataHandle> entities;
	
	public DataPack()
	{
		entities = new GList<DataHandle>();
	}
	
	public void put(DataHandle e)
	{
		entities.add(e);
	}
	
	@Override
	public byte[] toData() throws IOException
	{
		ByteWriter b = new ByteWriter();
		b.wByte(getDataType());
		b.wInt(entities.size());
		
		for(DataHandle i : entities)
		{
			b.wBytes(i.toData());
		}
		
		b.close();
		
		return DataHandle.compress(b.getData());
	}
	
	@Override
	public void fromData(byte[] cdata) throws IOException
	{
		byte[] data = DataHandle.decompress(cdata);
		ByteReader b = new ByteReader(data);
		byte ty = b.rByte();
		
		if(ty != getDataType())
		{
			b.close();
			return;
		}
		
		int size = b.rInt();
		
		for(int i = 0; i < size; i++)
		{
			byte[] idata = b.rBytes();
			ByteReader bi = new ByteReader(idata);
			byte type = bi.rByte();
			DataHandle e = PackageType.getInstance(type);
			
			if(e != null)
			{
				e.fromData(idata);
				put(e);
			}
		}
	}
	
	@Override
	public byte getDataType()
	{
		return 0;
	}
	
	public GList<DataHandle> getEntities()
	{
		return entities;
	}
	
	@Override
	public String toString()
	{
		String n = "Packages: " + entities.size();
		
		for(DataHandle i : entities)
		{
			n = n + "\n" + i.toString();
		}
		
		return n;
	}
}

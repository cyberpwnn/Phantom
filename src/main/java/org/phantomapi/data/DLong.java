package org.phantomapi.data;

import java.io.IOException;

public class DLong implements DataHandle
{
	private Long integer;
	
	public DLong()
	{
		this.integer = null;
	}
	
	public DLong(Long integer)
	{
		this.integer = integer;
	}
	
	@Override
	public byte[] toData() throws IOException
	{
		ByteWriter b = new ByteWriter();
		b.wByte(getDataType());
		b.wLong(integer);
		b.close();
		
		return b.getData();
	}

	@Override
	public void fromData(byte[] data) throws IOException
	{
		ByteReader b = new ByteReader(data);
		byte ty = b.rByte();
		
		if(ty != getDataType())
		{
			b.close();
			return;
		}
		
		integer = b.rLong();
		b.close();
	}

	@Override
	public byte getDataType()
	{
		return 8;
	}
}

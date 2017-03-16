package org.phantomapi.data;

import java.io.IOException;

public class DShort implements DataHandle
{
	private Short integer;
	
	public DShort()
	{
		this.integer = null;
	}
	
	public DShort(Short integer)
	{
		this.integer = integer;
	}
	
	@Override
	public byte[] toData() throws IOException
	{
		ByteWriter b = new ByteWriter();
		b.wByte(getDataType());
		b.wShort(integer);
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
		
		integer = b.rShort();
		b.close();
	}

	@Override
	public byte getDataType()
	{
		return 9;
	}
}

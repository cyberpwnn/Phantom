package org.phantomapi.data;

import java.io.IOException;

public class DBytes implements DataHandle
{
	private byte[] integer;
	
	public DBytes()
	{
		this.integer = null;
	}
	
	public DBytes(byte[] integer)
	{
		this.integer = integer;
	}
	
	@Override
	public byte[] toData() throws IOException
	{
		ByteWriter b = new ByteWriter();
		b.wByte(getDataType());
		b.wBytes(integer);
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
		
		integer = b.rBytes();
		b.close();
	}

	@Override
	public byte getDataType()
	{
		return 3;
	}
}

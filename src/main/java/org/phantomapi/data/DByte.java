package org.phantomapi.data;

import java.io.IOException;

public class DByte implements DataHandle
{
	private Byte integer;
	
	public DByte()
	{
		this.integer = null;
	}
	
	public DByte(Byte integer)
	{
		this.integer = integer;
	}
	
	@Override
	public byte[] toData() throws IOException
	{
		ByteWriter b = new ByteWriter();
		b.wByte(getDataType());
		b.wByte(integer);
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
		
		integer = b.rByte();
		b.close();
	}

	@Override
	public byte getDataType()
	{
		return 2;
	}
	
	public Byte get()
	{
		return integer;
	}
}

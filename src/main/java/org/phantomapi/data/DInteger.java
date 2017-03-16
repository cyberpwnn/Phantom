package org.phantomapi.data;

import java.io.IOException;

public class DInteger implements DataHandle
{
	private Integer integer;
	
	public DInteger()
	{
		this.integer = null;
	}
	
	public DInteger(Integer integer)
	{
		this.integer = integer;
	}
	
	@Override
	public byte[] toData() throws IOException
	{
		ByteWriter b = new ByteWriter();
		b.wByte(getDataType());
		b.wInt(integer);
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
		
		integer = b.rInt();
		b.close();
	}

	@Override
	public byte getDataType()
	{
		return 7;
	}
}

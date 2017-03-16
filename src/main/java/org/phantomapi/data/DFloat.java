package org.phantomapi.data;

import java.io.IOException;

public class DFloat implements DataHandle
{
	private Float integer;
	
	public DFloat()
	{
		this.integer = null;
	}
	
	public DFloat(Float integer)
	{
		this.integer = integer;
	}
	
	@Override
	public byte[] toData() throws IOException
	{
		ByteWriter b = new ByteWriter();
		b.wByte(getDataType());
		b.wFloat(integer);
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
		
		integer = b.rFloat();
		b.close();
	}

	@Override
	public byte getDataType()
	{
		return 6;
	}
	
	public Float get()
	{
		return integer;
	}
}

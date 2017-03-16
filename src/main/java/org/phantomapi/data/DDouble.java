package org.phantomapi.data;

import java.io.IOException;

public class DDouble implements DataHandle
{
	private Double integer;
	
	public DDouble()
	{
		this.integer = null;
	}
	
	public DDouble(Double integer)
	{
		this.integer = integer;
	}
	
	@Override
	public byte[] toData() throws IOException
	{
		ByteWriter b = new ByteWriter();
		b.wByte(getDataType());
		b.wDouble(integer);
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
		
		integer = b.rDouble();
		b.close();
	}

	@Override
	public byte getDataType()
	{
		return 5;
	}
	
	public Double get()
	{
		return integer;
	}
}

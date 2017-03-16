package org.phantomapi.data;

import java.io.IOException;

public class DBoolean implements DataHandle
{
	private Boolean integer;
	
	public DBoolean()
	{
		this.integer = null;
	}
	
	public DBoolean(Boolean integer)
	{
		this.integer = integer;
	}
	
	@Override
	public byte[] toData() throws IOException
	{
		ByteWriter b = new ByteWriter();
		b.wByte(getDataType());
		b.wBoolean(integer);
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
		
		integer = b.rBoolean();
		b.close();
	}

	@Override
	public byte getDataType()
	{
		return 1;
	}
	
	public Boolean get()
	{
		return integer;
	}
}

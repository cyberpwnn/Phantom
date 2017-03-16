package org.phantomapi.data;

import java.io.IOException;

public class DChar implements DataHandle
{
	private Character integer;
	
	public DChar()
	{
		this.integer = null;
	}
	
	public DChar(Character integer)
	{
		this.integer = integer;
	}
	
	@Override
	public byte[] toData() throws IOException
	{
		ByteWriter b = new ByteWriter();
		b.wByte(getDataType());
		b.wChar(integer);
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
		
		integer = b.rChar();
		b.close();
	}

	@Override
	public byte getDataType()
	{
		return 4;
	}
}

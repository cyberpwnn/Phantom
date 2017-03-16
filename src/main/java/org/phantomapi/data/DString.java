package org.phantomapi.data;

import java.io.IOException;

public class DString implements DataHandle
{
	private String string;
	
	public DString()
	{
		this.string = null;
	}
	
	public DString(String string)
	{
		this.string = string;
	}
	
	@Override
	public byte[] toData() throws IOException
	{
		ByteWriter b = new ByteWriter();
		b.wByte(getDataType());
		b.wString(string);
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
		
		string = b.rString();
		b.close();
	}

	@Override
	public byte getDataType()
	{
		return 10;
	}
	
	public String get()
	{
		return string;
	}
}

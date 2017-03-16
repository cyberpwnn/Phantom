package org.phantomapi.data;

import java.io.IOException;
import org.phantomapi.lang.GList;

public class DStringList implements DataHandle
{
	private GList<String> string;
	
	public DStringList()
	{
		this.string = null;
	}
	
	public DStringList(GList<String> string)
	{
		this.string = string;
	}
	
	@Override
	public byte[] toData() throws IOException
	{
		ByteWriter b = new ByteWriter();
		b.wByte(getDataType());
		b.wInt(string.size());
		
		for(String i : string)
		{
			b.wString(i);
		}
		
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
		
		int len = b.rInt();
		GList<String> s = new GList<String>();
		
		for(int i = 0; i < len; i++)
		{
			s.add(b.rString());
		}
		
		string = s;
		
		b.close();
	}
	
	@Override
	public byte getDataType()
	{
		return 11;
	}
}

package org.phantomapi.data;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class ByteReader
{
	private ByteArrayInputStream bois;
	private DataInputStream dis;
	
	public ByteReader(byte[] data)
	{
		bois = new ByteArrayInputStream(data);
		dis = new DataInputStream(bois);
	}
	
	public String rString() throws IOException
	{
		return dis.readUTF();
	}
	
	public byte[] rBytes() throws IOException
	{
		byte[] data = new byte[rInt()];
		
		for(int i = 0; i < data.length; i++)
		{
			data[i] = rByte();
		}
		
		return data;
	}
	
	public int rInt() throws IOException
	{
		return dis.readInt();
	}
	
	public byte rByte() throws IOException
	{
		return dis.readByte();
	}
	
	public short rShort() throws IOException
	{
		return dis.readShort();
	}
	
	public long rLong() throws IOException
	{
		return dis.readLong();
	}
	
	public char rChar() throws IOException
	{
		return dis.readChar();
	}
	
	public double rDouble() throws IOException
	{
		return dis.readDouble();
	}
	
	public float rFloat() throws IOException
	{
		return dis.readFloat();
	}
	
	public boolean rBoolean() throws IOException
	{
		return dis.readBoolean();
	}
	
	public void close() throws IOException
	{
		dis.close();
	}
}

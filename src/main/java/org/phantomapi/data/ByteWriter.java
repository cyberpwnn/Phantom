package org.phantomapi.data;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ByteWriter
{
	private ByteArrayOutputStream boas;
	private DataOutputStream dos;
	
	public ByteWriter()
	{
		boas = new ByteArrayOutputStream();
		dos = new DataOutputStream(boas);
	}
	
	public void wString(String data) throws IOException
	{
		dos.writeUTF(data);
	}
	
	public void wInt(int data) throws IOException
	{
		dos.writeInt(data);
	}
	
	public void wByte(byte data) throws IOException
	{
		dos.writeByte(data);
	}
	
	public void wShort(short data) throws IOException
	{
		dos.writeShort(data);
	}
	
	public void wLong(long data) throws IOException
	{
		dos.writeLong(data);
	}
	
	public void wChar(char data) throws IOException
	{
		dos.writeChar(data);
	}
	
	public void wDouble(double data) throws IOException
	{
		dos.writeDouble(data);
	}
	
	public void wFloat(float data) throws IOException
	{
		dos.writeFloat(data);
	}
	
	public void wBoolean(boolean data) throws IOException
	{
		dos.writeBoolean(data);
	}
	
	public void wBytes(byte[] data) throws IOException
	{
		wInt(data.length);
		
		for(byte i : data)
		{
			wByte(i);
		}
	}
	
	public byte[] getData()
	{
		return boas.toByteArray();
	}
	
	public void close() throws IOException
	{
		dos.close();
	}
}

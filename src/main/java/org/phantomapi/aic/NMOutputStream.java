package org.phantomapi.aic;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class NMOutputStream extends DataOutputStream
{
	private final OutputStream parent;
	
	public NMOutputStream(OutputStream parent)
	{
		super(parent);
		this.parent = parent;
	}
	
	public OutputStream getParent()
	{
		return parent;
	}
	
	public void write(int b, int amount) throws IOException
	{
		for(int i = 0; i < amount; i++)
		{
			write(b);
		}
	}
	
	public void writeVarInt(int i) throws IOException
	{
		while((i & 0xFFFFFF80) != 0)
		{
			writeByte(i & 0x7F | 0x80);
			i >>>= 7;
		}
		
		writeByte(i);
	}
	
	public void writeShort(short s) throws IOException
	{
		write((byte) (s >>> 8));
		write((byte) (s));
	}
	
	public void writeMedium(int m) throws IOException
	{
		write((byte) (m >>> 16));
		write((byte) (m >>> 8));
		write((byte) (m));
	}
	
	@Override
	public void close() throws IOException
	{
		parent.close();
	}
}

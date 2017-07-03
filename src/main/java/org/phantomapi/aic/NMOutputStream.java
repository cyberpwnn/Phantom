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
	
	public void writeVarInt(int value) throws IOException
	{
		do
		{
			byte temp = (byte) (value & 0b01111111);
			value >>>= 7;
			
			if(value != 0)
			{
				temp |= 0b10000000;
			}
			
			writeByte(temp);
		}
		
		while(value != 0);
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

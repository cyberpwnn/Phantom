package com.volmit.phantom.api.lang;

import java.io.IOException;
import java.io.OutputStream;

public class VoidOutputStream extends OutputStream 
{
	@Override
	public void write(int b) throws IOException
	{
		// poof
	}
}

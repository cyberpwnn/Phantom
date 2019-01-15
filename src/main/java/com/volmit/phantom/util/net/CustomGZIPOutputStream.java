package com.volmit.phantom.util.net;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import com.volmit.phantom.api.math.M;

public class CustomGZIPOutputStream extends GZIPOutputStream
{
	public CustomGZIPOutputStream(int compression, OutputStream out, boolean syncFlush) throws IOException
	{
		super(out, syncFlush);
		def.setLevel(M.iclip(compression, 0, 9));
	}

	public CustomGZIPOutputStream(int compression, OutputStream out, int size, boolean syncFlush) throws IOException
	{
		super(out, size, syncFlush);
		def.setLevel(M.iclip(compression, 0, 9));
	}

	public CustomGZIPOutputStream(int compression, OutputStream out, int size) throws IOException
	{
		super(out, size);
		def.setLevel(M.iclip(compression, 0, 9));
	}

	public CustomGZIPOutputStream(int compression, OutputStream out) throws IOException
	{
		super(out);
		def.setLevel(M.iclip(compression, 0, 9));
	}
}

package org.phantomapi.world;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import com.boydti.fawe.object.FaweOutputStream;
import com.boydti.fawe.object.clipboard.AbstractClipboardFormat;
import com.boydti.fawe.object.clipboard.DiskOptimizedClipboard;
import com.boydti.fawe.object.schematic.FaweFormat;
import com.boydti.fawe.util.MainUtil;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;

public class PPAFormat extends AbstractClipboardFormat
{
	public PPAFormat()
	{
		super("phantom-pore-artifact", "ppa", "pa");
	}

	@Override
	public ClipboardReader getReader(InputStream inputStream) throws IOException
	{
		return new FaweFormat(MainUtil.getCompressedIS(inputStream));
	}
	
	@Override
	public ClipboardWriter getWriter(OutputStream outputStream) throws IOException
	{
		return getWriter(outputStream, 8);
	}
	
	@Override
	public String getExtension()
	{
		return "ppa";
	}
	
	public ClipboardWriter getWriter(OutputStream os, int compression) throws IOException
	{
		FaweFormat writer = new FaweFormat(new FaweOutputStream(os));
		writer.compress(compression);
		return writer;
	}
	
	public DiskOptimizedClipboard getUncompressedReadWrite(File file) throws IOException
	{
		return new DiskOptimizedClipboard(file);
	}
	
	public DiskOptimizedClipboard createUncompressedReadWrite(int width, int height, int length, File file)
	{
		return new DiskOptimizedClipboard(width, height, length, file);
	}

	@Override
	public boolean isFormat(File f)
	{
		return f.getName().toLowerCase().endsWith(".ppa");
	}
}

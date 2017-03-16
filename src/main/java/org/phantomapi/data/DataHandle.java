package org.phantomapi.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.apache.commons.io.IOUtils;

public interface DataHandle
{
	public byte[] toData() throws IOException;
	
	public void fromData(byte[] data) throws IOException;
	
	public byte getDataType();
	
	public static byte[] compress(byte[] data) throws IOException
	{
		ByteArrayOutputStream boas = new ByteArrayOutputStream();
		GZIPOutputStream gzi = new GZIPOutputStream(boas);
		gzi.write(data);
		gzi.close();
		
		return boas.toByteArray();
	}
	
	public static byte[] decompress(byte[] data) throws IOException
	{
		ByteArrayInputStream bois = new ByteArrayInputStream(data);
		GZIPInputStream gzi = new GZIPInputStream(bois);
		
		return IOUtils.toByteArray(gzi);
	}
}

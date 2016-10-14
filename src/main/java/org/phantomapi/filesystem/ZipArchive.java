package org.phantomapi.filesystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.phantomapi.lang.GMap;

/**
 * Create a zip archive
 * 
 * @author cyberpwn
 */
public class ZipArchive implements Archive
{
	private GMap<File, File> fileSet;
	
	public ZipArchive()
	{
		this.fileSet = new GMap<File, File>();
	}
	
	@Override
	public void add(File file, File internal)
	{
		fileSet.put(file, internal);
	}
	
	@Override
	public void remove(File internal)
	{
		for(File i : fileSet.k())
		{
			if(fileSet.get(i).equals(internal))
			{
				fileSet.remove(i);
			}
		}
	}
	
	@Override
	public void compress(File destination) throws FileNotFoundException, IOException
	{
		ArchiveUtils.createZipArchive(fileSet, destination);
	}
	
	@Override
	public ArchiveType getType()
	{
		return ArchiveType.ZIP;
	}
}

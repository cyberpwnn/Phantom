package org.phantomapi.source;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.phantomapi.filesystem.Archive;
import org.phantomapi.filesystem.ArchiveUtils;
import org.phantomapi.filesystem.ZipArchive;

/**
 * A Source pack
 * 
 * @author cyberpwn
 */
public class SourcePack
{
	private File source;
	
	/**
	 * Create a sourcepack
	 * 
	 * @param source
	 *            the source of the sourcepack folder
	 */
	public SourcePack(File source)
	{
		this.source = source;
	}
	
	public void buildZip(File destination) throws FileNotFoundException, IOException
	{
		Archive a = new ZipArchive();
		
		for(File i : ArchiveUtils.allFiles(source))
		{
			if(i.getName().equals("Thumbs.db"))
			{
				continue;
			}
			
			a.add(i, ArchiveUtils.cropFile(source, i));
		}
		
		a.compress(destination);
	}
}

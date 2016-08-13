package org.phantomapi.clust;

import java.io.File;
import java.io.IOException;

/**
 * 
 * @author cyberpwn
 *
 */
public class DataOutput implements DataOutputtable
{
	@Override
	public void save(DataCluster cluster, File file) throws IOException
	{
		if(file.exists())
		{
			if(file.isDirectory())
			{
				throw new IOException("Target Directory (" + file.getPath() + ") is not a file!");
			}
		}
		
		else
		{
			file.getParentFile().mkdirs();
			file.createNewFile();
		}
	}
}
